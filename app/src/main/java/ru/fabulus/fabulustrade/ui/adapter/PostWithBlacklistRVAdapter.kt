package ru.fabulus.fabulustrade.ui.adapter

import android.widget.PopupMenu
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IPostWithBlacklistRVListPresenter

class PostWithBlacklistRVAdapter(private val presenter: IPostWithBlacklistRVListPresenter) :
    PostRVAdapter(presenter) {
    override fun setPopupMenuForSomeone(post: Post, popupMenu: PopupMenu) {
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
    }

    override fun inflateMenu(popupMenu: PopupMenu) {
        popupMenu.inflate(R.menu.menu_someone_post_with_blacklist)
    }
}