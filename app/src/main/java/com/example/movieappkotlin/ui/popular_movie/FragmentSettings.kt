package com.example.movieappkotlin.ui.popular_movie

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.Debug
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieappkotlin.R
import com.example.movieappkotlin.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import java.io.IOException

const val REQUEST_CODE = 42
class FragmentSettings : Fragment() {


    private var isDataSetWorld: Boolean = false
    private val REFRESH_PERIOD = 60000L
    private val MINIMAL_DISTANCE = 100f

    private val movie_genre = listOf(
        Movie_genre("Fantasy"),
        Movie_genre("Thriller"),
        Movie_genre("Sci-Fi"),
        Movie_genre("Comedy"),
        Movie_genre("18+"),
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_settings, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        recView_settings.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = JenreAdapter(movie_genre)

            mainFragmentFABLocation.setOnClickListener { checkPermission() }
        }
    }
    private val onLocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            context?.let {
                Toast.makeText(it, "Location update", Toast.LENGTH_SHORT).show()
                getAddressAsync(it, location)
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }
    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(getString(R.string.dialog_rationale_title))
                    .setMessage(getString(R.string.dialog_rationale_meaasge))
                    .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                        requestPermission()
                    }
                    .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
        }
    }
    private fun requestPermission() {
        requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
        )
    }

    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) ==
                    PackageManager.PERMISSION_GRANTED
            ) {
                // Получить менеджер геолокаций
                val locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                REFRESH_PERIOD,
                                MINIMAL_DISTANCE,
                                onLocationListener
                        )
                    }
                }
                val location =
                        locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                if (location == null) {
                    showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                    )
                } else {
                    getAddressAsync(context, location)
                    showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                    )
                }
            } else {
                showRationaleDialog()
            }
        }
    }
    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
        }
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                )
                mainFragmentFABLocation.post {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(getString(R.string.dialog_address_title))
                    .setMessage(address)
                    .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}