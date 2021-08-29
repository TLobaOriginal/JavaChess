package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing; //We will need to keep track of king
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentsMoves){
        this.board = board;
        this.legalMoves = legalMoves;
        this.playerKing = establishKing();
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentsMoves).isEmpty();
    }

    private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        /* By calculating all the moves that result in a piece moving to a specific tile
        * Then we can even use this to check to see whether or not a king is in check.*/

        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move: moves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    public King getPlayerKing() {
        return playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    private King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Not a legal chess state, should not reach here.");
    }

    public boolean isLegalMove(Move move){
        return this.legalMoves.contains(move);
    }
    //TODO must implement methods below soon

    public boolean isInCheckmate(){
        //You're in check but you cannot escape
        return isInCheck && !hasEscapeMoves();
    }

    protected boolean hasEscapeMoves(){
        for(final Move move : legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone())
                return true;
        }
        return false;
    }

    public boolean isInStalemate(){
        /*Not in check but all the moves you can make will leave you in check*/
        return !isInCheck && !hasEscapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(Move move){
        //If the move is Illegal then we need to deem it as one
        if(!isLegalMove(move))
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);

        //Otherwise we can then execute it
        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMoves());
        //If kingattacks isn't empty that means there is an attack that leads to the king being taken out
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }


    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}
