package com.chess;
import com.chess.engine.board.Board;
import gui.Table;
/*
* Created on the 23/05/21
* By Joseph Ogunbiyi*/

public class JavaChess {

    public static void main(String[] args){

        Board board = Board.createStandardBoard();

        System.out.println(board);

        Table table = new Table();
    }
}
