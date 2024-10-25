package com.taskmatrix.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Task
import com.taskmatrix.R
import com.taskmatrix.viewmodel.SettingsViewModel


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {

    val dropDialogState = remember { mutableStateOf(false) }
    val completedDialogState = remember { mutableStateOf(false) }

    if (dropDialogState.value)
        DropTasksAlertDialog(viewModel = viewModel, dialogState = dropDialogState)

    val completedTasks = viewModel.completedTasks.collectAsState(initial = emptyList()).value
    if (completedDialogState.value)
        ComlpetedTasksDialog(viewModel = viewModel, dialogState = dropDialogState, list = completedTasks)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable {
                    completedDialogState.value = true
                }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(text = stringResource(R.string.see_completed_tasks), fontSize = 20.sp)
                Text(text = stringResource(R.string.here_you_can_find_all_done_tasks))
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable {
                    dropDialogState.value = true
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clickable {

                    }
            ) {
                Text(text = stringResource(R.string.drop_all_tasks), fontSize = 20.sp)
                Text(text = stringResource(R.string.drops_all_tasks))
            }
        }

    }
}

@Composable
fun ComlpetedTasksDialog(
    viewModel: SettingsViewModel,
    dialogState: MutableState<Boolean>,
    list: List<Task>
) {

    AlertDialog(
        onDismissRequest = { dialogState.value = false },
        confirmButton = {
            TextButton(
                onClick = { dialogState.value = false }
            ) {
                Text(text = "Back", color = Color.DarkGray)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { dialogState.value = false }
            ) {
                viewModel.deleteCompleted()
                Text(text = "Clear all", color = Color.DarkGray)
            }
        },
        title = {
            Text(text = "Completed tasks", color = Color.DarkGray)
        },
        text = {
            LazyColumn {
                items(list) { task ->
                    Column(
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            val statusColor =
                                if (task.urgent && task.important)
                                    colorResource(id = R.color.red)
                                else if (task.urgent)
                                    colorResource(id = R.color.green)
                                else if (task.important)
                                    colorResource(id = R.color.yellow)
                                else
                                    colorResource(id = R.color.lightGray)

                            Icon(
                                painter = painterResource(id = R.drawable.circle),
                                contentDescription = "circle",
                                tint = statusColor,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(14.dp)
                            )
                            Text(text = task.title, color = Color.DarkGray)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Made at: ${task.date?.date}.${task.date?.month}",
                                color = Color.Gray
                            )
                            Text(
                                text = "Deadline: ${task.deadline?.date}.${task.deadline?.month}",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun DropTasksAlertDialog(
    viewModel: SettingsViewModel,
    dialogState: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = { dialogState.value = false },
        confirmButton = {
            TextButton(
                onClick = { dialogState.value = false }
            ) {
                Text(text = "Yes", color = Color.DarkGray)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.deleteAll()
                    dialogState.value = false
                }
            ) {
                Text(text = "Cancel", color = Color.DarkGray)
            }
        },
        title = {
            Text(text = "Are you sure?", color = Color.DarkGray)
        },
        text = {
            Text(text = "Do you want to delete all current and completed tasks?")
        }
    )
}