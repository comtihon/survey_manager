package com.surveyor.manager.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.surveyor.manager.data.entity.Survey;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SurveyDTO implements CommonDTO {
    @JsonProperty("id")
    private String surveyId;
    @NotEmpty
    private String name;
    @NotEmpty
    @JsonProperty("country_code")
    private String countryCode;
    private List<QuestionDTO> questions = new ArrayList<>();

    public SurveyDTO() {
    }

    public SurveyDTO(String id) {
        this.surveyId = id;
    }

    public SurveyDTO(String name, String countryCode) {
        this.name = name;
        this.countryCode = countryCode;
    }

    @Override
    public String getId() {
        return surveyId;
    }

    @Override
    public void setId(String id) {
        this.surveyId = id;
    }

    @Override
    public Class getEntityClass() {
        return Survey.class;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "SurveyDTO{" +
                "name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", questions=" + questions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyDTO surveyDTO = (SurveyDTO) o;

        if (surveyId != null ? !surveyId.equals(surveyDTO.surveyId) : surveyDTO.surveyId != null) return false;
        if (name != null ? !name.equals(surveyDTO.name) : surveyDTO.name != null) return false;
        if (countryCode != null ? !countryCode.equals(surveyDTO.countryCode) : surveyDTO.countryCode != null)
            return false;
        return questions != null ? questions.equals(surveyDTO.questions) : surveyDTO.questions == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
