package com.example.dependencytreeproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Parent;

import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class Model {
    private String groupId;
    private String artifactId;
    private String version;
    private Parent parent;
    private List<Dependency> dependencies;
    private DependencyManagement dependencyManagement;
    private Map<String, String> properties;
}
