package ar.edu.unicen.seminario.ddl.data

data class GenresResponseDto(
    val results: List<GenreDto>
)

data class GenreDto(
    val id: Int?,
    val name: String?
)