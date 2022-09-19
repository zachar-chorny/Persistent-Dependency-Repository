package com.example.dependencytreeproject.facade;

import com.example.dependencytreeproject.model.Setting;
import java.util.List;
import java.util.Map;

public interface ChangeFacade {

    Map<String, List<String>> getFutureChanges(Setting setting);
}
