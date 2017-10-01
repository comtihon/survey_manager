package com.surveyor.manager.data.dao;

import com.surveyor.manager.data.entity.Survey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyDAO extends CrudRepository<Survey, String> {
}
