package com.example.ominitracker.ui.screen.onboarding

import com.example.ominitracker.data.modal.UserInfo
import com.example.ominitracker.util.isValidAge

fun isPageValid(page: OnboardingPage, userInfo: UserInfo): Boolean {
    return when (page) {
        OnboardingPage.Name -> userInfo.name.isNotBlank()
        OnboardingPage.Age -> userInfo.age.isValidAge()
        OnboardingPage.Gender -> userInfo.gender.isNotBlank()
    }
}
