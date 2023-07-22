//package com.example.fitpeo.view
//
//
//import androidx.test.espresso.Espresso
//import androidx.test.espresso.assertion.ViewAssertions
//import androidx.test.espresso.matcher.ViewMatchers
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
//import com.example.fitpeo.R
//import com.example.fitpeo.presentation.FragmentContainerActivity
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4ClassRunner::class)
//class MainActivityTest {
//    @get:Rule
//    val activityRule = ActivityScenarioRule(FragmentContainerActivity::class.java)
//
//    @Test
//    fun test_isActivityInView() {
//        Espresso.onView(withId(R.id.activity_main))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//    }
//
//}