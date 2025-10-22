package ar.edu.unicen.seminario.ddl.data

import ar.edu.unicen.seminario.ddl.models.Game
import com.google.gson.annotations.SerializedName

data class GameDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("background_image")
    val imageUrl: String?,
    @SerializedName("platforms")
    val platforms: List<PlatformWrapper>
    ) {
}
data class PlatformWrapper(
    @SerializedName("platform")
    val platform: PlatformDto
)

fun GameDto.toGame(): Game {
    return Game(
        id = id,
        name = name,
        imageUrl = imageUrl,
        platforms = platforms.map { it.platform.name }
    )
    }
