package org.univaq.swa.model;

import java.util.List;

/**
 *
 * @author gianlucarea
 */
public class Classroom {
    
    int id, capacity, numberOfSockets, numberOfEthernet , positionID;
    String name , email, note;
    Integer[] equipmentsId;

    public Classroom() {
    }

    public Classroom(int capacity, int numberOfSockets, int numberOfEthernet, int positionID, String name, String email, String note, Integer[] equipmentsId) {
        this.capacity = capacity;
        this.numberOfSockets = numberOfSockets;
        this.numberOfEthernet = numberOfEthernet;
        this.positionID = positionID;
        this.name = name;
        this.email = email;
        this.note = note;
        this.equipmentsId = equipmentsId;
    }
    
    public Classroom(int id, int capacity, int numberOfSockets, int numberOfEthernet, int positionID, String name, String email, String note, Integer[] equipmentsId) {
        this.id = id;
        this.capacity = capacity;
        this.numberOfSockets = numberOfSockets;
        this.numberOfEthernet = numberOfEthernet;
        this.positionID = positionID;
        this.name = name;
        this.email = email;
        this.note = note;
        this.equipmentsId = equipmentsId;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getNumberOfSockets() {
        return numberOfSockets;
    }

    public void setNumberOfSockets(int numberOfSockets) {
        this.numberOfSockets = numberOfSockets;
    }

    public int getNumberOfEthernet() {
        return numberOfEthernet;
    }

    public void setNumberOfEthernet(int numberOfEthernet) {
        this.numberOfEthernet = numberOfEthernet;
    }

    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer[] getEquipmentsId() {
        return equipmentsId;
    }

    public void setEquipmentsId(Integer[] equipmentsId) {
        this.equipmentsId = equipmentsId;
    }
    
    @Override
    public String toString() {
        return "Classroom{" + "id=" + id + ", capacity=" + capacity + ", numberOfSockets=" + numberOfSockets + ", numberOfEthernet=" + numberOfEthernet + ", positionID=" + positionID + ", name=" + name + ", email=" + email + ", note=" + note + '}';
    }
   
    
}
