package com.example.dependencytreeproject.model.document;

import com.example.dependencytreeproject.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
@Data
@AllArgsConstructor
public class TreeDocument {
    private String repository;
    private Map<String, Model> content;
}
