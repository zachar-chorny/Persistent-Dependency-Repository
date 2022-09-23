package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.DependencyNode;
import com.example.dependencytreeproject.model.Model;
import com.example.dependencytreeproject.service.ModelService;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Parent;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultModelService implements ModelService {
    @Override
    public Model parseModel(org.apache.maven.model.Model model) {
        Map<String, String> propertiesMap = null;
        List<DependencyNode> dependencies = null;
        List<DependencyNode> dependenciesFromManagement = null;
        if (!model.getProperties().isEmpty()) {
            propertiesMap = new HashMap<>();
            Properties properties = model.getProperties();
            for (String name : properties.stringPropertyNames()) {
                propertiesMap.put(name, properties.getProperty(name));
            }
        }
        List<Dependency> modelDependencies = model.getDependencies();
        if (!modelDependencies.isEmpty()) {
            dependencies = new ArrayList<>();
            for (Dependency dependency : modelDependencies) {
                dependencies.add(parseToDependencyNode(dependency));
            }
        }
        DependencyManagement dependencyManagement = model.getDependencyManagement();
        if (dependencyManagement != null) {
            List<Dependency> modelDependenciesFromManagement = dependencyManagement.getDependencies();
            if (!modelDependenciesFromManagement.isEmpty()) {
                dependenciesFromManagement = new ArrayList<>();
                for (Dependency dependency : modelDependenciesFromManagement) {
                    dependenciesFromManagement.add(parseToDependencyNode(dependency));
                }
            }
        }
        String parent = null;
        Parent modelParent = model.getParent();
        if (modelParent != null) {
            parent = modelParent.getArtifactId();
        }
        return Model.builder().artifactId(model.getArtifactId())
                .parent(parent)
                .groupId(model.getGroupId())
                .version(model.getVersion())
                .dependencies(dependencies)
                .dependenciesFromManagement(dependenciesFromManagement)
                .properties(propertiesMap)
                .build();
    }

    private DependencyNode parseToDependencyNode(Dependency dependency) {
        return new DependencyNode(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion()
                , dependency.getType(), dependency.getScope());
    }
}
