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

public class Rook extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES ={-8, -1, 1, 8};

    Rook(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> LegalMoves = new ArrayList<>();

        for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if(isFirstColumnExclusion(this.piecePosition, candidateCoordinateOffset)||
                        isEighthColumnExclusion(this.piecePosition, candidateCoordinateOffset)){
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate); //This wil give us the tile we want to move on to
                    if(!candidateDestinationTile.isTileOccupied()){ //If not occupied
                        LegalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    else{
                        final Piece pieceDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceDestination.getPieceAlliance(); //We need to know what alliance it is
                        if(this.pieceAlliance != pieceAlliance){ //If enemy piece you can kill the enemy therefore it is a move
                            LegalMoves.add(new Move.AttackMove(board,this, candidateDestinationCoordinate, pieceDestination));
                        }
                        break;
                    }
                }
            }

        }

        return ImmutableList.copyOf(LegalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && candidateOffset == -1;
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && candidateOffset == 1;
    }
}
