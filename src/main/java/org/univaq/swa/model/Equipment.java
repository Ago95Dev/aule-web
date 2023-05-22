package org.univaq.swa.model;

/**
 *
 * @author gianlucarea
 */
public class Equipment {
    
    private int id;
    private String name;

    public Equipment() {
    }

    public Equipment(String name) {
        this.name = name;
    }

    public Equipment(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Equipment{" + "id=" + id + ", name=" + name + '}';
    }
    
    
}
