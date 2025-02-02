package com.example.ominitracker.ui.common.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ominitracker.data.modal.HabitEntity

@Composable
fun HabitDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onSave: (HabitEntity) -> Unit
) {

    Dialog(
        onDismissRequest = onCancel ,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        HabitDialogContent(
            modifier = modifier,
            onSave = onSave,
            onCancel = onCancel
        )
    }

}

@Composable
fun HabitDialogContent(
    modifier: Modifier = Modifier, onSave: (HabitEntity) -> Unit, onCancel: () -> Unit
) {
    var habitTitle by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Daily") }
    var cueTrigger by remember { mutableStateOf("") }
    var reward by remember { mutableStateOf("") }
    var goalCount by remember { mutableStateOf("") }
    val showMoreRepetitionSettings = remember { mutableStateOf(false) }
    var customInterval by remember { mutableStateOf("") }
    var repetitionCount by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Create a Habit",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Habit Title Input
        OutlinedTextField(value = habitTitle,
            onValueChange = { habitTitle = it },
            label = { Text("Habit Title") },
            modifier = Modifier.fillMaxWidth()
        )
        // Frequency Dropdown
        Column{
            DropdownMenuField(label = "Frequency",
                options = listOf("Daily", "Weekly", "Monthly"),
                selectedOption = frequency,
                onOptionSelected = { frequency = it })
            AnimatedVisibility(showMoreRepetitionSettings.value) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = customInterval,
                        onValueChange = { customInterval = it },
                        label = { Text("Custom Interval (e.g., 1 hour)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = repetitionCount,
                        onValueChange = { repetitionCount = it },
                        label = { Text("Repetition Count") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
            Text(
                text= "Show ${if (showMoreRepetitionSettings.value) "Less" else "More"}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable {
                    showMoreRepetitionSettings.value = !showMoreRepetitionSettings.value
                }
            )
        }

        // Cue/Trigger Input
        OutlinedTextField(value = cueTrigger,
            onValueChange = { cueTrigger = it },
            label = { Text("Cue/Trigger") },
            modifier = Modifier.fillMaxWidth()
        )

        // Goal Count Input
        OutlinedTextField(value = goalCount,
            onValueChange = { goalCount = it },
            label = { Text("Goal Count (e.g., 30 days)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        // Reward Input
        OutlinedTextField(value = reward,
            onValueChange = { reward = it },
            label = { Text("Reward (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Buttons
        Row(
            horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                onSave(
                    HabitEntity(
                        title = habitTitle,
                        frequency = frequency,
                        customInterval = customInterval,
                        repetitionCount = repetitionCount,
                        cueTrigger = cueTrigger,
                        goalCount = goalCount.toIntOrNull() ?: 30,
                        reward = reward
                    )
                )
            }) {
                Text("Save")
            }
        }
    }
}

@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        OutlinedTextField(value = selectedOption,
            onValueChange = { },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    Modifier.clickable { expanded = !expanded })
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}


