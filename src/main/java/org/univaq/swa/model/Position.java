package org.univaq.swa.model;

/**
 *
 * @author gianlucarea
 */
public class Position {
    
    private int id;
    private String location, building, floor;

    public Position() {
    }

    public Position(String location, String building, String floor) {
        this.location = location;
        this.building = building;
        this.floor = floor;
    }

    public Position(int id, String location, String building, String floor) {
        this.id = id;
        this.location = location;
        this.building = building;
        this.floor = floor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Position{" + "id=" + id + ", location=" + location + ", building=" + building + ", floor=" + floor + '}';
    }

}
