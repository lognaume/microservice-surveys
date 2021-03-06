package io.lozzikit.survey.api.spec.steps.surveys;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.lozzikit.survey.ApiException;
import io.lozzikit.survey.ApiResponse;
import io.lozzikit.survey.api.dto.NewSurvey;
import io.lozzikit.survey.api.dto.Question;
import io.lozzikit.survey.api.dto.User;
import io.lozzikit.survey.api.spec.helpers.Environment;
import io.lozzikit.survey.api.spec.helpers.HTTPRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.junit.Assert;

/**
 * Created by Tony on 16.11.2017.
 */
public class CreateSurveySteps extends SurveySteps {
    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;

    private String payload;
    private ContentType contentType = ContentType.APPLICATION_JSON;

    public CreateSurveySteps(Environment environment) {
        super(environment);
    }

    @Given("^there is a Surveys server$")
    public void there_is_a_Surveys_server() throws Throwable {
        Assert.assertNotNull(api);
    }

    @Given("^I have an empty survey payload$")
    public void i_have_a_survey_payload() {
        environment.setNewSurvey(new NewSurvey());
    }

    @Given("^I have a survey payload with first question not zero$")
    public void iHaveASurveyWithFirstQuestionNotZero() {
        NewSurvey newSurvey = new NewSurvey();
        newSurvey.setUser(new User());
        Question question = new Question();
        question.setNumber(1);

        newSurvey.addQuestionsItem(question);

        environment.setNewSurvey(newSurvey);
    }

    @Given("^I have a survey payload with not consecutive questions numbers$")
    public void iHaveASurveyWithNotConsecutiveQuestionsNumbers() {
        NewSurvey newSurvey = new NewSurvey();
        newSurvey.setUser(new User());

        Question question1 = new Question();
        question1.setNumber(0);

        Question question2 = new Question();
        question2.setNumber(1);

        Question question3 = new Question();
        question2.setNumber(3);

        newSurvey.addQuestionsItem(question1);
        newSurvey.addQuestionsItem(question2);
        newSurvey.addQuestionsItem(question3);

        environment.setNewSurvey(newSurvey);
    }

    @Given("^I have a survey payload with correct questions numbers$")
    public void iHaveASurveyPayloadWithCorrectQuestionsNumbers() throws Throwable {
        NewSurvey newSurvey = new NewSurvey();
        newSurvey.setUser(new User());

        Question question1 = new Question();
        question1.setNumber(0);

        Question question2 = new Question();
        question2.setNumber(1);

        Question question3 = new Question();
        question3.setNumber(2);

        newSurvey.addQuestionsItem(question1);
        newSurvey.addQuestionsItem(question2);
        newSurvey.addQuestionsItem(question3);

        environment.setNewSurvey(newSurvey);
    }

    @When("^I POST its payload to the /survey endpoint$")
    public void iPOSTItsPayloadToTheSurveyEndpoint() {
        try {
            lastApiResponse = api.addSurveyWithHttpInfo(environment.getNewSurvey());
            lastApiCallThrewException = false;
            lastApiException = null;
            environment.setLastStatusCode(lastApiResponse.getStatusCode());
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            environment.setLastStatusCode(lastApiException.getCode());
        }
    }

    @Given("^I have a survey payload without user")
    public void iHaveASurveyPayloadWithoutUser() throws Throwable {
        payload = "{\n" +
                "  \"title\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"questions\": [\n" +
                "    {\n" +
                "      \"question\": \"string\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    @Given("^I have a survey payload with wrong user type$")
    public void iHaveAPayloadWithWrongUserType() throws Throwable {
        payload = "{\n" +
                "  \"user\": \"THIS IS AN INVALID USER ID\",\n" +
                "  \"title\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"questions\": [\n" +
                "    {\n" +
                "      \"question\": \"string\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    @Given("^I have a wrong content type survey payload$")
    public void iHaveAWrongContentTypeSurveyPayload() throws Throwable {
        contentType = ContentType.TEXT_PLAIN;
        payload = "{\n" +
                "  \"user\": 1,\n" +
                "  \"title\": \"string\",\n" +
                "  \"description\": \"asdf\",\n" +
                "  \"questions\": [\n" +
                "    {\n" +
                "      \"question\": \"string\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    @When("^I custom POST it to the /survey endpoint$")
    public void iCustomPOSTItToTheSurveyEndpoint() throws Throwable {
        CloseableHttpResponse response = HTTPRequest.sendPostRequest(api.getApiClient().getBasePath() + "/surveys", payload, contentType);
        environment.setLastStatusCode(response.getStatusLine().getStatusCode());
        response.close();
    }

    @Given("^I have a survey with the mandatory properties set$")
    public void iHaveASurveyWithOnlyTheUserPropertySet() throws Throwable {
        NewSurvey survey = new NewSurvey();
        survey.setUser(new User());
        environment.setNewSurvey(survey);
    }

    /**
     * Add a question to the environment.newSurvey
     *
     * @throws Throwable
     */
    @And("^I add a question to the survey$")
    public void iAddAQuestionToTheSurvey() throws Throwable {
        NewSurvey survey = environment.getNewSurvey();

        Question q1 = new Question();
        q1.setNumber(0);
        q1.setQuestion("THE question ?");
        survey.addQuestionsItem(q1);
    }
}
