package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

/*
 * Created by Joseph Ogunbiyi on 16/03/21
 */
public abstract class Move {

    /*We will be materialising a new board and not mutating board*/
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate)
    {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public boolean isCastlingMove(){
        return false;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        //result = prime * result + this.movePiece.getPiecePosition();
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hasCode();
        return result;
    }

    @Override
    public boolean equals(final Object object){
        if(this == object)
            return true;
        if(!(object instanceof Move))
            return false;
        final Move otherMove = (Move)object;
        return  getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }

    public int getCurrentCoordinate(){return this.getMovedPiece().getPiecePosition();}

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute() {
        final Builder builder = new Builder(); //Materialise a new board
        for(Piece piece: board.getCurrentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece))
                builder.setPiece(piece);
            else
                builder.setPiece(this.movedPiece.movePiece(this));
        }

        for(Piece piece: board.getCurrentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        //Represent the moved piece after it's made its move
        builder.setMoveMaker(null);//Will be fixed
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    /**Different Move types*/

    public static final class MajorMove extends Move{

        public MajorMove(final Board board,
                         final Piece movePiece,
                         final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }

    }


    public static class AttackMove extends Move{
        //In attack move we need to know what piece is being attacked
        final Piece attackedPiece;
        public AttackMove(final Board board,
                   final Piece movePiece,
                   final int destinationCoordinate,
                   final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack(){
            return true;
        }

        @Override
        public Piece getAttackedPiece(){
            return attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hasCode();
        }

        @Override
        public boolean equals(final Object object){
            if(this == object)
                return true;
            if(!(object instanceof AttackMove))
                return false;
            final AttackMove otherAttackMove = (AttackMove) object;
            return  super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }

    public static final class PawnMove extends Move{

        public PawnMove(final Board board,
                         final Piece movePiece,
                         final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }

    }

    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(final Board board,
                        final Piece movePiece,
                        final int destinationCoordinate,
                        final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate, attackedPiece);
        }

    }

    public static final class PawnEnPassonAttackMove extends PawnAttackMove{

        public PawnEnPassonAttackMove(final Board board,
                              final Piece movePiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate, attackedPiece);
        }

    }

    public static final class PawnJump extends Move{

        public PawnJump(final Board board,
                         final Piece movePiece,
                         final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }


        @Override
        public Board execute(){
            /*Using the override we can make an execute function that is unique
            * To the PawnJump Move class*/
            final Builder builder = new Builder();
            for(final Piece piece: this.board.getCurrentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move{
        protected final Rook castRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                         final Piece movePiece,
                         final int destinationCoordinate,
                          final Rook castRook,
                          final int castleRookStart,
                          final int castleRookDestination) {
            super(board, movePiece, destinationCoordinate);

            this.castRook = castRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastRook() {
            return castRook;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece: this.board.getCurrentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castRook.equals(piece)) { //If the piece being set is not the moved piece or the castle rook then set the piece
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.castRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

    }

    public static final class KingSideCastleMove extends CastleMove{
        /*Implementation of the special move King Side Castle
        * We will then have to check the requirements for whether this is a legal move*/
        public KingSideCastleMove(final Board board,
                                  final Piece movePiece,
                                  final int destinationCoordinate,
                                  final Rook castRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movePiece, destinationCoordinate, castRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString(){
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove{
        /*Implementation of the special move King Side Castle*/
        public QueenSideCastleMove(final Board board,
                                   final Piece movePiece,
                                   final int destinationCoordinate,
                                   final Rook castRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movePiece, destinationCoordinate, castRook, castleRookStart, castleRookDestination);
        }
        @Override
        public String toString(){
            return "0-0";
        }
    }

    public static final class NullMove extends Move{

        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Cannot execute a null move");
        }

    }

    public static class MoveFactory{
        /* Has a convenience method defined*/
        private MoveFactory(){
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){
            for(final Move move: board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate)
                    return move;
            }
            return NULL_MOVE;
        }
    }

}
/**WE PLAN TO BUILD ON THIS INCREMENATLLY*/