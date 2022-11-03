package dev.ma7.fallingwords.data.repository

import android.content.Context
import arrow.core.Either
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.ma7.fallingwords.R
import dev.ma7.fallingwords.data.model.WordDto
import dev.ma7.fallingwords.domain.repository.WordsRepository
import java.io.InputStream
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : WordsRepository {

    override suspend fun getWordList(): Either<Throwable, List<WordDto>> {
        return try {
            val type = object : TypeToken<List<WordDto>>() {}.type
            Either.Right(gson.fromJson(readWordsJson(), type))
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    private fun readWordsJson(): String {
        val wordsInputStream: InputStream = context.resources.openRawResource(R.raw.words)
        return wordsInputStream.readBytes().toString(Charsets.UTF_8)
    }
}