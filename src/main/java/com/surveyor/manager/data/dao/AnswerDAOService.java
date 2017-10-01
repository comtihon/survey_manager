package com.surveyor.manager.data.dao;

import com.surveyor.manager.data.entity.Answer;
import com.surveyor.manager.data.entity.CommonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerDAOService implements DAOService {

    @Autowired
    private AnswerDAO answerDAO;

    @Override
    public Answer save(CommonEntity entity) {
        return answerDAO.save((Answer)entity);
    }

    @Override
    public Optional<Answer> findOne(String id) {
        return Optional.ofNullable(answerDAO.findOne(id));
    }

    public List<Answer> findAll() {
        Iterable<Answer> itr = answerDAO.findAll();
        return new ArrayList<>((Collection<Answer>) itr);
    }

    @Override
    public void delete(String id) {
        answerDAO.delete(id);
    }
}
