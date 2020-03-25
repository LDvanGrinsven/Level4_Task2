package com.example.rps4.ui

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
import com.example.rps4.R
import com.example.rps4.database.RPS4Repository
import com.example.rps4.model.RPS4

const val Rock = 0
const val Paper = 1
const val Scissors = 2
const val Win = 2
const val Lose = 1
const val Draw = 0

class MainActivity : AppCompatActivity() {
    private lateinit var rps4Repository: RPS4Repository
    private lateinit var context: Context

// Detemining what combinations are win draw or lose
    val ItemArray: Array<Array<Int>> = arrayOf(
        arrayOf(//rock
            Draw,
            Lose,
            Win
        ),
        arrayOf(//paper
            Win,
            Draw,
            Lose
        ),
        arrayOf(//scissors
            Lose,
            Win,
            Draw
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rps4Repository = RPS4Repository(this)
        context = this
        ivRock.setOnClickListener {
            PlayGame(Rock)
        }
        ivPaper.setOnClickListener {
            PlayGame(Paper)
        }
        ivScissors.setOnClickListener {
            PlayGame(Scissors)
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

        var computer = (0..2).random()
        var winner = ItemArray[selection][computer]

        CoroutineScope(Dispatchers.Main).launch {
            val Games = RPS4(
                computerPick = computer,
                userPick = selection,
                status = winner,
                date = System.currentTimeMillis()
            )
            withContext(Dispatchers.IO) {
                rps4Repository.insertGames(Games)
            }
        }

        //update screen
        changeStatus(winner, tvStatus, this)
        changeImage(selection, ivUser)
        changeImage(computer, ivComputer)
    }

    companion object {
        fun changeImage(selected: Int, imageView: ImageView) {
            if (selected == Rock) {
                imageView.setImageResource(R.drawable.rock)
            }
            if (selected == Paper) {
                imageView.setImageResource(R.drawable.paper)
            }
            if (selected == Scissors) {
                imageView.setImageResource(R.drawable.scissors)
            }
        }

        fun changeStatus(status: Int, textView: TextView, context: Context) {
            var StatusMessage = ""
            when (status) {
                Draw -> StatusMessage = context.getString(R.string.youDraw)
                Lose -> StatusMessage = context.getString(R.string.youLose)
                Win -> StatusMessage = context.getString(R.string.youWin)
            }
            textView.text = StatusMessage
        }
    }
}
