package br.ufma.lsdi.smartlab.service.data;

import java.util.UUID;

public class Rendezvous {
    private UUID mhubID;
    private UUID thingID;
    private long latitude;
    private long longitude;
    private long signal;
    private long timestamp;
    private long duration;
    private long arrive;
    private long depart;
    private String personName;
    private String roomName;

    public Rendezvous() {
    }

    public Rendezvous(UUID mhubID, UUID thingID, long duration) {
        this.mhubID = mhubID;
        this.thingID = thingID;
        this.duration = duration;
    }
    
    public Rendezvous(UUID mhubID, UUID thingID, long arrive, long depart) {
        this.mhubID = mhubID;
        this.thingID = thingID;
        this.arrive = arrive;
        this.depart = depart;
    }
    
    public Rendezvous(String personName, String roomName, long arrive, long depart) {
        this.arrive = arrive;
        this.depart = depart;
        this.personName = personName;
        this.roomName = roomName;
    }

    public Rendezvous(UUID mhubID, UUID thingID, long latitude, long longitude, long timestamp) {
        this.mhubID = mhubID;
        this.thingID = thingID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public Rendezvous(UUID mhubID, UUID thingID, long latitude, long longitude, long signal, long timestamp) {
        this.mhubID = mhubID;
        this.thingID = thingID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.signal = signal;
        this.timestamp = timestamp;
    }
    
    public UUID getMhubID() {
        return mhubID;
    }

    public void setMhubID(UUID mhubID) {
        this.mhubID = mhubID;
    }

    public UUID getThingID() {
        return thingID;
    }

    public void setThingID(UUID thingID) {
        this.thingID = thingID;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getSignal() {
        return signal;
    }

    public void setSignal(long signal) {
        this.signal = signal;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getArrive() {
        return arrive;
    }

    public void setArrive(long arrive) {
        this.arrive = arrive;
    }

    public long getDepart() {
        return depart;
    }

    public void setDepart(long depart) {
        this.depart = depart;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    @Override
    public String toString() {
        return "Rendezvous{" + "mhubID=" + mhubID + ", thingID=" + thingID + ", latitude=" + latitude + ", longitude=" + longitude + ", signal=" + signal + ", timestamp=" + timestamp + ", duration=" + duration + ", arrive=" + arrive + ", depart=" + depart + '}';
    }
    
}
