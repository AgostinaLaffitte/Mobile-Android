package ar.edu.unicen.seminario.ddl.data.Rawg

import android.util.Log
import ar.edu.unicen.seminario.ddl.data.GamesResponseDto
import ar.edu.unicen.seminario.ddl.data.toGame
import ar.edu.unicen.seminario.ddl.models.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class RawgRemoteDataSource  @Inject constructor(
    private val rawgApi: RawgApi,
    @Named("rawgApiKey") private val apiKey: String,
){
    suspend fun getAllGames(): List<Game>? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<GamesResponseDto> = rawgApi.getAllGames(apiKey)
                Log.d("RawgRemoteDS","getFilteredGames success=${response.isSuccessful} code=${response.code()}")
                if (response.isSuccessful) {
                    return@withContext response.body()?.results?.map { it.toGame() }
                } else {
                    // Si no es exitoso, loguea el error completo.
                    Log.e("RawgRemoteDS", "getAllGames FAILED: HTTP Code ${response.code()}, Message: ${response.errorBody()?.string()}")
                    return@withContext null
                }
                return@withContext response.body()?.results?.map { it.toGame() }
            } catch (e: Exception) {
                Log.e("RawgRemoteDS", "getAllGames failed", e)
                e.printStackTrace()
                return@withContext null
            }
        }
    }
    suspend fun getFilteredGames(platforms: String?, genres: String?, ordering: String?, search: String?): List<Game>? {
        val response = rawgApi.getFilteredGames(apiKey, platforms, genres, ordering, search)
        return response.body()?.results?.map { it.toGame() }
    }

}