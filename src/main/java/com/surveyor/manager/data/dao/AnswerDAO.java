package com.surveyor.manager.data.dao;

import com.surveyor.manager.data.entity.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerDAO extends CrudRepository<Answer, String> {
}
