package com.surveyor.manager.data.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "answer")
public class Answer implements CommonEntity {
    @Id
    @Column(name = "answer_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column
    private String name;

    public Answer() {
    }

    public Answer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Question getQuestion() {
        return question;
    }

    void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String getId() {
        return answerId;
    }

    public void setId(String answerId) {
        this.answerId = answerId;
    }

    @Override
    public void pullLazy() {
    }

    @Override
    public void setConnection(CommonEntity question) {
        if (question.getClass().equals(Question.class))
            this.question = (Question) question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        return answerId != null ? answerId.equals(answer.answerId) : answer.answerId == null;
    }

    @Override
    public int hashCode() {
        return answerId != null ? answerId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerId='" + answerId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
