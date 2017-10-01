package com.surveyor.manager.controller;

import com.surveyor.manager.data.dto.QuestionDTO;
import com.surveyor.manager.data.dto.ResponseDTO;
import com.surveyor.manager.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
public class QuestionController extends AbstractController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(path = "/question", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<?>> create(@Valid @RequestBody QuestionDTO questionDTO) {
        CompletableFuture<ResponseDTO<String>> saved = questionService.create(questionDTO);
        return saved.thenApply(this::returnResult);
    }

    @RequestMapping(path = "/question/{id}", method = RequestMethod.PUT)
    public CompletableFuture<ResponseEntity<?>> edit(
            @PathVariable("id") String id, @RequestBody QuestionDTO questionDTO) {
        questionDTO.setId(id);
        CompletableFuture<ResponseDTO<String>> saved = questionService.update(questionDTO);
        return saved.thenApply(this::returnResult);
    }

    @RequestMapping(path = "/question/{id}", method = RequestMethod.DELETE)
    public CompletableFuture<ResponseEntity<?>> remove(@PathVariable("id") String id) {
        CompletableFuture<ResponseDTO<String>> removed = questionService.delete(id);
        return removed.thenApply(this::returnResult);
    }

    @RequestMapping(path = "/question/{id}", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<?>> get(@PathVariable("id") String id) {
        CompletableFuture<ResponseDTO> got = questionService.read(new QuestionDTO(id));
        return got.thenApply(this::returnResult);
    }

    @RequestMapping(path = "/attach/question", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<?>> attach(
            @RequestParam("question_id") String questionId, @RequestParam("survey_id") String surveyId) {
        CompletableFuture<ResponseDTO<String>> attached = questionService.addToSurvey(surveyId, questionId);
        return attached.thenApply(this::returnResult);
    }
}
