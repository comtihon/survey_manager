package com.surveyor.manager.data.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey")
public class Survey implements CommonEntity {
    @Id
    @Column(name = "survey_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String surveyId;

    @Column
    private String name;

    @Column(name = "country_code", length = 5)
    private String countryCode;

    @OneToMany(orphanRemoval = true, mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    public Survey(String countryCode, String name) {
        this.countryCode = countryCode;
        this.name = name;
    }

    public Survey() {
    }

    public String getId() {
        return surveyId;
    }

    public void setId(String surveyId) {
        this.surveyId = surveyId;
    }

    @Override
    public void pullLazy() {
        questions.forEach(Question::pullLazy);
    }

    @Override
    public void setConnection(CommonEntity entity) {
        questions.forEach(a -> a.setConnection(Survey.this));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
        question.setSurvey(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setSurvey(null);
    }

    /**
     * Fill not set parameters of this survey from database
     * in order to avoid overriding real values with nulls.
     *
     * @param other filled entity from database
     */
    public void fillUnmodified(Survey other) {
        if (name == null || name.isEmpty())
            this.name = other.getName();
        if (countryCode == null || countryCode.isEmpty())
            this.countryCode = other.countryCode;
        questions = other.getQuestions();
    }

    @Override
    public String toString() {
        return "Survey{" +
                "surveyId='" + surveyId + '\'' +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", questions=" + questions +
                '}';
    }
}
