package com.example.dependencytreeproject.service;

import com.example.dependencytreeproject.model.document.TreeDocument;

import java.util.List;
import java.util.Optional;

public interface TreeService {

    Optional<TreeDocument> get(String id);

    void save(TreeDocument treeEntity);

    void saveAll(List<TreeDocument> treeEntities);

}
