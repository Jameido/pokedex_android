package dev.jameido.pokedex.presentation.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import dev.jameido.pokedex.R
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {

    private lateinit var viewModel: PkmnListVM
    private lateinit var adapter: PkmnAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        viewModel = PkmnListVM(application)
        adapter = PkmnAdapter()
        rv_pkmn.adapter = adapter.withLoadStateLoaderHeaderFooter(
                loader = PkmnLoadStateAdapter(adapter::retry),
                header = PkmnLoadStateAdapter(adapter::retry),
                footer = PkmnLoadStateAdapter(adapter::retry)
        )

        lifecycleScope.launch {
            viewModel.list.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

    }
}