package com.surveyor.manager;

import com.surveyor.manager.data.dao.SurveyDAOService;
import com.surveyor.manager.data.dto.AnswerDTO;
import com.surveyor.manager.data.dto.QuestionDTO;
import com.surveyor.manager.data.dto.ResponseDTO;
import com.surveyor.manager.data.dto.SurveyDTO;
import com.surveyor.manager.data.entity.Answer;
import com.surveyor.manager.data.entity.Question;
import com.surveyor.manager.data.entity.Survey;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.surveyor.manager.TestUtils.call;
import static com.surveyor.manager.TestUtils.check;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyLoadTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SurveyDAOService surveyDAOService;

    @Test
    @Transactional
    public void loadSurvey() {
        SurveyDTO surveyDTO = new SurveyDTO("name", "cc");
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setName("test pass?");
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setName("yes");
        questionDTO.setAnswers(Collections.singletonList(answerDTO));
        surveyDTO.setQuestions(Collections.singletonList(questionDTO));
        ResponseDTO responseDTO =
                this.restTemplate.postForObject(call(port, "/survey/load"), surveyDTO, ResponseDTO.class);
        check(responseDTO);
        String surveyId = String.valueOf(responseDTO.getResponse());
        Optional<Survey> find = surveyDAOService.findOne(surveyId);
        Assert.assertTrue(find.isPresent());
        Survey found = find.get();
        System.out.println("found " + found);
        Assert.assertEquals("name", found.getName());
        Assert.assertEquals("cc", found.getCountryCode());
        Assert.assertEquals(1, found.getQuestions().size());
        Question question = found.getQuestions().get(0);
        Assert.assertEquals("test pass?", question.getName());
        Assert.assertEquals(1, question.getAnswers().size());
        Answer answer = question.getAnswers().get(0);
        Assert.assertEquals("yes", answer.getName());
    }

    @Test
    public void loadIncorrect() {
        SurveyDTO surveyDTO = new SurveyDTO(null, "");
        ResponseDTO responseDTO =
                this.restTemplate.postForObject(call(port, "/survey/load"), surveyDTO, ResponseDTO.class);
        Assert.assertFalse(responseDTO.isResult());
        surveyDTO = new SurveyDTO("name", "");
        responseDTO =
                this.restTemplate.postForObject(call(port, "/survey/load"), surveyDTO, ResponseDTO.class);
        Assert.assertFalse(responseDTO.isResult());
    }
}
