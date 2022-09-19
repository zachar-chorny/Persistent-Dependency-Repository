package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.DependencyNode;
import com.example.dependencytreeproject.model.Node;
import com.example.dependencytreeproject.model.Project;
import com.example.dependencytreeproject.model.ProjectInstruction;
import com.example.dependencytreeproject.service.InstructionService;
import com.example.dependencytreeproject.service.NodeService;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Dependency;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class DefaultInstructionService implements InstructionService {
    private final NodeService nodeService;

    @Override
    public List<Project> doInstructions(List<Project> projects, List<ProjectInstruction> instructions) {
        for (ProjectInstruction instruction : instructions) {
            for (Project project : projects) {
                if (instruction.getName().equals(project.getName())) {
                    project.setNewProject(createNewProject(project, instruction));
                }
            }
        }
        return projects;
    }

    private Project createNewProject(Project project, ProjectInstruction instruction) {
        List<Node> nodes = new ArrayList<>(project.getNodes());
        nodes = removeNodes(nodes, instruction);
        nodes = replaceNodes(nodes, instruction);
        nodes = addNodes(nodes, instruction);
        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setNodes(nodes);
        newProject.setParentNode(project.getParentNode());
        return newProject;
    }

    private List<Node> addNodes(List<Node> nodes, ProjectInstruction instruction) {
        List<Node> changedNodes = new ArrayList<>(nodes);
        List<DependencyNode> dependencyNodes = instruction.getNodesForAdding();
        if (dependencyNodes != null) {
            for (DependencyNode dependencyNode : dependencyNodes) {
                changedNodes.add(getNode(dependencyNode));
            }
        }
        return changedNodes;
    }

    private List<Node> replaceNodes(List<Node> nodes, ProjectInstruction instruction) {
        List<Node> changedNodes = new ArrayList<>(nodes);
        List<DependencyNode> dependencyNodes = instruction.getNodesFroReplacing();
        if (dependencyNodes != null) {
            for (DependencyNode dependencyNode : dependencyNodes) {
                for (int i = 0; i < changedNodes.size(); i++) {
                    Node node = changedNodes.get(i);
                    if (dependencyNode.getGroupId().equals(node.getGroupId()) && dependencyNode.getArtifactId().equals(node.getArtifactId())) {
                        changedNodes.set(i, getNode(dependencyNode));
                    }
                }
            }
        }
        return changedNodes;

    }

    private List<Node> removeNodes(List<Node> nodes, ProjectInstruction instruction) {
        List<Node> changedNodes = new ArrayList<>(nodes);
        List<String> artifactIds = instruction.getArtifactIdsForRemoving();
        if (artifactIds != null) {
            for (String artifactId : artifactIds) {
                changedNodes.removeIf(node -> artifactId.equals(node.getArtifactId()));
            }
        }
        return changedNodes;
    }

    private Node getNode(DependencyNode dependencyNode) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(dependencyNode.getGroupId());
        dependency.setArtifactId(dependencyNode.getArtifactId());
        dependency.setType(dependencyNode.getType());
        dependency.setVersion(dependency.getVersion());
        dependency.setScope(dependencyNode.getScope());
        return nodeService.buildNode(dependency);
    }

}
