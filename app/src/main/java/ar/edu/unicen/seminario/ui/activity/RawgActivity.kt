package ar.edu.unicen.seminario.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.unicen.seminario.databinding.ActivityMainBinding
import ar.edu.unicen.seminario.ui.GameAdapter
import ar.edu.unicen.seminario.ui.view.RawgViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import ar.edu.unicen.seminario.R
import android.widget.ImageView

@AndroidEntryPoint
class RawgActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: RawgViewModel by viewModels()
    private lateinit var filterLauncher: ActivityResultLauncher<Intent>

    private lateinit var gamesAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("RawgActivity", "onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecyclerView: inicializar una sola vez
        gamesAdapter = GameAdapter(emptyList())
        val orientation = resources.configuration.orientation
        val columns = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1

        binding.gamesRecyclerView.layoutManager = GridLayoutManager(this, columns)
         binding.gamesRecyclerView.adapter = gamesAdapter

        viewModel.loading.onEach { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.INVISIBLE
        }.launchIn(lifecycleScope)

        viewModel.error.onEach { error ->
            binding.error.visibility = if (error) View.VISIBLE else View.INVISIBLE
        }.launchIn(lifecycleScope)

        viewModel.games.onEach { games ->
            Log.d("RawgActivity", "games emitted size=${games.size}")
            gamesAdapter.updateData(games)
        }.launchIn(lifecycleScope)

        // Lanzador de FilterActivity
        filterLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val platforms = data?.getStringExtra("platforms").toNullIfEmpty()
                val genres = data?.getStringExtra("genres").toNullIfEmpty()
                val ordering = data?.getStringExtra("ordering").toNullIfEmpty()


                val search = null

                viewModel.getFilteredGames(platforms, genres, ordering, search)
            }
        }

        binding.buttonFilter.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            filterLauncher.launch(intent)
        }
        val searchEditText = binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)


        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.white))


        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.white))
        val searchIcon = binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(ContextCompat.getColor(this, R.color.white))
        // SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchGames(query.orEmpty())

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        binding.searchView.isFocusable = true


        // Trigger initial load
        viewModel.getAllGames()
    }

    override fun onStart() {
        super.onStart()
        Log.d("RawgActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("RawgActivity", "onResume")
    }
    private fun String?.toNullIfEmpty(): String? {
        // Si la cadena es nula o solo tiene espacios, retorna null.
        return if (this.isNullOrBlank()) null else this
    }
}