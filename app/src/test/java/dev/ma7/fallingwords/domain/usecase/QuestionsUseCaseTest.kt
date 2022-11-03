package dev.ma7.fallingwords.domain.usecase

import arrow.core.Either
import arrow.core.merge
import com.google.gson.JsonSyntaxException
import dev.ma7.fallingwords.domain.model.GameDifficulty
import dev.ma7.fallingwords.domain.model.Question
import dev.ma7.fallingwords.domain.repository.WordsRepository
import dev.ma7.fallingwords.util.wordList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class QuestionsUseCaseTest {

    private val wordsRepository: WordsRepository = mock()
    private lateinit var questionsUseCase: QuestionsUseCase

    @Before
    fun setUp() {
        questionsUseCase = QuestionsUseCase(wordsRepository)
    }

    @Test
    fun `use case should get words list from repository successfully`() = runTest {
        // GIVEN
        val gameDifficulty = GameDifficulty.EASY
        whenever(wordsRepository.getWordList()) doReturn Either.Right(wordList)

        // WHEN
        val result: Either<Throwable, List<Question>> = questionsUseCase(gameDifficulty)

        // THEN
        verify(wordsRepository).getWordList()
        assertThat(result.orNull()?.size, `is`(gameDifficulty.wordsCount))
    }

    @Test
    fun `empty question list should be returned when world list is empty`() = runTest {
        // GIVEN
        val gameDifficulty = GameDifficulty.EASY
        whenever(wordsRepository.getWordList()) doReturn Either.Right(emptyList())

        // WHEN
        val result: Either<Throwable, List<Question>> = questionsUseCase(gameDifficulty)

        // THEN
        verify(wordsRepository).getWordList()
        assertThat(result.orNull()?.size, `is`(0))
    }

    @Test
    fun `an error should be returned when repository throws an exception`() = runTest {
        // GIVEN
        val gameDifficulty = GameDifficulty.EASY
        val exception = JsonSyntaxException("")
        whenever(wordsRepository.getWordList()) doReturn Either.Left(exception)

        // WHEN
        val result: Either<Throwable, List<Question>> = questionsUseCase(gameDifficulty)

        // THEN
        verify(wordsRepository).getWordList()
        assertThat(result.merge(), `is`(exception))
    }

    @Test
    fun `an error should be returned when game difficulty is not match with the word list`() =
        runTest {
            // GIVEN
            val gameDifficulty = GameDifficulty.HARD
            whenever(wordsRepository.getWordList()) doReturn Either.Right(wordList)

            // WHEN
            val result: Either<Throwable, List<Question>> = questionsUseCase(gameDifficulty)

            // THEN
            verify(wordsRepository).getWordList()
            assertThat(result.isLeft(), `is`(true))
        }
}