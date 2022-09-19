package com.example.dependencytreeproject.dao;

import com.example.dependencytreeproject.model.document.TreeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TreeRepository extends MongoRepository<TreeDocument, String> {

}
