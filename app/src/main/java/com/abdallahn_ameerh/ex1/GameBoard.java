package com.abdallahn_ameerh.ex1;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.TextView;
import java.util.Random;

public class GameBoard {
    boolean gameOver; // true if game over, false otherwise
    int positionX;
    Random RANDOM = new Random(); //random number
    TextView[] blocks ; //array of blocks
    public  int game_moves =0;
    public GameBoard(TextView[] blocks) {
        this.blocks = blocks;
    }

    private void shuffle() {
        for (int i = 0; i < this.blocks.length; i++) {
            int randomPosition = RANDOM.nextInt(this.blocks.length);
            swap(this.blocks[i],this.blocks[randomPosition]);
            game_moves=0;
        }
    }

    public void swap(TextView first,TextView second){
        String temp = first.getText().toString();
        Drawable draw = first.getBackground();
        int color = first.getCurrentTextColor();
        first.setText(second.getText().toString());
        first.setBackground(second.getBackground());
        first.setTextColor(second.getCurrentTextColor());
        second.setText(temp);
        second.setBackground(draw);
        second.setTextColor(color);
        game_moves++;
    }
    // find Position of blank from bottom
    private  void findXPosition() {
        for (int i = 15; i >= 0; i--)
            if (this.blocks[i].getText().toString().equals("0")){
               if( i == 0 || i == 15 || i == 14 ||i == 13  ){positionX =1;}
               else if (i == 12 || i == 11 || i == 10 ||i == 9  ){positionX =2;}
               else if (i == 5 || i == 6 || i == 7 ||i == 8  ){positionX =3;}
               else if (i == 1 || i == 2 || i == 3 ||i == 4  ){positionX =4;} }
    }
    int getInvCount()
    {
        int inv_count = 0;
        for (int i = 0; i < blocks.length - 1; i++)
            for (int j = i + 1; j < blocks.length; j++)
                if (Integer.parseInt(blocks[i].getText().toString()) >Integer.parseInt( blocks[j].getText().toString()))
                    inv_count++;
        return inv_count;
    }
    private boolean isSolvable() {
        boolean test;
        // Count inversions in given puzzle
        int invCount = getInvCount();
        //  return true if inversion count is even and the blank position is odd and vers versa.
        findXPosition();
        int pos = positionX;
        if (pos % 2 ==0 && invCount %2 !=0){
                test = true;
                return true; }
        else if(pos % 2 !=0 && invCount %2 ==0){
                test = true;
                return true; }
        else if (pos % 2 ==0 && invCount %2 ==0) {
            test = false;
            return false; }
        else {
            test = false;
            return false;
        }

    }
    public void newGame(){
        for (int i = 0; i < 16; i++)
            blocks[i].setClickable(true);
        do{
            shuffle();
        }while (!isSolvable());
        gameOver = false; }


    private boolean isSolved() {
        gameOver = false;
        // if blank tile is not in the solved position then not solved
        if (!blocks[0].getText().toString().equals("0")){
            return false;}
        else {
        for (int i =1; i < 16; i++) {
            if (Integer.parseInt(blocks[i].getText().toString()) != i )
                return false;
        }
        return true;
        }
    }

    public void movPuzzle(int index) {
        switch (index) {
            case 0:
                if (this.blocks[12].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[12]);
                    gameOver = isSolved();}
                if (this.blocks[15].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[15]);
                    gameOver = isSolved();}
                break;
            case 1:
                if (this.blocks[2].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[2]);
                    gameOver = isSolved();}
                if (this.blocks[5].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[5]);
                    gameOver = isSolved();}
                break;
            case 2:
                if (this.blocks[1].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[1]);
                    gameOver = isSolved();}
                if (this.blocks[3].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[3]);
                    gameOver = isSolved();}
                if (this.blocks[6].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[6]);
                    gameOver = isSolved();}
                break;
            case 3:
                if (this.blocks[2].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[2]);
                    gameOver = isSolved();}
                if (this.blocks[4].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[4]);
                    gameOver = isSolved();}
                if (this.blocks[7].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[7]);
                    gameOver = isSolved();}
                break;
            case 4:
                if (this.blocks[3].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[3]);
                    gameOver = isSolved();}
                if (this.blocks[8].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[8]);
                    gameOver = isSolved();}
                break;
            case 5:
                if (this.blocks[1].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[1]);
                    gameOver = isSolved();}
                if (this.blocks[6].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[6]);
                    gameOver = isSolved();}
                if (this.blocks[9].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[9]);
                    gameOver = isSolved();}
                break;
            case 6:
                if (this.blocks[2].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[2]);
                    gameOver = isSolved();}
                if (this.blocks[5].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[5]);
                    gameOver = isSolved();}
                if (this.blocks[7].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[7]);
                    gameOver = isSolved();}
                if (this.blocks[10].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[10]);
                    gameOver = isSolved();}
                break;
            case 7:
                if (this.blocks[3].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[3]);
                    gameOver = isSolved();}
                if (this.blocks[6].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[6]);
                    gameOver = isSolved();}
                if (this.blocks[8].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[8]);
                    gameOver = isSolved();}
                if (this.blocks[11].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[11]);
                    gameOver = isSolved();}
                break;
            case 8:
                if (this.blocks[4].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[4]);
                    gameOver = isSolved();}
                if (this.blocks[7].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[7]);
                    gameOver = isSolved();}
                if (this.blocks[12].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[12]);
                    gameOver = isSolved();}
                break;
            case 9:
                if (this.blocks[5].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[5]);
                    gameOver = isSolved();}
                if (this.blocks[10].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[10]);
                    gameOver = isSolved();}
                if (this.blocks[13].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[13]);
                    gameOver = isSolved();}
                break;
            case 10:
            if (this.blocks[6].getText().toString().equals("0")){
                swap(this.blocks[index],this.blocks[6]);
                gameOver = isSolved();}
            if (this.blocks[9].getText().toString().equals("0")){
                swap(this.blocks[index],this.blocks[9]);
                gameOver = isSolved();}
            if (this.blocks[11].getText().toString().equals("0")){
                swap(this.blocks[index],this.blocks[11]);
                gameOver = isSolved();}
            if (this.blocks[14].getText().toString().equals("0")){
                swap(this.blocks[index],this.blocks[14]);
                gameOver = isSolved();}
            break;
            case 11:
                if (this.blocks[7].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[7]);
                    gameOver = isSolved();}
                if (this.blocks[10].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[10]);
                    gameOver = isSolved();}
                if (this.blocks[12].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[12]);
                    gameOver = isSolved();}
                if (this.blocks[15].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[15]);
                    gameOver = isSolved();}
                break;
            case 12:
                if (this.blocks[0].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[0]);
                    gameOver = isSolved();}
                if (this.blocks[8].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[8]);
                    gameOver = isSolved();}
                if (this.blocks[11].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[11]);
                    gameOver = isSolved();}
                break;
            case 13:
                if (this.blocks[9].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[9]);
                    gameOver = isSolved();}
                if (this.blocks[14].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[14]);
                    gameOver = isSolved();}
                break;
            case 14:
                if (this.blocks[10].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[10]);
                    gameOver = isSolved();}
                if (this.blocks[13].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[13]);
                    gameOver = isSolved();}
                if (this.blocks[15].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[15]);
                    gameOver = isSolved();}
                break;
            case 15:
                if (this.blocks[0].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[0]);
                    gameOver = isSolved();}
                if (this.blocks[11].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[11]);
                    gameOver = isSolved();}
                if (this.blocks[14].getText().toString().equals("0")){
                    swap(this.blocks[index],this.blocks[14]);
                    gameOver = isSolved();}
                break;


        }
    }
}

