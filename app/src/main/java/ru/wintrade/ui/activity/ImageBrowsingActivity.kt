package ru.wintrade.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.wintrade.databinding.ActivityImageBrowsingBinding
import ru.wintrade.util.loadImage

const val IMAGE_PATH = "image_path"

// Активити для просмотра фото. Из главного функционала - зум фото чтобы рассмотреть его детальнее
class ImageBrowsingActivity : AppCompatActivity() {

    private var binding: ActivityImageBrowsingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBrowsingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val urlImage = intent.getStringExtra(IMAGE_PATH)
        binding?.let {
            loadImage("$urlImage", it.ivPostImage)
        }
    }

    companion object {
        fun getIntent(context: Context, urlImage: String) =
            Intent(context, ImageBrowsingActivity::class.java).apply {
                putExtra(IMAGE_PATH, urlImage)
            }
    }

}