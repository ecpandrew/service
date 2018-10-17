package br.pucrio.inf.lac.hospital.semantic.data;

public class HasA {

    private long hasAID;
    private long deviceID;
    private long personID;
    private long roomID;

    public HasA() {
    }

    public HasA(long hasAID, long deviceID, long personID, long roomID) {
        this.hasAID = hasAID;
        this.deviceID = deviceID;
        this.personID = personID;
        this.roomID = roomID;
    }
    
    public long getHasAID() {
        return hasAID;
    }

    public void setHasAID(long hasAID) {
        this.hasAID = hasAID;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(long deviceID) {
        this.deviceID = deviceID;
    }

    public long getPersonID() {
        return personID;
    }

    public void setPersonID(long personID) {
        this.personID = personID;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }
    
    @Override
    public String toString() {
        return "HasA{" + "hasAID=" + hasAID + ", deviceID=" + deviceID + ", personID=" + personID + ", roomID=" + roomID + '}';
    }
    
}
