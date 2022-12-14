package com.example.dependencytreeproject.model;

import lombok.Data;

@Data
public class ParentNode extends Node {
    private ParentNode parentNode;

    public ParentNode() {
    }

    public void setUp(Node node) {
        setArtifactId(node.getArtifactId());
        setGroupId(node.getGroupId());
        setVersion(node.getVersion());
        setScope(null);
        setType(node.getType());
        setChildren(node.getChildren());
    }
}
