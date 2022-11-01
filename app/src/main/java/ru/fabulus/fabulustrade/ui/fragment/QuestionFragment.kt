package ru.fabulus.fabulustrade.ui.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentQuestionBinding
import ru.fabulus.fabulustrade.mvp.presenter.QuestionPresenter
import ru.fabulus.fabulustrade.mvp.view.QuestionView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.ImageListOfQuestionAdapter
import ru.fabulus.fabulustrade.util.hideKeyboard
import ru.fabulus.fabulustrade.util.setDrawerLockMode
import ru.fabulus.fabulustrade.util.setToolbarVisible
import ru.fabulus.fabulustrade.util.showLongToast

class QuestionFragment : MvpAppCompatFragment(), QuestionView {
    private var _binding: FragmentQuestionBinding? = null
    private val binding: FragmentQuestionBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val MAX_QUESTION_LENGTH = 500
        fun newInstance() = QuestionFragment()
    }

    @InjectPresenter
    lateinit var presenter: QuestionPresenter

    @ProvidePresenter
    fun providePresenter() = QuestionPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private val imageAdapter by lazy {
        ImageListOfQuestionAdapter(presenter)
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            presenter.addImages(uris.map { it.toBitmap() })
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initView()
        initListeners()
    }

    override fun setEmail(email: String) {
        binding.etQuestionMail.text?.insert(binding.etQuestionMail.selectionStart, email)
    }

    override fun showToast() {
        Snackbar.make(binding.fragmentQuestion, resources.getString(R.string.question_success), Snackbar.LENGTH_LONG).show()
        this.hideKeyboard()
        presenter.deleteAllImage()
    }
    override fun clearField() {
        binding.etQuestionBody.text?.clear()
        binding.etQuestionBody.clearFocus()
    }

    override fun updateListOfImages(images: List<ByteArray>) {
        imageAdapter.updateImages(images)
    }

    override fun showImagesAddedMessage(count: Int) {
        requireContext().showLongToast(getString(R.string.images_added, count))
    }

    fun initListeners() {
        binding.btnQuestionSend.setOnClickListener {
            when {
                binding.etQuestionBody.text?.length!! > MAX_QUESTION_LENGTH -> requireContext().showLongToast(
                    resources.getString(
                        R.string.question_too_long
                    )
                )
                binding.etQuestionBody.text.isNullOrEmpty() -> requireContext().showLongToast(
                    resources.getString(
                        R.string.question_too_short
                    )
                )
                else -> presenter.sendMessage(binding.etQuestionBody.text.toString())
            }
        }
        binding.btnQuestionLoadFile.setOnClickListener {
            getContent.launch(getString(R.string.gallery_mask))
        }
    }

    private fun Uri.toBitmap() =
        BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(this))

    private fun initView() {
        binding.recyclerQuestionListOfImages.adapter = imageAdapter
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setToolbarVisible(true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}