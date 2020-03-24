package com.example.rps4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

const val ROCK = 0
const val PAPER = 1
const val SCISSORS = 2
const val WIN = 2
const val LOSE = 1
const val DRAW = 0

class MainActivity : AppCompatActivity() {
    private lateinit var rps4Repository: RPS4Repository
    private lateinit var context: Context
    var win = 0
    var draw = 0
    var lost = 0
    val ItemArray: Array<Array<Int>> = arrayOf(
        arrayOf(//rock [0]
            DRAW, //rock rock
            LOSE, //rock paper
            WIN //rock scissors
        ),
        arrayOf(//paper
            WIN, //paper rock
            DRAW, //paper paper
            LOSE //paper scissors
        ),
        arrayOf(//scissors
            LOSE, //scissors rock
            WIN, //scissors paper
            DRAW //scissors scissors
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rps4Repository = RPS4Repository(this)
        context = this
        ivRock.setOnClickListener {
            PlayGame(ROCK)
        }
        ivPaper.setOnClickListener {
            PlayGame(PAPER)
        }
        ivScissors.setOnClickListener {
            PlayGame(SCISSORS)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_show_history -> {
                val profileActivityIntent = Intent(this, HistoryActivity::class.java)
                startActivity(profileActivityIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun PlayGame(selection: Int) {

        var computer = computerDecides()
        var winner = whoWon(computer, selection)

        addGame(computer, selection, winner)

        //update screen
        changeStatus(winner, tvStatus, this)
        changeImage(selection, ivUser)
        changeImage(computer, ivComputer)
    }

    fun computerDecides(): Int {
        return (0..2).random()
    }

    companion object {
        fun changeImage(selected: Int, imageView: ImageView) {
            if (selected == ROCK) {
                imageView.setImageResource(R.drawable.rock)
            }
            if (selected == PAPER) {
                imageView.setImageResource(R.drawable.paper)
            }
            if (selected == SCISSORS) {
                imageView.setImageResource(R.drawable.scissors)
            }
        }

        fun changeStatus(status: Int, textView: TextView, context: Context) {
            var StatusMessage = ""
            when (status) {
                DRAW -> StatusMessage = context.getString(R.string.youDraw)
                LOSE -> StatusMessage = context.getString(R.string.youLose)
                WIN -> StatusMessage = context.getString(R.string.youWin)
            }
            textView.text = StatusMessage
        }
    }

    fun whoWon(computer: Int, user: Int): Int {
        return ItemArray[user][computer]
    }

    private fun addGame(computer: Int, user: Int, status: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val Games = RPS4(
                computerPick = computer,
                userPick = user,
                status = status,
                date = System.currentTimeMillis()
            )

            withContext(Dispatchers.IO) {
                rps4Repository.insertGames(Games)
            }
        }
    }
}
