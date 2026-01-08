// File: com.example.app.controller.SubjectDto.java (or com.example.app.dto.SubjectDto.java)

package com.example.app.controller; // or com.example.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.app.entity.Course;
import com.example.app.entity.Staff;

import lombok.AllArgsConstructor;

// You would need to add Lombok to your pom.xml if you haven't already:
// <dependency>
//     <groupId>org.projectlombok</groupId>
//     <artifactId>lombok</artifactId>
//     <scope>provided</scope>
// </dependency>

@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates a constructor with no arguments
@AllArgsConstructor // Generates a constructor with all arguments
public class SubjectDto {
    private String subjectId;
    private String subjectName;
    private Course course;
    private Staff staff;
    // Add any other necessary fields from your Subject entity
}