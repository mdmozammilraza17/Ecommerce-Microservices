package com.info.configdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuildInfo {
    private String id;
    private String version;
    private String name;
}
