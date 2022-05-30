package ru.fabulus.fabulustrade.ui.adapter

import android.view.Menu
import android.widget.PopupMenu
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IPostWithBlacklistRVListPresenter

class PostWithBlacklistRVAdapter(private val presenter: IPostWithBlacklistRVListPresenter): PostRVAdapter(presenter) {
    override fun setIvAttachedKebabMenuSomeone(post: Post, complaintList: List<Complaint>) {
        binding.incItemPostHeader.ivAttachedKebab.setOnClickListener { btn ->
            val popupMenu = PopupMenu(btn.context, btn)
            popupMenu.inflate(R.menu.menu_someone_post)

            val complaintItem = popupMenu.menu.findItem(R.id.mi_complain_on_post)
            complaintList.forEach { complaint ->
                complaintItem.subMenu.add(Menu.NONE, complaint.id, Menu.NONE, complaint.text)
                    .setOnMenuItemClickListener {
                        presenter.complainOnPost(post, complaint.id)
                        return@setOnMenuItemClickListener true
                    }
            }


            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.mi_copy_post_text -> {
                        presenter.copyPost(post)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.mi_add_to_blacklist -> {
                        presenter.askToAddToBlacklist(post.traderId)
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }

            popupMenu.show()
        }
    }
}