package com.example.mynotepp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynotepp.model.BaseItem
import com.example.mynotepp.model.Checklist
import com.example.mynotepp.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainScreenViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isAddItemDialogVisible by rememberSaveable { mutableStateOf(false) }
    val notesScreen = viewModel.notes.collectAsStateWithLifecycle().value
    val checklistsScreen = viewModel.checklists.collectAsStateWithLifecycle().value

    val items: List<BaseItem> = when (uiState.screenState) {
        ScreenContent.Notes -> notesScreen
        ScreenContent.Checklists -> checklistsScreen
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (uiState.screenState) {
                            ScreenContent.Checklists -> "Checklists"
                            ScreenContent.Notes -> "Notes"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = uiState.screenState == ScreenContent.Checklists,
                    label = { Text("Checklists") },
                    onClick = { viewModel.changeScreenContent(ScreenContent.Checklists) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Checklists"
                        )
                    }
                )
                NavigationBarItem(
                    selected = uiState.screenState == ScreenContent.Notes,
                    label = { Text("Notes") },
                    onClick = { viewModel.changeScreenContent(ScreenContent.Notes) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Notes"
                        )
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isAddItemDialogVisible = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(items) { item ->
                GridItem(item, viewModel)
            }
        }
    }

    if (isAddItemDialogVisible) {
        AddItemDialog(
            onDismiss = { isAddItemDialogVisible = false },
            onSave = { title ->
                viewModel.addNewItem(title)
                isAddItemDialogVisible = false
            }
        )
    }
}

@Composable
fun GridItem(item: BaseItem, viewModel: MainScreenViewModel) {
    val title = when (item) {
        is Checklist -> item.title
        is Note -> item.title
        else -> "Unknown"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                title,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )

            IconButton(
                onClick = { viewModel.toggleFavourite(item.id) },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}


@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Add new checklist") },
        text = {
            Column {
                Text("Enter the title:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(
                            text = "Title",
                            style = TextStyle(color = Color.Gray)
                        )
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (text.isNotBlank()) {
                    onSave(text)
                    onDismiss()
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun AddItemDialogPreview() {
    AddItemDialog(
        onDismiss = { /* No-op for preview */ },
        onSave = { item -> println("Saved item: $item") }
    )
}