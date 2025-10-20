package com.info.configdemo.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class BuildInfoController {

    @Value("${build.id}")
    private String id;

    @Value("${build.version}")
    private String version;

    @Value("${build.name}")
    private String name;

    @GetMapping("/build-info")
    public String getBuildInfo() {
        return "Build ID: "+id + " Version: "+version + " Name: "+name;
    }
}
