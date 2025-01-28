package com.example.mynotepp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynotepp.model.BaseItem
import com.example.mynotepp.model.Checklist
import com.example.mynotepp.model.Note
import com.example.mynotepp.ui.navigation.ChecklistScreenRoute
import com.example.mynotepp.ui.navigation.LocalNavController
import com.example.mynotepp.ui.navigation.NoteScreenRoute
import com.example.mynotepp.ui.screens.dialogs.InputDialog
import com.example.mynotepp.ui.theme.MyNotePPTheme


@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    HomeContent(
        onLaunchChecklistScreen = { navController.navigate(ChecklistScreenRoute(it)) },
        onLaunchNoteScreen = { navController.navigate(NoteScreenRoute(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onLaunchChecklistScreen: (checklistId: String) -> Unit,
    onLaunchNoteScreen: (noteId: String) -> Unit
) {

    val viewModel: MainScreenViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val notesScreen = viewModel.notes.collectAsStateWithLifecycle().value
    val checklistsScreen = viewModel.checklists.collectAsStateWithLifecycle().value
    var isNewItemAdding by remember { mutableStateOf(false) }

    val items: List<BaseItem> = when (uiState.value.screenState) {
        ScreenContent.Notes -> notesScreen
        ScreenContent.Checklists -> checklistsScreen
    }

    if(isNewItemAdding){
        InputDialog(
            title = "Input title for new item",
            onDismiss = { isNewItemAdding = false },
            onConfirm = { itemTitle ->
                viewModel.addNewItem(itemTitle)
                isNewItemAdding = false
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (uiState.value.screenState) {
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
                    selected = uiState.value.screenState == ScreenContent.Checklists,
                    label = { Text("Checklists") },
                    onClick = { viewModel.toggleScreenState() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Checklists"
                        )
                    }
                )
                NavigationBarItem(
                    selected = uiState.value.screenState == ScreenContent.Notes,
                    label = { Text("Notes") },
                    onClick = { viewModel.toggleScreenState() },
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
            FloatingActionButton(onClick = {
                isNewItemAdding = true
            }) {
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
                GridItem(
                    item = item,
                    onToggleFavorite = { viewModel.toggleFavourite(it) },
                    onItemClick = {
                        if (uiState.value.screenState == ScreenContent.Checklists) {
                            onLaunchChecklistScreen(item.id)
                        } else {
                            onLaunchNoteScreen(item.id)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GridItem(
    item: BaseItem,
    onToggleFavorite: (BaseItem) -> Unit,
    onItemClick: (BaseItem) -> Unit
) {
    val title = when (item) {
        is Checklist -> item.title
        is Note -> item.title
        else -> "Unknown"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onItemClick(item) }
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
                onClick = { onToggleFavorite(item) },
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


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyNotePPTheme {
        HomeContent({}, {})
    }
}



