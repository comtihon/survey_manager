package com.surveyor.manager.controller;

import com.surveyor.manager.data.dto.AnswerDTO;
import com.surveyor.manager.data.dto.ResponseDTO;
import com.surveyor.manager.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
public class AnswerController extends AbstractController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(path = "/answer", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<?>> create(@Valid @RequestBody AnswerDTO answer) {
        CompletableFuture<ResponseDTO<String>> saved = answerService.create(answer);
        return saved.thenApply(this::returnResult);
    }

    @RequestMapping(path = "/answer/{id}", method = RequestMethod.PUT)
    public CompletableFuture<ResponseEntity<?>> edit(
            @PathVariable("id") String id, @RequestBody AnswerDTO answer) {
        answer.setId(id);
        CompletableFuture<ResponseDTO<String>> saved = answerService.update(answer);
        return saved.thenApply(this::returnResult);
    }

    @RequestMapping(path = "/answer/{id}", method = RequestMethod.DELETE)
    public CompletableFuture<ResponseEntity<?>> remove(@PathVariable("id") String id) {
        CompletableFuture<ResponseDTO<String>> removed = answerService.delete(id);
        return removed.thenApply(this::returnResult);
    }

    @RequestMapping(path = "/answer/{id}", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<?>> get(@PathVariable("id") String id) {
        CompletableFuture<ResponseDTO> got = answerService.read(new AnswerDTO(id));
        return got.thenApply(this::returnResult);
    }

    @RequestMapping(path = "/attach/answer", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<?>> attach(
            @RequestParam("answer_id") String answerId, @RequestParam("question_id") String questionId) {
        CompletableFuture<ResponseDTO<String>> attached = answerService.addToQuestion(answerId, questionId);
        return attached.thenApply(this::returnResult);
    }
}
