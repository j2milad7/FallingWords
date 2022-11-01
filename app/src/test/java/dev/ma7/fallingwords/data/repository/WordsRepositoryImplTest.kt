package dev.ma7.fallingwords.data.repository

import android.content.Context
import android.content.res.Resources
import arrow.core.merge
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import dev.ma7.fallingwords.data.model.WordDto
import dev.ma7.fallingwords.domain.repository.WordsRepository
import dev.ma7.fallingwords.util.WORDS_JSON
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.ByteArrayInputStream
import java.io.InputStream

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WordsRepositoryImplTest {

    private val context: Context = mock()
    private val gson: Gson = mock()
    private lateinit var wordsRepository: WordsRepository

    @Before
    fun setUp() {
        wordsRepository = WordsRepositoryImpl(context, gson)
    }

    @Test
    fun `json should be converted to a list correctly when 'getWordList' is called`() = runTest {
        // GIVEN
        val resources: Resources = mock()
        val inputStream: InputStream = ByteArrayInputStream(WORDS_JSON.toByteArray(Charsets.UTF_8))
        val type = object : TypeToken<List<WordDto>>() {}.type
        val wordList: List<WordDto> = Gson().fromJson(WORDS_JSON, type)
        whenever(context.resources) doReturn resources
        whenever(resources.openRawResource(anyInt())) doReturn inputStream
        whenever(gson.fromJson<List<WordDto>>(WORDS_JSON, type)) doReturn wordList

        // WHEN
        val result = wordsRepository.getWordList()

        // THEN
        verify(gson).fromJson<List<WordDto>>(WORDS_JSON, type)
        assertThat(result.merge(), equalTo(wordList))
    }

    @Test
    fun `an error should be occurred when 'getWordList' is called and the json is wrong`() =
        runTest {
            // GIVEN
            val wrongJson = "test"
            val exception = JsonSyntaxException("")
            val resources: Resources = mock()
            val inputStream = ByteArrayInputStream(wrongJson.toByteArray(Charsets.UTF_8))
            val type = object : TypeToken<List<WordDto>>() {}.type
            whenever(context.resources) doReturn resources
            whenever(resources.openRawResource(anyInt())) doReturn inputStream
            whenever(gson.fromJson<List<WordDto>>(wrongJson, type)) doThrow exception

            // WHEN
            val result = wordsRepository.getWordList()

            // THEN
            verify(gson).fromJson<List<WordDto>>(wrongJson, type)
            assertThat(result.merge(), `is`(exception))
        }
}