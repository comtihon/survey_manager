package com.surveyor.manager.data.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question implements CommonEntity {
    @Id
    @Column(name = "question_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @Column
    private String name;

    public Question() {
    }

    public Question(String name) {
        this.name = name;
    }

    public String getId() {
        return questionId;
    }

    public void setId(String questionId) {
        this.questionId = questionId;
    }

    @Override
    public void pullLazy() {
        answers.size();
    }

    @Override
    public void setConnection(CommonEntity entity) {
        if (entity.getClass().equals(Survey.class))
            survey = (Survey) entity;
        answers.forEach(a -> a.setConnection(this));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
        answer.setQuestion(null);
    }

    public Survey getSurvey() {
        return survey;
    }

    void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public void fillUnmodified(Question other) {
        if (name == null || name.isEmpty())
            this.name = other.getName();
        answers = other.answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return questionId != null ? questionId.equals(question.questionId) : question.questionId == null;
    }

    @Override
    public int hashCode() {
        return questionId != null ? questionId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId='" + questionId + '\'' +
                ", answers=" + answers +
                ", name='" + name + '\'' +
                '}';
    }
}
