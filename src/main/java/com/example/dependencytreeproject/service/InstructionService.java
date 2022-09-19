package com.example.dependencytreeproject.service;


import com.example.dependencytreeproject.model.Project;
import com.example.dependencytreeproject.model.ProjectInstruction;

import java.util.List;

public interface InstructionService {

    List<Project> doInstructions(List<Project> projects, List<ProjectInstruction> instructions);

}
