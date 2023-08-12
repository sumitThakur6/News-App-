package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.fragments.BreakingNewsFragment
import com.example.newsapp.fragments.SavedNewsFragment
import com.example.newsapp.fragments.SearchNewsFragment
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.viewModels.NewsViewModel
import com.example.newsapp.viewModels.NewsViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var viewModel : NewsViewModel
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NewsRepository(ArticleDatabase.getDatabase(this))
        val viewModelProvideFactory = NewsViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProvideFactory)[NewsViewModel::class.java]

       /* binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.breakingNewsFragment -> {
                    replaceFragment(BreakingNewsFragment())
                    true
                }
                R.id.savedNewsFragment -> {
                    replaceFragment(SavedNewsFragment())
                    true
                }
                R.id.searchNewsFragment -> {
                    replaceFragment(SearchNewsFragment())
                    true
                }
                else -> {
                    true
                }
            }
        }*/

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        setupWithNavController(bottomNav, navController)

    }

//    private fun replaceFragment(fragment : Fragment){
//        val supportFragment = supportFragmentManager
//        val transaction = supportFragment.beginTransaction()
//        transaction.replace(R.id.flFragment, fragment)
//        transaction.commit()
//    }

}