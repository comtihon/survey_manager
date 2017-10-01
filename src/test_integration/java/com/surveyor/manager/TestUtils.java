package com.surveyor.manager;

import com.surveyor.manager.data.dto.ResponseDTO;
import org.springframework.util.Assert;

class TestUtils {

    static String call(int port, String controller) {
        return "http://localhost:" + port + controller;
    }

    static void check(ResponseDTO responseDTO) {
        Assert.isTrue(responseDTO.isResult(), "result should be true: " + responseDTO);
        Assert.notNull(responseDTO.getResponse(), "response body should not be null");
    }
}
