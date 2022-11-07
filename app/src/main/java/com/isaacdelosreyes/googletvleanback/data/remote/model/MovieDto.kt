package com.isaacdelosreyes.googletvleanback.data.remote.model

import com.google.gson.annotations.SerializedName
import com.isaacdelosreyes.googletvleanback.data.local.model.MovieBo

data class MovieDto(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)

fun MovieDto.toBo() = MovieBo(
    id = id,
    title = title,
    releaseDate = releaseDate,
    overview = overview,
    posterPath = "https://image.tmdb.org/t/p/w185/$posterPath",
    backdrop = "https://image.tmdb.org/t/p/w780$backdropPath"
)