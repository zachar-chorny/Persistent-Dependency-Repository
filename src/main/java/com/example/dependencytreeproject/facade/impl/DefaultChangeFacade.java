package com.example.dependencytreeproject.facade.impl;

import com.example.dependencytreeproject.facade.ChangeFacade;
import com.example.dependencytreeproject.model.Project;
import com.example.dependencytreeproject.model.ProjectInstruction;
import com.example.dependencytreeproject.model.Setting;
import com.example.dependencytreeproject.service.ChangeService;
import com.example.dependencytreeproject.service.ParseService;
import com.example.dependencytreeproject.service.ProjectService;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Model;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefaultChangeFacade implements ChangeFacade {

    private final ParseService parseService;
    private final ProjectService projectService;
    private final ChangeService changeService;

    @Override
    public Map<String, List<String>> getFutureChanges(Setting setting) {
        List<Model> models = parseService.parse(setting);
        List<Project> projects = models.stream()
                .map(projectService::createProjects)
                .collect(Collectors.toList());
        List<ProjectInstruction> instructions = setting.getInstructions();
        return changeService.getFutureChanges(projects, instructions);
    }
}
