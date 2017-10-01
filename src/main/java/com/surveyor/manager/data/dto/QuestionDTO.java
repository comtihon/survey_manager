package com.surveyor.manager.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.surveyor.manager.data.entity.Question;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDTO implements CommonDTO {
    @JsonProperty("id")
    private String questonId;
    @NotEmpty
    private String name;
    private List<AnswerDTO> answers = new ArrayList<>();

    public QuestionDTO() {
    }

    public QuestionDTO(String questonId) {
        this.questonId = questonId;
    }

    @Override
    public String getId() {
        return questonId;
    }

    public void setId(String questonId) {
        this.questonId = questonId;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    @Override
    public Class getEntityClass() {
        return Question.class;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "name='" + name + '\'' +
                ", answers=" + answers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionDTO that = (QuestionDTO) o;

        if (questonId != null ? !questonId.equals(that.questonId) : that.questonId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return answers != null ? answers.equals(that.answers) : that.answers == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
