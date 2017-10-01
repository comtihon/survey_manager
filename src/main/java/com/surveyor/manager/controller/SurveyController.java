package com.surveyor.manager.controller;

import com.surveyor.manager.data.dto.ResponseDTO;
import com.surveyor.manager.data.dto.SurveyDTO;
import com.surveyor.manager.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
public class SurveyController extends AbstractController {

    @Autowired
    private SurveyService surveyService;

    /**
     * Load ready survey in json format or just create an empty survey
     *
     * @return survey_id in case load is OK
     */
    @RequestMapping(path = {"/survey/load", "/survey"}, method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<?>> load(@Valid @RequestBody SurveyDTO survey) {
        CompletableFuture<ResponseDTO<String>> saved = surveyService.create(survey);
        return saved.thenApply(this::returnResult);
    }

    /**
     * Edit survey
     *
     * @param id     survey's id
     * @param survey changes
     * @return ok
     */
    @RequestMapping(path = "/survey/{id}", method = RequestMethod.PUT)
    public CompletableFuture<ResponseEntity<?>> edit(
            @PathVariable("id") String id, @RequestBody SurveyDTO survey) {
        survey.setId(id);
        CompletableFuture<ResponseDTO<String>> saved = surveyService.update(survey);
        return saved.thenApply(this::returnResult);
    }

    /**
     * Remove survey
     *
     * @param id to be removed
     * @return ok
     */
    @RequestMapping(path = "/survey/{id}", method = RequestMethod.DELETE)
    public CompletableFuture<ResponseEntity<?>> remove(@PathVariable("id") String id) {
        CompletableFuture<ResponseDTO<String>> removed = surveyService.delete(id);
        return removed.thenApply(this::returnResult);
    }

    /**
     * Get survey
     *
     * @param id of survey
     * @return survey
     */
    @RequestMapping(path = "/survey/{id}", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<?>> get(@PathVariable("id") String id) {
        CompletableFuture<ResponseDTO> got = surveyService.read(new SurveyDTO(id));
        return got.thenApply(this::returnResult);
    }
}
