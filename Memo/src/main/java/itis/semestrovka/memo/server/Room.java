package itis.semestrovka.memo.server;

public class Room {
    private String name;
    private Integer roomNumber;
    private Integer size;
    private Integer real_size;

    public Room(String name, Integer roomNumber) {
        this.name = name;
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }
}
