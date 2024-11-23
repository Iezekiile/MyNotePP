package com.example.mynotepp.di

import com.example.mynotepp.data.checklist.ChecklistsRepository
import com.example.mynotepp.data.checklist.impl.ChecklistsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChecklistsRepositoryModule {
    @Binds
    abstract fun bindChecklistRepository(
        checklistRepositoryImpl: ChecklistsRepositoryImpl
    ): ChecklistsRepository
}