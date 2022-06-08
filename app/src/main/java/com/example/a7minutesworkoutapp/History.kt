package com.example.a7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // onCreate

        // now we have access to the dao to pass in our function
        val historyDao = (application as HistoryApp).db.historyDao()

        // coroutine here cause we are getting data from our db and it can
        // take some time
        lifecycleScope.launch{

            historyDao.fetchAllHistory().collect { allCompletedDatesList ->

                // since we used array list of string instead of objects for entity
                // we are going to convert our entity to a string array list
                // before passing it in out set up recycler view function
                if (allCompletedDatesList.isNotEmpty()) {

                    val dates = ArrayList<String>()
                    for (date in allCompletedDatesList) {
                        dates.add(date.date)
                    }
                    settingUpItemsInOurRecycleView(dates)
                }
            }
        }

        //setting up our toolbar
        setSupportActionBar(binding?.tbHistory)

        //to show back button in the toolbar
        if(supportActionBar != null){
            //we need to enable this option to get back button
            //in the tool bar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            // changing the title of tool bar
            supportActionBar?.title = "History"
        }

        //setting a back button in our toolbar
        binding?.tbHistory?.setNavigationOnClickListener {
            // we are calling on back pressed method
            onBackPressed()
        }
    }
    // main
    private var binding: ActivityHistoryBinding? = null

    private fun settingUpItemsInOurRecycleView(historyList: ArrayList<String>) {

        // first checking if the list is not empty
        if (historyList.isNotEmpty()) {

            binding?.rvHistory?.visibility = View.VISIBLE
            // setting up the adapter
            val itemAdapter = HistoryAdapter(historyList)

            // setting up the layout
            binding?.rvHistory?.layoutManager = LinearLayoutManager(this)
            binding?.rvHistory?.adapter = itemAdapter

        } else {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}