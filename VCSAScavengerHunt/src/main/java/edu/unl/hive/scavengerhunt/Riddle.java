/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.hive.scavengerhunt;

import java.io.Serializable;

/**
 *
 * @author mstark
 */
public class Riddle extends ReturnMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private int riddleId;
    private String riddle;
    private Location locationInformation;

    public Riddle() {
    }

    public Riddle(int riddleId) {
        this.riddleId = riddleId;
    }

    public Riddle(int riddleId, String riddle) {
        this.riddleId = riddleId;
        this.riddle = riddle;
    }

    public int getRiddleId() {
        return riddleId;
    }

    public void setRiddleId(int riddleId) {
        this.riddleId = riddleId;
    }

    public String getRiddle() {
        return riddle;
    }

    public void setRiddle(String riddle) {
        this.riddle = riddle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (riddleId);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Riddle)) {
            return false;
        }
        Riddle other = (Riddle) object;
        if (this.riddleId != other.riddleId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.hive.scavengerhunt.Riddle[ riddleId=" + riddleId + " ]";
    }

    /**
     * @return the locationInformation
     */
    public Location getLocationInformation() {
        return locationInformation;
    }

    /**
     * @param locationInformation the locationInformation to set
     */
    public void setLocationInformation(Location locationInformation) {
        this.locationInformation = locationInformation;
    }
    
}
