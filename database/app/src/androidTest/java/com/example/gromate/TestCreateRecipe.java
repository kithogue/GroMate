package com.example.gromate;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;


import com.example.gromate.gromate.SignInActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.quickstart.database.R;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestCreateRecipe {

    public static final int SLEEP_TIME_MILLIS = 5000;
    @Rule
    public ActivityTestRule<SignInActivity> mActivityTestRule = new ActivityTestRule<>(SignInActivity.class);

    @NonNull
    private static ViewAction selectTabAtPosition(final int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "with tab at index" + position;
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof TabLayout) {
                    TabLayout tabLayout = (TabLayout) view;
                    TabLayout.Tab tab = tabLayout.getTabAt(position);

                    if (tab != null) {
                        tab.select();
                    }
                }
            }
        };
    }

    @Test
    public void testNewReceiptCreated() throws InterruptedException {
        String username = "user" + randomDigits();
        String email = username + "@example.com";
        String password = "testuser";
        String orderName = "Receipt " + randomDigits();
        String orderItemName = "Ingredient " + randomDigits();
        String unit = "kg";
        String quantity = "3";

        // Go back to the sign in screen if we're logged in from a previous test
        logOutIfPossible();

        // Select email field
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.fieldEmail), isDisplayed()));
        appCompatEditText.perform(click());

        // Enter email address
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.fieldEmail), isDisplayed()));
        appCompatEditText2.perform(replaceText(email));

        // Enter password
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.fieldPassword), isDisplayed()));
        appCompatEditText3.perform(replaceText(password));

        // Click sign up
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.buttonSignUp), withText(R.string.sign_up), isDisplayed()));
        appCompatButton.perform(click());

        Thread.sleep(SLEEP_TIME_MILLIS);

        onView(withId(R.id.tabs)).perform(selectTabAtPosition(1));

        Thread.sleep(SLEEP_TIME_MILLIS);

        //click add receipt
        ViewInteraction addReceiptButton = onView(
                allOf(withId(R.id.fabAddOrder), isDisplayed()));
        addReceiptButton.perform(click());

        Thread.sleep(SLEEP_TIME_MILLIS);

        //fill receipt name
        ViewInteraction orderNameField = onView(
                allOf(withId(R.id.orderName), isDisplayed()));
        orderNameField.perform(replaceText(orderName));

        //click add ingredient
        ViewInteraction addIngredientButton = onView(
                allOf(withId(R.id.fabAddOrderItem), isDisplayed()));
        addIngredientButton.perform(click());


        //fill ingredient name
        ViewInteraction ingredientNameField = onView(
                allOf(withId(R.id.orderItemName), isDisplayed()));
        ingredientNameField.perform(replaceText(orderItemName));

        //fill quantity
        ViewInteraction ingredientQuantityField = onView(
                allOf(withId(R.id.orderItemQuantity), isDisplayed()));
        ingredientQuantityField.perform(replaceText(quantity));

        //fill unit
        ViewInteraction ingredientUnitField = onView(
                allOf(withId(R.id.orderItemUnit), isDisplayed()));
        ingredientUnitField.perform(replaceText(unit));

        //click save ingredient
        ViewInteraction saveIngredientButton = onView(
                allOf(withId(R.id.fabSaveOrderItem), isDisplayed()));
        saveIngredientButton.perform(click());

        Thread.sleep(SLEEP_TIME_MILLIS);

        //check number of ingredients
        onView(withId(R.id.orderItemsList)).check(new RecyclerViewItemCountAssertion(1));

        //click save ingredient
        ViewInteraction saveReceiptButton = onView(
                allOf(withId(R.id.fabSaveOrder), isDisplayed()));
        saveReceiptButton.perform(click());

        Thread.sleep(SLEEP_TIME_MILLIS);

        onView(withId(R.id.ordersList)).check(new RecyclerViewItemCountAssertion(1));


        //click save ingredient
        ViewInteraction orderDeleteBtn = onView(
                allOf(withId(R.id.orderDeleteBtn), isDisplayed()));
        orderDeleteBtn.perform(click());

        onView(withId(R.id.ordersList)).check(new RecyclerViewItemCountAssertion(0));

    }

    /**
     * Click the 'Log Out' overflow menu if it exists (which would mean we're signed in).
     */
    private void logOutIfPossible() {
        try {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
            onView(withText(R.string.menu_logout)).perform(click());
        } catch (NoMatchingViewException e) {
            // Ignore exception since we only want to do this operation if it's easy.
        }

    }

    /**
     * Generate a random string of digits.
     */
    private String randomDigits() {
        Random random = new Random();
        return String.valueOf(random.nextInt(99999999));
    }

}

