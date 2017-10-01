package com.surveyor.manager.data.dao;

import com.surveyor.manager.data.entity.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionDAO extends CrudRepository<Question, String> {
}
