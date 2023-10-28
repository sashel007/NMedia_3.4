package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.intent.NewPostResultContract
import ru.netology.nmedia.recyclerview.OnInteractionListener
import ru.netology.nmedia.recyclerview.PostAdapter
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by activityViewModels()

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            val (postId, content) = result
            if (postId == 0L) {
                viewModel.addNewPost(content)
            } else {
                viewModel.updatePost(postId, content)
            }
        }
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun like(post: Post) {
                viewModel.like(post.id)
            }

            override fun remove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun edit(post: Post) {
                val action = R.id.action_feedFragment_to_newPostFragment
                val bundle = bundleOf(
                    "postId" to post.id,
                    "postContent" to post.content
                )
                findNavController().navigate(action, bundle)
            }

            override fun share(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun playVideo(videoUrl: String?) {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(videoUrl)
                }
                val shareIntent = Intent.createChooser(intent, "Выберите приложение")
                startActivity(shareIntent)
            }

            override fun onPostClicked(post: Post) {
                val action = R.id.action_feedFragment_to_targetPostFragment
                val bundle = bundleOf("postId" to post.id)
                findNavController().navigate(action, bundle)
            }
        })
        findNavController().navigateUp()

        binding.postList?.layoutManager = LinearLayoutManager(requireContext())
        binding.postList?.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) binding.postList?.smoothScrollToPosition(0)
            }
        }
        binding.addButton?.setOnClickListener {
            val action = R.id.action_feedFragment_to_newPostFragment
            val bundle = bundleOf(
                "postId" to 0L,
                "postContent" to ""
            )
            findNavController().navigate(action, bundle)
        }

        return binding.root
    }

}