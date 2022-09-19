package com.example.dependencytreeproject.service;

import com.example.dependencytreeproject.model.Project;
import com.example.dependencytreeproject.model.ProjectInstruction;

import java.util.List;
import java.util.Map;

public interface ChangeService {

    Map<String, List<String>> getFutureChanges(List<Project> projects,
                                               List<ProjectInstruction> instructions);
}
