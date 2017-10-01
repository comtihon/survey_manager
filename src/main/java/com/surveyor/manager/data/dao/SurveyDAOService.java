package com.surveyor.manager.data.dao;

import com.surveyor.manager.data.entity.CommonEntity;
import com.surveyor.manager.data.entity.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyDAOService implements DAOService {
    @Autowired
    private SurveyDAO surveyDAO;

    @Override
    public Survey save(CommonEntity entity) {
        Survey toSave = (Survey) entity;
        if (toSave.getId() != null) {
            Survey existing = surveyDAO.findOne(entity.getId());
            if (existing != null)
                toSave.fillUnmodified(existing);
        }
        return surveyDAO.save(toSave);
    }

    @Override
    public Optional<Survey> findOne(String id) {
        return Optional.ofNullable(surveyDAO.findOne(id));
    }

    public List<Survey> findAll() {
        Iterable<Survey> itr = surveyDAO.findAll();
        return new ArrayList<>((Collection<Survey>) itr);
    }

    @Override
    public void delete(String surveyId) {
        surveyDAO.delete(surveyId);
    }
}
