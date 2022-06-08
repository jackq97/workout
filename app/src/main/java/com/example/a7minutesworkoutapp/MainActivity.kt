package com.example.a7minutesworkoutapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.a7minutesworkoutapp.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //now we gonna set a new content view in on create method after making
        //binding object ( feature of view binding )
        binding = ActivityMainBinding.inflate(layoutInflater)
        //here now we set the view binding (our new approach)
        //we inflated the xml file and stored it in binding variable
        //after that we set it to our set content view (to access the views)
        setContentView(binding?.root)

        //now after setting the view binding we don't need to create
        //variables for any of our views

        //val flStartButton: FrameLayout = findViewById(R.id.frame_layout)
        //the alternative
        //no need to set global variables , we can access this view throughout the main file
        binding?.flStart?.setOnClickListener {
            //here on button click we want to move to some other activity
            //for that we are going to use intent
            //in intent class we going to pass the context
            //and the activity we are moving to
            val intent = Intent(this, ExerciseActivity::class.java)
            //now to start our activity
            //we will use start activity and pass the intent in it.
            startActivity(intent)
        }

        // launching bmi activity
        binding?.llBmi?.setOnClickListener {
            val intent = Intent(this@MainActivity, Bmi::class.java)
            startActivity(intent)
        }

        binding?.llHistory?.setOnClickListener{
            val intent = Intent(this@MainActivity, History::class.java)
            startActivity(intent)

        }

        //now whenever you use the view binding also
        //use the onDestroy fun to set the binding back to null
        //to avoid memory leaks
/* It is necessary and a really good practice, specially in Android where memory restrictions are huge,
you really need to take care of cleaning up resources as and when you are done with them.
ViewBinding will generate a custom ViewBinding class which will keep references to all your views inside Fragment, if ViewBinding is not cleared or set to null,
 it won't be eligible for GC, thereby holding all the views in memory even though you are not using it,
 leading to memory leaks. So yes, it is always better to set it to null at the end of life cycle */

        fun onDestroy(){
            super.onDestroy()
            binding = null
        }

    }
    //main

    //code to use view binding
    //we need to pass the activity we are using it in
    private var binding: ActivityMainBinding? = null



}