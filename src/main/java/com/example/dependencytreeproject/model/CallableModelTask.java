package com.example.dependencytreeproject.model;

import com.example.dependencytreeproject.service.ParseService;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Model;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class CallableModelTask implements Callable<Optional<Model>> {
    private final ParseService parseService;
    private final File file;


    @Override
    public Optional<Model> call() throws Exception {
        return parseService.parseFile(file);
    }
}
