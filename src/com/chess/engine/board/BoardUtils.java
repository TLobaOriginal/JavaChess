package com.chess.engine.board;
/*
 * Created by Joseph Ogunbiyi on 16/03/21
 */
public class BoardUtils {

    public static final boolean[] FIRST_COLUMN =  initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] SECOND_ROW = null;
    public static final boolean[] SEVENTH_ROW = null;

    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        while(columnNumber < NUM_TILES && columnNumber >= 0){
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW; //SKIPS TO NEXT ROW irrespective of column
        }
        return column;
    }

    private BoardUtils(){
        throw new RuntimeException("You cannot instantiate me");
    }

    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES; //Checks if the tile number is between 0 and 63 inclusive
                                                   //If not then it will return false, therefore it is an invalid Tile Coordinate
    }
}
