package com.chess.engine.player;

public enum MoveStatus {
    DONE{
        @Override
        boolean isDone(){
            return true;
        }
    },
    ILLEGAL_MOVE{
        @Override
        boolean isDone() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK{
        @Override
        boolean isDne(){
            return false;
        }
    };

    abstract boolean isDone();
}
