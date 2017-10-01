package com.surveyor.manager;

import com.surveyor.manager.data.dao.AnswerDAOService;
import com.surveyor.manager.data.dao.QuestionDAOService;
import com.surveyor.manager.data.dao.SurveyDAOService;
import com.surveyor.manager.data.dto.*;
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

import java.util.Optional;

import static com.surveyor.manager.TestUtils.call;
import static com.surveyor.manager.TestUtils.check;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AttachTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SurveyDAOService surveyDAOService;

    @Autowired
    private QuestionDAOService questionDAOService;

    @Autowired
    private AnswerDAOService answerDAOService;

    @Test
    public void stepByStepCreate() {
        String surveyId = createSurvey();
        String questionId = createQuestion();
        String answerId = createAnswer();
        attach("/question", "?question_id=" + questionId + "&survey_id=" + surveyId);
        attach("/answer", "?question_id=" + questionId + "&answer_id=" + answerId);

        Optional<Survey> findSurvey = surveyDAOService.findOne(surveyId);
        Assert.assertTrue(findSurvey.isPresent());
        Assert.assertEquals("test", findSurvey.get().getName());
        Assert.assertEquals("cc", findSurvey.get().getCountryCode());
        Assert.assertEquals(1, findSurvey.get().getQuestions().size());

        Optional<Question> findQuestion = questionDAOService.findOne(questionId);
        Assert.assertTrue(findQuestion.isPresent());
        Assert.assertEquals("test pass?", findQuestion.get().getName());
        Assert.assertEquals(1, findQuestion.get().getAnswers().size());

        Optional<Answer> findAnswer = answerDAOService.findOne(answerId);
        Assert.assertTrue(findAnswer.isPresent());
        Assert.assertEquals("yes", findAnswer.get().getName());
    }

    private String createSurvey() {
        SurveyDTO surveyDTO = new SurveyDTO("test", "cc");
        return create("/survey", surveyDTO);
    }

    private String createQuestion() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setName("test pass?");
        return create("/question", questionDTO);
    }

    private String createAnswer() {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setName("yes");
        return create("/answer", answerDTO);
    }

    private String create(String controller, CommonDTO dto) {
        ResponseDTO responseDTO =
                this.restTemplate.postForObject(call(port, controller), dto, ResponseDTO.class);
        check(responseDTO);
        return String.valueOf(responseDTO.getResponse());
    }

    private void attach(String controller, String params) {
        System.out.println(call(port, "/attach" + controller + params));
        ResponseDTO responseDTO =
                this.restTemplate.getForObject(call(port, "/attach" + controller + params),
                        ResponseDTO.class);
        check(responseDTO);
    }
}
