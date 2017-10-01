package com.surveyor.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveyor.manager.data.dao.SurveyDAOService;
import com.surveyor.manager.data.dto.QuestionDTO;
import com.surveyor.manager.data.dto.ResponseDTO;
import com.surveyor.manager.data.dto.SurveyDTO;
import com.surveyor.manager.data.entity.Survey;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.surveyor.manager.TestUtils.call;
import static com.surveyor.manager.TestUtils.check;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyCRUDTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SurveyDAOService surveyDAOService;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:survey.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:clean.sql")
    public void readSurvey() throws IOException {
        ResponseDTO responseDTO =
                this.restTemplate.getForObject(call(port, "/survey/survey_id1"), ResponseDTO.class);
        check(responseDTO);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(responseDTO.getResponse());
        SurveyDTO got = mapper.readValue(jsonInString, SurveyDTO.class);
        Assert.assertTrue(got.getName().equals("test_survey1"));
        Assert.assertEquals(3, got.getQuestions().size());
        List<QuestionDTO> questions = got.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            QuestionDTO question = questions.get(i);
            Assert.assertEquals("question" + (i + 1), question.getName());
            Assert.assertEquals(3, question.getAnswers().size());
            Assert.assertEquals("true", question.getAnswers().get(0).getName());
            Assert.assertEquals("false", question.getAnswers().get(1).getName());
            Assert.assertEquals("unknown", question.getAnswers().get(2).getName());
        }
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:survey.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:clean.sql")
    public void updateSurvey() {
        Optional<Survey> found = surveyDAOService.findOne("survey_id1");
        Assert.assertTrue(found.isPresent());
        SurveyDTO surveyDTO = new SurveyDTO("survey_id1");
        surveyDTO.setName("updated_name");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<SurveyDTO> entity = new HttpEntity<>(surveyDTO, headers);
        ResponseEntity<ResponseDTO> exchange = this.restTemplate.exchange(
                call(port, "/survey/survey_id1"),
                HttpMethod.PUT,
                entity,
                ResponseDTO.class);
        ResponseDTO responseDTO = exchange.getBody();
        check(responseDTO);
        found = surveyDAOService.findOne("survey_id1");
        Assert.assertTrue(found.isPresent());
        Survey survey = found.get();
        Assert.assertEquals("updated_name", survey.getName());
        Assert.assertEquals("EN", survey.getCountryCode());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:survey.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:clean.sql")
    public void deleteSurvey() {
        //TODO
    }
}
