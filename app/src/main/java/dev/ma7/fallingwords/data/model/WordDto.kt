package dev.ma7.fallingwords.data.model

import com.google.gson.annotations.SerializedName

data class WordDto(
    @SerializedName("text_eng")
    val english: String,
    @SerializedName("text_spa")
    val spanish: String
)