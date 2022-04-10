package ru.fabulus.fabulustrade.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.fabulus.fabulustrade.databinding.ItemImageOfPostBinding
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter

class ImageListOfPostAdapter(private val presenter: CreatePostPresenter) :
    RecyclerView.Adapter<ImageListOfPostAdapter.ImageViewHolder>() {

    private var imagesList: List<CreatePostPresenter.ImageOfPost> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageOfPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = ImageViewHolder(binding)
        binding.buttonDeleteImage.setOnClickListener {
            viewHolder.imageOfPost?.let { imageOfPost -> presenter.markToDeleteImageOnServer(imageOfPost) }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imagesList[position])
    }

    override fun getItemCount(): Int = imagesList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateImages(images: List<CreatePostPresenter.ImageOfPost>) {
        imagesList = images
        notifyDataSetChanged()
    }

    class ImageViewHolder(private val binding: ItemImageOfPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var imageOfPost: CreatePostPresenter.ImageOfPost? = null
            private set

        fun bind(imageOfPost: CreatePostPresenter.ImageOfPost) {
            this.imageOfPost = imageOfPost
            val imageInDevice = imageOfPost as? CreatePostPresenter.ImageOfPost.ImageOnDevice
            val imageOnBackend = imageOfPost as? CreatePostPresenter.ImageOfPost.ImageOnBack
            imageInDevice?.let { imageAsByteArray ->
                Glide.with(itemView)
                    .load(imageAsByteArray.image)
                    .into(binding.imageOfPost)
            } ?: imageOnBackend?.let { imageOnPath ->
                Glide.with(itemView)
                    .load(imageOnPath.image)
                    .into(binding.imageOfPost)
            }
        }
    }
}
