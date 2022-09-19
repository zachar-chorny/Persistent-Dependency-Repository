package com.example.dependencytreeproject.service;

import org.eclipse.aether.artifact.Artifact;

public interface ArtifactResolveService {

    Artifact resolve(Artifact artifact);
}
