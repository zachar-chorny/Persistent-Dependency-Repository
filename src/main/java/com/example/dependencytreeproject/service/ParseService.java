package com.example.dependencytreeproject.service;

import com.example.dependencytreeproject.model.Setting;
import org.apache.maven.model.Model;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface ParseService {

    List<Model> parse(Setting setting);

    Optional<Model> parseFile(File file);
}
