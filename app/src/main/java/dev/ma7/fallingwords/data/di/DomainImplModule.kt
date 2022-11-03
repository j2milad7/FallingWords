package dev.ma7.fallingwords.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.ma7.fallingwords.data.repository.WordsRepositoryImpl
import dev.ma7.fallingwords.domain.repository.WordsRepository

@Module
@InstallIn(ViewModelComponent::class)
interface DomainImplModule {

    @Binds
    fun bindWordsRepository(wordsRepositoryImpl: WordsRepositoryImpl): WordsRepository
}