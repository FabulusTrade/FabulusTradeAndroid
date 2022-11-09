package ru.fabulus.fabulustrade.ui.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar
import ru.fabulus.fabulustrade.util.hideKeyboard
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentQuestionBinding
import ru.fabulus.fabulustrade.mvp.presenter.QuestionPresenter
import ru.fabulus.fabulustrade.mvp.view.QuestionView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.ImageListOfQuestionAdapter
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
        initSpinner()
    }

    private fun initSpinner() {
        val spinner = binding.spinnerQuestionTheme
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.question_theme,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
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
                binding.etQuestionMail.text.toString().isEmpty() -> {
                    requireContext().showLongToast(resources.getString(R.string.email_is_empty))
                }
                !(Patterns.EMAIL_ADDRESS.matcher(binding.etQuestionMail.text.toString()).matches()) -> {
                    requireContext().showLongToast(resources.getString(R.string.email_is_not_correct))
                }
                else -> {
                    val position: Int = binding.spinnerQuestionTheme.selectedItemPosition
                    val array = requireContext().resources.getStringArray(R.array.question_theme)
                    presenter.sendMessage(
                        msg = binding.etQuestionBody.text.toString(),
                        email = binding.etQuestionMail.text.toString(),
                        theme = array[position]
                    )
                }
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