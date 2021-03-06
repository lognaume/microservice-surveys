package io.lozzikit.survey.services;

import io.lozzikit.survey.api.model.Answer;
import io.lozzikit.survey.api.model.SurveyResponses;
import io.lozzikit.survey.entities.AnswerEntity;
import io.lozzikit.survey.entities.SurveyResponsesEntity;
import io.lozzikit.survey.repositories.ResponsesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyResponsesService {
    @Autowired
    ResponsesRepository responsesRepository;

    public void createResponses(SurveyResponses responses, String surveyId) {
        SurveyResponsesEntity surveyResponsesEntity = DTOToEntity(responses);
        surveyResponsesEntity.setSurveyId(surveyId);

        responsesRepository.save(surveyResponsesEntity);
    }

    public List<SurveyResponses> getAllSurveyResponses(String surveyId) {
        return responsesRepository.findBySurveyId(surveyId).stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    private SurveyResponses entityToDTO(SurveyResponsesEntity responsesEntity) {
        SurveyResponses responses = new SurveyResponses();

        responses.setAnswers(responsesEntity.getAnswers().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList())
        );

        return responses;
    }

    private Answer entityToDTO(AnswerEntity answerEntity) {
        Answer answer = new Answer();
        answer.setQuestionNumber(answerEntity.getQuestionNumber());
        answer.setAnswer(answerEntity.getAnswer());

        return answer;
    }

    private SurveyResponsesEntity DTOToEntity(SurveyResponses responses) {
        SurveyResponsesEntity responsesEntity = new SurveyResponsesEntity();

        responsesEntity.setUser(responses.getUser());
        responsesEntity.setAnswers(responses.getAnswers().stream()
                .map(this::DTOToEntity)
                .collect(Collectors.toList())
        );

        return responsesEntity;
    }

    private AnswerEntity DTOToEntity(Answer answer) {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setQuestionNumber(answer.getQuestionNumber());
        answerEntity.setAnswer(answer.getAnswer());

        return answerEntity;
    }
}
