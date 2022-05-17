package com.example.trhealth.Patient

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trhealth.R
import com.example.trhealth.SplashScreens.ContinueAs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.labters.documentscanner.DocumentScannerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CropScannedReportActivity : AppCompatActivity() {
    var pdfDocument: PdfDocument? = null
    var fileName = "fileName.pdf"
    lateinit var imageBitmap: Bitmap
    var databaseReference: DatabaseReference? = null
    var storageReference: StorageReference? = null

    companion object {
        private const val FILE_DIR = "FileDir"
        fun newIntent(context: Context, selectedFilePath: String, fileName: String) =
            Intent(context, CropScannedReportActivity::class.java).putExtra(FILE_DIR, selectedFilePath)
                .putExtra("fileName", fileName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_scanned_report)
        fileName = intent.getStringExtra("fileName")?:"fileName.pdf"
        storageReference = FirebaseStorage.getInstance().reference.child("PdfFiles")
        databaseReference = FirebaseDatabase.getInstance().reference.child("Reports")
        val save: Button = findViewById(R.id.btnImageSave);
        val docu: DocumentScannerView = findViewById(R.id.document_scanner);
        val progressBar: ProgressBar = findViewById(R.id.progressBar);
        val btnImageCrop: Button = findViewById(R.id.btnImageCrop);
        val resultImage: ImageView = findViewById(R.id.result_image);

        val bitmap = assetToBitmap(intent.extras?.getString(FILE_DIR)!!)
        docu.setOnLoadListener { loading ->
            if (progressBar.visibility == View.VISIBLE) {
                progressBar.visibility = View.GONE
            }
        }
        docu.setImage(bitmap)

        btnImageCrop.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            imageBitmap = docu.getCroppedImage()
            progressBar.visibility = View.GONE
            resultImage.visibility = View.VISIBLE
            makePDF(imageBitmap)
            resultImage.setImageBitmap(imageBitmap)
            save.visibility = View.VISIBLE
        }

        save.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            save.isEnabled = false
            saveFile()
        }
    }

    private fun assetToBitmap(file: String): Bitmap =
        contentResolver.openInputStream(Uri.parse(file)).run {
            BitmapFactory.decodeStream(this)
        }

    fun makePDF(bitmap: Bitmap) {
        pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page: PdfDocument.Page = pdfDocument!!.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.parseColor("#FFFFFF")
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument!!.finishPage(page)
        if (fileName.isNullOrEmpty()) {
            Toast.makeText(this, "You need to enter file name as follow\nyour_fileName.pdf", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveFile() {
        if (pdfDocument == null) {
            Log.i("local-dev", "pdfDocument in 'saveFile' function is null")
//            hideLoadingDialog()
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
                pdfDocument!!.writeTo(fileOutputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            pdfDocument!!.close()
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
//            hideLoadingDialog()
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
//                        hideLoadingDialog()
                        Toast.makeText(
                            applicationContext,
                            "File uploaded",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@CropScannedReportActivity, PatientDashboard::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
            }
        }
    }

}