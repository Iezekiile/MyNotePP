package com.example.mynotepp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mynotepp.ui.screens.checklist.ChecklistScreen
import com.example.mynotepp.ui.screens.home.HomeScreen
import com.example.mynotepp.ui.screens.note.NoteScreen


@Composable
fun NavApp() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = HomeScreenRoute
        ) {
            composable<HomeScreenRoute> { HomeScreen() }
            composable<NoteScreenRoute> { entry ->
                val route: ChecklistScreenRoute = entry.toRoute()
                NoteScreen(noteId = route.id)
            }
            composable<ChecklistScreenRoute> { entry ->
                val route: ChecklistScreenRoute = entry.toRoute()
                ChecklistScreen(checklistId = route.id)
            }
        }
    }

}

