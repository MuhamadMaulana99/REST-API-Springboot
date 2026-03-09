package com.example.demoapi.service;

import com.example.demoapi.model.UserLog;
import com.example.demoapi.repository.UserLogRepository;
import com.example.demoapi.utils.DeviceInfoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserLogService {

    @Autowired
    private UserLogRepository logRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void saveLog(HttpServletRequest request, String email, String action, Object oldData, Object newData) {
        String ua = request.getHeader("User-Agent");
        
        UserLog log = new UserLog();
        log.setEmail(email);
        log.setAction(action);
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(ua);
        log.setDevice(DeviceInfoUtil.getDevice(ua));
        log.setBrowser(DeviceInfoUtil.getBrowser(ua));
        log.setOs(DeviceInfoUtil.getOS(ua));
        log.setTimestamp(LocalDateTime.now());

        try {
            if (oldData != null) log.setOldValue(objectMapper.writeValueAsString(oldData));
            if (newData != null) log.setNewValue(objectMapper.writeValueAsString(newData));
        } catch (Exception e) {
            log.setOldValue("Error parsing data: " + e.getMessage());
        }

        logRepository.save(log);
    }
}