package com.zef.wipro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zef.wipro.adapter.FactsAdapter
import com.zef.wipro.constants.LoadingStatus
import com.zef.wipro.utils.InjectorUtil
import com.zef.wipro.viewmodel.FactsViewModel

class MainActivity : AppCompatActivity() {
    var viewModel: FactsViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init ViewModel
        val factory = InjectorUtil.getInstance(this)?.provideFactsViewModelFactory()
        viewModel = factory?.let { ViewModelProvider(this, it).get(FactsViewModel::class.java) }

        initUi()
    }

    private fun initUi() {
        val progressBar = findViewById<View>(R.id.progress_circular)
        supportActionBar?.setTitle(R.string.title)

        //Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = FactsAdapter()
        recyclerView.adapter = adapter

        // Swipe to refresh
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh)
        swipeRefreshLayout.setOnRefreshListener { viewModel?.refresh() }

        // Observe data loading status
        viewModel?.dataLoadingStatusLiveData?.observe(this, Observer { apiStatus ->
            swipeRefreshLayout.isRefreshing = false
            progressBar.visibility =
                if (apiStatus === LoadingStatus.LOADING) View.VISIBLE
                else View.INVISIBLE
            if (apiStatus === LoadingStatus.OFFLINE) {
                Toast.makeText(this, R.string.msg_offline, Toast.LENGTH_SHORT).show()
            } else if (apiStatus === LoadingStatus.ERROR) {
                Toast.makeText(this, R.string.msg_error, Toast.LENGTH_SHORT).show()
            }
        })

        // Observe Page Data
        viewModel?.factsLiveData?.observe(this, Observer { (rows, title) ->
            adapter.updateData(rows)
            if (!TextUtils.isEmpty(title)) {
                supportActionBar?.title = title
            } else {
                supportActionBar?.setTitle(R.string.title)
            }
        })
        viewModel?.fetchData()
    }
}