package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ru.wintrade.ui.adapter.ImageBrowsingAdapter
import kotlin.math.max
import kotlin.math.min

class ImageBrowsingFragment : Fragment() {

    companion object {
        private const val PARAM_IMAGE_URLS = "PARAM_IMAGE_URLS"
        private const val PARAM_POSITION = "PARAM_POSITION"

        fun newInstance(imageUrls: List<String>, position: Int = 0) =
            ImageBrowsingFragment().apply {
                arguments = bundleOf(
                    PARAM_IMAGE_URLS to imageUrls.toTypedArray(),
                    PARAM_POSITION to max(0, min(position, imageUrls.size))
                )
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewPager2(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageUrls = (arguments?.getStringArray(PARAM_IMAGE_URLS) ?: emptyArray()).toList()
        val position = arguments?.getInt(PARAM_POSITION) ?: 0
        with(view as ViewPager2) {
            adapter = ImageBrowsingAdapter(imageUrls) {
                isUserInputEnabled = it && imageUrls.size > 1
            }
            doOnNextLayout {
                setCurrentItem(position, false)
            }
        }
    }
}