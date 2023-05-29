package com.devhub.pinchesystemback.service.impl;


import com.devhub.pinchesystemback.domain.Log;
import com.devhub.pinchesystemback.exception.BusinessException;
import com.devhub.pinchesystemback.mapper.LogMapper;
import com.devhub.pinchesystemback.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wak
 */
@Service
public class LogServiceImpl implements LogService {
    @Resource
    private LogMapper mapper;

    @Override
    public void addLog(Log log) {
        mapper.insert(log);
    }
}
