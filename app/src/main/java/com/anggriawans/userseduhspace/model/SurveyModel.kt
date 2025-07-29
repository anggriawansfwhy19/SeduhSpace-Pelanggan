package com.anggriawans.userseduhspace.model

import android.os.Bundle

class SurveyModel {
    var slider1Value = 0
    var slider2Value = 0
    var slider3Value = 0
    var slider4Value = 0
    var slider5Value = 0
    var slider6Value = 0
    var slider7Value = 0
    var slider8Value = 0
    var slider9Value = 0
    var slider10Value = 0
    var slider11Value = 0
    var slider12Value = 0
    var slider13Value = 0
    var slider14Value = 0
    var slider15Value = 0

    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putInt("slider1Value", slider1Value)
        bundle.putInt("slider2Value", slider2Value)
        bundle.putInt("slider3Value", slider3Value)
        bundle.putInt("slider4Value", slider4Value)
        bundle.putInt("slider5Value", slider5Value)
        bundle.putInt("slider6Value", slider6Value)
        bundle.putInt("slider7Value", slider7Value)
        bundle.putInt("slider8Value", slider8Value)
        bundle.putInt("slider9Value", slider9Value)
        bundle.putInt("slider10Value", slider10Value)
        bundle.putInt("slider11Value", slider11Value)
        bundle.putInt("slider12Value", slider12Value)
        bundle.putInt("slider13Value", slider13Value)
        bundle.putInt("slider14Value", slider14Value)
        bundle.putInt("slider15Value", slider15Value)
        return bundle
    }

    companion object {
        fun fromBundle(bundle: Bundle): SurveyModel {
            val surveyModel = SurveyModel()
            surveyModel.slider1Value = bundle.getInt("slider1Value")
            surveyModel.slider2Value = bundle.getInt("slider2Value")
            surveyModel.slider3Value = bundle.getInt("slider3Value")
            surveyModel.slider4Value = bundle.getInt("slider4Value")
            surveyModel.slider5Value = bundle.getInt("slider5Value")
            surveyModel.slider6Value = bundle.getInt("slider6Value")
            surveyModel.slider7Value = bundle.getInt("slider7Value")
            surveyModel.slider8Value = bundle.getInt("slider8Value")
            surveyModel.slider9Value = bundle.getInt("slider9Value")
            surveyModel.slider10Value = bundle.getInt("slider10Value")
            surveyModel.slider11Value = bundle.getInt("slider11Value")
            surveyModel.slider12Value = bundle.getInt("slider12Value")
            surveyModel.slider13Value = bundle.getInt("slider13Value")
            surveyModel.slider14Value = bundle.getInt("slider14Value")
            surveyModel.slider15Value = bundle.getInt("slider15Value")
            return surveyModel
        }
    }
}


