package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player{
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        /*The requirements of the kings castling is
        * 1. The castling must be kingside or queenside.[5]
          2. Neither the king nor the chosen rook has previously moved.
          3. There are no pieces between the king and the chosen rook.
          4. The king is not currently in check.
          5. The king does not pass through a square that is attacked by an enemy piece.
          6. The king does not end up in check. (True of any legal move.)*/
        if(this.playerKing.isFirstMove() && !this.isInCheckmate()){ //Covers 2a and 4
            //Whites kingside castle
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){
                final Tile rookTile = this.board.getTile(63); //Covers 3
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){ // Covers 2b
                    if(calculateAttacksOnTile(61, opponentLegals).isEmpty()&&
                            calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){ //Deals with 5
                        kingCastles.add(new KingSideCastleMove(this.board,
                                this.playerKing, 62,
                                (Rook)rookTile.getPiece(),
                                rookTile.getTileCoordinate(), 61));
                    }
                }
            }
            if(!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()){
                    final Tile rookTile = this.board.getTile(56);
                    if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(58 ,opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()){
                        kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58,
                                (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                    }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
