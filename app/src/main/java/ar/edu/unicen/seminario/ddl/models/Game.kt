package ar.edu.unicen.seminario.ddl.models

data class Game (
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val platforms: List<String>
)