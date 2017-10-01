package com.surveyor.manager.service;

import com.surveyor.manager.data.dao.QuestionDAOService;
import com.surveyor.manager.data.dao.SurveyDAOService;
import com.surveyor.manager.data.dto.ResponseDTO;
import com.surveyor.manager.data.entity.Question;
import com.surveyor.manager.data.entity.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class QuestionService extends AbstractService {

    @Autowired
    private SurveyDAOService surveyDAOService;

    @Autowired
    public void setDaoService(QuestionDAOService questionDAOService) {
        this.daoService = questionDAOService;
    }

    @Async
    @Transactional
    public CompletableFuture<ResponseDTO<String>> addToSurvey(String surveyId, String questionId) {
        logger.debug("Add {} to {}", questionId, surveyId);
        Optional<Question> question = ((QuestionDAOService) daoService).findOne(questionId);
        Optional<Survey> survey = surveyDAOService.findOne(surveyId);
        if (!survey.isPresent()) {
            return CompletableFuture.completedFuture(fail("No such survey"));
        } else if (!question.isPresent()) {
            return CompletableFuture.completedFuture(fail("No such question"));
        } else {
            Survey source = survey.get();
            source.addQuestion(question.get());
            surveyDAOService.save(source);
            return CompletableFuture.completedFuture(ok());
        }
    }
}
