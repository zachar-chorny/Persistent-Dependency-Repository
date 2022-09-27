package com.example.dependencytreeproject.service;

import com.example.dependencytreeproject.model.document.TreeDocument;
import org.apache.maven.model.Model;

import java.util.Map;

public interface TreeParserService {

    TreeDocument parseToTree(Map.Entry<String, java.util.Map<String, Model>> project);
}
