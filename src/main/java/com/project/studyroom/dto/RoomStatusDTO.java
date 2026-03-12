package com.project.studyroom.dto;

public class RoomStatusDTO {

    private String roomId;
    private String roomName;
    private boolean isOccupied;
    private String currentOccupant;
    private String until;


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getCurrentOccupant() {
        return currentOccupant;
    }

    public void setCurrentOccupant(String currentOccupant) {
        this.currentOccupant = currentOccupant;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }
}
