package com.example.marioapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.marioapp.adapter.UserAdapter
import com.example.marioapp.data.AppDatabase
import com.example.marioapp.data.entity.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private var list = mutableListOf<User>()
    private lateinit var adapter: UserAdapter
    private lateinit var database: AppDatabase
    private lateinit var totalValueTextView: TextView
    private lateinit var averageYearsTextView: TextView
    private lateinit var editSearch: EditText
    private lateinit var btnSearch: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var logoImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        titleTextView = findViewById(R.id.title)
        logoImageView = findViewById(R.id.logo)
        recyclerView = findViewById(R.id.recycler_view)
        fab = findViewById(R.id.fab)
        editSearch = findViewById(R.id.edit_search)
        btnSearch = findViewById(R.id.btn_search)
        titleTextView.text = "DINAMO ZAGREB squad"
        logoImageView.setImageResource(R.drawable.dinamo_logo)
        totalValueTextView = findViewById(R.id.totalValue)
        averageYearsTextView = findViewById(R.id.averageYears)

        database = AppDatabase.getInstance(applicationContext)
        adapter = UserAdapter(list)
        adapter.setDialog(object : UserAdapter.Dialog{
            override fun onClick(position: Int) {
                val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle(list[position].playerName)
                dialog.setItems(R.array.items_option,DialogInterface.OnClickListener{ dialog, which ->
                    if(which==0){
                        val intent = Intent(this@MainActivity, EditorActivity::class.java)
                        intent.putExtra("id", list[position].uid)
                        startActivity(intent)
                    }else if(which==1){
                        database.userDao().delete(list[position])
                        getData()
                    }else{
                        dialog.dismiss()
                    }

                })
                val dialogView = dialog.create()
                dialogView.show()
            }

        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, VERTICAL))

        fab.setOnClickListener {
            startActivity(Intent(this, EditorActivity::class.java))
        }
        btnSearch.setOnClickListener{
            if(editSearch.text.isNotEmpty()){
                searchData(editSearch.text.toString())
            }else{
                getData()
                Toast.makeText(applicationContext,"blablabla", LENGTH_SHORT).show()
            }
        }
        editSearch.setOnKeyListener { v, keyCode, event ->
            if(editSearch.text.isNotEmpty()){
                searchData(editSearch.text.toString())
            }else{
                getData()
            }
            false
        }

    }
    private fun updateTotalSquadValue() {
        var totalValue = 0
        for (user in list) {
            val price = user.playerPrice?.removePrefix("  VALUE:                ")?.removeSuffix(" €")?.toIntOrNull()
            if (price != null) {
                totalValue += price
            }
        }
        totalValueTextView.text = "Total squad value: $totalValue €"
    }

    private fun updateAverageYears() {
        var totalYears = 0
        for (user in list) {
            val years = user.playerYears?.removePrefix("  YEARS:                ")?.toIntOrNull()
            if (years != null) {
                totalYears += years
            }
        }
        val averageYears = if (list.isNotEmpty()) totalYears / list.size else 0
        averageYearsTextView.text = "Average age of the team: $averageYears"
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun getData(){
        list.clear()
        list.addAll(database.userDao().getAll())
        var totalValue = 0
        var totalYears = 0
        for (user in list) {
            val price = user.playerPrice?.removePrefix("  VALUE:                ")?.removeSuffix(" €")?.toIntOrNull()
            val years = user.playerYears?.removePrefix("  YEARS:                ")?.toIntOrNull()
            if (price != null) {
                totalValue += price
            }
            if (years != null) {
                totalYears += years
            }
        }
        val averageYears = if (list.isNotEmpty()) totalYears / list.size else 0

        totalValueTextView.text = "Total squad value: $totalValue €"
        averageYearsTextView.text = "Average age of the team: $averageYears"
        adapter.notifyDataSetChanged()
        updateTotalSquadValue()
        updateAverageYears()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun searchData(search: String){
        list.clear()

        list.addAll(database.userDao().searchByName(search))
        var totalValue = 0
        var totalYears = 0
        for (user in list) {
            val price = user.playerPrice?.removePrefix("  VALUE:                ")?.removeSuffix(" €")?.toIntOrNull()
            val years = user.playerYears?.removePrefix("  YEARS:                ")?.toIntOrNull()
            if (price != null) {
                totalValue += price
            }
            if (years != null) {
                totalYears += years
            }
        }
        val averageYears = if (list.isNotEmpty()) totalYears / list.size else 0

        totalValueTextView.text = "Total squad value: $totalValue €"
        averageYearsTextView.text = "Average age of the team: $averageYears"
        adapter.notifyDataSetChanged()
        updateTotalSquadValue()
        updateAverageYears()
    }

}