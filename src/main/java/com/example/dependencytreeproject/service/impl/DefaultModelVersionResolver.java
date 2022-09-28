package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.service.ModelVersionResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DefaultModelVersionResolver implements ModelVersionResolver {
    private static final String REPLACE_REGEX = "[${}]";
    private static final String VERSION_REGEX = "\\$\\{.+}";
    private static final String PROJECT_VERSION = "${project.version}";
    private static final String VERSION = ".version";
    private Map<String, Map<String, Model>> actualProjects;
    private Map.Entry<String, Map<String, Model>> actualProject;

    @Override
    public void resolveVersions(Map<String, Map<String, Model>> projects) {
        actualProjects = projects;
        for (Map.Entry<String, Map<String, Model>> project : projects.entrySet()) {
            for (Map.Entry<String, Model> models : project.getValue().entrySet()) {
                Model model = models.getValue();
                if (model.getVersion() == null) {
                    model.setVersion(initializeVersion(model, project));
                }
            }
        }
        for (Map.Entry<String, Map<String, Model>> project : projects.entrySet()) {
            for (Map.Entry<String, Model> models : project.getValue().entrySet()) {
                if (models.getKey().equals("apic-em-inventory-manager-service-das-client")) {
                    System.out.println();
                }
                List<Dependency> dependencies = models.getValue().getDependencies();
                if (dependencies != null) {
                    for (Dependency dependency : dependencies) {
                        if (dependency.getArtifactId().equals("maglev-adapter-sdk")) {
                            System.out.println();
                        }
                        dependency.setVersion(getVersion(dependency, models.getValue(), project));
                    }
                }
                DependencyManagement dependencyManagement = models.getValue().getDependencyManagement();
                if (dependencyManagement != null) {
                    for (Dependency dependency : dependencyManagement.getDependencies()) {
                        dependency.setVersion(getVersion(dependency, models.getValue(), project));
                    }
                }
            }
        }
    }

    private String initializeVersion(Model model,
                                     Map.Entry<String, Map<String, Model>> project) {
        actualProject = project;
        String version = model.getVersion();
        while (version == null) {
            Parent parent = model.getParent();
            model = getParent(parent, actualProject);
            version = model.getVersion();
        }
        return version;
    }

    private String getVersion(Dependency dependency, Model model, Map.Entry<String, Map<String, Model>> project) {
        String version = dependency.getVersion();
        if (version == null) {
            version = getVersionFromParent(dependency, model, project);
        } else if (version.equals(PROJECT_VERSION)) {
            return model.getVersion();
        }
        version = getVersionFromProperties(model, version, dependency);
        if(version == null) {
            System.out.println();
        }
        if (version != null && version.matches(VERSION_REGEX)) {
            return getVersionFromParent(dependency, model, project);
        }
        return version;
    }

    private Model getSingleModel(String artifactId, Map.Entry<String, Map<String, Model>> project) {
        return project.getValue().get(artifactId);
    }

    private String getVersionFromParent(Dependency dependency, Model model, Map.Entry<String, Map<String, Model>> project) {
        String version = null;
        actualProject = project;
        while (model != null) {
            version = getVersionFromProperties(model, version, dependency);
            if (model.getDependencies() != null && version == null) {
                for (Dependency dep : model.getDependencies()) {
                    version = findVersion(version, dependency, dep, model);
                }
            }
            version = getVersionFromChildDependency(dependency, model, actualProject, version);
            model = getParent(model.getParent(), actualProject);
        }
        return version;
    }

    private List<Model> getModel(String artifactId, Map.Entry<String, Map<String, Model>> project) {
        Model model = getSingleModel(artifactId, project);
        if (model == null) {
            List<Model> models = new ArrayList<>();
            for (Map.Entry<String, Map<String, Model>> document : actualProjects.entrySet()) {
                model = document.getValue().get(artifactId);
                if (model != null) {
                    models.add(model);
                }
            }
            return models;
        }
        return List.of(model);
    }

    private Model getParent(Parent parent, Map.Entry<String, Map<String, Model>> project) {
        if (parent == null) {
            return null;
        }
        Model model = getSingleModel(parent.getArtifactId(), project);
        if (model == null) {
            for (Map.Entry<String, Map<String, Model>> document : actualProjects.entrySet()) {
                model = document.getValue().get(parent.getArtifactId());
                if (model != null) {
                    actualProject = document;
                    return model;
                }
            }
        }
        return project.getValue().get(parent.getArtifactId());
    }

    private String getVersionFromChildDependency(Dependency dependency, Model model,
                                                 Map.Entry<String, Map<String, Model>> project, String version) {
        List<Model> dependencyModels = new ArrayList<>();
        DependencyManagement dependencyManagement = model.getDependencyManagement();
        if (dependencyManagement != null) {
            for (Dependency dependencyNode : dependencyManagement.getDependencies()) {
                String managementVersion = findVersion(version, dependency, dependencyNode, model);
                if (managementVersion != null) {
                    return managementVersion;
                }
                List<Model> models = getModel(dependencyNode.getArtifactId(), project);
                dependencyModels.addAll(models);
            }
            for (Model dependencyModel : dependencyModels) {
                DependencyManagement childManagement = dependencyModel.getDependencyManagement();
                if (childManagement != null) {
                    for (Dependency dep : childManagement.getDependencies()) {
                        String parentVersion = findVersion(version, dependency, dep, dependencyModel);
                        if (parentVersion != null) {
                            return parentVersion;
                        }
                    }
                }
            }
        }
        return version;
    }


    private String findVersion(String version, Dependency mainDependency,
                               Dependency dependency, Model model) {
        if (dependency.getArtifactId().equals(mainDependency.getArtifactId())
                && dependency.getVersion() != null) {
            version = dependency.getVersion();
        }
        return getVersionFromProperties(model, version, dependency);
    }

    private String getVersionFromProperties(Model model, String version, Dependency dependency) {
        if (version != null && version.matches(VERSION_REGEX)) {
            if (model.getProperties() != null) {
                String propertiesVersion = model.getProperties()
                        .getProperty(version.replaceAll(REPLACE_REGEX, ""));
                if (propertiesVersion != null) {
                    return propertiesVersion;
                }
            }
        }
        if (version == null) {
            String defaultProperty = dependency.getArtifactId() + VERSION;
            String updatedProperty = dependency.getArtifactId().replaceAll("-", ".") + VERSION;
            if (model.getProperties() != null) {
                String propertiesVersion = model.getProperties()
                        .getProperty(defaultProperty);
                if (propertiesVersion != null) {
                    return propertiesVersion;
                }
                propertiesVersion = model.getProperties()
                        .getProperty(updatedProperty);
                if (propertiesVersion != null) {
                    return propertiesVersion;
                }
            }
        }
        return version;
    }
}
