package org.univaq.swa.model;

/**
 *
 * @author gianlucarea
 */
public class EventCoordinator {
    
    private int id;
    private String email;

    public EventCoordinator() {
    }

    public EventCoordinator(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public EventCoordinator(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "EventCoordinator{" + "id=" + id + ", email=" + email + '}';
    }
    
    
    
}
