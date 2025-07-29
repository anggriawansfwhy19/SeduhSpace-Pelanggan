package com.anggriawans.userseduhspace

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.anggriawans.userseduhspace.databinding.ActivityChooseLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class ChooseLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityChooseLocationBinding
    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupDropdown()
        setupNextButton()
        setupZoomButtons()
    }

    private fun setupZoomButtons() {
        binding.btnZoomIn.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }

        binding.btnZoomOut.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }

    private fun setupDropdown() {
        val locationList = arrayOf(
            "Jakarta", "Bandung", "Surabaya", "Yogyakarta", "Medan",
            "Semarang", "Makassar", "Palembang", "Denpasar", "Pilih di Maps"
        )

        val adapter = ArrayAdapter(this, R.layout.custom_dropdown_item, locationList)
        val autoComplete: AutoCompleteTextView = binding.listOfLocation
        autoComplete.setAdapter(adapter)

        autoComplete.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER) {
                searchLocation(textView.text.toString())
                true
            } else false
        }

        autoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position).toString()
            if (selected != "Pilih di Maps") {
                searchLocation(selected)
            } else {
                Toast.makeText(this, "Silakan pilih langsung dari peta.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchLocation(locationName: String) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocationName(locationName, 1)
                if (!addresses.isNullOrEmpty()) {
                    val location = LatLng(addresses[0].latitude, addresses[0].longitude)
                    updateMapLocation(location)
                } else {
                    Toast.makeText(this, "Lokasi tidak ditemukan, coba nama yang lebih spesifik.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal mencari lokasi.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Izin lokasi belum diberikan.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMapLocation(latLng: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        if (marker == null) {
            marker = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Lokasi Anda")
                    .draggable(true)
            )
        } else {
            marker?.position = latLng
        }
    }

    private fun setupNextButton() {
        binding.nextBtn.setOnClickListener {
            val selectedLocation = binding.listOfLocation.text.toString()
            if (selectedLocation.isNotBlank()) {
                binding.progressBarLocation.visibility = View.VISIBLE
                binding.progressBarLocation.progress = 0

                val animator = ObjectAnimator.ofInt(binding.progressBarLocation, "progress", 0, 100)
                animator.duration = 2000
                animator.interpolator = DecelerateInterpolator()
                animator.start()

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.progressBarLocation.visibility = View.GONE
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }, 3000)
            } else {
                Toast.makeText(this, "Pilih atau ketik lokasi terlebih dahulu 😉", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Aktifkan kontrol zoom
        mMap.uiSettings.isZoomControlsEnabled = false // Nonaktifkan default zoom controls
        mMap.uiSettings.isZoomGesturesEnabled = true // Aktifkan zoom gesture

        checkLocationPermissionAndGetLocation()

        mMap.setOnMapClickListener { latLng ->
            updateMapLocation(latLng)
            updateAddressFromLatLng(latLng)
        }

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                val latLng = marker.position
                updateAddressFromLatLng(latLng)
            }
        })
    }

    private fun updateAddressFromLatLng(latLng: LatLng) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (!addressList.isNullOrEmpty()) {
                    val city = addressList[0].locality ?: addressList[0].adminArea ?: "Lokasi tidak dikenal"
                    binding.listOfLocation.setText(city, false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal mendapatkan alamat dari lokasi.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Izin lokasi belum diberikan.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermissionAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Jika belum diberi izin, tidak lakukan apa-apa
            Toast.makeText(this, "Izin lokasi belum diberikan", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null && ::mMap.isInitialized) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                updateMapLocation(currentLatLng)
                updateAddressFromLatLng(currentLatLng)
            } else {
                Toast.makeText(this, "Tidak dapat mendeteksi lokasi saat ini", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Izin lokasi diperlukan untuk menentukan posisi Anda", Toast.LENGTH_SHORT).show()
            }
        }
    }
}