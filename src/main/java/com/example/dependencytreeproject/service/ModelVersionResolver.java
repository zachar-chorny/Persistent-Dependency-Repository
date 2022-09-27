package com.example.dependencytreeproject.service;


import org.apache.maven.model.Model;

import java.util.Map;

public interface ModelVersionResolver {

    void resolveVersions(Map<String, Map<String, Model>> projects);
}
