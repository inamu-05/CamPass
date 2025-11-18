package com.example.app.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId; // Crucial for mapping relationships

@Entity
@jakarta.persistence.Table(name = "subject_class")
public class SubjectClass {

    // 1. Embed the composite ID class
    @EmbeddedId
    private SubjectClassId id;

    // 2. Define the relationship to Subject
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subjectId") // Maps the 'subjectId' field in the SubjectClassId to this relationship
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // 3. Define the relationship to User (Student)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // Maps the 'userId' field in the SubjectClassId to this relationship
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Use the parent User entity since it's the FK

    // Default constructor is required
    public SubjectClass() {}

    // Convenience constructor for creating a new link
    public SubjectClass(Subject subject, User user) {
        // Initialize the embedded ID
        this.id = new SubjectClassId(subject.getSubjectId(), user.getUserId());
        
        // Set the entity references
        this.subject = subject;
        this.user = user;
    }
    
    // --- Getters and Setters ---

    public SubjectClassId getId() {
        return id;
    }

    public void setId(SubjectClassId id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}