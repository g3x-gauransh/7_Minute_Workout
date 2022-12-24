package io.realworld.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer?= null
    private var restProgress = 0

    private var exerciseTimer:CountDownTimer?= null
    private var exerciseProgress= 0

    private var exerciseTimerCount=10
    private var restTimerCount= 5


    private var exerciseList:ArrayList<ExerciseModel>? =null
    private var currentExercisePosition = -1

    private var tts:TextToSpeech?=null

    private var player:MediaPlayer?=null

    private var exerciseAdapter: ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        val actionbar= supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        tts= TextToSpeech(this,this)

        exerciseList =Constants.defaultExersiceList()
        setupRestView()

        setupExerciseStatusRecyclerView()

    }

    override fun onDestroy() {
        if(restTimer !=null){
            restTimer!!.cancel()
            restProgress=0
        }

        if(exerciseTimer !=null){
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }

        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player!=null){
            player!!.stop()
        }

        super.onDestroy()
    }

    private fun setRestProgressBar(){
        progressBar.progress =restProgress
        restTimer= object :CountDownTimer((restTimerCount*1000).toLong(),1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress=restTimerCount-restProgress
                tvTimer.text= (restTimerCount- restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }
        }.start()
    }

    private fun setupRestView(){

        try{
            player =MediaPlayer.create(applicationContext,R.raw.press_start)
            player!!.isLooping =false
            player!!.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

        llRestView.visibility=View.VISIBLE
        llExerciseView.visibility=View.GONE
        if(restTimer !=null){
            restTimer!!.cancel()
            restProgress=0
        }

        tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition+1].getName()

        setRestProgressBar()
    }


    private fun setExerciseProgressBar(){
        ExerciseProgressBar.progress=exerciseProgress
        exerciseTimer= object :CountDownTimer((exerciseTimerCount*1000).toLong(),1000){

            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                ExerciseProgressBar.progress=exerciseTimerCount-exerciseProgress
                tvExerciseTimer.text =(exerciseTimerCount-exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition<exerciseList?.size!! -1){

                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()

                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    private fun setupExerciseView(){

        llRestView.visibility = View.GONE
        llExerciseView.visibility=View.VISIBLE

        if(exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }

        speakout(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()

        iv_image.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tv_exercise_name.text =exerciseList!![currentExercisePosition].getName()
    }

    override fun onInit(status: Int) {
        if(status ==TextToSpeech.SUCCESS){
            val result =tts!!.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result ==TextToSpeech.LANG_NOT_SUPPORTED)
                Log.e("TTS","The language specified is not supported")
        }
        else{
            Log.e("TTS","initialisation failed")
        }
    }

    private fun speakout(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
        Log.e("TTS","TTS working")
    }

    private fun setupExerciseStatusRecyclerView(){                                  //setup the adapter
        rvExerciseStatus.layoutManager =LinearLayoutManager(this,            // type of recycler view using llmanager
            LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!,this)        //initialise the adapter
        rvExerciseStatus.adapter =exerciseAdapter
    }

    private fun customDialogForBackButton(){
        val customDialog =Dialog(this)

        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)

        customDialog.tvYes.setOnClickListener {

            finish()
            customDialog.dismiss()
        }

        customDialog.tvNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()
    }
}