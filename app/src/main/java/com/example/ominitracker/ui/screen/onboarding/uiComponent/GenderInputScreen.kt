package com.example.ominitracker.ui.screen.onboarding.uiComponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun GenderInputScreen(
    gender: String,
    onGenderInfoChange: (String) -> Unit
) {
    val genders = listOf("Male", "Female", "Other")
    var selectedGender by remember { mutableStateOf(gender) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select your gender", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        genders.forEach { gender ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedGender = gender
                        onGenderInfoChange(gender)
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedGender == gender,
                    onClick = {
                        selectedGender = gender
                        onGenderInfoChange(gender)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = gender)
            }
        }
    }
}