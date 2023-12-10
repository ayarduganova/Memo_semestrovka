package itis.semestrovka.memo.game;

import java.util.Random;

public class Board {
    public Cell[][] board = new Cell[6][6];

    public void populateMatrix(){
        board = new Cell[6][6];
        String[] images = {"piranha", "toucan", "lion", "crocodile", "tiger"};
        Random random = new Random();
        while(!isBoardFull()){
            int randomImageIndex = random.nextInt(images.length);
            String randomImageSelected = images[randomImageIndex];

            int randomRow1 = random.nextInt(6);
            int randomCol1 = random.nextInt(6);
            while(board[randomRow1][randomCol1] != null){
                randomRow1 = random.nextInt(6);
                randomCol1 = random.nextInt(6);
            }

            int randomRow2 = random.nextInt(6);
            int randomCol2 = random.nextInt(6);
            while((randomCol1 == randomCol2 && randomRow1 == randomRow2) || board[randomRow1][randomCol1] != null){
                randomRow1 = random.nextInt(6);
                randomCol1 = random.nextInt(6);
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

}
