package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.DependencyNode;
import com.example.dependencytreeproject.model.Model;
import com.example.dependencytreeproject.service.ModelService;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Parent;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultModelService implements ModelService {
    @Override
    public Model parseModel(org.apache.maven.model.Model model) {
        List<DependencyNode> dependencies = null;
        List<Dependency> modelDependencies = model.getDependencies();
        if (!modelDependencies.isEmpty()) {
            dependencies = new ArrayList<>();
            for (Dependency dependency : modelDependencies) {
                dependencies.add(parseToDependencyNode(dependency));
            }
        }
        DependencyManagement dependencyManagement = model.getDependencyManagement();
        if (dependencyManagement != null) {
            List<Dependency> modelDependenciesFromManagement = dependencyManagement.getDependencies();
            if (!modelDependenciesFromManagement.isEmpty()) {
                if(dependencies == null){
                    dependencies = new ArrayList<>();
                }
                for (Dependency dependency : modelDependenciesFromManagement) {
                    dependencies.add(parseToDependencyNode(dependency));
                }
            }
        }
        String parent = null;
        Parent modelParent = model.getParent();
        if (modelParent != null) {
            parent = modelParent.getArtifactId();
        }
        return Model.builder().artifactId(model.getArtifactId())
                .parent(parent)
                .groupId(model.getGroupId())
                .version(model.getVersion())
                .dependencies(dependencies)
                .build();
    }

    private DependencyNode parseToDependencyNode(Dependency dependency) {
        return new DependencyNode(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion()
                , dependency.getType(), dependency.getScope());
    }
}
