package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.Model;
import com.example.dependencytreeproject.service.ModelService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class DefaultModelService implements ModelService {
    @Override
    public Model parseModel(org.apache.maven.model.Model model) {
        Map<String, String> propertiesMap = new HashMap<>();
        Properties properties = model.getProperties();
        for (String name : properties.stringPropertyNames()) {
            propertiesMap.put(name, properties.getProperty(name));
        }
        return Model.builder().artifactId(model.getArtifactId())
                .parent(model.getParent())
                .groupId(model.getGroupId())
                .dependencies(model.getDependencies())
                .dependencyManagement(model.getDependencyManagement())
                .version(model.getVersion())
                .properties(propertiesMap)
                .build();
    }
}
