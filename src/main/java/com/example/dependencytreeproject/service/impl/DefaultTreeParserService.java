package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.document.TreeDocument;
import com.example.dependencytreeproject.service.ModelService;
import com.example.dependencytreeproject.service.TreeParserService;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Model;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DefaultTreeParserService implements TreeParserService {
    private final ModelService modelService;

    @Override
    public TreeDocument parseToTree(Map.Entry<String, Map<String, Model>> project) {
        Map<String, com.example.dependencytreeproject.model.Model> models = new HashMap<>();
        for(Map.Entry<String, Model> modelEntry : project.getValue().entrySet()) {
            models.put(modelEntry.getKey(), modelService.parseModel(modelEntry.getValue()));
        }
        return new TreeDocument(project.getKey(), models);
    }
}
