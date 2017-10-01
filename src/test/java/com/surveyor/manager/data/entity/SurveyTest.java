package com.surveyor.manager.data.entity;

import com.surveyor.manager.data.dao.AnswerDAO;
import com.surveyor.manager.data.dao.QuestionDAO;
import com.surveyor.manager.data.dao.SurveyDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SurveyTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SurveyDAO surveyDAO;

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private AnswerDAO answerDAO;

    @Test
    public void testCRUD() {
        Survey survey = new Survey();
        surveyDAO.save(survey);
        Assert.isTrue(survey.getId() != null, "Id should be filled");
        Survey find = surveyDAO.findOne(survey.getId());
        Assert.isTrue(find != null, "Survey should be found");
        Iterable<Survey> surveys = surveyDAO.findAll();
        Assert.isTrue(surveys.iterator().hasNext(), "Survey should be found via findAll");
        Assert.isTrue(survey.getId().equals(surveys.iterator().next().getId()), "Id should match");
        surveyDAO.delete(survey);
        find = surveyDAO.findOne(survey.getId());
        Assert.isTrue(find == null, "Survey should not be found after deletion");
        surveys = surveyDAO.findAll();
        Assert.isTrue(!surveys.iterator().hasNext(), "Survey list is empty");
    }

    @Test
    public void testWriteWithQuestions() {
        Question question1 = new Question();
        Question question2 = new Question();
        Survey survey = new Survey();
        survey.addQuestion(question1);
        survey.addQuestion(question2);
        surveyDAO.save(survey);
        List<Survey> surveys = new ArrayList<>((Collection<Survey>) surveyDAO.findAll());
        Assert.isTrue(surveys.size() == 1, "Survey should be saved");
        List<Question> questions = new ArrayList<>((Collection<Question>) questionDAO.findAll());
        Assert.isTrue(questions.size() == 2, "All questions should be saved");
        Assert.isTrue(question1.getId() != null, "QuestionIds should be filled");
        Assert.isTrue(question2.getId() != null, "QuestionIds should be filled");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:surveys.sql")
    public void testReadWithQuestions() {
        Survey survey = surveyDAO.findOne("survey_id1");
        Assert.isTrue(survey != null, "Survey should be found");
        Assert.isTrue(survey.getQuestions().size() == 3, "Survey1 has got 3 questions");
        for (Question q : survey.getQuestions())
            Assert.isTrue(q.getAnswers().size() == 3, "Each Survey1's question has 3 answers");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:surveys.sql")
    public void testAddQuestion() {
        Survey survey = surveyDAO.findOne("survey_id1");
        Assert.isTrue(survey != null, "Survey should be found");
        Question saveMe = new Question("What is the meaning of life?");
        saveMe.addAnswer(new Answer("girls"));
        saveMe.addAnswer(new Answer("games"));
        saveMe.addAnswer(new Answer("rock'n'roll"));
        survey.addQuestion(saveMe);
        surveyDAO.save(survey);
        survey = surveyDAO.findOne("survey_id1");
        Assert.isTrue(survey.getQuestions().size() == 4, "Survey1 has got 4 questions");
        for(Question question : survey.getQuestions())
            if(question.getName().equals(saveMe.getName()))
                Assert.isTrue(question.getAnswers().size() == 3, "Answers also saved");
        Map<String, Question> questions = survey.getQuestions().stream().
                collect(Collectors.toMap(Question::getName, q -> q));
        Question found = questionDAO.findOne(questions.get("What is the meaning of life?").getId());
        Assert.isTrue(found != null, "Saved question is found");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:surveys.sql")
    public void testEditQuestion() {
        Survey survey = surveyDAO.findOne("survey_id1");
        Assert.isTrue(survey != null, "Survey should be found");
        Question found = questionDAO.findOne("question_id1");
        Assert.isTrue(found != null, "Question should be found");
        found.setName("new name");
        questionDAO.save(found);
        Map<String, Question> questions = survey.getQuestions().stream().
                collect(Collectors.toMap(Question::getName, q -> q));
        Assert.isTrue(questions.containsKey("new name"),
                "Survey's question should be updated in case of manual question saving");
        questions.get("new name").setName("other new name");
        surveyDAO.save(survey);
        found = questionDAO.findOne("question_id1");
        Assert.isTrue(found.getName().equals("other new name"),
                "Question should be updated in case of survey save");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:surveys.sql")
    public void testRemoveQuestion() {
        Survey survey = surveyDAO.findOne("survey_id1");
        Question forDelete = survey.getQuestions().get(0);
        survey.removeQuestion(forDelete);
        surveyDAO.save(survey);
        testEntityManager.flush();
        survey = surveyDAO.findOne("survey_id1");
        Assert.isTrue(survey.getQuestions().size() == 2, "Survey1 has only 2 questions now");
        Question found = questionDAO.findOne(forDelete.getId());
        Assert.isTrue(found == null, "Question should be deleted");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:surveys.sql")
    public void testRemoveSurvey() {
        Survey survey1 = surveyDAO.findOne("survey_id1");
        surveyDAO.delete(survey1);
        Survey survey2 = surveyDAO.findOne("survey_id2");
        surveyDAO.delete(survey2);
        List<Survey> surveys = new ArrayList<>((Collection<Survey>) surveyDAO.findAll());
        Assert.isTrue(surveys.size() == 0, "No surveys");
        List<Question> questions = new ArrayList<>((Collection<Question>) questionDAO.findAll());
        Assert.isTrue(questions.size() == 0, "No questions");
        List<Answer> answers = new ArrayList<>((Collection<Answer>) answerDAO.findAll());
        Assert.isTrue(answers.size() == 0, "No answers");
    }
}