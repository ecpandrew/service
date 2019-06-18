package br.ufma.lsdi.smartlab.service.data;

public class PhysicalSpace {

    private long roomID;
    private String roomName;
    private String roomDescription;
    private long sectionID;

    public PhysicalSpace() {
    }
    
    public PhysicalSpace(long roomID, String roomName, String roomDescription) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
    }

    public PhysicalSpace(long roomID, String roomName, long sectionID) {
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
    
    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }
    
    @Override
    public String toString() {
        return "Room{" + "roomID=" + roomID + ", roomName=" + roomName + ", sectionID=" + sectionID + '}';
    }
    
}
