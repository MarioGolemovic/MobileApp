package com.example.marioapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.marioapp.data.AppDatabase
import com.example.marioapp.data.entity.User

class EditorActivity : AppCompatActivity() {
    private lateinit var playerName: EditText
    private lateinit var playerPrice: EditText
    private lateinit var playerNumber: EditText
    private lateinit var playerYears: EditText
    private lateinit var playerPosition: EditText
    private lateinit var btnSave: Button
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        playerName = findViewById(R.id.name)
        playerPrice = findViewById(R.id.price)
        playerNumber = findViewById(R.id.number)
        playerYears = findViewById(R.id.years)
        playerPosition = findViewById(R.id.position)
        btnSave = findViewById(R.id.btn_save)
        database = AppDatabase.getInstance(applicationContext)

        var intent = intent.extras
        if (intent != null) {
            val id = intent.getInt("id", 0)
            val user = database.userDao().get(id)

            playerName.setText(user.playerName?.removePrefix("  NAME:                 "))
            playerPrice.setText(
                user.playerPrice?.removePrefix("  VALUE:                ")?.removeSuffix(" €")
            )
            playerNumber.setText(user.playerNumber?.removePrefix("  NUMBER:            "))
            playerYears.setText(user.playerYears?.removePrefix("  YEARS:                "))
            playerPosition.setText(user.playerPosition?.removePrefix("  POSITION:          "))
        }

        btnSave.setOnClickListener {
            val name = playerName.text.toString().trim()
            val price = playerPrice.text.toString().trim()
            val number = playerNumber.text.toString().trim()
            val years = playerYears.text.toString().trim()
            val position = playerPosition.text.toString().trim()

            if (playerName.text.isNotEmpty() && playerPrice.text.isNotEmpty() && playerNumber.text.isNotEmpty() && playerYears.text.isNotEmpty() && playerPosition.text.isNotEmpty()) {
                val formattedName = "  NAME:                 $name"
                val formattedPrice = "  VALUE:                $price €"
                val formattedNumber = "  NUMBER:            $number"
                val formattedYears = "  YEARS:                $years"

                if (isValidPosition(position) && isValidNumber(number) && isValidYears(years) && isUniqueNumber(
                        number
                    )
                ) {
                    val formattedPosition = "  POSITION:          $position"

                    if (intent != null) {
                        database.userDao().update(
                            User(
                                intent.getInt("id", 0),
                                formattedName,
                                formattedPrice,
                                formattedNumber,
                                formattedYears,
                                formattedPosition
                            )
                        )
                    } else {
                        database.userDao().insertAll(
                            User(
                                null,
                                formattedName,
                                formattedPrice,
                                formattedNumber,
                                formattedYears,
                                formattedPosition
                            )
                        )
                    }

                    finish()
                } else {
                    if (!isValidPosition(position)) {
                        Toast.makeText(
                            applicationContext,
                            "Invalid position. Please enter either GK, DEF, MID, or ATT.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!isValidNumber(number)) {
                        Toast.makeText(
                            applicationContext,
                            "Invalid number. Please enter a number between 1 and 99.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!isValidYears(years)) {
                        Toast.makeText(
                            applicationContext,
                            "Invalid years. Please enter a number between 16 and 45.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!isUniqueNumber(number)) {
                        Toast.makeText(
                            applicationContext,
                            "The jersey number already exists. Please choose a different number.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Complete the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidPosition(position: String): Boolean {
        return position.equals("GK", ignoreCase = true)
                || position.equals("DEF", ignoreCase = true)
                || position.equals("MID", ignoreCase = true)
                || position.equals("ATT", ignoreCase = true)
    }

    private fun isValidNumber(number: String): Boolean {
        val numberValue = number.toIntOrNull()
        return numberValue != null && numberValue in 1..99
    }

    private fun isValidYears(years: String): Boolean {
        val yearsValue = years.toIntOrNull()
        return yearsValue != null && yearsValue in 16..45
    }

    private fun isUniqueNumber(number: String): Boolean {
        val existingUsers = database.userDao().getAll()
        for (user in existingUsers) {
            val existingNumber =
                user.playerNumber?.removePrefix("  NUMBER:            ")?.toIntOrNull()
            if (existingNumber != null && existingNumber == number.toInt()) {
                return false
            }
        }
        return true
    }
}