package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.Setting;
import com.example.dependencytreeproject.model.document.TreeDocument;
import com.example.dependencytreeproject.service.*;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Model;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DefaultInitializationService implements InitializationService {
    private final TreeService treeService;
    private final ParseService parseService;
    private final TreeParserService treeParserService;
    private final ModelVersionResolver versionResolver;

    @Override
    public void initializeModelsMap(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        List<TreeDocument> treeDocuments = new ArrayList<>();
        Map<String, Map<String, Model>> projects = new HashMap<>();
        if(files != null){
            for(File loopFile : files){
                projects.put(loopFile.getName(), getProjectModels(loopFile));
            }
        }
        versionResolver.resolveVersions(projects);
        for(Map.Entry<String, Map<String, Model>> project : projects.entrySet()) {
            treeDocuments.add(treeParserService.parseToTree(project));
        }
        treeDocuments.forEach(treeService::save);
    }

    private Map<String, Model> getProjectModels(File file) {
        Setting setting = new Setting();
        setting.setPath(file.getAbsolutePath());
        List<Model> models = parseService.parse(setting);
        Map<String, Model> content = new HashMap<>();
        models.forEach(m -> content.put(m.getArtifactId(), m));
        return content;
    }
}
