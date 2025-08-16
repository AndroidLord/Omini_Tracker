package com.example.ominitracker.ui.screen.onboarding

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ominitracker.data.modal.UserInfo
import com.example.ominitracker.ui.screen.onboarding.uiComponent.AgeInputScreen
import com.example.ominitracker.ui.screen.onboarding.uiComponent.GenderInputScreen
import com.example.ominitracker.ui.screen.onboarding.uiComponent.NameInputScreen
import kotlinx.coroutines.launch

// ---------- OnboardingScreen.kt ----------
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    skip: () -> Unit,
    onComplete: (UserInfo) -> Unit
) {
    var userInfo by remember { mutableStateOf(UserInfo()) }

    // unlockedCount is a COUNT (not an index). Start with 1 => page 0 only.
    var unlockedCount by remember { mutableIntStateOf(1) }

    // Use dynamic pageCount so swiping can’t go past unlocked pages
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { unlockedCount }
    )

    val current = pagerState.currentPage
    val currentPage = OnboardingPage.pageAt(current)
    val isCurrentPageValid = remember(userInfo, current) {
        isPageValid(currentPage, userInfo)
    }


    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    // Auto-unlock next when current page becomes valid; optionally lock back to current if invalid
    LaunchedEffect(userInfo, pagerState.currentPage) {
        val currentIndex = pagerState.currentPage
        val currentPage  = OnboardingPage.pageAt(currentIndex)

        if (isPageValid(currentPage, userInfo)) {
            // unlock NEXT page: count must include current + next => currentIndex + 2
            val targetCount = OnboardingPage.countUpToInclusive(currentIndex + 1)
            if (unlockedCount < targetCount) {
                unlockedCount = targetCount
            }
        } else {
            // STRICT mode (re-lock forwards): keep pages only up to current
            val targetCount = OnboardingPage.countUpToInclusive(currentIndex)
            if (unlockedCount > targetCount) {
                unlockedCount = targetCount
                // if current was pushed out by shrink (rare), snap back safely
                val safeIndex = (unlockedCount - 1).coerceIn(0, OnboardingPage.lastIndex)
                if (pagerState.currentPage > safeIndex) {
                    coroutineScope.launch { pagerState.animateScrollToPage(safeIndex) }
                }
            }
        }
    }

    // ----- UI shell -----
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = true // swipe is allowed but capped by unlockedCount
            ) { index ->
                when (OnboardingPage.pageAt(index)) {
                    OnboardingPage.Name -> NameInputScreen(
                        name = userInfo.name,
                        onNameInputChange = { userInfo = userInfo.copy(name = it) }
                    )
                    OnboardingPage.Age -> AgeInputScreen(
                        age = userInfo.age?.toString() ?: "",
                        onAgeChange = { userInfo = userInfo.copy(age = it) }
                    )
                    OnboardingPage.Gender -> GenderInputScreen(
                        gender = userInfo.gender,
                        onGenderInfoChange = { userInfo = userInfo.copy(gender = it) }
                    )
                }
            }

            // Nav bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (pagerState.currentPage > 0) {
                    Button(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }) { Text("Back") }
                } else {
                    Spacer(Modifier.width(8.dp))
                }

                Spacer(Modifier.weight(1f))

                val isLast = pagerState.currentPage == unlockedCount - 1 &&
                        unlockedCount == OnboardingPage.totalCount

                Button(
                    onClick = {
                    val currentPage = OnboardingPage.pageAt(pagerState.currentPage)

                    if (!isPageValid(currentPage, userInfo)) {
                        // 🔥 Validation toast
                        Toast.makeText(
                            context,
                            "Please fill out this page before continuing",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (isLast) {
                        onComplete(userInfo)
                    } else {
                        // can go next only if next page is unlocked
                        val nextIndex = pagerState.currentPage + 1
                        if (nextIndex < unlockedCount) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(nextIndex)
                            }
                        }
                    }
                },
                    enabled = isCurrentPageValid
                ) {
                    Text(if (isLast) "Finish" else "Next")
                }
            }
        }

        Text(
            text = "Skip All",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable { skip() }
        )
    }
}


