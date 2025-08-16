package com.example.ominitracker.ui.screen.onboarding

// ---------- OnboardingPage.kt ----------
sealed class OnboardingPage(val order: Int, val show: Boolean) {
    object Name   : OnboardingPage(0, true)
    object Age    : OnboardingPage(1, true)
    object Gender : OnboardingPage(2, true)

    companion object {
        // Only the pages we actually want to show, in order
        val visiblePages: List<OnboardingPage>
            get() = listOf(Name, Age, Gender).filter { it.show }

        val lastIndex: Int
            get() = visiblePages.lastIndex

        val totalCount: Int
            get() = visiblePages.size

        fun pageAt(index: Int): OnboardingPage =
            visiblePages.getOrNull(index) ?: visiblePages.first()

        // count of pages from 0..index (inclusive)
        fun countUpToInclusive(index: Int): Int =
            (index + 1).coerceIn(1, totalCount)
    }
}
