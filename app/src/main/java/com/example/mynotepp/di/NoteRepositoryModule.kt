package com.example.mynotepp.di

import com.example.mynotepp.data.note.NotesRepository
import com.example.mynotepp.data.note.impl.NotesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class NoteRepositoryModule {
    @Binds
    abstract fun bindNoteRepository(
        noteRepositoryImpl: NotesRepositoryImpl
    ): NotesRepository
}