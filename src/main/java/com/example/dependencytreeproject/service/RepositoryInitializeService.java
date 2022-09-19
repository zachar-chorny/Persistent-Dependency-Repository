package com.example.dependencytreeproject.service;

import java.util.List;
import java.util.Map;

public interface RepositoryInitializeService {

    void initialize(String path, Map<String, String> repositoryUrls);
}
