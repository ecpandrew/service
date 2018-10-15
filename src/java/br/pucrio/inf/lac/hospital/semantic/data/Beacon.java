package br.pucrio.inf.lac.hospital.semantic.data;

import java.sql.Date;
import java.util.UUID;

public class Beacon {
    private long beaconID;
    private long roomID;
    private UUID thingID;
    private boolean active;
    private Date lastBateryChange;

    public Beacon() {
    }

    public Beacon(long roomID, UUID thingID, boolean active, Date lastBateryChange) {
        this.roomID = roomID;
        this.thingID = thingID;
        this.active = active;
        this.lastBateryChange = lastBateryChange;
    }

    public Beacon(long beaconID, long roomID, UUID thingID, boolean active, Date lastBateryChange) {
        this.beaconID = beaconID;
        this.roomID = roomID;
        this.thingID = thingID;
        this.active = active;
        this.lastBateryChange = lastBateryChange;
    }

    public long getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(long beaconID) {
        this.beaconID = beaconID;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public UUID getThingID() {
        return thingID;
    }

    public void setThingID(UUID thingID) {
        this.thingID = thingID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getLastBateryChange() {
        return lastBateryChange;
    }

    public void setLastBateryChange(Date lastBateryChange) {
        this.lastBateryChange = lastBateryChange;
    }

    @Override
    public String toString() {
        return "Beacon{" + "beaconID=" + beaconID + ", roomID=" + roomID + ", thingID=" + thingID + ", active=" + active + ", lastBateryChange=" + lastBateryChange + '}';
    }
    
}
