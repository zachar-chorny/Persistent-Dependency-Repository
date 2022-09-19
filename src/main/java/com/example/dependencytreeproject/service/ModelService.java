package com.example.dependencytreeproject.service;

import com.example.dependencytreeproject.model.Model;

public interface ModelService {

    Model parseModel(org.apache.maven.model.Model model);
}
