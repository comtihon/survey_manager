package com.surveyor.manager.data.dao;

import com.surveyor.manager.data.entity.CommonEntity;
import com.surveyor.manager.data.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionDAOService implements DAOService {
    @Autowired
    private QuestionDAO questionDAO;

    @Override
    public Question save(CommonEntity entity) {
        return questionDAO.save((Question)entity);
    }

    @Override
    public Optional<Question> findOne(String id) {
        return Optional.ofNullable(questionDAO.findOne(id));
    }

    public List<Question> findAll() {
        Iterable<Question> itr = questionDAO.findAll();
        return new ArrayList<>((Collection<Question>) itr);
    }

    public void delete(String id) {
        questionDAO.delete(id);
    }
}
