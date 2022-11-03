package dev.ma7.fallingwords.domain.repository

import arrow.core.Either
import dev.ma7.fallingwords.data.model.WordDto

interface WordsRepository {

    suspend fun getWordList(): Either<Throwable, List<WordDto>>
}