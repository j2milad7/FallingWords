package dev.ma7.fallingwords.domain.model

private const val EASY_WORDS_COUNT = 5
private const val MEDIUM_WORDS_COUNT = 10
private const val HARD_WORDS_COUNT = 15

enum class GameDifficulty(val wordsCount: Int) {
    EASY(EASY_WORDS_COUNT), MEDIUM(MEDIUM_WORDS_COUNT), HARD(HARD_WORDS_COUNT)
}