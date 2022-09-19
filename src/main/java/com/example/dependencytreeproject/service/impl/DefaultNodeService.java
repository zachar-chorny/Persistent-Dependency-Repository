package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.Node;
import com.example.dependencytreeproject.service.NodeService;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Dependency;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class DefaultNodeService implements NodeService {

    @Override
    public Node buildNode(Dependency dependency) {
        return new Node(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(),
                dependency.getType(), dependency.getScope(), new ArrayList<>());
    }
}
