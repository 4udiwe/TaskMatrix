package com.taskmatrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.domain.model.Task
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.taskmatrix.ui.theme.TaskMatrixTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaskMatrixTheme {
                val addingTask = remember { mutableStateOf(false) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            addingTask.value = true
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                ) { innerPadding ->
                    innerPadding
                    val today = Date()
                    if (addingTask.value){
                        AddTaskDialog(dialogState = addingTask, viewModel = viewModel, today = today)
                    }
                    MainScreen(viewModel = viewModel, today = today)

                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    today: Date
) {
    val list = viewModel.tasks.collectAsState(initial = emptyList()).value
    TasksCard(tasks = list, today = today)
}

@Composable
fun TasksCard(
    //color: Color,
    //title: String,
    today: Date,
    tasks: List<Task>
) {

    Card {
        Text(
            text = "title",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(vertical = 8.dp, horizontal = 24.dp)
        )
        Column {
            tasks.forEach { task ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = task.title)
                    val deadline = task.deadline
                    if (deadline != null){
                        val daysRemaining = Duration.between(
                            LocalDate.of(today.year, today.month, today.date).atStartOfDay(),
                            LocalDate.of(deadline.year, deadline.month, deadline.date).atStartOfDay()
                        ).toDays()

                        Text(text = "через: $daysRemaining")
                    }
                }
                Divider()
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
    val deadline:MutableState<Date?> = remember { mutableStateOf(null) }
    val isUrgent = remember { mutableStateOf(true) }
    val isImportant = remember { mutableStateOf(true) }

    val addingDescription = remember { mutableStateOf(false) }
    val addingDeadline = remember { mutableStateOf(false) }
    val calendarState = rememberSheetState()


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
            }) {
                Text(text = "Add", modifier = Modifier.padding(horizontal = 10.dp)) }
            },
        dismissButton = {
            TextButton(onClick = {
                dialogState.value = false
            }) {
                Text(text = "Cancel", modifier = Modifier.padding(horizontal = 10.dp)) }
            },
        title = { Text(text = "Add new task")},
        text = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OutlinedTextField(
                    modifier = Modifier.padding(vertical = 4.dp),
                    value = title.value,
                    onValueChange = { title.value = it},
                    placeholder = { Text(text = "Title") }
                )
                if (!addingDescription.value) {
                    TextButton(onClick = { addingDescription.value = true }) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = "Add description"
                        )
                    }
                } else {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .padding(vertical = 4.dp),
                            value = description.value,
                            onValueChange = {description.value = it},
                            placeholder = { Text(text = "Description") }
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
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        val date = deadline.value
                        Text(
                            text =
                            if (date == null) ""
                            else "Deadline: ${date.date}.${date.month + 1}",
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        IconButton(onClick = {
                            addingDeadline.value = false
                            deadline.value = null
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "remove")
                        }
                    }
                }

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceAround
                ){
                    TextButton(onClick = { isUrgent.value = !isUrgent.value }) {
                        Text(
                            text = "Is urgent?",
                            color =
                                if(isUrgent.value)
                                    Color.Red
                                else
                                    Color.Gray
                        )
                    }
                    TextButton(onClick = { isImportant.value = !isImportant.value }) {
                        Text(
                            text = "Is important?",
                            color =
                            if(isImportant.value)
                                Color.Red
                            else
                                Color.Gray
                        )
                    }
                }

            }
        }
    )
    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date{ date ->
            deadline.value = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        }
    )
}

