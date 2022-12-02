package com.sameh.externalstotage


import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sameh.externalstotage.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val PUBLIC_FILE_NAME = "note_public.txt"
    private val PRIVATE_FILE_NAME = "note_private.txt"

    private val REQUEST_PERMISSION_CODE = 1

    private lateinit var myPath: String
    private val FILE_TYPE = "myFolder"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSavePublic.setOnClickListener {
            if (checkWritePermissionFromDevice() && checkReadPermissionFromDevice()) {
                writePublicFileInExternalStorage()
            }
            else {
                requestPermission()
            }
        }

        binding.btnSavePrivate.setOnClickListener {
            if (checkWritePermissionFromDevice() && checkReadPermissionFromDevice()) {
                writePrivateFileInExternalStorage()
            }
            else {
                requestPermission()
            }
        }

        binding.btnReadPublic.setOnClickListener {
            if (checkWritePermissionFromDevice() && checkReadPermissionFromDevice()) {
                readFromPublicFile()
            }
            else {
                requestPermission()
            }
        }

        binding.btnReadPrivate.setOnClickListener {
            if (checkWritePermissionFromDevice() && checkReadPermissionFromDevice()) {
                readFromPrivateFile()
            }
            else {
                requestPermission()
            }
        }
    }

    private fun writePublicFileInExternalStorage() {
        try {
            val userInput = binding.edUserInput.text.toString()
            val folder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val myFile = File(folder, PUBLIC_FILE_NAME)
            val fileOutputStream = FileOutputStream(myFile)
            fileOutputStream.write(userInput.toByteArray())
            fileOutputStream.close()
            Toast.makeText(this, "saved public", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun writePrivateFileInExternalStorage() {
        try {
            val userInput = binding.edUserInput.text.toString()
            val folder = getExternalFilesDir(FILE_TYPE)
            myPath = folder!!.path
            val myFile = File(folder, PRIVATE_FILE_NAME)
            val fileOutputStream = FileOutputStream(myFile)
            fileOutputStream.write(userInput.toByteArray())
            fileOutputStream.close()
            Toast.makeText(this, "saved private", Toast.LENGTH_SHORT).show()
            Log.d("myTAG", "path: $myPath")
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFromPublicFile() {
        try {
            val folder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val myFile = File(folder, PUBLIC_FILE_NAME)
            val fileInputStream = FileInputStream(myFile)

            val stringBuilder = StringBuilder()
            fileInputStream.bufferedReader().forEachLine {
                stringBuilder.append(it)
            }
            binding.tvReadPublicData.text = stringBuilder.toString()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFromPrivateFile() {
        try {
            val folder = getExternalFilesDir(FILE_TYPE)
            val myFile = File(folder, PRIVATE_FILE_NAME)
            val fileInputStream = FileInputStream(myFile)

            val stringBuilder = StringBuilder()
            fileInputStream.bufferedReader().forEachLine {
                stringBuilder.append(it)
            }
            binding.tvReadPrivateData.text = stringBuilder.toString()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkWritePermissionFromDevice(): Boolean {
        val writeExternalStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return writeExternalStorage == PackageManager.PERMISSION_GRANTED
    }

    private fun checkReadPermissionFromDevice(): Boolean {
        val readExternalStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        return readExternalStorage == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty()) {
            when (requestCode) {
                REQUEST_PERMISSION_CODE -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}