package com.example.dependencytreeproject.service;

import com.example.dependencytreeproject.model.Project;
import org.apache.maven.model.Model;

public interface ProjectService {

    Project createProjects(Model model);
}
