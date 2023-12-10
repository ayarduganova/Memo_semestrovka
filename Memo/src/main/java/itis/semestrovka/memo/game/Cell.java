package itis.semestrovka.memo.game;

public class Cell {
    public String value;
    public int row;
    public int col;
    public boolean wasGuessed;

    public Cell(String value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.wasGuessed = false;
    }
}
