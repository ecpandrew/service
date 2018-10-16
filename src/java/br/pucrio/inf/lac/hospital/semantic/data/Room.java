package br.pucrio.inf.lac.hospital.semantic.data;

public class Room {

    private long roomID;
    private String roomName;
    private long sectionID;

    public Room() {
    }

    public Room(long roomID, String roomName, long sectionID) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.sectionID = sectionID;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getSectionID() {
        return sectionID;
    }

    public void setSectionID(long sectionID) {
        this.sectionID = sectionID;
    }
    
    @Override
    public String toString() {
        return "Room{" + "roomID=" + roomID + ", roomName=" + roomName + ", sectionID=" + sectionID + '}';
    }
    
}
