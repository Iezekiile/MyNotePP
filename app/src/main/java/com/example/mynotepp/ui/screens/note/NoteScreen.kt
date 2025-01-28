package com.example.mynotepp.ui.screens.note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynotepp.ui.navigation.LocalNavController
import com.example.mynotepp.ui.screens.dialogs.InputDialog

@Composable
fun NoteScreen(noteId: String) {
    val navController = LocalNavController.current
    NoteContent(
        noteId
    ) { navController.popBackStack() }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteContent(
    noteId: String,
    onBackScreen: () -> Unit
) {
    val viewModel: NoteViewModel = hiltViewModel()
    val note =
        viewModel.notes.collectAsStateWithLifecycle().value.filter { it.id == noteId }[0]
    var isTitleEditing by remember { mutableStateOf(false) }
    var isContentEditing by remember { mutableStateOf(false) }

    if (isTitleEditing) {
        InputDialog(
            title = "Input new title",
            onDismiss = { isTitleEditing = false },
            onConfirm = { taskText ->
                viewModel.editTitle(noteId, taskText)
                isTitleEditing = false
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackScreen() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(
                        text = note.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {},
                                onLongClick = {
                                    isTitleEditing = true
                                }
                            ),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(noteId) }) {
                        Icon(
                            imageVector = if (note.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                    IconButton(onClick = {
                        viewModel.deleteNote(noteId)
                        onBackScreen()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isContentEditing = !isContentEditing
                },
            ) {
                Icon(
                    imageVector = if (isContentEditing) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (isContentEditing) "Save Note" else "Edit Note"
                )
            }
        },
        contentWindowInsets = WindowInsets.ime,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isContentEditing) {
                TextField(
                    value = note.content,
                    onValueChange = { viewModel.editContent(noteId, it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                )
            } else {
                Text(
                    text = note.content,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {
                                isContentEditing = true
                            }
                        ),
                )
            }
        }
    }
}