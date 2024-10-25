package com.taskmatrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.taskmatrix.ui.screens.AddTaskDialog
import com.taskmatrix.ui.screens.MainScreen
import com.taskmatrix.ui.theme.TaskMatrixTheme
import com.taskmatrix.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
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


