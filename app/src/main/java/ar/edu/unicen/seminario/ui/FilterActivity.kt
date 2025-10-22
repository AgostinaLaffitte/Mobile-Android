package ar.edu.unicen.seminario.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ar.edu.unicen.seminario.BuildConfig
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.FilterActivityBinding
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import ar.edu.unicen.seminario.ddl.data.Rawg.RawgApi
import com.google.android.material.chip.Chip

@AndroidEntryPoint
class FilterActivity : AppCompatActivity() {

    private lateinit var binding: FilterActivityBinding

    @Inject
    lateinit var rawgApi: RawgApi

    private val apiKey = BuildConfig.RAWG_API_KEY
    private val selectedPlatforms = mutableSetOf<String>()
    private val selectedGenres = mutableSetOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FilterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("API_KEY", BuildConfig.RAWG_API_KEY)
        cargarPlataformas()
        cargarGeneros()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Filtros y Ordenamiento"

        binding.buttonApplyFilters.setOnClickListener {
            val selectedPlatforms = obtenerIdsSeleccionados(binding.chipGroupPlatforms)
            val selectedGenres = obtenerIdsSeleccionados(binding.chipGroupGenres)
            val ordering = obtenerOrdenamientoSeleccionado(binding.chipGroupOrdering)
            val resultIntent = Intent()
            resultIntent.putExtra("platforms", selectedPlatforms.joinToString(","))
            resultIntent.putExtra("genres", selectedGenres.joinToString(","))
            resultIntent.putExtra("ordering", ordering)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        binding.buttonRetryFilters.setOnClickListener {
            cargarPlataformas()
            cargarGeneros()
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        // maneja la flecha de retroceso. Cierra la actividad con RESULT_CANCELED
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    private fun obtenerOrdenamientoSeleccionado(chipGroup: ChipGroup): String? {
        // Como es de selección única, solo necesitamos el ID del chip chequeado.
        val checkedChipId = chipGroup.checkedChipId
        return if (checkedChipId != View.NO_ID) {
            val chip = chipGroup.findViewById<Chip>(checkedChipId)
            chip.tag.toString()
        } else {
            // Retorna "name" o el valor por defecto si, por alguna razón, ninguno está seleccionado.
            "name"
        }
    }
    private fun obtenerIdsSeleccionados(chipGroup: ChipGroup): List<String> {
        val selected = mutableListOf<String>()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selected.add(chip.tag.toString())
            }
        }
        return selected
    }

    private fun cargarPlataformas() {
        lifecycleScope.launch {
            val response = rawgApi.getPlatforms(apiKey)
            val plataformas = response.body()?.results ?: emptyList()
            plataformas.forEach { platform ->
                val chip = LayoutInflater.from(this@FilterActivity)
                    .inflate(R.layout.item_filter_chip, binding.chipGroupPlatforms, false) as Chip

                chip.text = platform.name
                chip.tag = platform.id
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedPlatforms.add(platform.id.toString())
                    } else {
                        selectedPlatforms.remove(platform.id.toString())
                    }
                }

                binding.chipGroupPlatforms.addView(chip)
            }
        }
    }

    private fun cargarGeneros() {
        mostrarCarga()
        lifecycleScope.launch {
            try {
                val response = rawgApi.getGenres(apiKey)
                val generos = response.body()?.results ?: emptyList()
                generos.forEach { genre ->
                    val chip = LayoutInflater.from(this@FilterActivity)
                        .inflate(R.layout.item_filter_chip, binding.chipGroupGenres, false) as Chip

                    chip.text = genre.name
                    chip.tag = genre.id
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            selectedGenres.add(genre.id.toString())
                        } else {
                            selectedGenres.remove(genre.id.toString())
                        }
                    }

                    binding.chipGroupGenres.addView(chip)
                }
                ocultarCarga()
            } catch (e: Exception) {
                e.printStackTrace()
                mostrarError()
            }
        }
    }
    private fun mostrarCarga() {
        binding.progressBarFilters.visibility = View.VISIBLE
        binding.errorFilters.visibility = View.GONE
        binding.buttonRetryFilters.visibility = View.GONE
    }

    private fun ocultarCarga() {
        binding.progressBarFilters.visibility = View.GONE
    }

    private fun mostrarError() {
        binding.progressBarFilters.visibility = View.GONE
        binding.errorFilters.visibility = View.VISIBLE
        binding.buttonRetryFilters.visibility = View.VISIBLE
    }
}
