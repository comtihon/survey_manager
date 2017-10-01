package com.surveyor.manager.controller;

import com.surveyor.manager.data.dto.ResponseDTO;
import com.surveyor.manager.data.dto.SurveyDTO;
import com.surveyor.manager.service.AbstractService;
import com.surveyor.manager.service.SurveyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(SurveyController.class)
public class SurveyControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean(classes = {SurveyService.class})
    private AbstractService service;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void saveSurvey() throws Exception {
        given(service.create(any(SurveyDTO.class))).willReturn(mockReturn("id"));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post("/survey")
                        .content("{\"name\":\"name\",\"country_code\":\"cc\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        mvc.perform(asyncDispatch(result))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(contentType))
                .andExpect(jsonPath("$.response", is("id")))
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void loadSurvey() throws Exception {
        given(service.create(any(SurveyDTO.class))).willReturn(mockReturn("id"));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post("/survey/load")
                        .content("{\"name\":\"name\",\"country_code\":\"cc\", \"questions\": [" +
                                "{\"name\":\"q1\",\"answers\":[{\"name\":\"yes\"}, {\"name\":\"no\"}]}]}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        mvc.perform(asyncDispatch(result))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(contentType))
                .andExpect(jsonPath("$.response", is("id")))
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void removeSurvey() throws Exception {
        given(service.delete("id")).willReturn(mockReturn("ok"));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.delete("/survey/id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        mvc.perform(asyncDispatch(result))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(contentType))
                .andExpect(jsonPath("$.response", is("ok")))
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void editSurvey() throws Exception {
        given(service.update(any(SurveyDTO.class))).willReturn(mockReturn("ok"));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.put("/survey/id")
                        .content("{\"name\":\"new_name\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        mvc.perform(asyncDispatch(result))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(contentType))
                .andExpect(jsonPath("$.response", is("ok")))
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void getSurvey() throws Exception {
        SurveyDTO survey = new SurveyDTO("test", "cc");
        given(service.read(new SurveyDTO("id"))).willReturn(mockReturn(survey));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get("/survey/id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        mvc.perform(asyncDispatch(result))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(contentType))
                .andExpect(jsonPath("$.response.name", is("test")))
                .andExpect(jsonPath("$.response.country_code", is("cc")))
                .andExpect(jsonPath("$.result", is(true)));
    }

    private CompletableFuture<ResponseDTO<String>> mockReturn(String data) {
        return CompletableFuture.completedFuture(new ResponseDTO<>(data));
    }

    private CompletableFuture<ResponseDTO> mockReturn(SurveyDTO data) {
        return CompletableFuture.completedFuture(new ResponseDTO<>(data));
    }
}