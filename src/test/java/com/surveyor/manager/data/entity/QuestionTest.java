package com.surveyor.manager.data.entity;

import com.surveyor.manager.data.dao.AnswerDAO;
import com.surveyor.manager.data.dao.QuestionDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class QuestionTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private QuestionDAO questionDAO;

    @Test
    public void testCRUD() {
        Question question = new Question();
        questionDAO.save(question);
        Assert.isTrue(question.getId() != null, "Id should be filled");
        Question find = questionDAO.findOne(question.getId());
        Assert.isTrue(find != null, "Question should be found");
        Iterable<Question> surveys = questionDAO.findAll();
        Assert.isTrue(surveys.iterator().hasNext(), "Question should be found via findAll");
        Assert.isTrue(question.getId().equals(surveys.iterator().next().getId()), "Id should match");
        questionDAO.delete(question);
        find = questionDAO.findOne(question.getId());
        Assert.isTrue(find == null, "Question should not be found after deletion");
        surveys = questionDAO.findAll();
        Assert.isTrue(!surveys.iterator().hasNext(), "Question list is empty");
    }

    @Test
    public void testWriteWithAnswers() {
        Question question = new Question();
        question.addAnswer(new Answer("yes"));
        question.addAnswer(new Answer("no"));
        question.addAnswer(new Answer("maybe"));
        questionDAO.save(question);
        List<Question> questions = new ArrayList<>((Collection<Question>) questionDAO.findAll());
        Assert.isTrue(questions.size() == 1, "Question is saved");
        question = questions.get(0);
        Assert.isTrue(question.getAnswers().size() == 3, "Question has 3 answers attached");
        List<Answer> answers = new ArrayList<>((Collection<Answer>) answerDAO.findAll());
        Assert.isTrue(answers.size() == 3, "There are 3 answers in the db");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:surveys.sql")
    public void testReadWithAnswers() {
        Question question = questionDAO.findOne("question_id1");
        Assert.isTrue(question.getAnswers().size() == 3, "Question has 3 answers attached");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:surveys.sql")
    public void testEditAnswer() {
        Question question = questionDAO.findOne("question_id1");
        Answer editMe = question.getAnswers().get(0);
        editMe.setName("maybe yes");
        questionDAO.save(question);
        Answer answer = answerDAO.findOne(editMe.getId());
        Assert.isTrue(answer.getName().equals(editMe.getName()), "Name was changed");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:surveys.sql")
    public void testDeleteAnswer() {
        Question question = questionDAO.findOne("question_id1");
        Answer answer = question.getAnswers().get(0);
        question.removeAnswer(answer);
        questionDAO.save(question);
        testEntityManager.flush();
        question = questionDAO.findOne("question_id1");
        Assert.isTrue(question.getAnswers().size() == 2, "One answer was deleted");
        Answer deleted = answerDAO.findOne(answer.getId());
        Assert.isTrue(deleted == null, "Answer was deleted from db");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:surveys.sql")
    public void testDeleteQuestion() {
        Question question = questionDAO.findOne("question_id1");
        questionDAO.delete(question);
        question = questionDAO.findOne("question_id1");
        Assert.isTrue(question == null, "Question was deleted");
        Assert.isTrue(answerDAO.findOne("answer_id1") == null, "Related answers also deleted");
        Assert.isTrue(answerDAO.findOne("answer_id2") == null, "Related answers also deleted");
        Assert.isTrue(answerDAO.findOne("answer_id3") == null, "Related answers also deleted");
    }
}