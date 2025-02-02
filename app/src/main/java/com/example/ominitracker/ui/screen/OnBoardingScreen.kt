package com.example.ominitracker.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ominitracker.data.modal.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    skip: () -> Unit,
    onComplete: (UserInfo) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val userInfo = remember { mutableStateOf(UserInfo()) }
    val coroutineScope = rememberCoroutineScope()


    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        Text(
            text = "skip",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable {
                    skip.invoke()
                }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> NameInputScreen(userInfo.value) { userInfo.value = it }
                    1 -> AgeInputScreen(userInfo.value) { userInfo.value = it }
                    2 -> GenderInputScreen(userInfo.value) { userInfo.value = it }
                }
            }

            // Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (pagerState.currentPage > 0) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    ) {
                        Text("Back")
                    }
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        if (pagerState.currentPage < 2) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onComplete(userInfo.value)
                        }
                    }
                ) {
                    Text(if (pagerState.currentPage == 2) "Finish" else "Next")
                }
            }
        }
    }

}


@Composable
fun NameInputScreen(
    userInfo: UserInfo,
    onUserInfoChange: (UserInfo) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("What's your name?", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = userInfo.name,
            onValueChange = { onUserInfoChange(userInfo.copy(name = it)) },
            placeholder = { Text("Enter your name") },
            singleLine = true
        )
    }
}

@Composable
fun AgeInputScreen(
    userInfo: UserInfo,
    onUserInfoChange: (UserInfo) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("How old are you?", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = userInfo.age?.toString() ?: "",
            onValueChange = {
                val age = it.toIntOrNull()
                onUserInfoChange(userInfo.copy(age = age))
            },
            placeholder = { Text("Enter your age") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun GenderInputScreen(
    userInfo: UserInfo,
    onUserInfoChange: (UserInfo) -> Unit
) {
    val genders = listOf("Male", "Female", "Other")
    var selectedGender by remember { mutableStateOf(userInfo.gender) }

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
                        onUserInfoChange(userInfo.copy(gender = gender))
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedGender == gender,
                    onClick = {
                        selectedGender = gender
                        onUserInfoChange(userInfo.copy(gender = gender))
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = gender)
            }
        }
    }
}

