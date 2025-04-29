package org.health.se7a.patients;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chronic-diseases")
public class ChronicDiseaseController {

    @GetMapping
    public List<String> getAllChronicDiseases() {
        return Arrays.stream(ChronicDisease.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}