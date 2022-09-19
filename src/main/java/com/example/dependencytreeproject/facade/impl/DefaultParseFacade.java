package com.example.dependencytreeproject.facade.impl;

import com.example.dependencytreeproject.facade.ParseFacade;
import com.example.dependencytreeproject.model.Project;
import com.example.dependencytreeproject.model.ProjectInstruction;
import com.example.dependencytreeproject.model.Setting;
import com.example.dependencytreeproject.service.InstructionService;
import com.example.dependencytreeproject.service.ParseService;
import com.example.dependencytreeproject.service.ProjectService;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Model;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefaultParseFacade implements ParseFacade {

    private final ParseService parseService;
    private final ProjectService projectService;
    private final InstructionService instructionService;

    @Override
    public List<Project> parseToProjects(Setting setting) {
        List<Model> models = parseService.parse(setting);
        List<Project> projects = models.stream()
                .map(projectService::createProjects)
                .collect(Collectors.toList());
        List<ProjectInstruction> instructions = setting.getInstructions();
        if(instructions != null){
            projects = instructionService.doInstructions(projects, instructions);
        }
        return projects;
    }


}
