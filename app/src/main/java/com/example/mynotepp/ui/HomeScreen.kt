package com.example.mynotepp.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynotepp.model.Checklist
import com.example.mynotepp.ui.theme.MyNotePPTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    val checklists = viewModel.checklists.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Checklists", // TODO: Change to string resources
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {} // TODO: Button logic
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "text" // TODO: Change to string resources
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {} // TODO: Button logic
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "text" // TODO: Change to string resources
                        )
                    }
                }
            )

        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    label = { Text("Checklists") }, // TODO: Change to string resources
                    onClick = {}, // TODO: add logic
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "text" // TODO: Change to string resources
                        )
                    }
                )
                NavigationBarItem(
                    selected = true,
                    label = { Text("Notes") }, // TODO: Change to string resources
                    onClick = {}, // TODO: add logic
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "text" // TODO: Change to string resources
                        )
                    }
                )

            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "text" // TODO: Change to string resources
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp, 16.dp)
        ) {
            items(checklists.value.toList()) { item ->
                GridItem(item, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun ItemListScreenPreview() {
    MyNotePPTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Composable
fun GridItem(item: Checklist, viewModel: MainScreenViewModel) {
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
                item.title,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
            IconButton(
                onClick = { viewModel.toggleFavourite(item)},
                modifier = Modifier
                    .align(Alignment.TopStart)

            ) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (item.isFavorite) "Favorite" else "Not Favorite"
                )
            }
        }
    }
}

@Composable
fun AddMenu(viewModel: MainScreenViewModel){

}

