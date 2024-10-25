package com.taskmatrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Task
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.taskmatrix.ui.theme.TaskMatrixTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaskMatrixTheme {
                val addingTask = remember { mutableStateOf(false) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        SmallTopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            addingTask.value = true
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                ) { innerPadding ->
                    val today = Date()
                    if (addingTask.value) {
                        AddTaskDialog(
                            dialogState = addingTask,
                            viewModel = viewModel,
                            today = today
                        )
                    }
                    MainScreen(viewModel = viewModel, today = today, innerPadding = innerPadding)

                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    today: Date = Date(),
    innerPadding: PaddingValues = PaddingValues(vertical = 30.dp)
) {
    val taskInfoCardState = remember { mutableStateOf(false) }
    val currentTask = remember { mutableStateOf(Task()) }

    if (taskInfoCardState.value){
        TaskCard(
            task = currentTask.value,
            viewModel = viewModel,
            cardState = taskInfoCardState
        )
    }

    val list = viewModel.tasks.collectAsState(initial = emptyList()).value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        MatrixCard(
            modifier = Modifier.fillMaxHeight(0.25f),
            tasks = list.filter { it.urgent && it.important },
            today = today,
            title = "Urgent & important",
            titleColor = colorResource(id = R.color.red),
            onClickTaskInfo = { task ->
                currentTask.value = task
                taskInfoCardState.value = true
            }
        )
        MatrixCard(
            modifier = Modifier.fillMaxHeight(0.33f),
            tasks = list.filter { it.urgent && !it.important },
            today = today,
            title = "Urgent but not important",
            titleColor = colorResource(id = R.color.green),
            onClickTaskInfo = { task ->
                currentTask.value = task
                taskInfoCardState.value = true
            }
        )
        MatrixCard(
            modifier = Modifier.fillMaxHeight(0.5f),
            tasks = list.filter { !it.urgent && it.important },
            today = today,
            title = "Not urgent but important",
            titleColor = colorResource(id = R.color.yellow),
            onClickTaskInfo = { task ->
                currentTask.value = task
                taskInfoCardState.value = true
            }
        )
        MatrixCard(
            modifier = Modifier.fillMaxHeight(),
            tasks = list.filter { !it.urgent && !it.important },
            today = today,
            title = "Nor urgent neither important",
            titleColor = colorResource(id = R.color.lightGray),
            onClickTaskInfo = { task ->
                currentTask.value = task
                taskInfoCardState.value = true
            }
        )
    }
}

@Composable
fun MatrixCard(
    modifier: Modifier,
    titleColor: Color,
    title: String,
    today: Date,
    tasks: List<Task>,
    onClickTaskInfo: (Task) -> Unit
) {

    Card(
        modifier = modifier.padding(4.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(titleColor)
                .padding(vertical = 8.dp, horizontal = 24.dp)
        )

        if (tasks.isNotEmpty()) {
            LazyColumn {
                items(tasks.sortedBy { it.deadline }) { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clickable {
                                onClickTaskInfo.invoke(task)
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = task.title)

                        val deadline = task.deadline
                        if (deadline != null) {
                            val daysRemaining = Duration.between(
                                LocalDate.of(today.year, today.month, today.date).atStartOfDay(),
                                LocalDate.of(deadline.year, deadline.month, deadline.date)
                                    .atStartOfDay()
                            ).toDays().toInt()
                            val deadlineColor = when (daysRemaining) {
                                1 -> colorResource(id = R.color.deadline)
                                in 2..3 -> colorResource(id = R.color.closeDeadline)
                                in 4..7 -> colorResource(id = R.color.midDeadline)
                                else -> colorResource(id = R.color.farDeadline)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "in $daysRemaining d.")
                                Icon(
                                    painter = painterResource(id = R.drawable.circle),
                                    contentDescription = "circle",
                                    tint = deadlineColor,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(14.dp)
                                )
                            }
                        }
                    }
                    Divider()
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Empty yet...", color = colorResource(id = R.color.lightGray))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    dialogState: MutableState<Boolean>,
    viewModel: MainViewModel,
    today: Date
) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val deadline: MutableState<Date?> = remember { mutableStateOf(null) }
    val isUrgent = remember { mutableStateOf(true) }
    val isImportant = remember { mutableStateOf(true) }

    val addingDescription = remember { mutableStateOf(false) }
    val addingDeadline = remember { mutableStateOf(false) }
    val calendarState = rememberSheetState(onCloseRequest = {
        if (deadline.value == null) addingDeadline.value = false
    })


    AlertDialog(
        modifier = Modifier,
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.addTask(
                    Task(
                        title = title.value,
                        description = description.value,
                        date = today,
                        deadline = deadline.value,
                        urgent = isUrgent.value,
                        important = isImportant.value
                    )
                )
                dialogState.value = false
            }) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                dialogState.value = false
            }) {
                Text(text = "Cancel")
            }
        },
        title = { Text(text = "Add new task") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.padding(vertical = 4.dp),
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text(text = "Title") }
                )
                if (!addingDescription.value) {
                    TextButton(onClick = { addingDescription.value = true }) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = "Add description"
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(0.85f),
                            value = description.value,
                            onValueChange = { description.value = it },
                            label = { Text(text = "Description") }
                        )
                        IconButton(onClick = { addingDescription.value = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "remove")
                        }
                    }
                }
                if (!addingDeadline.value) {
                    TextButton(onClick = {
                        addingDeadline.value = true
                        calendarState.show()
                    }) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = "Add deadline"
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val date = deadline.value
                        Text(
                            text =
                            if (date == null) ""
                            else "Deadline: ${date.date}.${date.month + 1}",
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .padding(start = 16.dp)
                        )
                        IconButton(onClick = {
                            addingDeadline.value = false
                            deadline.value = null
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "remove")
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceAround
                ) {
                    TextButton(
                        onClick = { isUrgent.value = !isUrgent.value },
                        colors = if (isUrgent.value) ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.yellow),
                            contentColor = Color.Gray
                        )
                        else ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Gray
                        )
                    ) {
                        Text(text = "Is urgent?")
                    }
                    TextButton(
                        onClick = { isImportant.value = !isImportant.value },
                        colors = if (isImportant.value) ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.yellow),
                            contentColor = Color.Gray
                        )
                        else ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Gray
                        )
                    ) {
                        Text(text = "Is important?")
                    }
                }
            }
        }
    )
    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            deadline.value = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        },
    )
}

@Composable
fun TaskCard(
    task: Task,
    viewModel: MainViewModel,
    cardState: MutableState<Boolean>,
) {
    val status: String
    val statusColor: Color

    if (task.urgent && task.important) {
        status = "Urgent and important"
        statusColor = colorResource(id = R.color.red)
    } else if (task.urgent) {
        status = "Urgent"
        statusColor = colorResource(id = R.color.green)
    } else if (task.important) {
        status = "Important"
        statusColor = colorResource(id = R.color.yellow)
    } else {
        status = "nor urgent neither important"
        statusColor = colorResource(id = R.color.lightGray)
    }

    AlertDialog(
        onDismissRequest = { cardState.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.deleteTask(task)
                    cardState.value = false
                }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Mark as done", color = Color.DarkGray)
                    Icon(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(30.dp),
                        imageVector = Icons.Default.Check,
                        contentDescription = "check",
                        tint = colorResource(id = R.color.green)
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = { cardState.value = false }) {
                Text(text = "Cancel", color = Color.DarkGray)
            }
        },
        title = {
            Column {
                Text(text = task.title, color = Color.DarkGray)
                Text(text = status, fontSize = 14.sp, color = statusColor)
                Text(
                    text = "Made at: ${task.date.toString().dropLast(12)}",
                    color = colorResource(
                        id = R.color.lightGray
                    ),
                    fontSize = 14.sp
                )
            }
        },
        text = {
            Column {
                if (task.description.isNotEmpty()) {
                    Text(modifier = Modifier.padding(bottom = 14.dp), text = task.description)
                }
                if (task.deadline != null) {
                    Text(
                        modifier = Modifier
                            .border(
                                BorderStroke(
                                    2.dp,
                                    color = colorResource(id = R.color.deadline)
                                ), shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp), text = "Deadline: ${task.deadline.toString().dropLast(18)}"
                    )
                }
            }
        }
    )
}

