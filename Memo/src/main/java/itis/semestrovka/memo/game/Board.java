package itis.semestrovka.memo.game;

import java.util.Random;

public class Board {
    public Cell[][] board;

    private int boardRow;
    private int boardCol;

    public void setBoardSize(int roomSize) {
        if(roomSize == 2){
            this.boardRow = this.boardCol = 6;
        } else if (roomSize == 3) {
            this.boardRow = 12;
            this.boardCol = 7;
        }
        else{
            this.boardRow = 13;
            this.boardCol = 8;
        }
        this.board = new Cell[boardRow][boardCol];
    }

    public Board(){
    }

    public void populateMatrix(){
        String[] images = {"piranha", "toucan", "lion", "crocodile", "tiger"};
        Random random = new Random();
        while(!isBoardFull()){
            int randomImageIndex = random.nextInt(images.length);

            String randomImageSelected = images[randomImageIndex];

            int randomRow1 = random.nextInt(boardRow);
            int randomCol1 = random.nextInt(boardCol);

            while(board[randomRow1][randomCol1] != null){
                randomRow1 = random.nextInt(boardRow);
                randomCol1 = random.nextInt(boardCol);
            }

            int randomRow2 = random.nextInt(boardRow);
            int randomCol2 = random.nextInt(boardCol);

            while((randomCol1 == randomCol2 && randomRow1 == randomRow2) || (board[randomRow2][randomCol2] != null)){
                randomRow2 = random.nextInt(boardRow);
                randomCol2 = random.nextInt(boardCol);
            }

            board[randomRow1][randomCol1] = new Cell(randomImageSelected, randomRow1, randomCol1);
            board[randomRow2][randomCol2] = new Cell(randomImageSelected, randomRow2, randomCol2);

        }
    }

    private boolean isBoardFull() {
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                if(board[i][j] == null){
                    return false;
                }
            }
        }
        return true;
    }

    public int getBoardRow() {
        return boardRow;
    }

    public void setBoardRow(int boardRow) {
        this.boardRow = boardRow;
    }

    public int getBoardCol() {
        return boardCol;
    }

    public void setBoardCol(int boardCol) {
        this.boardCol = boardCol;
    }


}
