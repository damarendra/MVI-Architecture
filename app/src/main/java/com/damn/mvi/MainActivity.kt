package com.damn.mvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.damn.mvi.data.api.ApiHelperImpl
import com.damn.mvi.data.api.RetrofitBuilder
import com.damn.mvi.data.model.User
import com.damn.mvi.databinding.ActivityMainBinding
import com.damn.mvi.intent.MainIntent
import com.damn.mvi.model.MainViewModel
import com.damn.mvi.state.MainState
import com.damn.mvi.util.ViewModelFactory
import com.damn.mvi.view.adapter.MainAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private var adapter: MainAdapter = MainAdapter(arrayListOf())

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupClicks()

        observeViewModels()
    }

    private fun setupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    binding.recyclerView.context,
                    (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService))
            )
            .get(MainViewModel::class.java)

    }

    private fun setupClicks() {
        binding.button.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.FetchUser)
            }
        }
    }

    private fun observeViewModels() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainState.Idle -> {}
                    is MainState.Loading -> {
                        binding.button.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainState.Users -> {
                        binding.progressBar.visibility = View.GONE
                        binding.button.visibility = View.GONE
                        renderList(it.users)
                    }
                    is MainState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.button.visibility = View.VISIBLE
                        Toast.makeText(
                            this@MainActivity,
                            it.error,
                            Toast.LENGTH_LONG
                        )
                        Log.e("Damn", "Error = "+it.error)
                    }
                }
            }
        }
    }

    private fun renderList(users: List<User>) {
        users.forEach { Log.d("Damn", it.name) }
        binding.recyclerView.visibility = View.VISIBLE
        users.let { listOfUsers -> listOfUsers.let { adapter.addData(it) } }
        adapter.notifyDataSetChanged()
    }
}