package com.taskmatrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.taskmatrix.ui.screens.AddTaskDialog
import com.taskmatrix.ui.screens.MainScreen
import com.taskmatrix.ui.screens.SettingsScreen
import com.taskmatrix.ui.theme.TaskMatrixTheme
import com.taskmatrix.viewmodel.MainViewModel
import com.taskmatrix.viewmodel.SettingsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModel<MainViewModel>()
    private val settingsViewModel by viewModel<SettingsViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaskMatrixTheme {
                val navController = rememberNavController()
                val onMainScreen = remember { mutableStateOf(true) }

                val addingTask = remember { mutableStateOf(false) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(id = R.string.app_name)) },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        if (onMainScreen.value)
                                            navController.navigate(ScreenB)
                                        else
                                            navController.navigate(ScreenA)
                                        onMainScreen.value = !onMainScreen.value
                                    }
                                ) {
                                    Icon(
                                        imageVector =
                                        if (!onMainScreen.value)
                                            Icons.Default.List
                                        else
                                            Icons.Default.Settings,
                                        contentDescription = "icon"
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        if (onMainScreen.value) {
                            FloatingActionButton(
                                onClick = {
                                    addingTask.value = true
                                },

                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }
                ) { innerPadding ->
                    val today = Date()
                    NavHost(
                        navController = navController,
                        startDestination = ScreenA
                    ){
                        composable<ScreenA> {
                            if (addingTask.value) {
                                AddTaskDialog(
                                    dialogState = addingTask,
                                    viewModel = mainViewModel,
                                    today = today
                                )
                            }
                            MainScreen(viewModel = mainViewModel, today = today, innerPadding = innerPadding)
                        }
                        composable<ScreenB> {
                            SettingsScreen(viewModel = settingsViewModel, innerPadding = innerPadding)
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object ScreenA
@Serializable
object ScreenB


