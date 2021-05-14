package com.example.uploadpicture

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.uploadpicture.api.ImageClass
import com.example.uploadpicture.retrofit.RetrofitClient
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var select: Button
    lateinit var upload: Button
    lateinit var imageView: ImageView
     var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        select = findViewById(R.id.select)
        upload = findViewById(R.id.upload)
        imageView = findViewById(R.id.imageView)

        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)


        select.setOnClickListener {
           READ_EXTERNAL_STORAGE.checkForPermission(NAME, READ_IMAGE_STORAGE)
        }
        upload.setOnClickListener {
             uploadImage()
        }

    }
    // checking for permission
    private fun String.checkForPermission( name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(this@MainActivity, this) == PackageManager.PERMISSION_GRANTED -> {
                    // call read contact function
                    openImage()
                }
                shouldShowRequestPermissionRationale(this) -> showDialog(this,
                    name,
                    requestCode
                )
                else -> ActivityCompat.requestPermissions(this@MainActivity, arrayOf(this), requestCode)
            }
        }
    }
    // check for permission and make call
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT)
                    .show()
                openImage()
            }
        }
        when (requestCode) {
            READ_IMAGE_STORAGE -> innerCheck(NAME)
        }
    }
    // Show dialog for permission dialog
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("Ok") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun openImage(){
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_IMAGE_CODE)

    }
    private fun uploadImage() {
    // Toast.makeText(this, "Image uploaded ", Toast.LENGTH_SHORT).show()
        val file = File(FileUtil.getPath(selectedImage!!,this)!!)
        val requestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val multiPartBody = MultipartBody.Part.createFormData("file", file.name,requestBody)
        RetrofitClient.getImage().upLoadImage(multiPartBody).enqueue(object :
            Callback<ImageClass> {
            override fun onResponse(call: Call<ImageClass>, response: Response<ImageClass>) {
                if (response.isSuccessful) {
                    response.body()?.let { Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show() }
                }
            }
            override fun onFailure(call: Call<ImageClass>, t: Throwable) {
                t.message?.let { Toast.makeText(applicationContext, it , Toast.LENGTH_SHORT).show() }
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when(requestCode){
                    SELECT_IMAGE_CODE->{
                        selectedImage = data?.data
                        imageView.setImageURI(selectedImage)
                    }
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object{
        const val READ_IMAGE_STORAGE = 100
        const val NAME = "Emmanuel"
        const val SELECT_IMAGE_CODE = 105
    }
}