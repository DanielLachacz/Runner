package com.example.runner.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.runner.R
import com.example.runner.data.model.Run
import kotlinx.android.synthetic.main.save_fragment.*
import kotlinx.android.synthetic.main.save_fragment.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class SaveDialogFragment: DialogFragment() {

    private var distanceExtra: Double? = 0.0
    private var maxSpeedExtra: Double? = 0.0
    private var averageSpeedExtra: Double? = 0.0
    private var timeExtra: String? = ""
    private lateinit var saveViewModel: SaveViewModel

    companion object {
        const val DISTANCE = "distance"
        const val MAX_SPEED = "maxSpeed"
        const val AVERAGE_SPEED = "averageSpeed"
        const val TIME = "time"

        fun newInstance(distance: Double, maxSpeed: Double, averageSpeed: Double, time: String): SaveDialogFragment {
            val fragment = SaveDialogFragment()
            val bundle = Bundle().apply {
                putDouble(DISTANCE, distance)
                putDouble(MAX_SPEED, maxSpeed)
                putDouble(AVERAGE_SPEED, averageSpeed)
                putString(TIME, time)
            }
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        distanceExtra = arguments?.getDouble(DISTANCE)
        maxSpeedExtra = arguments?.getDouble(MAX_SPEED)
        averageSpeedExtra = arguments?.getDouble(AVERAGE_SPEED)
        timeExtra = arguments?.getString(TIME)

        return inflater.inflate(R.layout.save_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val df = DecimalFormat("0.0")
        distance_save_text_view.text = df.format(distanceExtra).plus(" m")
        max_speed_save_text_view.text = df.format(maxSpeedExtra).plus(" km/h")
        average_speed_save_text_view.text = df.format(averageSpeedExtra).plus(" km/h")
        time_save_text_view.text = timeExtra

        val dist: Double = distanceExtra!!.toDouble()
        val maxSpeed: Double = maxSpeedExtra!!.toDouble()
        val aveSpeed: Double = averageSpeedExtra!!.toDouble()
        val time: String = timeExtra.toString()

        val date: Date? = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = formatter.format(date)

        view.cancel_button.setOnClickListener {
            dialog.dismiss()
        }

        view.save_button.setOnClickListener {
            if (distance_save_text_view.text.toString().trim().isNotEmpty()) {
                val run = Run(dist, maxSpeed, aveSpeed, time, currentDate)
                saveViewModel.addRun(run)

                dialog.dismiss()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        saveViewModel = ViewModelProviders.of(this).get(SaveViewModel::class.java)
    }
}