package com.example.dependencytreeproject.runner;

import com.example.dependencytreeproject.service.InitializationService;
import com.example.dependencytreeproject.service.RepositoryInitializeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SetupRunner implements CommandLineRunner {
    private static final String REPO_PATH = "/Users/zchornyi/Documents/LocalRepository";
    private final InitializationService initializationService;
    private final RepositoryInitializeService repositoryInitializeService;
    private final Map<String, String> repositoryUrls = new HashMap<>();

    @PostConstruct
    private void setUp(){
        repositoryUrls.put("rapevine-services",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/grapevine-services.git");
        repositoryUrls.put("device-packs",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/device-packs.git");
        repositoryUrls.put("shared",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/shared.git");
        repositoryUrls.put("models",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/models.git");
        repositoryUrls.put("network-automation-components",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/network-automation-components.git");
        repositoryUrls.put("mashup",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/mashup.git");
        repositoryUrls.put("apic-em-core",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/apic-em-core.git");
        repositoryUrls.put("maps-app",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/maps-app.git");
        repositoryUrls.put("pnp-app",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/pnp-app.git");
        repositoryUrls.put("enterprise-fabric",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/enterprise-fabric.git");
        repositoryUrls.put("policyapps",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/policyapps.git");
        repositoryUrls.put("esa-app",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/esa-app.git");
        repositoryUrls.put("dna-core-apps",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/dna-core-apps.git");
        repositoryUrls.put("isac",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/isac.git");
        repositoryUrls.put("sys-eng",
                "https://bitbucket-eng-sjc1.cisco.com/bitbucket/scm/engsdn/sys-eng.git");
    }

    @Override
    public void run(String... args) throws Exception {
//        repositoryInitializeService.initialize(REPO_PATH, repositoryUrls);
        initializationService.initializeModelsMap(REPO_PATH);
    }
}
