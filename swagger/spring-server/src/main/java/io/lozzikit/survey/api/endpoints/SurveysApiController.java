package io.lozzikit.survey.api.endpoints;

import io.lozzikit.survey.api.SurveysApi;
import io.lozzikit.survey.api.exceptions.NotFoundException;
import io.lozzikit.survey.api.model.Event;
import io.lozzikit.survey.api.model.Status;
import io.lozzikit.survey.api.model.Survey;
import io.lozzikit.survey.services.EventService;
import io.lozzikit.survey.services.SurveyService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
public class SurveysApiController implements SurveysApi {
    @Autowired
    SurveyService surveyService;

    @Autowired
    EventService eventService;

    @Override
    public ResponseEntity<List<Survey>> getSurveys() {
        List<Survey> surveys = surveyService.getAllSurveys();

        return new ResponseEntity<>(surveys, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> addSurvey(@Valid @RequestBody Survey body) {
        body.setStatus(Status.DRAFT);
        String newSurveyId = surveyService.createSurvey(body);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newSurveyId).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Void> changeSurveysStatus(@PathVariable("surveyId") String surveyId, @RequestBody Status status) {
        try {
            Survey survey = surveyService.getSurvey(surveyId);
            Status oldStatus = survey.getStatus();

            // Update status if value changed
            // The following status changes are forbidden : closed -> any, open -> draft, draft -> closed
            if (status.equals(oldStatus)
                    || oldStatus.equals(Status.CLOSED)
                    || (oldStatus.equals(Status.OPENED) && status.equals(Status.DRAFT))
                    || (oldStatus.equals(Status.DRAFT) && status.equals(Status.CLOSED))
                    ) {
                return ResponseEntity.badRequest().build();
            }

            Event event = new Event();
            event.setSurveyId(surveyId);
            event.setStatus(status);

            eventService.saveEvent(event);

            survey.setStatus(status);
            surveyService.updateSurvey(survey, surveyId);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Survey> getSurveyById(@ApiParam(value = "ID of survey to return", required = true) @PathVariable("surveyId") String surveyId) {
        try {
            Survey survey = surveyService.getSurvey(surveyId);

            return new ResponseEntity<>(survey, HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
