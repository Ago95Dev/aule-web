package org.univaq.swa.model;

import java.util.Date;
/**
 *
 * @author gianlucarea
 */
public class Recurrent {
    
    private int id;
    private Date untilDate ;
    private TypeOfRecurrency typeOfRecurrency; 

    public Recurrent() {
    }

    public Recurrent(Date untilDate, TypeOfRecurrency typeOfRecurrency) {
        this.untilDate = untilDate;
        this.typeOfRecurrency = typeOfRecurrency;
    }

    public Recurrent(int id, Date untilDate, TypeOfRecurrency typeOfRecurrency) {
        this.id = id;
        this.untilDate = untilDate;
        this.typeOfRecurrency = typeOfRecurrency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public TypeOfRecurrency getTypeOfRecurrency() {
        return typeOfRecurrency;
    }

    public void setTypeOfRecurrency(TypeOfRecurrency typeOfRecurrency) {
        this.typeOfRecurrency = typeOfRecurrency;
    }

    @Override
    public String toString() {
        return "Recurrent{" + "id=" + id + ", untilDate=" + untilDate + ", type=" + typeOfRecurrency + '}';
    }
    
    
}
