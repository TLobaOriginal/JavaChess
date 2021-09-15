package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

/*Knights can only move
* 6 10 15 17
* */
public class Knight extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATE = {-17, -15, -10, - 6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT);
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        int candidateDestinationCoordinate;

        List<Move> LegalMoves = new ArrayList<>();
        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE){
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset; //Applies the offset to new position
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) /*If this is a valid position or move can be made (CHANGE MADE)*/) {
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate); //This wil give us the tile we want to move on to
                if(!candidateDestinationTile.isTileOccupied()){ //If not occupied
                    LegalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
                else{
                    final Piece pieceDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceDestination.getPieceAlliance(); //We need to know what alliance it is
                    if(this.pieceAlliance != pieceAlliance){ //If enemy piece you can kill the enemy therefore it is a move
                        LegalMoves.add(new AttackMove(board,this, candidateDestinationCoordinate, pieceDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(LegalMoves);
    }

    @Override
    public Piece movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), this.getPieceAlliance());
    }

    /**Edge Case*/
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] &&  (candidateOffset == 6 || candidateOffset == -10);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15|| candidateOffset == -6
        || candidateOffset == 10 || candidateOffset == 17);
    }
}

