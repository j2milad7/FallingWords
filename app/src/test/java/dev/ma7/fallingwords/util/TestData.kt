@file:Suppress("Indentation")

package dev.ma7.fallingwords.util

import dev.ma7.fallingwords.data.model.WordDto

const val WORDS_JSON = "[\n" +
        "  {\n" +
        "    \"text_eng\": \"primary school\",\n" +
        "    \"text_spa\": \"escuela primaria\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"text_eng\": \"teacher\",\n" +
        "    \"text_spa\": \"profesor / profesora\"\n" +
        "  }\n" +
        "]"

val wordList = listOf(
    WordDto(english = "English 1", spanish = "Spanish 1"),
    WordDto(english = "English 2", spanish = "Spanish 2"),
    WordDto(english = "English 3", spanish = "Spanish 3"),
    WordDto(english = "English 4", spanish = "Spanish 4"),
    WordDto(english = "English 5", spanish = "Spanish 5"),
    WordDto(english = "English 6", spanish = "Spanish 6"),
    WordDto(english = "English 7", spanish = "Spanish 7"),
    WordDto(english = "English 8", spanish = "Spanish 8"),
    WordDto(english = "English 9", spanish = "Spanish 9"),
    WordDto(english = "English 10", spanish = "Spanish 10")
)