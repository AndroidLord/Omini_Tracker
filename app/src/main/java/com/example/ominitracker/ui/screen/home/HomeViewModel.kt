package com.example.ominitracker.ui.screen.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.workDataOf
import com.example.ominitracker.data.dao.HabitDao
import com.example.ominitracker.data.modal.HabitEntity
import com.example.ominitracker.schedular.ReminderWorker
import com.example.ominitracker.schedular.WorkMaster
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitDao: HabitDao,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OmniEvent -> {
                handleOmniTrackerEvents(event)
            }
            is HomeUiEvent.OmniWorkMasterEvent -> {
                viewModelScope.launch {
                    handleWorkMasterEvents(event)
                }
            }
        }
    }

    private suspend fun handleWorkMasterEvents(event: HomeUiEvent.OmniWorkMasterEvent) {
        when (event.workerEvent) {
            is WorkMasterEvent.addHabit -> {
                val habit = event.workerEvent.habit
                val inserted = habitDao.upsertHabit(habit)
                if(inserted != -1L) {
                    WorkMaster.enqueuePeriodicWork(
                        context = context,
                        workerClass = ReminderWorker::class.java,
                        repeatInterval = habit.customInterval,
                        timeUnit = TimeUnit.MINUTES,
                        inputData = workDataOf(
                            "habitJson" to habit.title,
                            "message" to habit.cueTrigger,
                            "icon" to habit.reward
                        ),
                        constraints = Constraints.Builder()
                            .setRequiresBatteryNotLow(true)
                            .build()
                    )
                }
                _uiState.update { it.copy(workMasterEvent = WorkMasterEvent.Initiate) }
            }

            else -> Unit
        }
    }

    private fun handleOmniTrackerEvents(event: HomeUiEvent.OmniEvent) {
        when (event.uiEvent) {
            OmniTrackerUiEvent.Initiate -> {
                _uiState.update { it.copy(omniEvent = OmniTrackerUiEvent.Initiate) }
            }
            OmniTrackerUiEvent.MakeHabit -> {
                _uiState.update { it.copy(omniEvent = OmniTrackerUiEvent.MakeHabit) }
            }
            else -> {
                _uiState.update { it.copy(omniEvent = OmniTrackerUiEvent.MakeToast("Feature not available yet")) }
            }
        }
    }
}

data class HomeUiState(
    val omniEvent: OmniTrackerUiEvent = OmniTrackerUiEvent.Initiate,
    val workMasterEvent: WorkMasterEvent = WorkMasterEvent.Initiate
)


// HomeUiEvent for top-level events
sealed interface HomeUiEvent {
    data class OmniEvent(val uiEvent: OmniTrackerUiEvent) : HomeUiEvent
    data class OmniWorkMasterEvent(val workerEvent: WorkMasterEvent) : HomeUiEvent
}

// WorkMasterUiEvent for specific events
sealed interface WorkMasterEvent {
    data object Initiate : WorkMasterEvent
    data class addHabit(val habit: HabitEntity) : WorkMasterEvent
}

// OmniTrackerUiEvent for specific events
sealed interface OmniTrackerUiEvent {
    data object Initiate : OmniTrackerUiEvent
    data object MakeHabit : OmniTrackerUiEvent
    data object MakeReminder : OmniTrackerUiEvent
    data object MakeNote : OmniTrackerUiEvent
    data object MakeGoal : OmniTrackerUiEvent
    data object MakeAlarm : OmniTrackerUiEvent
    data object MakeToDo : OmniTrackerUiEvent
    data object MakeJournal : OmniTrackerUiEvent
    data class MakeToast(val message: String) : OmniTrackerUiEvent
    data class MakeMood(val mood: Mood) : OmniTrackerUiEvent
}


sealed interface Mood{
    data object Happy: Mood
    data object Sad: Mood
    data object Angry: Mood
    data object Anxious: Mood
    data object Excited: Mood
    data object Tired: Mood
    data object Stressed: Mood
    data object Relaxed: Mood
    data object Bored: Mood
    data object Motivated: Mood
    data object Inspired: Mood
    data object Confused: Mood
    data object Frustrated: Mood
    data object Overwhelmed: Mood
    data object Content: Mood
    data object Disappointed: Mood
    data object Hopeful: Mood
    data object Grateful: Mood
    data object Guilty: Mood
    data object Lonely: Mood
    data object Nostalgic: Mood
    data object Optimistic: Mood
    data object Pessimistic: Mood
    data object Proud: Mood
    data object Regretful: Mood
    data object Resentful: Mood
    data object Sympathetic: Mood
    data object Worried: Mood
    data object Envious: Mood
    data object Jealous: Mood
    data object Insecure: Mood
    data object Inferior: Mood
    data object Superior: Mood
    data object Confident: Mood
    data object Hopeless: Mood
    data object Indifferent: Mood
    data object Irritated: Mood
    data object Overjoyed: Mood
    data object Panicked: Mood
    data object Paranoid: Mood
    data object Peaceful: Mood
    data object Pleased: Mood
    data object Rejected: Mood
    data object Restless: Mood
    data object Satisfied: Mood
    data object Scared: Mood
    data object Shocked: Mood
    data object Shy: Mood
    data object Skeptical: Mood
    data object Surprised: Mood
    data object Suspicious: Mood
    data object Terrified: Mood
    data object Threatened: Mood
    data object Uncomfortable: Mood
    data object Uneasy: Mood
    data object Unhappy: Mood
    data object Unsettled: Mood
    data object Upset: Mood
}