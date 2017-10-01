package com.surveyor.manager.data.entity;

import com.surveyor.manager.data.dao.AnswerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AnswerTest {

    @Autowired
    private AnswerDAO answerDAO;

    @Test
    public void testCRUD() {
        Answer answer = new Answer("Yes");
        answerDAO.save(answer);
        Assert.isTrue(answer.getId() != null, "Id should be filled");
        Answer find = answerDAO.findOne(answer.getId());
        Assert.isTrue(find != null, "Answer should be found");
        Iterable<Answer> surveys = answerDAO.findAll();
        Assert.isTrue(surveys.iterator().hasNext(), "Answer should be found via findAll");
        Assert.isTrue(answer.getId().equals(surveys.iterator().next().getId()), "Id should match");
        answerDAO.delete(answer);
        find = answerDAO.findOne(answer.getId());
        Assert.isTrue(find == null, "Answer should not be found after deletion");
        surveys = answerDAO.findAll();
        Assert.isTrue(!surveys.iterator().hasNext(), "Answer list is empty");
    }

}