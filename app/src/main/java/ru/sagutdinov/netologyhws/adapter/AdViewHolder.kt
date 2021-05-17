package ru.sagutdinov.netologyhws.adapter

import android.view.View
import kotlinx.android.synthetic.main.post_card.view.*
import ru.sagutdinov.netologyhws.dto.PostModel

class AdViewHolder(
    adapter: PostAdapter,
    view: View,
    list: MutableList<PostModel>): PostViewHolder(adapter, view, list) {

    override fun bind(post: PostModel) {
        super.bind(post)
        with(view) {
            textViewPost.text = post.textOfPost
        }
    }


}