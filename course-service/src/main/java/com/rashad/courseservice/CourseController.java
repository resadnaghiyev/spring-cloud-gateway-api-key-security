package com.rashad.courseservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
public class CourseController {

    @GetMapping("/courses")
    public List<Course> getCourses(){
        return Stream.of(new Course("CT12", "Java", 5, 5000),
                        new Course("CT98", "Spring boot", 3, 8000),
                        new Course("CT53467", "AWS", 2, 10000))
                .collect(Collectors.toList());
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return request.getPassword() + request.getUsername();
    }

    @PostMapping("/secureApi")
    public ResponseEntity<String> listAllHeaders(@RequestHeader Map<String, String> headers) {
        headers.forEach((key, value) -> log.info(String.format("Header '%s' = %s", key, value)));
        return new ResponseEntity<>(String.format("Listed %d headers", headers.size()), HttpStatus.OK);
    }
}
