package com.example.app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.entity.Course;
import com.example.app.repository.CourseRepository;

import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired; // For dependency injection
// import com.example.app.controller.Course;
// import com.example.app.controller.CourseRepository;

@RestController
@CrossOrigin(origins = "*")   // temporarily allow all origins
public class HelloController {
    private final CourseRepository courseRepository;

    // @Autowired // Inject the repository here
    public HelloController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
  @GetMapping("/hello")
  public String sayHello() {
    // 1. Fetch all course members from the database
    List<Course> courseList = courseRepository.findAll();

    StringBuilder response = new StringBuilder();
    response.append("<h1>Hello from Spring Boot!</h1>");
    response.append("<h2>Available Courses:</h2>");

    // 2. Iterate through the list and format the output
    if (courseList.isEmpty()) {
        response.append("<p>No course data found.</p>");
    } else {
        for (Course course : courseList) {
            response.append("<p>ID: ")
                    .append(course.getCourseId())
                    .append(" | Name: ")
                    .append(course.getCourseName());
        }
    }
    return response.toString();
}
}