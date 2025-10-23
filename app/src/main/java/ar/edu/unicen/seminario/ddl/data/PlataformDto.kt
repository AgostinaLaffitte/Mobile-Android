package ar.edu.unicen.seminario.ddl.data

import com.google.gson.annotations.SerializedName

data class PlatformsResponseDto(
    val results: List<PlatformDto>
)

data class PlatformDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)