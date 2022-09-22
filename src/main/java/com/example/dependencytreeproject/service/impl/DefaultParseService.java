package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.CallableModelTask;
import com.example.dependencytreeproject.model.Setting;
import com.example.dependencytreeproject.service.ParseService;
import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class DefaultParseService implements ParseService {
    private static final int MAX_THREADS = 50;
    private final MavenXpp3Reader reader;

    @Override
    public List<Model> parse(Setting setting) {
        File mainDirectory = new File(setting.getPath());
        List<File> files = getFiles(mainDirectory);
        return getModels(files);
    }

    @Override
    public Optional<Model> parseFile(File file) {
        Model model;
        try {
            model = reader.read(new FileReader(file));
            model.setPomFile(file);
            if(model.getGroupId() == null) {
                model.setGroupId(model.getParent().getGroupId());
            }
        } catch (XmlPullParserException | IOException ignored) {
            return Optional.empty();
        }
        return Optional.of(model);
    }

    private List<Model> getModels(List<File> files) {
        List<Model> models = new ArrayList<>();
        ExecutorService executorService;
        if (files.size() < MAX_THREADS) {
            executorService = Executors.newCachedThreadPool();
        } else {
            executorService = Executors.newFixedThreadPool(MAX_THREADS);
        }
        List<Future<Optional<Model>>> futures = new ArrayList<>();
        for (File iterFile : files) {
            futures.add(executorService.submit(new CallableModelTask(this, iterFile)));
        }
        for (Future<Optional<Model>> future : futures) {
            try {
                future.get().ifPresent(models::add);
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
        executorService.shutdown();
        return models;
    }

    private List<File> getFiles(File file) {
        List<File> fileList = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(f -> fileList.addAll(getFiles(f)));
            }
        } else {
            if (file.getName().equals("pom.xml")) {
                fileList.add(file);
            }
        }
        return fileList;
    }
}
