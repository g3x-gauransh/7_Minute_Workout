package io.realworld.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_finish.*
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        setSupportActionBar(toolbar_finish_activity)

        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)       //set back button
        }

        toolbar_finish_activity.setNavigationOnClickListener { onBackPressed() }

        btnFinish.setOnClickListener{
            finish()
        }

        addDateTODatabase()

    }

    private fun addDateTODatabase(){
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        Log.i("DATE:", ""+dateTime)

        val sdf = SimpleDateFormat("dd MM yyyy HH:mm:ss",Locale.getDefault())
        val date = sdf.format(dateTime)

        val dbhandler = SqliteOpenHelper(this,null)
        dbhandler.addDate(date)
        Log.i("DATE:","ADDED")
    }
}