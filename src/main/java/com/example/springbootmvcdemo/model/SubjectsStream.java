package com.example.springbootmvcdemo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class SubjectsStream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String streamName;
    @OneToMany(mappedBy = "subjectsStream")
    private List<SchoolClass> schoolClass;
    @OneToMany(mappedBy = "subjectsStream")
    List<Subject> subjects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public List<SchoolClass> getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(List<SchoolClass> schoolClass) {
        this.schoolClass = schoolClass;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
