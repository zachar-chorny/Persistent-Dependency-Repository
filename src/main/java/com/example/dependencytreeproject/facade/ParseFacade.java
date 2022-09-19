package com.example.dependencytreeproject.facade;

import com.example.dependencytreeproject.model.Project;
import com.example.dependencytreeproject.model.Setting;

import java.util.List;

public interface ParseFacade {

    List<Project> parseToProjects(Setting setting);
}
