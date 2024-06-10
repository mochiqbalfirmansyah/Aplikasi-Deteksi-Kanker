package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                moveToResult()

            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    private fun startGallery() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .galleryOnly()
            .createIntent { intent ->
                launcherGallery.launch(intent)
            }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data
            if (selectedImg != null) {
                currentImageUri = selectedImg
                showImage()
            } else {
                showToast(getString(R.string.no_media_selected))
            }
        }
    }



    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra(ResultActivity.IMAGE_URI, uri.toString())
            }
            startActivity(intent)
        } ?: run {
            showToast(getString(R.string.image_classifier_failed))
        }
    }



    private fun moveToResult() {
        currentImageUri?.let { uri ->
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra(ResultActivity.IMAGE_URI, uri.toString())
            }
            startActivity(intent)
        } ?: run {
            showToast(getString(R.string.image_classifier_failed))
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}