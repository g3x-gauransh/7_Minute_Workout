package io.realworld.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_bmiactivity.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRIC_UNITS_VIEW ="METRIC_UNIT_VIEW"
    val US_UNITS_VIEW="US_UNIT_VIEW"

    var currentVisibleView :String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        setSupportActionBar(toolbar_bmi_activity)

        val actionbar = supportActionBar //actionbar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true) //set back button
            actionbar.title = "CALCULATE BMI" // Setting an title in the action bar.
        }

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // TODO(Step 3 : Adding a click event to METRIC UNIT Calculate button and after valid input calculating it.)
        // START
        // Button will calculate the input values in Metric Units
        btnCalculateUnits.setOnClickListener {

            // The values are validated.
            if(currentVisibleView.equals(METRIC_UNITS_VIEW)){
                if (validateMetricUnits()) {

                    // The height value is converted to a float value and divided by 100 to convert it to meter.
                    val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100

                    // The weight value is converted to a float value
                    val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()

                    // BMI value is calculated in METRIC UNITS using the height and weight value.
                    val bmi = weightValue / (heightValue * heightValue)

                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else{
                if(validateUsUnits()){
                    val usUnitHeightValueFeet:String = etUsUnitHeightFeet.text.toString()
                    val usUnitHeightValueInch:String = etUsUnitHeightInch.text.toString()
                    val usUnitWeightValue:Float = etUsUnitWeight.text.toString().toFloat()

                    val heightValue :Float = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat()*12

                    val bmi = 703 * ( usUnitWeightValue / ( heightValue * heightValue))

                    displayBMIResult(bmi)
                }
                else{
                    Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        // END

        makeVisibleMetricUnitsView()

        rgUnits.setOnCheckedChangeListener{group,checkedId ->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }
            else{
                makeVisibleUsUnitsView()
            }
        }
    }

    // TODO(Step 2 : Validating the METRIC UNITS CALCULATION input.)
    // START
    /**
     * Function is used to validate the input values for METRIC UNITS.
     */
    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (etMetricUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (etMetricUnitHeight.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun validateUsUnits(): Boolean {
        var isValid = true

        when {
            etUsUnitWeight.text.toString().isEmpty() -> {
                isValid = false
            }
            etUsUnitHeightFeet.text.toString().isEmpty() -> {
                isValid = false
            }
            etUsUnitHeightInch.text.toString().isEmpty() -> {
                isValid = false
            }
        }

        return isValid
    }
    // END

    private fun makeVisibleUsUnitsView(){
        currentVisibleView =US_UNITS_VIEW
        tilMetricUnitHeight.visibility = View.GONE
        tilMetricUnitWeight.visibility = View.GONE

        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightInch.text!!.clear()
        etUsUnitWeight.text!!.clear()

        tilUsUnitWeight.visibility = View.VISIBLE
        llUsUnitsHeight.visibility = View.VISIBLE

        llDiplayBMIResult.visibility = View.INVISIBLE
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView =METRIC_UNITS_VIEW
        tilMetricUnitHeight.visibility = View.VISIBLE
        tilMetricUnitWeight.visibility = View.VISIBLE

        etMetricUnitWeight.text!!.clear()
        etMetricUnitHeight.text!!.clear()

        tilUsUnitWeight.visibility = View.GONE
        llUsUnitsHeight.visibility = View.GONE

        llDiplayBMIResult.visibility = View.INVISIBLE
    }


// TODO(Step 4 : Displaying the calculated BMI UI what we have designed earlier.)
    // START
    /**
     * Function is used to display the result of METRIC UNITS.
     */
    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDiplayBMIResult.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue // Value is set to TextView
        tvBMIType.text = bmiLabel // Label is set to TextView
        tvBMIDescription.text = bmiDescription // Description is set to TextView
    }
}