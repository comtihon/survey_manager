package com.surveyor.manager.data.dao;

import com.surveyor.manager.data.entity.CommonEntity;

import java.util.Optional;

public interface DAOService<T extends CommonEntity> {
    CommonEntity save(CommonEntity entity);

    Optional<T> findOne(String id);

    void delete(String id);
}
