package ar.edu.unicen.seminario.ddl.data.Rawg


import ar.edu.unicen.seminario.ddl.data.GamesResponseDto
import ar.edu.unicen.seminario.ddl.data.GenresResponseDto
import ar.edu.unicen.seminario.ddl.data.PlatformsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApi{

    @GET("games")
    suspend fun getAllGames(
        @Query("key") apiKey: String
    ): Response<GamesResponseDto>
    @GET("platforms")
    suspend fun getPlatforms(@Query("key") apiKey: String): Response<PlatformsResponseDto>

    @GET("genres")
    suspend fun getGenres(@Query("key") apiKey: String): Response<GenresResponseDto>
    @GET("games")
    suspend fun getFilteredGames(
        @Query("key") apiKey: String,
        @Query("platforms") platforms: String?,
        @Query("genres") genres: String?,
        @Query("ordering") ordering: String?,
        @Query("search") search: String?
    ): Response<GamesResponseDto>

}