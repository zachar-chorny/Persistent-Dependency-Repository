package com.example.dependencytreeproject.service;

import com.example.dependencytreeproject.model.document.TreeDocument;

import java.util.List;

public interface ModelVersionResolver {

    void resolveVersions(List<TreeDocument> treeDocuments);
}
