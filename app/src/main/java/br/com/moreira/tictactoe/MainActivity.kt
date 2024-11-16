package br.com.moreira.tictactoe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Board and player control
    private lateinit var board: Array<Array<Button>>
    private var currentPlayer = "X"
    private var boardState = Array(3) { Array(3) { "" } }
    private var xWins = 0
    private var oWins = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Button to exit the game
        findViewById<ImageView>(R.id.imageViewQuit).setOnClickListener {
            finish()  // Close the Activity and exit the game
        }

        // Handling system window insets (padding for system bars like status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initializing the game board buttons
        board = arrayOf(
            arrayOf(
                findViewById(R.id.button0),
                findViewById(R.id.button1),
                findViewById(R.id.button2)
            ),
            arrayOf(
                findViewById(R.id.button3),
                findViewById(R.id.button4),
                findViewById(R.id.button5)
            ),
            arrayOf(
                findViewById(R.id.button6),
                findViewById(R.id.button7),
                findViewById(R.id.button8)
            )
        )

        // Adding click event listeners for each button on the board
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j].setOnClickListener { onCellClick(it, i, j) }
            }
        }

        // Button to reset the game
        findViewById<Button>(R.id.buttonReset).setOnClickListener {
            resetGame()
        }
    }

    // Handle click on a cell in the board
    private fun onCellClick(view: View, row: Int, col: Int) {
        val button = view as Button

        // Check if the cell is empty
        if (button.text.isEmpty()) {
            button.text = currentPlayer
            boardState[row][col] = currentPlayer

            // Change button text color based on the current player
            if (currentPlayer == "X") {
                button.setTextColor(resources.getColor(R.color.colorSecondaryDark)) // X color
            } else {
                button.setTextColor(resources.getColor(R.color.colorPrimaryLight2)) // O color
            }

            // Check for a win or a tie
            if (checkWin()) {
                if (currentPlayer == "X") {
                    xWins++ // Increment X's win count
                    findViewById<TextView>(R.id.valueYou).text = xWins.toString() // Update X's score
                } else {
                    oWins++ // Increment O's win count
                    findViewById<TextView>(R.id.valueDroid).text = oWins.toString() // Update O's score
                }

                Toast.makeText(this, "Player $currentPlayer wins!", Toast.LENGTH_SHORT).show()
                disableBoard() // Disable all buttons after a win

                // Restart the game after 2 seconds
                Handler(Looper.getMainLooper()).postDelayed({
                    resetGame() // Reset the game
                }, 2000)

            } else if (isBoardFull()) {
                Toast.makeText(this, "It's a tie!", Toast.LENGTH_SHORT).show()
                // Reset the game after 2 seconds to allow the tie to be seen
                Handler(Looper.getMainLooper()).postDelayed({
                    resetGame() // Reset the game
                }, 2000)
            } else {
                // Switch to the other player
                currentPlayer = if (currentPlayer == "X") "O" else "X"
            }
        }
    }

    // Check if the current player has won
    private fun checkWin(): Boolean {
        // Check rows, columns, and diagonals
        for (i in 0..2) {
            if (boardState[i][0] == currentPlayer && boardState[i][1] == currentPlayer && boardState[i][2] == currentPlayer) return true
            if (boardState[0][i] == currentPlayer && boardState[1][i] == currentPlayer && boardState[2][i] == currentPlayer) return true
        }
        if (boardState[0][0] == currentPlayer && boardState[1][1] == currentPlayer && boardState[2][2] == currentPlayer) return true
        if (boardState[0][2] == currentPlayer && boardState[1][1] == currentPlayer && boardState[2][0] == currentPlayer) return true

        return false
    }

    // Check if the board is full (no empty spaces left)
    private fun isBoardFull(): Boolean {
        for (i in boardState.indices) {
            for (j in boardState[i].indices) {
                if (boardState[i][j].isEmpty()) return false
            }
        }
        return true
    }

    // Disable all buttons on the board
    private fun disableBoard() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j].isEnabled = false
            }
        }
    }

    // Reset the game to the initial state
    private fun resetGame() {
        currentPlayer = "X"
        boardState = Array(3) { Array(3) { "" } }
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j].text = ""
                board[i][j].isEnabled = true
            }
        }
    }
}