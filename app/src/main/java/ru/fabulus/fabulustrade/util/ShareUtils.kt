package ru.fabulus.fabulustrade.util

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.ImageView
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider

fun ResourceProvider.getSharePostIntent(post: Post, imageViewIdList: List<ImageView>): Intent {
    var bmpUri: Uri? = null

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"

        var title = formatString(
            R.string.share_message_pattern,
            post.userName,
            post.text
        )

        if (title.length > MAX_SHARED_LEN_POST_TEXT) {
            title = formatString(
                R.string.share_message_pattern_big_text,
                title.substring(0, MAX_SHARED_LEN_POST_TEXT)
            )
        }

        putExtra(Intent.EXTRA_TEXT, title)
        if (post.images.count() > 0) {
            imageViewIdList[0].getBitmapUriFromDrawable()?.let { uri ->
                bmpUri = uri
                putExtra(Intent.EXTRA_STREAM, bmpUri)
                type = "image/*"
            }
        }
    }

    val chooser = Intent.createChooser(
        shareIntent,
        getStringResource(R.string.share_message_title)
    )

    bmpUri?.let { uri ->
        imageViewIdList[0].context.let { context ->
            val resInfoList: List<ResolveInfo> = context.packageManager
                .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context.grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }

    }

    return chooser
}