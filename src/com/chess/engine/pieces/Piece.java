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
    protected final PieceType pieceType;

    Piece(final int piecePosition, final Alliance pieceAlliance, final PieceType pieceType){
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.pieceType = pieceType;
        /*Work needed to be done*/
        this.isFirstMove = false;
    }


    
    //@Override
    public boolean equals(final Piece other){
        if(this == other){
            return true;
        }

        if(! (other instanceof Piece))
            return false;

        final Piece otherPiece = (Piece)other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == ((Piece) other).getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == ((Piece) other).isFirstMove;
    }

    //@Override
    public int hasCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public boolean isFirstMove(){ return this.isFirstMove; } //If it's the pieces' first move

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    } //Returns the alliance (black or white)

    public int getPiecePosition() { return piecePosition; }         //Returns the piece's position

    public abstract Collection<Move> calculateLegalMoves(final Board board); //List all possible moves a player can make

    public abstract Piece movePiece(Move move);

    public PieceType getPieceType() {
        return this.pieceType;
    }
    /*We're gonna have to override for the different types of pieces
    * We're gonna have to use  extensions*/

    public enum PieceType{

        /* This gives us the piece type so that we may
        * Identify a piece via toString*/

        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}
