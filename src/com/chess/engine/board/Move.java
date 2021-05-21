package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
/*
 * Created by Joseph Ogunbiyi on 16/03/21
 */
public abstract class Move {

    final Board board;
    final Piece movePiece;
    final int destinationCoordinate;

    private Move(final Board board, final Piece movePiece, final int destinationCoordinate)
    {
        this.board = board;
        this.movePiece = movePiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    /**Different Move types*/

    public static final class MajorMove extends Move{

        public MajorMove(final Board board,
                         final Piece movePiece,
                         final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }
    }


    public static final class AttackMove extends Move{
        //In attack move we need to know what piece is being attacked
        final Piece attackedPiece;
        public AttackMove(final Board board,
                   final Piece movePiece,
                   final int destinationCoordinate,
                   final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
    }

}
/**WE PLAN TO BUILD ON THIS INCREMENATLLY*/