package com.sergeev.studapp.model;

public class Mark implements Identified {

    private String id;

    private Lesson lesson;

    private User student;
    private Integer value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", lesson=" + lesson +
                ", student=" + student +
                ", value=" + value +
                '}';
    }
}
