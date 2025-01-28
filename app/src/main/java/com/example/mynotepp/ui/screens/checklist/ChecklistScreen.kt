package com.example.mynotepp.ui.screens.checklist

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynotepp.model.Task
import com.example.mynotepp.ui.navigation.LocalNavController
import com.example.mynotepp.ui.screens.dialogs.InputDialog


@Composable
fun ChecklistScreen(checklistId: String) {
    val navController = LocalNavController.current
    ChecklistContent(
        checklistId
    ) { navController.popBackStack() }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChecklistContent(
    checklistId: String,
    onBackScreen: () -> Unit
) {
    val viewModel: ChecklistScreenViewModel = hiltViewModel()
    val checklist =
        viewModel.checklists.collectAsStateWithLifecycle().value.filter { it.id == checklistId }[0]
    var isTitleEditing by remember { mutableStateOf(false) }
    var isTaskEditing by remember { mutableStateOf(false) }
    var editingTaskId by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (isTitleEditing) {
        InputDialog(
            title = "Input new title",
            onDismiss = { isTitleEditing = false },
            onConfirm = { taskText ->
                viewModel.editTitle(checklistId, taskText)
                isTitleEditing = false
            }
        )
    }

    if (isTaskEditing) {
        InputDialog(
            title = "Input new task",
            onDismiss = { isTaskEditing = false },
            onConfirm = { taskText ->
                viewModel.editTask(
                    checklistId = checklistId,
                    taskId = editingTaskId,
                    taskText = taskText
                )
                isTaskEditing = false
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = checklist.title,
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
                navigationIcon = {
                    IconButton(onClick = { onBackScreen() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },

                actions = {
                    IconButton(onClick = { viewModel.toggleFavourite(checklistId) }) {
                        Icon(
                            if (checklist.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                    IconButton(onClick = {
                        viewModel.deleteChecklist(checklistId)
                        Toast.makeText(context, "Checklist deleted", Toast.LENGTH_SHORT).show()
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
            FloatingActionButton(onClick = { isTaskEditing = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(checklist.tasks, key = { it.id }) { task ->
                TaskRow(
                    task = task,
                    onTaskCheckedChange = {
                        viewModel.toggleTaskState(checklistId, task.id, it)
                    },
                    onEditTask = { taskId ->
                        editingTaskId = taskId
                        isTaskEditing = true
                    },
                    onRemoveTask = { taskToRemove ->
                        viewModel.deleteTask(checklistId, taskToRemove.id)
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskRow(
    task: Task,
    onTaskCheckedChange: (Boolean) -> Unit,
    onEditTask: (taskId: String) -> Unit,
    onRemoveTask: (Task) -> Unit,
) {
    val context = LocalContext.current
    val currentTask by rememberUpdatedState(task)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) {
                onRemoveTask(currentTask)
                Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
                return@rememberSwipeToDismissBoxState true
            }
            return@rememberSwipeToDismissBoxState false
        },
        positionalThreshold = { it * 0.25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier.padding(vertical = 4.dp),
        backgroundContent = { DismissBackground(dismissState) },
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp)
            ) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = onTaskCheckedChange
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.title,
                    style = if (task.isDone) {
                        MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        MaterialTheme.typography.bodyLarge
                    },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onEditTask(task.id) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit task")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.secondaryContainer
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete task",
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}


