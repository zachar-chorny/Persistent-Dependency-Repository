package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.*;
import com.example.dependencytreeproject.service.ChangeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultChangeService implements ChangeService {

    @Override
    public Map<String, List<String>> getFutureChanges(List<Project> projects,
                                                      List<ProjectInstruction> instructions) {
        Map<String, List<String>> changes = new HashMap<>();
        for (ProjectInstruction instruction : instructions) {
            List<Project> childProjects = getChildProjects(instruction.getName(), projects);
            for (Project project : childProjects) {
                removeNodes(project, instruction, changes);
                replaceNodes(project, instruction, changes);
            }
        }
        return changes;
    }

    private List<Project> getChildProjects(String name, List<Project> projects) {
        List<Project> correctProjects = new ArrayList<>();
        for (Project project : projects) {
            ParentNode parentNode = project.getParentNode();
            if (parentNode != null) {
                if (parentNode.getArtifactId().equals(name)) {
                    correctProjects.add(project);
                    correctProjects.addAll(getChildProjects(project.getName(), projects));
                }
            }
        }
        return correctProjects;
    }

    private Map<String, List<String>> removeNodes(Project project, ProjectInstruction instruction,
                                                  Map<String, List<String>> changes) {
        List<Node> changedNodes = new ArrayList<>(project.getNodes());
        List<String> artifactIds = instruction.getArtifactIdsForRemoving();
        if (artifactIds != null) {
            for (String artifactId : artifactIds) {
                for (Node node : changedNodes) {
                    if (node.getArtifactId().equals(artifactId)) {
                        if (changes.containsKey(artifactId)) {
                            changes.get(artifactId).add(project.getName());
                        } else {
                            changes.put(artifactId, new ArrayList<>(List.of(project.getName())));
                        }
                    }
                }
            }
        }
        return changes;
    }

    private Map<String, List<String>> replaceNodes(Project project, ProjectInstruction instruction,
                                                   Map<String, List<String>> changes) {
        List<Node> changedNodes = new ArrayList<>(project.getNodes());
        List<DependencyNode> dependencyNodes = instruction.getNodesFroReplacing();
        if (dependencyNodes != null) {
            for (DependencyNode dependencyNode : dependencyNodes) {
                for (Node node : changedNodes) {
                    if (dependencyNode.getArtifactId().equals(node.getArtifactId())) {
                        String artifactId = dependencyNode.getArtifactId();
                        if (changes.containsKey(artifactId)) {
                            changes.get(artifactId).add(project.getName());
                        } else {
                            changes.put(artifactId, new ArrayList<>(List.of(project.getName())));
                        }
                    }
                }
            }
        }
        return changes;
    }
}
