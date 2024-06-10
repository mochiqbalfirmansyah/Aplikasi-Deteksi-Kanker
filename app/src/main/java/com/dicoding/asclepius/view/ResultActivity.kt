package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.

        val imageString = intent.getStringExtra(IMAGE_URI)
        if (imageString != null) {
            val imageUri = Uri.parse(imageString)
            displayPicture(imageUri)

            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        Log.d(TAG, "Error: $error")
                    }

                    override fun onResults(result: List<Classifications>?, interenceTime: Long) {
                        result?.let { getData(it)}
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(imageUri)
        } else {
            Log.e(TAG, "No Image Provide")
            finish()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getData(result: List<Classifications>) {
        val topResult = result[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }
        binding.resultText.text = "$label ${score.formatToString()}"
    }

    private fun displayPicture(uri: Uri) {
        Log.d(TAG, "Display Picture: $uri")
        binding.resultImage.setImageURI(uri)
    }

    companion object {
        const val IMAGE_URI = "img_uri"
        const val TAG = "imagepPicker"
    }
}