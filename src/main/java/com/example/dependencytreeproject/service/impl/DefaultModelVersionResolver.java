package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.model.DependencyNode;
import com.example.dependencytreeproject.model.Model;
import com.example.dependencytreeproject.model.document.TreeDocument;
import com.example.dependencytreeproject.service.ModelVersionResolver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DefaultModelVersionResolver implements ModelVersionResolver {
    private static final String REPLACE_REGEX = "[${}]";
    private static final String VERSION_REGEX = "\\$\\{.+}";
    private static final String PROJECT_VERSION = "${project.version}";
    private List<TreeDocument> actualTreeDocuments;

    @Override
    public void resolveVersions(List<TreeDocument> treeDocuments) {
        actualTreeDocuments = treeDocuments;
        for (TreeDocument treeDocument : treeDocuments) {
            for (Map.Entry<String, Model> model : treeDocument.getContent().entrySet()) {
                List<DependencyNode> dependencies = model.getValue().getDependencies();
                if (dependencies != null) {
                    for (DependencyNode dependency : dependencies) {
                        dependency.setVersion(getVersion(dependency, model.getValue(), treeDocument));
                    }
                }
            }
        }
    }

    private String getVersion(DependencyNode dependency, Model model, TreeDocument treeDocument) {
        String version = dependency.getVersion();
        if (version == null) {
            version = getVersionFromParent(dependency, model, treeDocument);
        }else if(version.equals(PROJECT_VERSION)) {
            return model.getVersion();
        }
        return getVersionFromProperties(model, version);
    }

    private String getVersionFromParent(DependencyNode dependency, Model model, TreeDocument treeDocument) {
        String version = null;
        while (model != null) {
            version = getVersionFromProperties(model, version);
            if (model.getDependencies() != null && version == null) {
                for (DependencyNode dep : model.getDependencies()) {
                    version = findVersion(version, dependency, dep, model);
                }
            }
            version = getVersionFromChildDependency(dependency, model, treeDocument, version);
            model = getParent(model.getParent(), treeDocument);
        }
        return version;
    }

    private List<Model> getModel(String artifactId, TreeDocument treeDocument) {
        Model model = treeDocument.getContent().get(artifactId);
        if (model == null) {
            List<Model> models = new ArrayList<>();
            for (TreeDocument document : actualTreeDocuments) {
                model = document.getContent().get(artifactId);
                if (model != null) {
                    models.add(model);
                }
            }
            return models;
        }
        return List.of(model);
    }

    private Model getParent(String artifactId, TreeDocument treeDocument) {
        return treeDocument.getContent().get(artifactId);
    }

    private String getVersionFromChildDependency(DependencyNode dependency, Model model, TreeDocument treeDocument,
                                                 String version) {
        List<Model> dependencyModels = new ArrayList<>();
        List<DependencyNode> dependencyNodes = model.getDependenciesFromManagement();
        if (dependencyNodes != null) {
            for (DependencyNode dependencyNode : dependencyNodes) {
                String managementVersion = findVersion(version, dependency, dependencyNode, model);
                if (managementVersion != null) {
                    return managementVersion;
                }
                List<Model> models = getModel(dependencyNode.getArtifactId(), treeDocument);
                dependencyModels.addAll(models);
            }
            for (Model dependencyModel : dependencyModels) {
                List<DependencyNode> dependenciesFromManagement = dependencyModel.getDependenciesFromManagement();
                if (dependenciesFromManagement != null) {
                    for (DependencyNode dep : dependencyModel.getDependenciesFromManagement()) {
                        version = findVersion(version, dependency, dep, dependencyModel);
                    }
                }
            }
        }
        return version;
    }


    private String findVersion(String version, DependencyNode mainDependency,
                               DependencyNode dependency, Model model) {
        if (dependency.getArtifactId().equals(mainDependency.getArtifactId())) {
            version = dependency.getVersion();
        }
        return getVersionFromProperties(model, version);
    }

    private String getVersionFromProperties(Model model, String version) {
        if (version != null && version.matches(VERSION_REGEX)) {
            if (model.getProperties() != null) {
                String propertiesVersion = model.getProperties().get(version.replaceAll(REPLACE_REGEX, ""));
                if(propertiesVersion != null) {
                    return propertiesVersion;
                }
            }
        }
        return version;
    }
}
