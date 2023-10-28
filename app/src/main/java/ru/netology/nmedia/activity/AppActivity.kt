package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ru.netology.nmedia.R
//import ru.netology.nmedia.activity.NewPostFragment.Companion.text
import ru.netology.nmedia.databinding.ActivityAppLayoutBinding

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val text: String = ""
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
//        val navController = navHostFragment.navController
//        navController.navigate(
//            R.id.action_feedFragment_to_newPostFragment,
//            Bundle().also { it.text = text }
//        )
    }
}