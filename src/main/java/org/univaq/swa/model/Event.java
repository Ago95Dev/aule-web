package org.univaq.swa.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author gianlucarea
 */
public class Event {
    
    private int id , courseID;
    private String name, description, email;
    private LocalTime startTime,endTime;
    private LocalDate date;
    private Type type;

    public Event() {
    }

    public Event(String email, int courseID, String name, LocalTime startTime, LocalTime endTime, String description, LocalDate date, Type type) {
        this.email = email;
        this.courseID = courseID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public Event(int id, String eventCoordinatorID, int courseID, String name, LocalTime startTime, LocalTime endTime, String description, LocalDate date, Type type) {
        this.id = id;
        this.email = email;
        this.courseID = courseID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", eventCoordinatorID=" + email + ", courseID=" + courseID + ", name=" + name + ", startTime=" + startTime + ", endTime=" + endTime + ", description=" + description + ", date=" + date + ", type=" + type + '}';
    }
    
}
