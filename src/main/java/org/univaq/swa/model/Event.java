package org.univaq.swa.model;

import java.util.Date;

/**
 *
 * @author gianlucarea
 */
public class Event {
    
    private int id , eventCoordinatorID, courseID;
    private String name, startTime, endTime, description;
    private Date date;
    private Type type;

    public Event() {
    }

    public Event(int eventCoordinatorID, int courseID, String name, String startTime, String endTime, String description, Date date, Type type) {
        this.eventCoordinatorID = eventCoordinatorID;
        this.courseID = courseID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public Event(int id, int eventCoordinatorID, int courseID, String name, String startTime, String endTime, String description, Date date, Type type) {
        this.id = id;
        this.eventCoordinatorID = eventCoordinatorID;
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

    public int getEventCoordinatorID() {
        return eventCoordinatorID;
    }

    public void setEventCoordinatorID(int eventCoordinatorID) {
        this.eventCoordinatorID = eventCoordinatorID;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
        return "Event{" + "id=" + id + ", eventCoordinatorID=" + eventCoordinatorID + ", courseID=" + courseID + ", name=" + name + ", startTime=" + startTime + ", endTime=" + endTime + ", description=" + description + ", date=" + date + ", type=" + type + '}';
    }
    
}
