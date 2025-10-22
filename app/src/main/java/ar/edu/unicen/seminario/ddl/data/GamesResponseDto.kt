package ar.edu.unicen.seminario.ddl.data

import com.google.gson.annotations.SerializedName

data class GamesResponseDto (
    @SerializedName("results") val results: List<GameDto>
)
