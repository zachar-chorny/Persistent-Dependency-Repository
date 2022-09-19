package com.example.dependencytreeproject.controller;

import com.example.dependencytreeproject.facade.ParseFacade;
import com.example.dependencytreeproject.model.Project;
import com.example.dependencytreeproject.model.Setting;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ParseController {
    private final ParseFacade parseFacade;

    @PostMapping(value = "/create")
    public List<Project> createProjects(@RequestBody @Valid Setting setting) {
        return parseFacade.parseToProjects(setting);
    }
}
