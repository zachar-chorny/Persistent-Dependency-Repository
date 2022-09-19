package com.example.dependencytreeproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class DependencyNode {

    private String groupId;
    private String artifactId;
    private String version;
    private String type;
    private String scope;
}
