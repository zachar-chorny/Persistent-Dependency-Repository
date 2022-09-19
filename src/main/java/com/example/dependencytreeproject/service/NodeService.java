package com.example.dependencytreeproject.service;

import com.example.dependencytreeproject.model.Node;
import org.apache.maven.model.Dependency;

public interface NodeService {

    Node buildNode(Dependency dependency);

}
