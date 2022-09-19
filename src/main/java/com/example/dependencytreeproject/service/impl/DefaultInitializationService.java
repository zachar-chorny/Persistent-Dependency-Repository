package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.Setting;
import com.example.dependencytreeproject.model.Model;
import com.example.dependencytreeproject.model.document.TreeDocument;
import com.example.dependencytreeproject.service.InitializationService;
import com.example.dependencytreeproject.service.ModelService;
import com.example.dependencytreeproject.service.ParseService;
import com.example.dependencytreeproject.service.TreeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefaultInitializationService implements InitializationService {
    private final TreeService treeService;
    private final ParseService parseService;
    private final ModelService modelService;

    @Override
    public void initializeModelsMap(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if(files != null){
            for(File loopFile : files){
                addRepositoryTreeToBd(loopFile);
            }
        }
    }

    private void addRepositoryTreeToBd(File file) {
        Setting setting = new Setting();
        setting.setPath(file.getAbsolutePath());
        List<org.apache.maven.model.Model> models = parseService.parse(setting);
        List<Model> customModels = models.stream().map(modelService::parseModel)
                .collect(Collectors.toList());
        Map<String, Model> content = new HashMap<>();
        customModels.forEach(m -> content.put(m.getArtifactId(), m));
        treeService.save(new TreeDocument(file.getName(), content));
    }
}
