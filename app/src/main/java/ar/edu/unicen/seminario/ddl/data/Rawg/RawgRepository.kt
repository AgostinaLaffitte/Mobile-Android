package ar.edu.unicen.seminario.ddl.data.Rawg

import ar.edu.unicen.seminario.ddl.models.Game
import javax.inject.Inject

class RawgRepository  @Inject constructor(
    private val rawgRemoteDataSource: RawgRemoteDataSource
){
    suspend fun getAllGames(): List<Game>{
        return rawgRemoteDataSource.getAllGames()?.filterNotNull() ?: emptyList()
    }
    suspend fun getFilteredGames(platforms: String?, genres: String?, ordering: String?, search: String?): List<Game> {
        return rawgRemoteDataSource.getFilteredGames(platforms, genres, ordering, search)?.filterNotNull() ?: emptyList()
    }
}