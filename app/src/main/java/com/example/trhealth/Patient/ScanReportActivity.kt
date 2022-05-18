package com.example.trhealth.Patient

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.trhealth.Doctor.Screens.ViewPatient
import com.example.trhealth.R
import com.google.android.material.textfield.TextInputEditText
import com.kotlinpermissions.KotlinPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ScanReportActivity : AppCompatActivity() {
    lateinit var btnPick: Button
    lateinit var btnScan: Button
    lateinit var photoPath: String
    var fileName: TextInputEditText? = null
    var validity: TextView? = null
    var fileNameAlert: TextView? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == RESULT_OK && data != null) {
            startActivity(CropScannedReportActivity.newIntent(this, data.data.toString(), fileName!!.text.toString()))
        } else if (requestCode == 1231 && resultCode == Activity.RESULT_OK) {
            startActivity(CropScannedReportActivity.newIntent(this, photoPath, fileName!!.text.toString()))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_report)
        fileNameAlert = findViewById(R.id.fileNameAlert)
        btnScan = findViewById(R.id.btnScan)
        btnPick = findViewById(R.id.btnPick)
        fileName = findViewById(R.id.fileName)
        validity = findViewById(R.id.validity)

        fileName!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkFileName()
            }

            override fun afterTextChanged(s: Editable) {
                checkFileName()
            }
        })

        btnScan.setOnClickListener {
            if (checkFileName()) {
                startActivity(
                    Intent(applicationContext, ScanActivity::class.java).putExtra(
                        "fileName",
                        fileName!!.text.toString()
                    )
                )
            } else {
                fileNameAlert!!.visibility = View.VISIBLE
            }
        }

        askPermission()
    }

    fun askPermission() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            KotlinPermissions.with(this)
                .permissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .onAccepted { permissions ->
                    setView()
                }
                .onDenied { permissions ->
                    askPermission()
                }
                .onForeverDenied { permissions ->
                    Toast.makeText(
                        MainActivity@ this,
                        "You have to grant the permissions! Grant them from app settings please.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
                .ask()
        } else {
            setView()
        }
    }

    fun setView() {
        btnPick.setOnClickListener(View.OnClickListener {
            if (checkFileName()) {
                val builder = AlertDialog.Builder(this@ScanReportActivity)
                builder.setTitle("Report")
                builder.setMessage("Where would you like to choose the image?")
                builder.setPositiveButton("Gallery") { dialog, which ->
                    dialog.dismiss()
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, 1111)
                }
                builder.setNegativeButton("Camera") { dialog, which ->
                    dialog.dismiss()
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (cameraIntent.resolveActivity(packageManager) != null) {
                        var photoFile: File? = null
                        try {
                            photoFile = createImageFile()
                        } catch (ex: IOException) {
                            Log.i("Main", "IOException")
                        }
                        if (photoFile != null) {
                            val builder = StrictMode.VmPolicy.Builder()
                            StrictMode.setVmPolicy(builder.build())
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                            startActivityForResult(cameraIntent, 1231)
                        }
                    }
                }
                builder.setNeutralButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {
                fileNameAlert!!.visibility = View.VISIBLE
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName, // prefix
            ".jpg", // suffix
            storageDir      // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = "file:" + image.absolutePath
        return image
    }

    fun checkFileName(): Boolean {
        return if (fileName!!.getText().toString().endsWith(".pdf")) {
            validity!!.setTextColor(Color.GREEN)
            validity!!.setText("Valid")
            true
        } else {
            validity!!.setTextColor(Color.RED)
            validity!!.setText("INVALID!")
            false
        }
    }
}