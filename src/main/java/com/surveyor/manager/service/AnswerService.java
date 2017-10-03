package com.surveyor.manager.service;

import com.surveyor.manager.data.dao.AnswerDAOService;
import com.surveyor.manager.data.dao.QuestionDAOService;
import com.surveyor.manager.data.dto.ResponseDTO;
import com.surveyor.manager.data.entity.Answer;
import com.surveyor.manager.data.entity.Question;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AnswerService extends AbstractService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private QuestionDAOService questionDAOService;

    @Autowired
    public void setDaoService(AnswerDAOService answerDAOService) {
        this.daoService = answerDAOService;
    }

    @Async
    @Transactional
    public CompletableFuture<ResponseDTO<String>> addToQuestion(String answerId, String questionId) {
        LOGGER.debug("Add {} to {}", answerId, questionId);
        Optional<Answer> answer = ((AnswerDAOService) daoService).findOne(answerId);
        Optional<Question> question = questionDAOService.findOne(questionId);
        if (!answer.isPresent()) {
            return CompletableFuture.completedFuture(fail("No such answer"));
        } else if (!question.isPresent()) {
            return CompletableFuture.completedFuture(fail("No such question"));
        } else {
            Question source = question.get();
            source.addAnswer(answer.get());
            questionDAOService.save(source);
            return CompletableFuture.completedFuture(ok());
        }
    }

    @Async
    @Transactional
    public CompletableFuture<ResponseDTO<String>> delete(String id) {
        LOGGER.debug("Remove {}", id);
        Optional<Answer> answer = ((AnswerDAOService) daoService).findOne(id);
        if (answer.isPresent()) {
            Question question = answer.get().getQuestion();
            question.removeAnswer(answer.get());
            questionDAOService.save(question);
        }
        return CompletableFuture.completedFuture(ok());
    }
}
