package com.example.dependencytreeproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class Model {
    private String groupId;
    private String artifactId;
    private String version;
    private String parent;
    private List<DependencyNode> dependencies;
    private List<DependencyNode> dependenciesFromManagement;
    private Map<String, String> properties;
}
