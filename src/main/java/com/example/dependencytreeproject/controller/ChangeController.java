package com.example.dependencytreeproject.controller;

import com.example.dependencytreeproject.exception.WrongParamsException;
import com.example.dependencytreeproject.facade.ChangeFacade;
import com.example.dependencytreeproject.model.Setting;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequestMapping("/changes")
@RestController
@AllArgsConstructor
public class ChangeController {

    private final ChangeFacade changeFacade;

    @PostMapping(value = "/get")
    public Map<String, List<String>> getChanges(@RequestBody @Valid Setting setting) {
        if (setting.getInstructions() != null) {
            return changeFacade.getFutureChanges(setting);
        }
        throw new WrongParamsException("Instructions can't be null");
    }

}
