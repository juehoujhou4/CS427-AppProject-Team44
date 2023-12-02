package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestUserLogoff {

    @Rule
    public ActivityScenarioRule<CreateAccountActivity> activityRule = new ActivityScenarioRule<>(CreateAccountActivity.class);

    // Test that a valid signoff  takes a user to the MainActivity
    @Test
    public void testLogoffSuccess() {
        // Log the user in
        // Enter Valid Inputs
        String testUsername = "riley";
        String testPassword = "team44";

        // Enter text in username and password fields
        onView(withId(R.id.inputUsername)).perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.inputPassword)).perform(typeText(testPassword), closeSoftKeyboard());

        // Sleep to slow time
        try {Thread.sleep(2000);} catch(InterruptedException e) {System.out.println("Interrupted!");}

        // Click on the sign in button
        onView(withId(R.id.buttonSignIn)).perform(click());

        // Sleep to slow time
        try {Thread.sleep(2000);} catch(InterruptedException e) {System.out.println("Interrupted!");}

        // Perform logoff action
        onView(withId(R.id.buttonSignUp)).perform(click());

        // Assert that we are back on the login page after logoff
        String loginPageString = "Authentication Page";
        onView(withId(R.id.textViewHeader)).check(matches(withText(loginPageString)));

        // Sleep to slow time
        try {Thread.sleep(2000);} catch(InterruptedException e) {System.out.println("Interrupted!");}

        // Delay at the end
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Test that a user remains on the main page if logoff is unsuccessful
    @Test
    public void testSignInFail() {
        // Assume the user is already logged in

        // Sleep to slow time
        try {Thread.sleep(2000);} catch(InterruptedException e) {System.out.println("Interrupted!");}
        // Perform logoff action
        // This may involve clicking a logoff button or navigating to a logoff screen, depending on your app's design
        onView(withId(R.id.buttonSignUp)).perform(click());

        // Assuming there's an error message displayed on a failed logoff attempt
        String errorMessage = "Logoff Failed";
        onView(withId(R.id.textViewHeader)).check(matches(withText(errorMessage)));

        // Assert that we are still on the main page
        String mainPageString = "  CS427 Project App";
        onView(withId(R.id.textView3)).check(matches(withText(mainPageString)));

        // Sleep to slow time
        try {Thread.sleep(2000);} catch(InterruptedException e) {System.out.println("Interrupted!");}

        // Delay at the end
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

