package ru.fabulus.fabulustrade.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.fabulus.fabulustrade.databinding.ItemImageOfPostBinding
import ru.fabulus.fabulustrade.mvp.presenter.QuestionPresenter

class ImageListOfQuestionAdapter(private val presenter: QuestionPresenter) :
    RecyclerView.Adapter<ImageListOfQuestionAdapter.QuestionImageViewHolder>() {

    private var imagesList: List<ByteArray> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionImageViewHolder {
        val binding = ItemImageOfPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = QuestionImageViewHolder(binding)
        binding.buttonDeleteImage.setOnClickListener {
            viewHolder.imageOfPost?.let { image ->
                presenter.deleteImage(image)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: QuestionImageViewHolder, position: Int) {
        holder.bind(imagesList[position])
    }

    override fun getItemCount(): Int = imagesList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateImages(images: List<ByteArray>) {
        imagesList = images
        notifyDataSetChanged()
    }

    class QuestionImageViewHolder(private val binding: ItemImageOfPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var imageOfPost: ByteArray? = null
            private set

        fun bind(imageOfPost: ByteArray) {
            this.imageOfPost = imageOfPost
            val imageInDevice = imageOfPost as? ByteArray
            imageInDevice?.let { imageAsByteArray ->
                Glide.with(itemView)
                    .load(imageAsByteArray)
                    .into(binding.imageOfPost)
            }
        }
    }
}
