package com.xyxg.android.unittestexample;

import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    MainActivity mainActivity;
    TextView textView;

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Throwable {
        mainActivity =  testRule.getActivity();
        textView = (TextView) mainActivity.findViewById(R.id.textView);
        /*ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);
        activityTestRule.launchActivity(null);
        loginActivity = activityTestRule.getActivity();*/
    }

    @Test @UiThreadTest @SdkSuppress(minSdkVersion = 19)
    public void setText() {
        textView.performClick();
        assertEquals("Hello Test!", textView.getText().toString());
    }
}
