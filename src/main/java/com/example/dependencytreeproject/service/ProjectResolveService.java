package com.example.dependencytreeproject.service;

import com.example.dependencytreeproject.model.Project;

import java.util.List;

public interface ProjectResolveService {

    List<Project> resolveProjects(List<Project> projects);
}
