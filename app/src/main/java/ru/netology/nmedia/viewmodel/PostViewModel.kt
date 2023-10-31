package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.recyclerview.OnInteractionListener
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val empty = Post(
        id = 0L,
        content = "",
        author = "",
        likedByMe = false,
        likes = 0,
        published = "",
        sharings = 0,
        video = null
    )
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.get()
    private val edited = MutableLiveData(empty)


    // Функции для установки обработчика взаимодействий и переменная для хранения
    private var interactionListener: OnInteractionListener? = null
    fun setInteractionListener(listener: OnInteractionListener) {
        this.interactionListener = listener
    }
    fun getInteractionListener(): OnInteractionListener? {
        return interactionListener
    }

    fun getPostById(id: Long) = data.map { posts ->
        return@map posts.find { it.id == id}
    }

    fun like(id: Long) = repository.like(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun resetEditingState() {
        edited.value = empty
    }

    fun addNewPost(content: String) {
        val newPost = empty.copy(content = content.trim(), id = 0L)
        repository.save(newPost)
    }

    fun updatePost(postId: Long, content: String) {
        // Получаем текущий пост
        val currentPost = repository.getById(postId) ?: return

        // Обновляем его
        val updatedPost = currentPost.copy(content = content.trim())

        // Сохраняем его
        repository.save(updatedPost)

        resetEditingState()
    }
}