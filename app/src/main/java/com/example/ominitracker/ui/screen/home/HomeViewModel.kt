package com.example.ominitracker.ui.screen.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OmniEvent -> {
                handleOmniTrackerEvents(event)
            }
            is HomeUiEvent.OmniWorkMasterEvent -> {
                handleWorkMasterEvents(event)
            }
        }
    }

    private fun handleWorkMasterEvents(event: HomeUiEvent.OmniWorkMasterEvent) {
        when (event.workerEvent) {
            WorkMasterUiEvent.oneTimeWorkEvent -> {
                _state.update { it.copy(workMasterEvent = WorkMasterUiEvent.oneTimeWorkEvent) }
            }
            WorkMasterUiEvent.periodicWorkEvent -> {
                _state.update { it.copy(workMasterEvent = WorkMasterUiEvent.periodicWorkEvent) }
            }
            else -> {
                _state.update { it.copy(workMasterEvent = WorkMasterUiEvent.Initiate) }
            }
        }
    }

    private fun handleOmniTrackerEvents(event: HomeUiEvent.OmniEvent) {
        when (event.uiEvent) {
            OmniTrackerUiEvent.Initiate -> {
                _state.update { it.copy(omniEvent = OmniTrackerUiEvent.Initiate) }
            }
            OmniTrackerUiEvent.MakeHabit -> {
                _state.update { it.copy(omniEvent = OmniTrackerUiEvent.MakeHabit) }
            }
            else -> {
                _state.update { it.copy(omniEvent = OmniTrackerUiEvent.MakeToast("Feature not available yet")) }
            }
        }
    }
}

data class HomeUiState(
    val omniEvent: OmniTrackerUiEvent = OmniTrackerUiEvent.Initiate,
    val workMasterEvent: WorkMasterUiEvent = WorkMasterUiEvent.Initiate
)


// HomeUiEvent for top-level events
sealed interface HomeUiEvent {
    data class OmniEvent(val uiEvent: OmniTrackerUiEvent) : HomeUiEvent
    data class OmniWorkMasterEvent(val workerEvent: WorkMasterUiEvent) : HomeUiEvent
}

// WorkMasterUiEvent for specific events
sealed interface WorkMasterUiEvent {
    data object Initiate : WorkMasterUiEvent
    data object oneTimeWorkEvent : WorkMasterUiEvent
    data object periodicWorkEvent : WorkMasterUiEvent
}

// OmniTrackerUiEvent for specific events
sealed interface OmniTrackerUiEvent {
    object Initiate : OmniTrackerUiEvent
    object MakeHabit : OmniTrackerUiEvent
    object MakeReminder : OmniTrackerUiEvent
    object MakeNote : OmniTrackerUiEvent
    object MakeGoal : OmniTrackerUiEvent
    object MakeAlarm : OmniTrackerUiEvent
    object MakeToDo : OmniTrackerUiEvent
    object MakeJournal : OmniTrackerUiEvent
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