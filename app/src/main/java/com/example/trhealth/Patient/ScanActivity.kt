package com.example.trhealth.Patient

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.trhealth.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ScanActivity : AppCompatActivity() {
    private val LOG_TAG = "Text API"
    private val PHOTO_REQUEST = 10
    private var scanResults: TextView? = null
    private var imageUri: Uri? = null
    private var detector: TextRecognizer? = null
    private val REQUEST_WRITE_PERMISSION = 20
    private val SAVED_INSTANCE_URI = "uri"
    private val SAVED_INSTANCE_RESULT = "result"
    var databaseReference: DatabaseReference? = null
    var storageReference: StorageReference? = null
    lateinit var btnSave: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        val button: Button = findViewById<View>(R.id.button) as Button
        scanResults = findViewById<View>(R.id.results) as TextView
        btnSave = findViewById(R.id.btnSave);
        storageReference = FirebaseStorage.getInstance().reference.child("PdfFiles")
        databaseReference = FirebaseDatabase.getInstance().reference.child("Reports")
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI))
            scanResults!!.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT))
        }
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                ActivityCompat.requestPermissions(
                    this@ScanActivity,
                    arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_PERMISSION
                )
            }
        })
    }

    // take permission for camera
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            } else {
                Toast.makeText(this@ScanActivity, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            try {
                //Extract the image

                //Extract the image
                val bitmap: Bitmap? = decodeBitmapUri(this, imageUri)

                // initialize
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                val image: InputImage = InputImage.fromFilePath(this, imageUri!!)

                // process image and get text = visionText
                val result = recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        // Task completed successfully
                        var finalText = ""
                        for (block in visionText.textBlocks) {
                            for (line in block.lines) {
                                finalText += line.text + " \n"
                            }
                            finalText += "\n"
                        }
                        scanResults!!.text = finalText

                        btnSave.visibility = View.VISIBLE

                        btnSave.setOnClickListener {
                            createandDisplayPdf(finalText,bitmap!!)
                        }
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                        scanResults!!.text = e.message.toString()
                    }

            } catch (e: Exception) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                    .show()
                Log.e(LOG_TAG, e.toString())
            }
        }
    }

    private fun createandDisplayPdf(text: String?, bitmap: Bitmap) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height,1).create()
        val page: PdfDocument.Page = pdfDocument!!.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.parseColor("#444444")


        canvas.drawText(text!!, 10f, 10f, paint)
        pdfDocument!!.finishPage(page)
        saveFile(pdfDocument)
    }

    fun saveFile(pdfDocument: PdfDocument) {
        val fileName =  intent.getStringExtra("fileName")!!
        if (pdfDocument == null) {
            Log.i("local-dev", "pdfDocument in 'saveFile' function is null")
            return
        }
        val root = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ImgToPDF")
        var isDirectoryCreated = root.exists()
        if (!isDirectoryCreated) {
            isDirectoryCreated = root.mkdir()
        }
        var userInputFileName = ""
        if (fileName.isNotBlank()) {
            userInputFileName = fileName
            val file = File(root, userInputFileName)
            try {
                val fileOutputStream = FileOutputStream(file)
                pdfDocument.writeTo(fileOutputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            pdfDocument.close()
        }
        Log.i("local-dev", "'saveFile' function successfully done")
        val file = Uri.fromFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/ImgToPDF/" + userInputFileName
            )
        )
        val reference: StorageReference = storageReference!!.child(userInputFileName)
        val filesName = userInputFileName
        val uploadTask = reference.putFile(file)
        val finalUserInputFileName = userInputFileName
        uploadTask.addOnFailureListener { exception -> // Handle unsuccessful uploads
            Log.d("uploadFail", "" + exception)
        }.addOnSuccessListener { taskSnapshot ->
            if (taskSnapshot.metadata != null) {
                if (taskSnapshot.metadata!!.reference != null) {
                    val result = taskSnapshot.storage.downloadUrl
                    result.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        Log.d("downloadUrl", "" + downloadUrl)
                        val fileHash = HashMap<String, Any>()
                        val temp = finalUserInputFileName.split(".pdf").toTypedArray()
                        fileHash[temp[0]] = downloadUrl
                        databaseReference!!.child(FirebaseAuth.getInstance().uid!!)
                            .child(System.currentTimeMillis().toString())
                            .updateChildren(fileHash)
                        Toast.makeText(
                            applicationContext,
                            "File uploaded",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, PatientDashboard::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun takePicture() {
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
                imageUri = Uri.fromFile(photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                setResult(RESULT_OK, cameraIntent);
                startActivityForResult(cameraIntent, PHOTO_REQUEST)
            }
        }
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
//        photoPath = "file:" + image.absolutePath
        return image
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString())
            outState.putString(SAVED_INSTANCE_RESULT, scanResults!!.getText().toString())
        }
        super.onSaveInstanceState(outState)
    }

    @Throws(FileNotFoundException::class)
    private fun decodeBitmapUri(ctx: Context, uri: Uri?): Bitmap? {
        val targetW = 600
        val targetH = 600
        val bmOptions: BitmapFactory.Options = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri!!), null, bmOptions)
        val photoW: Int = bmOptions.outWidth
        val photoH: Int = bmOptions.outHeight
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        return BitmapFactory.decodeStream(
            ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions
        )
    }
}