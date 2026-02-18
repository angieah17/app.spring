package com.midominio.group.app.spring.mongo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.midominio.group.app.spring.mongo.entity.TestLog;
import com.midominio.group.app.spring.mongo.service.TestLogService;

@RestController
@RequestMapping("/api/mongo/logs")
public class TestLogController {

    private final TestLogService testLogService;

    public TestLogController(TestLogService testLogService) {
        this.testLogService = testLogService;
    }

    @PostMapping
    public TestLog saveLog(@RequestParam String username, @RequestParam Double nota) {
        return testLogService.saveLog(username, nota);
    }

    @GetMapping
    public List<TestLog> findAll() {
        return testLogService.findAll();
    }
}
