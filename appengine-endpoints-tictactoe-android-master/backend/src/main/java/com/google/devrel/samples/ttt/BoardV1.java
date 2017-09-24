/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.google.devrel.samples.ttt;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

import java.util.Random;

/**
 * Defines v1 of a Board resource as part of the tictactoe API, which provides
 * clients the ability to query for a computer's next move given an input
 * board.
 */
@Api(
        name = "tictactoe",
        version = "v1",
        clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID, Ids.IOS_CLIENT_ID},
        audiences = {Ids.ANDROID_AUDIENCE}
)
public class BoardV1 {
    public static final char X = 'X';
    public static final char O = 'O';
    public static final char DASH = '-';

    /**
     * Provides the ability to insert a new Score entity.
     *
     * @param board object representing the state of the board
     * @return the board including the computer's move
     */
    @ApiMethod(name = "board.getmove", httpMethod = "POST")
    public Board getmove(Board board) {
        char[][] parsed = parseBoard(board.getState());
        int free = countFree(parsed);
        parsed = addMove(parsed, free);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parsed.length; i++) {
            builder.append(String.valueOf(parsed[i]));
        }
        Board updated = new Board();
        updated.setState(builder.toString());
        return updated;
    }

    private char[][] parseBoard(String boardString) {
        char[][] board = new char[3][3];
        char[] chars = boardString.toCharArray();
        if (chars.length == 9) {
            for (int i = 0; i < chars.length; i++) {
                board[i/3][i%3] = chars[i];
            }
        }

        return board;
    }

    private int countFree(char[][] board) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != X && board[i][j] != O) {
                    count++;
                }
            }
        }
        return count;
    }

    private char[][] addMove(char[][] board, int free) {
        int index = new Random().nextInt(free) + 1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == DASH) {
                    if (free == index) {
                        board[i][j] = O;
                        return board;
                    } else {
                        free--;
                    }
                }
            }
        }
        // Only occurs when empty > the number of actual empty squares.
        return board;
    }
}