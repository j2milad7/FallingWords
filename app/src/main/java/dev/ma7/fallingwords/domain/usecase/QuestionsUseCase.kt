package dev.ma7.fallingwords.domain.usecase

import arrow.core.Either
import dev.ma7.fallingwords.data.model.WordDto
import dev.ma7.fallingwords.domain.model.GameDifficulty
import dev.ma7.fallingwords.domain.model.Question
import dev.ma7.fallingwords.domain.repository.WordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class QuestionsUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {

    suspend operator fun invoke(gameDifficulty: GameDifficulty): Either<Throwable, List<Question>> {
        return withContext(Dispatchers.IO) {
            val response = wordsRepository.getWordList()
            if (response.isLeft()) {
                return@withContext response as Either.Left
            }

            return@withContext try {
                Either.Right(
                    generateQuestionList(gameDifficulty, wordList = response.orNull().orEmpty())
                )
            } catch (e: Exception) {
                Either.Left(e)
            }
        }
    }

    private fun generateQuestionList(
        gameDifficulty: GameDifficulty,
        wordList: List<WordDto>
    ): List<Question> {
        if (wordList.isEmpty()) return emptyList()
        if (wordList.size < gameDifficulty.wordsCount) {
            throw IllegalArgumentException("This game difficulty could not be chosen at this time!")
        }

        val shuffledWordList = wordList.shuffled()
        val gameWordList = shuffledWordList.subList(0, gameDifficulty.wordsCount)
        val leftWorldList = shuffledWordList.subList(
            gameDifficulty.wordsCount,
            shuffledWordList.size
        )
        return gameWordList.map {
            val shouldHaveCorrectTranslation = Random.nextBoolean()
            val translation = when {
                shouldHaveCorrectTranslation -> it.spanish
                leftWorldList.isEmpty() -> gameWordList.random().spanish
                else -> leftWorldList.random().spanish
            }
            if (translation.contains("/")) {
                translation.replace(oldValue = "/", newValue = "/\n")
            }
            Question(it.english, translation, shouldHaveCorrectTranslation)
        }
    }
}