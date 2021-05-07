package com.example.Catatan

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.Catatan.R
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.text.SimpleDateFormat
import java.util.*

class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var currentAddress:String
    private lateinit var currentDateTime:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getCurrentLocation()
        btn_simpan.setOnClickListener{
            addRecord()
        }
    }
    // Memanggil Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }
    // Cara kerja Toolbar untuk menampilkan histor saat di Pilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.myhistory) {
            Toast.makeText(this, "History Saat Ini", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, History::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    // Method untuk memanggil maps setting GPS
    @SuppressLint("RestrictedApi")
    fun getCurrentLocation() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        val locationRequest = LocationRequest ()
            .setInterval(3000)
            .setFastestInterval(3000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(
                this@MapsActivity, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations) {
                        mapFragment.getMapAsync(OnMapReadyCallback {
                            mMap = it
                            if (ActivityCompat.checkSelfPermission(
                                    this@MapsActivity,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    this@MapsActivity,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                            }
                            mMap.isMyLocationEnabled = true
                            mMap.uiSettings.isZoomControlsEnabled = true
                            val locationResult = LocationServices.getFusedLocationProviderClient(
                                this@MapsActivity
                            ).lastLocation
                            locationResult.addOnCompleteListener(this@MapsActivity) {
                                if (it.isSuccessful && it.result != null) {

                                    var currentLocation = it.result
                                    var currentLatitude = currentLocation.latitude
                                    var currentLogtitude = currentLocation.longitude
                                    val geocoder = Geocoder(this@MapsActivity)
                                    var geocoderresult = geocoder.getFromLocation(
                                        currentLatitude,
                                        currentLogtitude,
                                        1
                                    )
                                    currentAddress = geocoderresult[0].getAddressLine(0)
                                    currentDateTime = SimpleDateFormat("dd-MM-yyyy || HH:mm:ss", Locale.getDefault()).format(
                                        Date()
                                    )

                                    var myLocation = LatLng(currentLatitude, currentLogtitude)
                                    mMap.addMarker(
                                        MarkerOptions().position(myLocation).title(
                                            currentAddress
                                        )
                                    ).showInfoWindow()
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                                    mMap.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            myLocation,
                                            15f
                                        )
                                    )

                                }
                            }

                        })

                    }
                }
            },
            Looper.myLooper()
        )
    }
    //Method untuk membuat reord baru
    private fun addRecord(){

        val kegiatan = tx_kegiatan.text.toString()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!kegiatan.isEmpty()){
            val status = databaseHandler.addEmployee(EmpModel(0, kegiatan, currentDateTime, currentAddress))
            if (status > -1) {
                Toast.makeText(this , "Kegiatan kamu berhasil disimpan" , Toast.LENGTH_SHORT).show()
                tx_kegiatan.text.clear()
            }
        } else {
            Toast.makeText(this , "Kegiatan kamu tidak boleh kosong" , Toast.LENGTH_SHORT).show()
        }
    }

//    fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        if(currentFocus != null){
//            val imm: InputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//        }
//        return super.dispatchTouchEvent(ev)
//    }
}