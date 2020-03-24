package com.example.rps4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.view.MenuItem


class HistoryActivity : AppCompatActivity() {
    private val games = arrayListOf<RPS4>()
    private val rps4Adapter = ProductAdapter(games)
    private lateinit var rps4Repository: RPS4Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =  this.getString(R.string.gameHistory)
        rps4Repository = RPS4Repository(this)
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_history -> {
                val profileActivityIntent = Intent(this, HistoryActivity::class.java)
                deleteGameHistory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        rvHistory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvHistory.adapter = rps4Adapter
        rvHistory.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        createItemTouchHelper().attachToRecyclerView(rvHistory)
        getGamesFromDatabase()
    }

    private fun deleteGameHistory(){
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                rps4Repository.deleteAllGames()
            }
            getGamesFromDatabase()
        }
    }

    private fun getGamesFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val shoppingList = withContext(Dispatchers.IO) {
                rps4Repository.getAllGames()
            }
            games.clear()
            games.addAll(shoppingList)
            rps4Adapter.notifyDataSetChanged()
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = games[position]
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        rps4Repository.deleteGames(gameToDelete)
                    }
                    getGamesFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }
}
