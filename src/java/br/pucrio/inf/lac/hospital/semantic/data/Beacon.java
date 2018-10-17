package br.pucrio.inf.lac.hospital.semantic.data;

import java.util.UUID;

public class Beacon {
    private UUID thingID;
    private boolean active;

    public Beacon() {
    }

    public Beacon(UUID thingID, boolean active) {
        this.thingID = thingID;
        this.active = active;
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
    
    @Override
    public String toString() {
        return "Beacon{" + "thingID=" + thingID + ", active=" + active + '}';
    }
    
}
