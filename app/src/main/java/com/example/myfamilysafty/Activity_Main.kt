package com.example.myfamilysafty

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.d4d5.myfamily.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Activity_Main : AppCompatActivity() {

    val permission = arrayOf(
        Manifest.permission.READ_CONTACTS
        )

    val cameracode = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        askPermissions()

        val bottombar = findViewById<BottomNavigationView>(R.id.bottom_bar)

        bottombar.setOnItemSelectedListener {

            if(it.itemId == R.id.nav_guard)
            {
                inflateFragment(GuardFragment.newInstance())
            } else if(it.itemId == R.id.nav_home)
            {
                inflateFragment(HomeFragment.newInstance())
            } else if(it.itemId == R.id.nav_dashboard)
            {
                inflateFragment(DashboardFragment.newInstance())
            }
            else{
                inflateFragment(ProfileFragment.newInstance())
            }

            true
        }
    }

    private fun askPermissions() {
        ActivityCompat.requestPermissions(this,permission,cameracode)
    }

    private fun inflateFragment(newInstance : Fragment) {
        val transaction  = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,newInstance)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == cameracode)
        {
            if(allPermissionGranted()){
                openCamera()
            }else{
               Toast.makeText(this,"You Denied Permission so app doesnt work",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun allPermissionGranted(): Boolean {

        for(item in permission){
            if(ContextCompat.checkSelfPermission(this,item) != PackageManager.PERMISSION_GRANTED)
            {
                return false
            }
        }
        return true
    }

}