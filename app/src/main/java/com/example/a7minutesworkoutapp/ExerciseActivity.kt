package com.example.a7minutesworkoutapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkoutapp.databinding.ActivityExcerciseBinding
import com.example.a7minutesworkoutapp.databinding.LayoutAlertDialogBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    // TODO: clean up code , multiple flag checks clean 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // onCreate

        // filling our exercise list with data model
        exerciseList = Constants.defaultExerciseList()

        // setting up the exercise adapter
        exerciseStatusAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseStatusAdapter

        // setting up tts here
        // we gonna implement listener directly into the main class
        tts = TextToSpeech(this, this)
        
        // calling our timer function in on create
        setupRestView()

        /*//making it speak the first exercise name
        try {
            speakOut(exerciseList!![0].getName())
        } catch (e: Exception) {
            Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
        }*/

        //setting up our toolbar
        //setSupportActionBar(binding?.toolbarExercise)

        //to show back button in the toolbar
        if(supportActionBar != null){
            //we need to enable this option to get back button
            //in the tool bar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        //setting a back button in our toolbar
        /*binding?.toolbarExercise?.setNavigationOnClickListener {
            // we are calling our alert dialog on back pressed
            onBackPressedAlertDialog()
        }*/

        // function for setting up recycler view
        setupExerciseStatusRecyclerView()

        // on back press alert dialog function

    }
    // main

    // inside our object exercise list method
    private var currentExercisePosition = -1

    private val exerciseTimer: Long = 30
    private val restTimer: Long = 10

    private var binding: ActivityExcerciseBinding? = null // binding
    //tts
    private var tts: TextToSpeech? = null
    // media player
    private var player: MediaPlayer? = null

    private var exerciseStatusAdapter: ExerciseStatusAdapter? = null

    // our rest count down timer
    private var restCountdownTimer: CountDownTimer? = null
    private var exerciseCountdownTimer: CountDownTimer? = null
    // this variable will be set to out progress bar
    private var timerProgress = 0
    // exercise list array list that will pull information
    // from our data model, constants
    private var exerciseList: ArrayList<ExerciseModel>? = null
    // variable for index of the current position of the element

    // our rest timer function
    private fun setRestProgress() {
        timerProgress = 0
        restCountdownTimer = object : CountDownTimer(restTimer*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerProgress++
                binding?.progressBar?.progress = 10 - timerProgress
                binding?.textviewTimer?.text = (10 - timerProgress).toString()
            }


            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {

                // here we are incrementing on timer finish
                // so we can move on in our index
                currentExercisePosition++

                Log.i("in rest timer", "$currentExercisePosition")
                // we gonna need a check here , we only want to move to the rest timer
                // if the current position index doesn't exceed the size of our
                // exercise list array , otherwise the app will crash


                    // once the rest is finished our first exercise start
                    // so we gonna set is selected to true here
                    // TODO: error here
                    exerciseList!![currentExercisePosition].setIsSelected(true)

                    // notifying adapter of changes
                    exerciseStatusAdapter?.notifyDataSetChanged()
                    setupExerciseView()
            }
        }.start()
    }

    /* important function to check if the timer is not already
    * running, if it's running and we start another timer it's
    * gonna mess everything up, so we gonna check with flags if
    * timer is not null and then run the code */
    @SuppressLint("SetTextI18n")
    private fun setupRestView() {

        // rest timer function
        if (restCountdownTimer != null) {
            restCountdownTimer?.cancel()
            timerProgress = 0
        }

        // here we will play our media player file
        // so the users get the audio que to rest
        // error can occur so try and catch
        try {
            // setting the path to our sound directory
            val soundURI = Uri.parse(
                "android.resource://com.example.a7minutesworkoutapp/"
            + R.raw.press_start)
            // now we have the path we will be setting our player
            // inside the media player. create method
            // we gonna pass the context and path
            player = MediaPlayer.create(applicationContext, soundURI)
            // now we are setting a couple of settings for our player
            player?.isLooping = false // we don't want it to loop
            player?.start() // <- this method plays the sound

        } catch (e: Exception) {
            e.printStackTrace()
        }

        // first changing constraints back to default
        setDefaultConstraints()
        // changing text to back to rest
        binding?.tvExercise?.text = "Get Ready For"

        // making the image view invisible
        binding?.ivExercise?.visibility = View.INVISIBLE
        // changing the max progress so it can run shorter timer
        binding?.progressBar?.max = 10

        // setting the upcoming exercise name
        // TODO: error here
        if (exerciseList!!.size > currentExercisePosition + 1) {
            binding?.tvUpcomingExercise?.text =
                exerciseList!![currentExercisePosition + 1].getName()
        }
        // executing rest timer
        setRestProgress()
    }

    // different timer for our exercise this will get called
    // when our rest timer is finished
    private fun setExerciseProgress() {
        // one start of the timer we want to set progress to 0
        timerProgress = 0

        exerciseCountdownTimer = object : CountDownTimer(exerciseTimer*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //for visual countdown
                timerProgress++
                binding?.progressBar?.progress = 30 - timerProgress
                binding?.textviewTimer?.text = (30 - timerProgress).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {


                if (exerciseList!!.size != currentExercisePosition+1) {
                    Log.i(
                        "exercise list size",
                        "${exerciseList!!.size} >  $currentExercisePosition"
                    )
                    // once the exercise is finished our exercise is completed
                    // so we gonna set is completed to true here
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    // notifying adapter of changes
                    exerciseStatusAdapter?.notifyDataSetChanged()
                    // once the exercise finishes we want to execute the rest timer
                    setupRestView()

            } else {
                // right here our exercises are ending

                val intent = Intent(this@ExerciseActivity , FinishActivity::class.java)
                    startActivity(intent)
                    finish()
            }
            }
        }.start()
    }

    // this function does the check to see if our timer
    // is already running or not, it will only run if the
    // timer is not already running
    private fun setupExerciseView() {

        if (exerciseCountdownTimer != null) {
            exerciseCountdownTimer?.cancel()
            timerProgress = 0
        }

        // once this function is executes we want to move our view
        // that's why we are calling this fun here
        setConstraints()
        // in here there our changed to our progress bar view
        // first the max is set to 30 since our exercise timer
        // is 30 seconds long
        binding?.progressBar?.max = 30
        // changing the textview to the exercise name
        //making the image view visible once the exercise starts
        binding?.ivExercise?.visibility = View.VISIBLE

        // TODO: error here
        if (exerciseList!!.size != currentExercisePosition) {

            binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()
            // setting tts for upcoming exercise
            speakOut(exerciseList!![currentExercisePosition].getName())
            //setting the image to our image view
            binding?.ivExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())

        }

        // passing the timer function here after check
        setExerciseProgress()
    }

    // binding null function also, cancelling any running
    // timer to they don't run in background
    override fun onDestroy() {
        super.onDestroy()
        if (restCountdownTimer != null) {
            restCountdownTimer?.cancel()
            timerProgress = 0
        }

        if (exerciseCountdownTimer != null) {
            exerciseCountdownTimer?.cancel()
            timerProgress = 0
        }

        // here if the application is closed
        // we stop the tts
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        // now destroying the media player
        // so it doesn't run in background
        if ( player != null ) {
            player!!.stop()
            player!!.release()
        }



        binding = null
    }


    // constraint function
    private fun setConstraints() {

        // LayoutParams are used by views to tell their parents how they want to be laid out
        // we are setting params from our linear layout view
        // in other words dynamically changing the attributes of a view
        val params = binding?.llFrameLayout?.layoutParams as ConstraintLayout.LayoutParams
        // here we can set the constraints for our view
        params.topToBottom = binding?.ivExercise!!.id
        params.bottomToBottom = binding?.rvExerciseStatus!!.id
        /* If something about your view changes that will affect the size, then you should call requestLayout().
         This will trigger onMeasure and onLayout not only for this view but all the way up the line for the parent views. */
        binding?.llFrameLayout?.requestLayout()
        // we need to make some things invisible

        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE
        binding?.tvDialogUpcomingExercise?.visibility = View.INVISIBLE
    }

    //default constraint fun
    private fun setDefaultConstraints() {

        val params = binding?.llFrameLayout?.layoutParams as ConstraintLayout.LayoutParams
        //params.topToBottom = binding?.toolbarExercise!!.id
        params.bottomToBottom = binding?.rvExerciseStatus!!.id
        binding?.llFrameLayout?.requestLayout()

        binding?.tvUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvDialogUpcomingExercise?.visibility = View.VISIBLE

    }

    // tts status function, we can do checks here
    override fun onInit(status: Int) {

        // first we gonna check if our status is success
        if ( status == TextToSpeech.SUCCESS ) {
            // now here we will set the language
            val result = tts?.setLanguage(Locale.US)

            // checking for errors in the result
            // checking if the tts is missing data or if the language is
            // not supported
            if ( result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {

                Log.i("TTS", "LANGUAGE SPECIFIED IS NOT SUPPORTED")

            }
            // checking here if it's not success meaning there's error
        } else {
            Log.i("TTS", "initialization failed")
        }

    }


    // tts function to speak text
    private fun speakOut(text: String){
        // setting up all the params of speak method
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")

    }
    // fun to set up our recycle view
    private fun setupExerciseStatusRecyclerView() {

        // first setting the layout and orientation to horizontal
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false)
    }

    // function to override back button in android is called
    // in main method, here we are calling our dialog on back pressed
    override fun onBackPressed() {
        onBackPressedAlertDialog()
        // super.onBackPressed()
    }

    // alert confirmation dialog , when user presses back button while exercising
    private fun onBackPressedAlertDialog(){
        val alertDialog = Dialog(this)
        // since we are using binding , we will need to bind
        // our xml layout views
        val dialogBinding = LayoutAlertDialogBinding.inflate(layoutInflater)
        // setting the view
        alertDialog.setContentView(dialogBinding.root)
        // setting the action to button
        dialogBinding.btnYes.setOnClickListener {
            // finishing the activity
            // telling it finish the specific exercise
            this@ExerciseActivity.finish()
            alertDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            // dismissing the dialog
            alertDialog.dismiss()
        }
        // setting clicking outside to false
        alertDialog.setCanceledOnTouchOutside(false)
        // showing the dialog
        alertDialog.show()
    }


}