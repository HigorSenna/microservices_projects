package br.com.microservices.course.endpoint.controller;

import br.com.microservices.core.model.Course;
import br.com.microservices.course.endpoint.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/v1/admin/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<Iterable<Course>> findAll(Pageable pageable) {
        return new ResponseEntity<>(this.courseService.list(pageable), HttpStatus.OK) ;
    }
}
