package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.service.RepositoryInitializeService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class DefaultRepositoryInitializeService implements RepositoryInitializeService {
    private static final String USERNAME = "yservatk";
    private static final String PASSWORD = "ghtyGHTY34#$";

    @Override
    public void initialize(String path, Map<String, String> repositoryUrls) {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider
                (USERNAME, PASSWORD);
        TextProgressMonitor consoleProgressMonitor = new TextProgressMonitor(new PrintWriter(System.out));
        for (Map.Entry url : repositoryUrls.entrySet()) {
            File localRepository = new File(path + "/" + url.getKey());
            try(Repository repo = Git.cloneRepository()
                    .setCredentialsProvider(credentialsProvider)
                    .setProgressMonitor(consoleProgressMonitor).setDirectory(localRepository)
                    .setURI((String) url.getValue()).call().getRepository()){
            } catch (GitAPIException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
