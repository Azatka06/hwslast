package ru.sagutdinov.netologyhws.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.sagutdinov.netologyhws.R
import ru.sagutdinov.netologyhws.dto.PostModel
import ru.sagutdinov.netologyhws.dto.PostType

private const val TYPE_ITEM_POST = 0
private const val TYPE_ITEM_REPOST = 2
private const val TYPE_ITEM_AD = 4
private const val ITEM_FOOTER = 5


class PostAdapter(var list: MutableList<PostModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var repostBtnClickListener: OnRepostBtnClickListener? = null

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnRepostBtnClickListener {
        fun onRepostBtnClicked(item: PostModel, position: Int, textOfPost: String)
    }

    fun newRecentPosts(newData: List<PostModel>) {
        this.list.clear()
        this.list.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.post_card, parent, false)
        return when (viewType) {
            TYPE_ITEM_REPOST -> RepostViewHolder(this, view, list)
            TYPE_ITEM_AD -> AdViewHolder(this, view, list)
            ITEM_FOOTER -> FooterViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false)
            )
            else -> PostViewHolder(this, view, list)
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            list.size -> ITEM_FOOTER
            else -> {
                if (list.isNotEmpty()) {
                    val post = list[position]
                    when (post.postType) {
                        PostType.REPOST -> TYPE_ITEM_REPOST
                        PostType.AD_POST -> TYPE_ITEM_AD
                        else -> TYPE_ITEM_POST
                    }
                } else TYPE_ITEM_POST
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != list.size) {
            val post = list[position]
            when (post.postType) {
                PostType.REPOST -> {
                    with(holder as RepostViewHolder) {
                        bind(post)
                    }
                }

                PostType.AD_POST -> {
                    with(holder as AdViewHolder) {
                        bind(post)
                    }
                }
                else -> {
                    with(holder as PostViewHolder) {
                        bind(post)
                    }
                }
            }
        }
    }
}




