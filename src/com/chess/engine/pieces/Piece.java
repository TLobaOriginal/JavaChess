package com.chess.engine.pieces;


import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;
/*
 * Created by Joseph Ogunbiyi on 16/03/21
 */
public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    Piece(final int piecePosition, final Alliance pieceAlliance){
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        /*Work needed to be done*/
        this.isFirstMove = false;
    }

    public boolean isFirstMove(){ return this.isFirstMove; }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board); //List all possible moves a player can make
    /*We're gonna have to override for the different types of pieces
    * We're gonna have to use  extensions*/
}
