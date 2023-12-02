package itis.semestrovka.memo.server;

public class Room {

    private String name;
    private Integer realSize;
    private Integer maxSize;

    public Room(String name, Integer maxSize) {
        this.name = name;
        this.maxSize = maxSize;
    }

    public Integer getRealSize() {
        return realSize;
    }

    public void setRealSize(Integer realSize) {
        this.realSize = realSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }
}
