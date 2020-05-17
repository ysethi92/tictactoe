package com.yashsethi.tictactoe.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import com.yashsethi.tictactoe.Board
import com.yashsethi.tictactoe.R
import kotlinx.android.synthetic.main.activity_player_vs_bot.*
import java.lang.StringBuilder
import java.util.*

class GamePlay : AppCompatActivity() {

    private var PVB_COMPUTER_WIN_COUNT = 0
    private var PVB_PLAYER_WIN_COUNT = 0
    private var PVP_PLAYER1_WIN_COUNT = 0
    private var PVP_PLAYER2_WIN_COUNT = 0
    val bestMove: Board.Move = Board.Move()

    var grid = arrayOf(
            arrayOf('_', '_', '_'),
            arrayOf('_', '_', '_'),
            arrayOf('_', '_', '_')
    )
    var occupied = arrayOf(
            arrayOf(0,0,0),
            arrayOf(0,0,0),
            arrayOf(0,0,0)
    )
    private val board = Board(grid)
    private var playCount = -1
    private var gameMode = ""
    private var botLevel = ""
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.activity_player_vs_bot)

        player1_win_count.text = "0"
        player2_win_count.text = "0"

        val intent: Intent = getIntent()
        gameMode = intent.getStringExtra("gameMode")
        player1.text = intent.getStringExtra("player1")
        player2.text = intent.getStringExtra("player2")
        if(intent.getStringExtra("level") != null) {
            botLevel = intent.getStringExtra("level")
        }
        loadBoard()
        home.setOnClickListener {
            if(!goBack() || layout_board.visibility == View.GONE) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                showSnackBar()
            }
        }


        reset.setOnClickListener {
            player1_win_count.text = "0"
            player2_win_count.text = "0"
            PVB_COMPUTER_WIN_COUNT = 0
            PVB_PLAYER_WIN_COUNT = 0
            PVP_PLAYER1_WIN_COUNT = 0
            PVP_PLAYER2_WIN_COUNT = 0
        }
        game_restart.setOnClickListener {
            if(gameMode == "PvB") {
                resetGame()
                loadBoard()
            }
            else if(!goBack() || layout_board.visibility == View.GONE) {
                resetGame()
                loadBoard()
            } else {
                showSnackBar()
            }
        }
    }
    private fun goBack() : Boolean{
        var flag = false
        for (i in grid.indices) {
            for (j in grid.indices) {
                if(grid[i][j] != '_') {
                    flag = true
                }
            }
        }
        return flag
    }

    private fun showSnackBar() {
        val view : View= findViewById(R.id.parentLayout)
        Snackbar.make(view, "Match in Progress", Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if(!goBack() || layout_board.visibility == View.GONE)
            super.onBackPressed()
        else {
            showSnackBar()
        }
    }

    private fun resetGame() {
        grid = arrayOf(
                arrayOf('_', '_', '_'),
                arrayOf('_', '_', '_'),
                arrayOf('_', '_', '_')
        )

        for(i in boardCells.indices) {
            for(j in boardCells.indices) {
                boardCells[i][j] = null
                occupied[i][j] = 0
            }
        }
        playCount = -1

        layout_board.visibility =View.VISIBLE
        tv_game_result.visibility = View.GONE
    }

    private fun loadBoard() {

        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 230
                    bottomMargin = 5
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                boardCells[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))
                layout_board.addView(boardCells[i][j])
            }
        }
    }

    inner class CellClickListener(
            private val i : Int,
            private val j : Int
    ) : View.OnClickListener {
        override fun onClick(v: View?) {
            if(occupied[i][j] == 0) {
                if (gameMode == "PvP") {
                    playCount++
                    if (playCount % 2 == 0) {
                        grid[i][j] = board.player
                        boardCells[i][j]?.setImageResource(R.drawable.circle)
                        occupied[i][j] = 1
                    } else {
                        grid[i][j] = board.player2
                        boardCells[i][j]?.setImageResource(R.drawable.cross)
                        occupied[i][j] = 1
                    }
                } else if (gameMode == "PvB") {
                    playCount++
                    if (playCount % 2 == 0) {
                        grid[i][j] = board.player
                        boardCells[i][j]?.setImageResource(R.drawable.circle)
                        occupied[i][j] = 1
                    }
                    if (board.isMovesLeft(grid)) {
                        if(botLevel == "easy"){
                            var x = Random().nextInt(3)
                            var y = Random().nextInt(3)
                            while(occupied[x][y]==1) {
                                x = Random().nextInt(3)
                                y = Random().nextInt(3)
                            }
                            val thread = Thread {
                                Thread.sleep(100)
                                runOnUiThread(Runnable {
                                    boardCells[x][y]?.setImageResource(R.drawable.cross)
                                })
                            }
                            thread.start()
                            grid[x][y] = board.bot
                            occupied[x][y] = 1
                        } else {
                            val bestMove: Board.Move = board.findBestMove(grid, botLevel)
                            grid[bestMove.row][bestMove.col] = board.bot
                            boardCells[bestMove.row][bestMove.col]?.setImageResource(R.drawable.cross)
                            occupied[bestMove.row][bestMove.col] = 1
                        }
                        playCount++
                    }
                }

                val result = evaluate(grid, board)
                if (result != "") {
                    layout_board.visibility = View.GONE
                    tv_game_result.visibility = View.VISIBLE
                    val resultText = StringBuilder()
                    if (result == "Won") {
                        resultText.append(player1.text)
                        resultText.append(" Won")
                        tv_game_result.text = resultText
                        if(gameMode == "PvB") {
                            PVB_PLAYER_WIN_COUNT++
                            player1_win_count.text = PVB_PLAYER_WIN_COUNT.toString()
                        } else {
                            PVP_PLAYER1_WIN_COUNT++
                            player1_win_count.text = PVP_PLAYER1_WIN_COUNT.toString()
                        }
                    } else if (result == "Lost") {
                        resultText.append(player2.text)
                        resultText.append(" Won")
                        tv_game_result.text = resultText
                        if(gameMode == "PvB") {
                            PVB_COMPUTER_WIN_COUNT++
                            player2_win_count.text = PVB_COMPUTER_WIN_COUNT.toString()
                        } else {
                            PVP_PLAYER2_WIN_COUNT++
                            player2_win_count.text = PVP_PLAYER2_WIN_COUNT.toString()
                        }
                    }
                } else {
                    if (!board.isMovesLeft(grid)) {
                        layout_board.visibility = View.GONE
                        tv_game_result.visibility = View.VISIBLE
                        tv_game_result.text = getString(R.string.match_draw)
                    }
                }
            }
        }
    }

    private fun evaluate(grid: Array<Array<Char>>, board: Board): String {
        for (i in grid.indices) {
            if (grid[i][0] == grid[i][1] &&
                    grid[i][1] == grid[i][2]) {
                if (grid[i][0] == board.bot)
                    return "Lost"
                else if (grid[i][0] == board.player)
                    return "Won"
            }
        }
        for (i in grid.indices) {
            if (grid[0][i] == grid[1][i] &&
                    grid[1][i] == grid[2][i]) {
                if (grid[0][i] == board.bot)
                    return "Lost"
                else if (grid[0][i] == board.player)
                    return "Won"
            }
        }
        if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) {
            if (grid[0][0] == board.bot)
                return "Lost"
            else if (grid[0][0] == board.player)
                return "Won"
        }

        if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]) {
            if (grid[0][2] == board.bot)
                return "Lost"
            else if (grid[0][2] == board.player)
                return "Won"
        }
        return ""
    }
}
