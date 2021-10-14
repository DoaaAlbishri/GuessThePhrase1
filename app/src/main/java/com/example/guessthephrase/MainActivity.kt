package com.example.guessthephrase

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var editText : EditText
    lateinit var button : Button
    lateinit var myLayout : ConstraintLayout
    lateinit var myRv : RecyclerView
    lateinit var tv1 : TextView
    lateinit var tv5 : TextView

    var pharse = "Hello World"
    var str = "*".repeat(pharse.length)
    var Newstr = str
    var left = 10
    var letter = 10
    var guessList = arrayListOf<String>()
    var leftList = arrayListOf<String>()
    var letterList = arrayListOf<String>()

    private val sharedPrefFile = "kotlinsharedpreference"
    var score = 0
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myLayout = findViewById(R.id.clMain)
        tv1 = findViewById(R.id.tv)
        tv5 = findViewById(R.id.tv5)
        myRv = findViewById(R.id.recyclerView)
        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)
        sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        tv1.setText("Pharse: ${str} \n Gussed Letter: ")

        val sharedIdValue = sharedPreferences.getInt("id_key",0)
        if(sharedIdValue.equals(0)){
            tv5.setText("default Score: ${sharedIdValue.toString()}")
        }else{
            tv5.setText("High Score : ${sharedIdValue.toString()}")
        }

        myRv.adapter = RecyclerViewAdapter(guessList, leftList, letterList)
        myRv.layoutManager = LinearLayoutManager(this)

        button.setOnClickListener {
            pharse()
            editText.text.clear()

        }
    }

    private fun pharse() {
        if (left > 0) {
            var input = editText.text.toString()
            if (!editText.text.isEmpty()) {
                if (pharse.toLowerCase() == input.toLowerCase()) {
                    score = 20 - left
                    --left
                    tv1.setText("Pharse: ${input} \n Gussed Letter: ")
                    guessList.add("")
                    letterList.add("")
                    leftList.add("${left} guesses remaining")
                    myRv.adapter?.notifyDataSetChanged()
                    val id:Int = Integer.parseInt(score.toString())
                    val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                    val sharedIdValue = sharedPreferences.getInt("id_key",0)
                    if(score > sharedIdValue) {
                        editor.putInt("id_key", id)
                        editor.apply()
                        editor.commit()
                    }
                    if(sharedIdValue.equals(0)){
                        tv5.setText("default Score: ${sharedIdValue.toString()}")
                    }else{
                        tv5.setText("High Score : ${sharedIdValue.toString()}")
                    }
                    this.recreate()
                } else {
                    --left
                    guessList.add("Wrong guess : ${input}")
                    leftList.add("${left} guesses remaining")
                    letterList.add("")
                    myRv.adapter?.notifyDataSetChanged()
                }
                if (left == 0) {
                    editText.hint = "Gusses the letter"
                }
            }
        } else {
                letter()
        }
    }

    private fun letter(){
            editText.hint = "Gusses the letter"
            var input: Char = editText.text.toString()[0]
            var count = 0
            if (!editText.text.isEmpty() && editText.text.length == 1) {
                letter--
                for (i in 0..pharse.length - 1) {
                    if (pharse[i].toLowerCase().equals(input.toLowerCase())) {
                        var chars = Newstr.toCharArray()
                        chars[i] = input
                        Newstr = String(chars)
                        // Newstr = Newstr.replace(Newstr[i], input)
                        count++
                    }
                    if (pharse[i] == ' ') {
                        var chars = Newstr.toCharArray()
                        chars[i] = ' '
                        Newstr = String(chars)
                    }
                }
                if (count > 0) {
                    tv1.setText("Pharse: ${Newstr} \n Gussed Letter: ${input} ")
                    guessList.add("Wrong guess : ")
                    letterList.add("Founds: ${count} ${input.toUpperCase()}(s)")
                    leftList.add("${letter} guesses remaining")
                    myRv.adapter?.notifyDataSetChanged()
                } else {
                    guessList.add("Wrong guess : ${input}")
                    letterList.add("")
                    leftList.add("${letter} guesses remaining")
                    myRv.adapter?.notifyDataSetChanged()
                }
            } else {
                Snackbar.make(myLayout, "Enter one letter please", Snackbar.LENGTH_LONG).show()
            }
            if (Newstr.equals(pharse, true) || letter == 0) {
                score=(20-10)-letter
                val id:Int = Integer.parseInt(score.toString())
                val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                val sharedIdValue = sharedPreferences.getInt("id_key",0)
                if(score > sharedIdValue) {
                    editor.putInt("id_key", id)
                    editor.apply()
                    editor.commit()
                }
                if(sharedIdValue.equals(0)){
                    tv5.setText("default Score: ${sharedIdValue.toString()}")
                }else{
                    tv5.setText("High Score : ${sharedIdValue.toString()}")
                }
                this.recreate()
            }
        }
    }
