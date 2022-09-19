package com.example.dependencytreeproject.service.impl;

import com.example.dependencytreeproject.dao.Storage;
import com.example.dependencytreeproject.model.Node;
import com.example.dependencytreeproject.model.ParentNode;
import com.example.dependencytreeproject.model.Project;
import com.example.dependencytreeproject.service.NodeService;
import com.example.dependencytreeproject.service.ParseService;
import com.example.dependencytreeproject.service.ProjectService;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.building.ModelSource2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DefaultProjectService implements ProjectService {
    private static final String VERSION_REGEX = "\\$\\{.+}";
    private static final String REPLACE_REGEX = "[${}]";
    private final NodeService nodeService;
    private final ParseService parseService;

    @Override
    public Project createProjects(Model model) {
        Project project = new Project();
        project.setName(model.getArtifactId());
        project.setParentNode(createParent(model));
        project.setNodes(createNodes(model));
        return project;
    }

    private List<Node> createNodes(Model model) {
        List<Node> nodes = new ArrayList<>();
        List<Dependency> dependencies = getDependenciesFromModel(model);
        for (Dependency dependency : dependencies) {
            nodes.add(nodeService.buildNode(dependency));
        }
        return nodes;
    }

    private ParentNode createParent(Model model) {
        Parent parent = model.getParent();
        if (parent != null) {
            ParentNode parentNode = new ParentNode();
            parentNode.setUp(nodeService.buildNode(getDependencyFromParent(parent)));
            return parentNode;
        }
        return null;
    }

    private List<Dependency> getDependenciesFromModel(Model model) {
        List<Dependency> dependencies = new ArrayList<>();
        model.getDependencies().forEach(d -> {
            d.setVersion(getVersion(d, model));
            dependencies.add(d);
        });
        return dependencies;
    }

    private String getVersion(Dependency dependency, Model model) {
        String version = dependency.getVersion();
//        if (version == null) {
//            version = getVersionFromParent(dependency, model);
//        } else if (version.matches(VERSION_REGEX)) {
//            version = model.getProperties().getProperty(version.replaceAll(REPLACE_REGEX, ""));
//        }
        return version;
    }

//    private String getVersionFromParent(Dependency dependency, Model model) {
//        Model parentModel;
//        String version = null;
//        while (model.getParent() != null) {
//            ModelSource2 parentModelSource = getParentPomFile(model, new FileModelSource(model.getPomFile()));
//            if (parentModelSource != null) {
//                parentModel = parseService.parseFile(((FileModelSource) parentModelSource).getPomFile()).get();
//            } else {
////                String parentArtifactId = model.getParent().getArtifactId();
////                parentModel = Storage.models.get(parentArtifactId);
//                return version;
//            }
//            for (Dependency dep : parentModel.getDependencies()) {
//                version = findVersion(version, dependency, dep, model);
//            }
//            if (parentModel.getDependencyManagement() != null) {
//                version = getVersionFromChildDependency(dependency, parentModel);
//                if (version != null) {
//                    return version;
//                }
//            }
//            model = parentModel;
//        }
//        return version;
//    }
//
//    private String getVersionFromChildDependency(Dependency dependency, Model model) {
//        List<Model> dependencyModels = new ArrayList<>();
//        String version = null;
//        DependencyManagement dependencyManagement = model.getDependencyManagement();
//        if (dependencyManagement != null) {
//            List<Dependency> dependencies = dependencyManagement.getDependencies();
//            for (Dependency dep : dependencies) {
//                List<Model> models = Storage.models.get(dep.getArtifactId());
//                if (models != null) {
//                    dependencyModels.addAll(models);
//                }
//            }
//            for (Model dependencyModel : dependencyModels) {
//                DependencyManagement childManagement = dependencyModel.getDependencyManagement();
//                if (childManagement != null) {
//                    for (Dependency dep : childManagement.getDependencies()) {
//                        version = findVersion(version, dependency, dep, dependencyModel);
//                        if (version != null) {
//                            return version;
//                        }
//                    }
//                }
//            }
//        }
//        return version;
//    }
//
//    private ModelSource2 getParentPomFile(Model model, ModelSource modelSource) {
//        if (!(modelSource instanceof ModelSource2)) {
//            return null;
//        } else {
//            String parentPath = model.getParent().getRelativePath();
//            return parentPath != null && parentPath.length() > 0 ?
//                    ((ModelSource2) modelSource).getRelatedSource(parentPath) : null;
//        }
//    }
//
//    private String findVersion(String version, Dependency mainDependency,
//                               Dependency dependency, Model model) {
//        if (dependency.getArtifactId().equals(mainDependency.getArtifactId())) {
//            version = dependency.getVersion();
//        }
//        if (version != null && version.matches(VERSION_REGEX)) {
//            version = model.getProperties().getProperty(version.replaceAll(REPLACE_REGEX, ""));
//        }
//        return version;
//    }

    private Dependency getDependencyFromParent(Parent parent) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(parent.getArtifactId());
        dependency.setVersion(parent.getVersion());
        dependency.setType("pom");
        return dependency;
    }

}
