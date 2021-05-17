package ru.sagutdinov.netologyhws.adapter

import android.annotation.SuppressLint
import android.view.View
import kotlinx.android.synthetic.main.post_card.view.*
import ru.sagutdinov.netologyhws.dto.PostModel

class RepostViewHolder (adapter: PostAdapter, view: View,  list: MutableList<PostModel>) :  PostViewHolder (adapter, view, list) {

    @SuppressLint("SetTextI18n")
    override fun bind(post: PostModel) {
        super.bind(post)
        with(view) {
            textViewPost.text = "${post.textOfPost} \n repost: '${post.nameAuthor}'"
            textViewAnother.visibility = View.VISIBLE
            textViewAnother.text = "Repost of: ${post.nameAuthor}"
        }
    }
}