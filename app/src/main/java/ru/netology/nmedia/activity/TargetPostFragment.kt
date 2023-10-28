package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.functions.formatAmount
import ru.netology.nmedia.databinding.TargetPostLayoutBinding
import ru.netology.nmedia.repository.PostRepositoryFileImpl
import ru.netology.nmedia.viewmodel.PostViewModel

class TargetPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = TargetPostLayoutBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by activityViewModels()

        val args: NewPostFragmentArgs by navArgs()
        val postId = args.postId

        // Получаем данные из репозитория по postId
        val post = PostRepositoryFileImpl(requireContext()).getById(postId)
        if (post != null) {
            binding.apply {
                includedPostCard.author.text = post.author
                includedPostCard.published.text = post.published
                includedPostCard.content.text = post.content
                includedPostCard.likeIcon.isChecked = post.likedByMe
                includedPostCard.likeIcon.text = formatAmount(post.likes)

                if (post.video.isNullOrEmpty()) {
                    includedPostCard.videoGroupViews.visibility = View.GONE
                } else {
                    includedPostCard.videoGroupViews.visibility = View.VISIBLE
                    includedPostCard.playButton.setOnClickListener(videoCLickListener)
                    includedPostCard.videoImage.setOnClickListener(videoCLickListener)
                }
            }
        }
        return binding.root
    }


}

author.text = post.author
published.text = post.published
content.text = post.content
likeIcon.isChecked = post.likedByMe
likeIcon.text = formatAmount(post.likes)
likeIcon.setOnClickListener {
    onInteractionListener.like(post)
}
sharingIcon.setOnClickListener {
    onInteractionListener.share(post)
}
sharingIcon.text = formatAmount(post.sharings)
if (post.video.isNullOrEmpty()) {
    videoGroupViews.visibility = View.GONE
} else {
    videoGroupViews.visibility = View.VISIBLE
    playButton.setOnClickListener(videoCLickListener)
    videoImage.setOnClickListener(videoCLickListener)
}
menu.setOnClickListener {
    PopupMenu(it.context, it).apply {
        inflate(R.menu.menu_options)
        setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.remove -> {
                    onInteractionListener.remove(post)
                    true
                }

                R.id.edit -> {
                    onInteractionListener.edit(post)
                    true
                }

                else -> false
            }
        }
    }.show()
}
clickableView.setOnClickListener {
    onInteractionListener.onPostClicked(post)
}