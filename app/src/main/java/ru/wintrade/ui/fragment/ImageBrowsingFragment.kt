package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.wintrade.databinding.FragmentImageBrowsingBinding
import ru.wintrade.util.loadImage

const val IMAGE_PATH = "image_path"

/**
 * Fragment для просмотра фото. Из главного функционала - зум фото чтобы рассмотреть его детальнее
 * Для инициализации нужно передать url картинки
 */
class ImageBrowsingFragment : Fragment() {

    private var binding: FragmentImageBrowsingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentImageBrowsingBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val urlImage = requireArguments().getString(IMAGE_PATH)
        binding?.let {
            loadImage("$urlImage", it.ivPostImage)
        }
    }

    companion object {
        fun newInstance(urlImage: String): Fragment = ImageBrowsingFragment().apply {
            arguments = bundleOf(IMAGE_PATH to urlImage)
        }
    }

}