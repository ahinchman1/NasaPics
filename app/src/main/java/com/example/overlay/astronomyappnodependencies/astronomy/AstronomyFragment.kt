package com.example.overlay.astronomyappnodependencies.astronomy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.overlay.astronomyappnodependencies.databinding.FragmentAstronomyBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AstronomyFragment : Fragment() {

    private lateinit var viewModel: AstronomyListViewModel

    private lateinit var binding: FragmentAstronomyBinding
    private var astronomyAdapter: AstronomyAdapter = AstronomyAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAstronomyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AstronomyListViewModel::class.java]

        binding.astronomyRecyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = astronomyAdapter

            // avoid accidental refresh trigger while scrolling all the way to the top
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int
                ) {
                    super.onScrollStateChanged(recyclerView, newState)
                    binding.pullDownToRefresh.isEnabled = layoutManager?.findViewByPosition(0) != null
                }
            })

            binding.pullDownToRefresh.setOnRefreshListener {
                viewModel.loadPhotos()
            }
        }
        render()
    }

    private fun render() {
        lifecycleScope.launch {
            val spinner = binding.spinner
            val errorMessage = binding.errorMessageTextview
            val recyclerView = binding.astronomyRecyclerview

            viewModel.viewState.collect { viewState ->
                Log.d("AstronomyFragment", "viewState: $viewState")
                when (viewState) {
                    is AstronomyListViewState.Loading -> withContext(Dispatchers.Main) {
                        spinner.visibility = View.VISIBLE
                        errorMessage.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                    }
                    is AstronomyListViewState.Content ->  withContext(Dispatchers.Main) {
                        spinner.visibility = View.GONE
                        errorMessage.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        astronomyAdapter.updatePhotos(viewState.list)
                    }
                    is AstronomyListViewState.Error ->  withContext(Dispatchers.Main) {
                        spinner.visibility = View.GONE
                        errorMessage.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance() = AstronomyFragment()
    }
}
