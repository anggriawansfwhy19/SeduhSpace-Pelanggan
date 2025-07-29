package com.anggriawans.userseduhspace.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.anggriawans.userseduhspace.R
import com.anggriawans.userseduhspace.SurveyActivity
import com.anggriawans.userseduhspace.model.SurveyModel
import com.google.firebase.database.FirebaseDatabase

class SurveyFragment : Fragment() {

    private lateinit var slider1: SeekBar
    private lateinit var slider2: SeekBar
    private lateinit var slider3: SeekBar
    private lateinit var slider4: SeekBar
    private lateinit var slider5: SeekBar
    private lateinit var slider6: SeekBar
    private lateinit var slider7: SeekBar
    private lateinit var slider8: SeekBar
    private lateinit var slider9: SeekBar
    private lateinit var slider10: SeekBar
    private lateinit var slider11: SeekBar
    private lateinit var slider12: SeekBar
    private lateinit var slider13: SeekBar
    private lateinit var slider14: SeekBar
    private lateinit var slider15: SeekBar
    private lateinit var progressBarSurvey: ProgressBar
    private lateinit var surveyButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_survey, container, false)

        progressBarSurvey = view.findViewById(R.id.progressBarSurvey)

        progressBarSurvey.visibility = View.INVISIBLE

        // Initialize the SeekBar and Button
        slider1 = view.findViewById(R.id.slider1)
        slider2 = view.findViewById(R.id.slider2)
        slider3 = view.findViewById(R.id.slider3)
        slider4 = view.findViewById(R.id.slider4)
        slider5 = view.findViewById(R.id.slider5)
        slider6 = view.findViewById(R.id.slider6)
        slider7 = view.findViewById(R.id.slider7)
        slider8 = view.findViewById(R.id.slider8)
        slider9 = view.findViewById(R.id.slider9)
        slider10 = view.findViewById(R.id.slider10)
        slider11 = view.findViewById(R.id.slider11)
        slider12 = view.findViewById(R.id.slider12)
        slider13 = view.findViewById(R.id.slider13)
        slider14 = view.findViewById(R.id.slider14)
        slider15 = view.findViewById(R.id.slider15)
        surveyButton = view.findViewById(R.id.surveyButton)

        // Set onClickListener for the surveyButton
        surveyButton.setOnClickListener {

            progressBarSurvey.visibility = View.VISIBLE
            // Get the values from the SeekBars and send them to the model for further processing
            val value1 = slider1.progress
            val value2 = slider2.progress
            val value3 = slider3.progress
            val value4 = slider4.progress
            val value5 = slider5.progress
            val value6 = slider6.progress
            val value7 = slider7.progress
            val value8 = slider8.progress
            val value9 = slider9.progress
            val value10 = slider10.progress
            val value11 = slider11.progress
            val value12 = slider12.progress
            val value13 = slider13.progress
            val value14 = slider14.progress
            val value15 = slider15.progress

            // Create an instance of the model class and set the values
            val surveyModel = SurveyModel()
            surveyModel.slider1Value = value1
            surveyModel.slider2Value = value2
            surveyModel.slider3Value = value3
            surveyModel.slider4Value = value4
            surveyModel.slider5Value = value5
            surveyModel.slider6Value = value6
            surveyModel.slider7Value = value7
            surveyModel.slider8Value = value8
            surveyModel.slider9Value = value9
            surveyModel.slider10Value = value10
            surveyModel.slider11Value = value11
            surveyModel.slider12Value = value12
            surveyModel.slider13Value = value13
            surveyModel.slider14Value = value14
            surveyModel.slider15Value = value15

            // Call a method to process the surveyModel and display the recommended coffee menu
            displayRecommendedMenu(surveyModel)

            // Reset the SeekBars to their initial state
            resetSeekBars()
            Handler().postDelayed({
                progressBarSurvey.visibility = View.GONE
            }, 3000)
        }

        return view
    }

    private fun displayRecommendedMenu(surveyModel: SurveyModel) {
        // Get a reference to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val surveyRef = database.getReference("surveys")

        // Save the survey data to the Firebase Realtime Database
        surveyRef.push().setValue(surveyModel)

        // Navigate to SurveyActivity
        val intent = Intent(activity, SurveyActivity::class.java)
        startActivity(intent)
        // Pop this fragment from the back stack to remove it
        fragmentManager?.popBackStack()
    }

    private fun resetSeekBars() {
        slider1.progress = 0
        slider2.progress = 0
        slider3.progress = 0
        slider4.progress = 0
        slider5.progress = 0
        slider6.progress = 0
        slider7.progress = 0
        slider8.progress = 0
        slider9.progress = 0
        slider10.progress = 0
        slider11.progress = 0
        slider12.progress = 0
        slider13.progress = 0
        slider14.progress = 0
        slider15.progress = 0
    }
}