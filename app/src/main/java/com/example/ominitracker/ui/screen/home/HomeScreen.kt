package com.example.ominitracker.ui.screen.home

import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ominitracker.schedular.ReminderWorker
import com.example.ominitracker.schedular.WorkMaster
import com.example.ominitracker.ui.common.compose.HabitDialog
import com.example.ominitracker.util.PermissionHandler

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PermissionHandler.RequestNotificationPermission()
    }
    PermissionHandler.RequestForegroundServiceSpecialUsePermission()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
    ){
        Scaffold(
            floatingActionButton = {
                AnimatedVisibility(visible = state.omniEvent == OmniTrackerUiEvent.Initiate) {
                    HomeFloatingActionButton(
                        onClickEvent = viewModel::onEvent
                    )
                }
            }
        ) {
            HomeScreenContent(modifier = modifier.padding(it), state)
        }
        AnimatedVisibility(visible = state.omniEvent != OmniTrackerUiEvent.Initiate) {
            when (state.omniEvent) {
                OmniTrackerUiEvent.Initiate -> Unit
                OmniTrackerUiEvent.MakeHabit -> {
                    HabitDialog(
                        onCancel = {
                            viewModel.onEvent(HomeUiEvent.OmniEvent(OmniTrackerUiEvent.Initiate))
                        },
                        onSave = {
                            viewModel.onEvent(HomeUiEvent.OmniEvent(OmniTrackerUiEvent.Initiate))
                        }
                    )
                }
                else -> {
                    LaunchedEffect(Unit) {
                        Toast.makeText(
                            context,
                            "Feature not available yet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeUiState
) {
    val context = LocalContext.current
    AnimatedVisibility(state.omniEvent==OmniTrackerUiEvent.Initiate) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Home Screen",
                modifier = modifier.clickable(onClick = {
                    WorkMaster.enqueueSingleWork(
                        context,
                        ReminderWorker::class.java,
                        initialDelay = 5000
                    )
                })
            )
        }
    }
}


@Composable
private fun HomeFloatingActionButton(
    onClickEvent: (HomeUiEvent) -> Unit
) {
    var fabToggle by remember { mutableStateOf(false) }

    // FloatingActionButton
    FloatingActionButton(
        modifier = Modifier.wrapContentSize(),
        onClick = {
            fabToggle = !fabToggle
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(visible = fabToggle) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    IconButton(
                        onClick = {
                            onClickEvent(HomeUiEvent.OmniEvent(OmniTrackerUiEvent.MakeHabit))
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Add Habit"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Spacing between FABs
                    IconButton(
                        onClick = {
                            onClickEvent(HomeUiEvent.OmniEvent(OmniTrackerUiEvent.MakeReminder))
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Add Reminder"
                        )
                    }
                }
            }
            Icon(
                imageVector = if (fabToggle) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = if (fabToggle) "Close FAB" else "Open FAB",
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
            )
        }
    }
}
