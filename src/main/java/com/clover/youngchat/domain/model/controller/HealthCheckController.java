package com.clover.youngchat.domain.model.controller;

import com.clover.youngchat.domain.model.dto.HealthRes;
import com.clover.youngchat.global.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/health")
@RestController
@Slf4j
public class HealthCheckController {

    @GetMapping
    public RestResponse<HealthRes> healthCheck() {
        log.info("healCheck");
        return RestResponse.success(new HealthRes());
    }
}
