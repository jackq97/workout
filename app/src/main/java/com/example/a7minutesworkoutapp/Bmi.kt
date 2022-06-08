package com.example.a7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkoutapp.databinding.ActivityBmiBinding
import java.text.DecimalFormat

// TODO: add visual elements to our radio buttons
class Bmi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // onCreate

        //  function when user change units
        // it makes us units elements visible and
        // metric units elements invisible and vice versa
        binding?.rbMetricUnits?.setOnClickListener {
            visibleMetric()
            invisibleUs()
        }

        binding?.rbUsUnits?.setOnClickListener {
            visibleUs()
            invisibleMetric()
        }

        //setting up our toolbar
        setSupportActionBar(binding?.tbBmi)

        //to show back button in the toolbar
        if(supportActionBar != null){
            //we need to enable this option to get back button
            //in the tool bar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            // changing the title of tool bar
            supportActionBar?.title = "BMI Calculator"
        }

        //setting a back button in our toolbar
        binding?.tbBmi?.setNavigationOnClickListener {
            // we are calling on back pressed method
            onBackPressed()
        }

        // main calculate button
        binding?.btnCalculate?.setOnClickListener {

            // flag check which radio button is selected US metrics or Metric
            if (binding?.rbMetricUnits?.isChecked == true) {

                // assigning variables for easy conversions to float or string
                val weight = binding?.etWeighKG?.text?.toString()
                val height = binding?.etHeightCm?.text?.toString()

                // flag to check if either of our text view is empty
            if (weight!!.isNotEmpty() && height!!.isNotEmpty()) {
                // function that calculates bmi and display result
                calculateAndDisplayBmi(weight.toFloat(), height.toFloat())

            } else {
                // if edit text is empty then it displays an error
                Toast.makeText(this@Bmi,
                    "you can not leave the field empty",
                    Toast.LENGTH_SHORT).show()
            }

            }

            // checking if the other radio button is clicked
            if (binding?.rbUsUnits?.isChecked == true) {



                // assigning variables for easy conversions to float or string
                val weightInPounds = binding?.etWeightPounds?.text.toString()
                val heightInFeet = binding?.etHeightFeet?.text.toString()
                var heightInInches = binding?.etHeightInches?.text.toString()

                // if our inches variable is empty it default value is set to 0
                if(heightInInches.isEmpty()) {
                    heightInInches = "0"
                }

                // checking if our main variables are empty
                if (weightInPounds.isNotEmpty() && heightInFeet.isNotEmpty()) {

                    // here we are converting the kg to lbs and feet to cms
                    val convertedWeightToKg = (weightInPounds.toFloat() / 2.20).toFloat()
                    val convertedHeightToCms = ((heightInFeet.toFloat() * 30.45) + (heightInInches.toFloat() * 2.54)).toFloat()

                    // function to calculate and display bmi
                    calculateAndDisplayBmi(convertedWeightToKg,
                        convertedHeightToCms)

                } else {
                    Toast.makeText(this@Bmi,
                        "you can not leave the field empty",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // main
    private var binding: ActivityBmiBinding? = null

    // our function that calculates bmi
    private fun calculateAndDisplayBmi(weight: Float, height: Float) {

        // formatting and rounding the result and then setting it
        val df = DecimalFormat("#.#")
        val result =  df.format((weight.toFloat() / height.toFloat() / height.toFloat() * 10000))

        // setting the result to our text view
        binding?.tvResult?.text = result

        // checking and comparing the result with if statement

        var message = ""
        binding?.tvYourBmi?.visibility = View.VISIBLE

        if (result.toFloat() < 18.5) {
            // this means user is under weight
            binding?.tvResultText?.text = "Under Weight"
            message = "You're under weight, pay attention to your diet and nutrients"

        } else if (result.toFloat() > 29.9) {
            // this means user is over weight
            binding?.tvResultText?.text = " Over Weight "
            message = "Oops! You really need to tak care of yourself! workout maybe!"
        } else {
            // this means user is of normal weight
            binding?.tvResultText?.text = " Normal Weight "
            message = "Your weight is normal, keep maintaining your diet and exercise routine :)"
        }

        binding?.tvMessage?.text = message

    }

    // visible invisible function
    private fun invisibleUs() {
        binding?.tilWeightUSUnit?.visibility = View.INVISIBLE
        binding?.llFeetInches?.visibility = View.INVISIBLE
    }

    private fun visibleUs() {
        binding?.tilWeightUSUnit?.visibility = View.VISIBLE
        binding?.llFeetInches?.visibility = View.VISIBLE
    }

    private fun invisibleMetric() {
        binding?.tilMetricWeightUnit?.visibility = View.INVISIBLE
        binding?.tilMetricHeightUnit?.visibility = View.INVISIBLE
    }

    private fun visibleMetric() {
        binding?.tilMetricWeightUnit?.visibility = View.VISIBLE
        binding?.tilMetricHeightUnit?.visibility = View.VISIBLE
    }

}