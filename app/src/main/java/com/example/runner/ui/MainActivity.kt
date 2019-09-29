package com.example.runner.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.location.LocationManager
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.*
import java.text.DecimalFormat
import com.example.runner.R


private const val MY_PERMISSION_ACCESS_FINE_LOCATION = 1

class MainActivity : AppCompatActivity(), LocationListener {

    private var locationManager: LocationManager? = null
    var startLat: Double = 0.0
    var startLon: Double = 0.0
    private var maxSpeedDouble = 0.0
    var averageSpeed: Double = 0.0
    var pressed: Boolean = false
    var timeStart: Boolean = false
    var runStart: Boolean = false
    private var mDistance: Double = 0.0
    private var pauseOffset: Long = 0
    private var pausedTime: Long = 0
    private lateinit var chronoTime: String
    private var location: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_activity.background = getDrawable(this,
            R.drawable.background_gradient
        )

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        requestLocationPermission()
        if (hasLocationPermission()) {
            getLocationUpdates()
        }

        start_pause_button.text = "START"

        start_pause_button.setOnClickListener {

            if (pressed == false && timeStart == true) {
               onResumeButton()
            }
            else if (pressed == false) {
                onStartButton()
                pressed = true

            }
            else if(pressed == true) {
                onPauseButton()
                pressed = false
            }

        }

        stop_button.setOnClickListener {
            onStopButton()
        }

        list_button.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

    }

    private fun onStartButton() {
        timeStart = true
        runStart = true
        start_pause_button.text = "PAUSE"
        stop_button.visibility = View.VISIBLE
        stop_button.isEnabled = true

        getLocationUpdates()

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.format = "00:%s"
        chronometer.start()
    }

    private fun onPauseButton() {
        timeStart = true
        runStart = false
        start_pause_button.text = "START"

        pausedTime = chronometer.base - SystemClock.elapsedRealtime()
        chronometer.stop()
    }

    private fun onResumeButton() {
        timeStart = true
        pressed = true
        runStart = true
        start_pause_button.text = "PAUSE"

        chronometer.base = SystemClock.elapsedRealtime() + pausedTime
        chronometer.start()
    }

    private fun onStopButton() {
        timeStart = false
        runStart = false
        start_pause_button.text = "START"

        chronometer.stop()
        chronoTime = chronometer.text.toString()
        chronometer.base = SystemClock.elapsedRealtime()
        pauseOffset = 0

        pressed = false
        if (pressed == false) {
            stop_button.visibility = View.INVISIBLE
            stop_button.isEnabled = false
        }

        if (distance_text_view.text.toString().trim().isNotEmpty()) {
            openSaveFragment()

            distance_text_view.text = ""
            max_speed_text_view.text = ""
            average_speed_text_view.text = ""

        }
        else {
            Toast.makeText(this, "I can't save your score. Something went wrong", Toast.LENGTH_SHORT).show()
        }

        locationManager?.removeUpdates(this)
    }

    private fun getLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 2f, this)
        location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        startLat = location!!.latitude
        startLon = location!!.longitude

    }

    private fun updateUI(speed: Double, distance: Double) {
        val df = DecimalFormat("0.0")

        mDistance = distance
        distance_text_view.text = df.format(distance).plus("m")

        if (speed > maxSpeedDouble) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this) //it saves the highest speed
            val editor = sharedPref.edit()
            editor
                .putString("SPEED", speed.toString())
                .commit()

            val getSharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            getSharedPref.apply {
                val getSpeed = getString("SPEED", "")
                maxSpeedDouble = getSpeed!!.toDouble()
                max_speed_text_view.text = df.format(maxSpeedDouble).plus(" km/h")

            }
        }

        var time: Long = chronometer.base //average speed

        if (time <= 0) {
            averageSpeed = 0.0
        }
        else {
            averageSpeed = (distance / (time / 1000.0 )) * 3.6
        }

        average_speed_text_view.text = df.format(averageSpeed).plus(" km/h")
    }

    @SuppressLint("ResourceAsColor")
    override fun onLocationChanged(location: Location?) {
        val speed : Double = (location!!.speed * 3600 / 1000).toDouble()

        if (location == null) {
            progress.visibility = View.VISIBLE
            start_pause_button.isClickable = false
            start_pause_button.isEnabled = false
        } else {
            progress.visibility = View.GONE
            chronometer.visibility = View.VISIBLE
            start_pause_button.isClickable = true
            start_pause_button.isEnabled = true
        }

        val endLat: Double = location.latitude
        val endLon: Double = location.longitude

        val results = FloatArray(1)
        Location.distanceBetween(startLat, startLon, endLat, endLon, results)

        val distance = results[0].toDouble()

        if (runStart == true) {
            updateUI(speed, distance)
        }

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun openSaveFragment() {
        val saveDialogFragment = SaveDialogFragment.newInstance(mDistance, maxSpeedDouble, averageSpeed, chronoTime)
        saveDialogFragment.show(supportFragmentManager, "SaveDialogFragment")
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_FINE_LOCATION
        )
    }

    private fun hasLocationPermission(): Boolean {
        return checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLocationUpdates()
            else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
        }
    }
}
