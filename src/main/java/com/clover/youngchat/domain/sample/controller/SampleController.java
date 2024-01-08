package com.clover.youngchat.domain.sample.controller;

import com.clover.youngchat.domain.sample.dto.request.SampleSaveReq;
import com.clover.youngchat.domain.sample.dto.response.SampleGetRes;
import com.clover.youngchat.domain.sample.dto.response.SampleSaveRes;
import com.clover.youngchat.domain.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/samples")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/{sampleId}")
    public ResponseEntity<SampleGetRes> getSample(@PathVariable Long sampleId) {
        return ResponseEntity.ok(sampleService.getSample(sampleId));
    }

    @PostMapping
    public ResponseEntity<SampleSaveRes> saveSample(@RequestBody SampleSaveReq sampleSaveReq) {
        return ResponseEntity.ok(sampleService.saveSample(sampleSaveReq));
    }

}
