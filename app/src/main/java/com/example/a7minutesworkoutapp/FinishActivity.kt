package com.example.a7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.a7minutesworkoutapp.databinding.FinishActivityBinding
import com.example.roomdemo.HistoryEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// TODO: finish the activity instead of creating new one

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FinishActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //on create

        val historyDao = (application as HistoryApp).db.historyDao()

        addRecord(historyDao)

        //setting up our toolbar
        setSupportActionBar(binding?.toolbarExercise)

        //to show back button in the toolbar
        if(supportActionBar != null){
            //we need to enable this option to get back button
            //in the tool bar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        //setting a back button in our toolbar
        binding?.toolbarExercise?.setNavigationOnClickListener {
            //here we are calling on back pressed method
            //it's going to
            finish()
        }

        //btn to return to start activity / main activity
        binding?.btnFinish?.setOnClickListener {
            onBackPressed()
        }

    }
    //main

    private var binding: FinishActivityBinding? = null



    // function to add date and time in history database
    private fun addRecord(historyDao: HistoryDao) {
        // setting the name and email
        val c = Calendar.getInstance()
        val dateTime = c.time
        val sdf = SimpleDateFormat("dd MMM yyyy     hh:mm aaa", Locale.getDefault())
        val date = sdf.format(dateTime)
        //Log.e( "local date", date)

        // flag check if they're not empty
        if (date.isNotEmpty()) {

            // coroutine function cause it takes time to insert data
            lifecycleScope.launch{
                // making use of parameter dao and setting the name and email
                // equal to our edit text with the help of insert function
                historyDao.insert(HistoryEntity(date = date))
                Toast.makeText(applicationContext,
                    "record saved"
                    , Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext,
                "Record not saved",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}