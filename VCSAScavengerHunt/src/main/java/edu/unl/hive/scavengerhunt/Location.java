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
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    private int locationId;
    private String locationName;
    private double topLatitude;
    private double leftLongitude;
    private double bottomLatitude;
    private double rightLongitude;

    public Location() {
    }

    public Location(int locationId) {
        this.locationId = locationId;
    }

    public Location(int locationId, String locationName, double topLatitude, double leftLongitude, double bottomLatitude, double rightLongitude) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.topLatitude = topLatitude;
        this.leftLongitude = leftLongitude;
        this.bottomLatitude = bottomLatitude;
        this.rightLongitude = rightLongitude;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getTopLatitude() {
        return topLatitude;
    }

    public void setTopLatitude(double nwCornerLatitude) {
        this.topLatitude = nwCornerLatitude;
    }

    public double getLeftLongitude() {
        return leftLongitude;
    }

    public void setLeftLongitude(double nwCornerLongitude) {
        this.leftLongitude = nwCornerLongitude;
    }

    public double getBottomLatitude() {
        return bottomLatitude;
    }

    public void setBottomLatitude(double neCornerLatitude) {
        this.bottomLatitude = neCornerLatitude;
    }

    public double getRightLongitude() {
        return rightLongitude;
    }

    public void setRightLongitude(double neCornerLongitude) {
        this.rightLongitude = neCornerLongitude;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (locationId);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if (this.locationId != other.locationId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.hive.scavengerhunt.Location[ locationId=" + locationId + " ]";
    }
    
}
