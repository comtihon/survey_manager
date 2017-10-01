package com.surveyor.manager.service;

import com.surveyor.manager.data.dao.SurveyDAOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyService extends AbstractService {

    @Autowired
    public void setDaoService(SurveyDAOService surveyDAOService) {
        this.daoService = surveyDAOService;
    }
}
