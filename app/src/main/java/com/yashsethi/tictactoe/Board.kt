package com.yashsethi.tictactoe

import org.jetbrains.annotations.Nullable
import kotlin.math.*

class Board(val board: Array<Array<Char>>) {

    val player = 'o'
    val bot = 'x'
    val player2 = 'x'

    private fun minMax(board : Array<Array<Char>>, depth: Int, isMax: Boolean): Int {
        val score = evaluate(board)
        if(score == 10)
            return score  // Computer won

        if(score == -10)
            return score  // user won

        if (!isMovesLeft(board))
            return 0
        if(isMax) {
            var best = -1000
            for(i in board.indices) {
                for(j in board.indices) {
                    if(board[i][j] == '_') {
                        board[i][j] = bot
                        best = max(best, minMax(board, depth+1, !isMax))
                        board[i][j] = '_'
                    }
                }
            }
            return best
        }
        else
        {
            var best = 1000
            for (i in board.indices) {
                for (j in board.indices) {
                    if (board[i][j]=='_') {
                        board[i][j] = player
                        best = min(best, minMax(board, depth+1, !isMax))
                        board[i][j] = '_'
                    }
                }
            }
            return best
        }
    }
    fun findBestMove(board: Array<Array<Char>>, botLevel: String): Move {
        var bestVal = -1000
        val bestMove = Move()
        bestMove.row = -1
        bestMove.col = -1
        for(i in board.indices) {
            for(j in board.indices) {
                if(board[i][j] == '_') {
                    board[i][j] = bot
                    val moveVal = minMax(board, 0, false)
                    board[i][j] = '_'
                    if(moveVal > bestVal) {
                        bestMove.row = i
                        bestMove.col = j
                        if(botLevel == "hard")
                            bestVal = moveVal
                    }
                }
            }
        }
        return bestMove
    }
    fun isMovesLeft(board: Array<Array<Char>>): Boolean {
        for(i in board.indices) {
            for( j in board.indices) {
                if(board[i][j] == '_') {
                    return true
                }
            }
        }
        return false
    }

    private fun evaluate(board: Array<Array<Char>>): Int {
        for (i in board.indices)
        {
            if (board[i][0]==board[i][1] &&
                    board[i][1]==board[i][2])
            {
                if (board[i][0]==bot)
                    return +10
                else if (board[i][0]==player)
                    return -10
            }
        }
        for (i in board.indices)
        {
            if (board[0][i]==board[1][i] &&
                    board[1][i]==board[2][i])
            {
                if (board[0][i]==bot)
                    return +10

                else if (board[0][i]==player)
                    return -10
            }
        }
        if (board[0][0]==board[1][1] && board[1][1]==board[2][2])
        {
            if (board[0][0]==bot)
                return +10
            else if (board[0][0]==player)
                return -10
        }

        if (board[0][2]==board[1][1] && board[1][1]==board[2][0])
        {
            if (board[0][2]==bot)
                return +10
            else if (board[0][2]==player)
                return -10
        }
        return 0

    }

    class Move {
        @Nullable var row: Int = -1
        @Nullable var col: Int = -1
    }
}