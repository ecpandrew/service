package br.pucrio.inf.lac.hospital.semantic.data;

import java.util.HashSet;
import java.util.Set;

public class GroupRendezvous {
    
    private Set<String> personGroup = new HashSet<>();
    private String roomName;
    private long arrive;
    private long depart;

    public GroupRendezvous() {
    }

    public GroupRendezvous(String roomName, long arrive, long depart) {
        this.roomName = roomName;
        this.arrive = arrive;
        this.depart = depart;
    }

    public Set<String> getPersonGroup() {
        return personGroup;
    }

    public void setPersonGroup(Set<String> personGroup) {
        this.personGroup = personGroup;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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
    
    @Override
    public String toString() {
        return "Rendezvous{" + "roomName=" + roomName + ", arrive=" + arrive + ", depart=" + depart + '}';
    }
    
}
