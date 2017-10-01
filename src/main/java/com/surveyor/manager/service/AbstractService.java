package com.surveyor.manager.service;

import com.surveyor.manager.data.dao.DAOService;
import com.surveyor.manager.data.dto.CommonDTO;
import com.surveyor.manager.data.dto.ResponseDTO;
import com.surveyor.manager.data.entity.CommonEntity;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public abstract class AbstractService {

    static final Logger logger = LoggerFactory.getLogger(SurveyService.class);

    DAOService daoService;

    @Autowired
    private ModelMapper modelMapper;

    @Async
    public CompletableFuture<ResponseDTO<String>> create(CommonDTO dto) {
        logger.debug("Create {}", dto);
        dto.setId(null);
        CommonEntity toSave = modelMapper.map(dto, dto.getEntityClass());
        toSave.setConnection(toSave);
        CommonEntity saved = daoService.save(toSave);
        return CompletableFuture.completedFuture(ok(saved.getId()));
    }

    @Async
    @Transactional
    public CompletableFuture<ResponseDTO> read(CommonDTO dto) {
        logger.debug("Get {}", dto.getId());
        Optional entity = daoService.findOne(dto.getId());
        if (entity.isPresent()) {
            CommonEntity read = (CommonEntity) entity.get();
            read.pullLazy();
            CommonDTO returnMe = modelMapper.map(read, dto.getClass());
            return CompletableFuture.completedFuture(ok(returnMe));
        }
        return CompletableFuture.completedFuture(fail("No such entity"));
    }

    @Async
    public CompletableFuture<ResponseDTO<String>> update(CommonDTO dto) {
        logger.debug("Edit {}", dto);
        CommonEntity mapped = modelMapper.map(dto, dto.getEntityClass());
        if (!daoService.findOne(mapped.getId()).isPresent())
            return CompletableFuture.completedFuture(fail("No such entity")); //TODO move error messages to static
        daoService.save(mapped);
        return CompletableFuture.completedFuture(ok());
    }

    @Async
    public CompletableFuture<ResponseDTO<String>> delete(String id) {
        logger.debug("Remove {}", id);
        daoService.delete(id);
        return CompletableFuture.completedFuture(ok());
    }


    ResponseDTO<String> fail(String message) {
        return new ResponseDTO<>(false, message);
    }

    ResponseDTO<String> ok() {
        return ok("ok");
    }

    private <T> ResponseDTO<T> ok(T message) {
        return new ResponseDTO<>(message);
    }
}
