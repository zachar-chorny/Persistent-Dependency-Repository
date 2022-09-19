package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.dao.TreeRepository;
import com.example.dependencytreeproject.model.document.TreeDocument;
import com.example.dependencytreeproject.service.TreeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultTreeService implements TreeService {
    private final TreeRepository treeRepository;

    @Override
    public Optional<TreeDocument> get(String id) {
        return treeRepository.findById(id);
    }

    @Override
    public void save(TreeDocument treeEntity) {
        treeRepository.save(treeEntity);
    }

    @Override
    public void saveAll(List<TreeDocument> treeEntities) {
        treeRepository.saveAll(treeEntities);
    }
}
