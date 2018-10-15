package br.pucrio.inf.lac.hospital.semantic.data;

public class Room {
    private long roomID;//Modified to long
    private String roomName;
    private String roomType;
    private long hospitalID;//Modified to long

    public Room() {
    }

    public Room(long roomID, String roomName, String roomType, long hospitalID) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomType = roomType;
        this.hospitalID = hospitalID;
    }

    public Room(String roomName, String roomType, long hospitalID) {
        this.roomName = roomName;
        this.roomType = roomType;
        this.hospitalID = hospitalID;
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

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public long getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(long hospitalID) {
        this.hospitalID = hospitalID;
    }

    @Override
    public String toString() {
        return "Room{" + "roomID=" + roomID + ", roomName=" + roomName + ", roomType=" + roomType + ", hospitalID=" + hospitalID + '}';
    }
    
}
