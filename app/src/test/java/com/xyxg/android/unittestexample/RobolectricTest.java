package com.xyxg.android.unittestexample;

import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertEquals;

/**
 * @author YML
 * @date 2016/9/7
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RobolectricTest {

    ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);

    MainActivity mainActivity;

    @Before
    public void setUp() {
        //mainActivity = Robolectric.setupActivity(MainActivity.class);
        ShadowLog.stream = System.out;
        mainActivity = controller.create().get();
    }

    @Test
    public void lifeTest() {
        ShadowActivity s = Shadows.shadowOf(mainActivity);
        controller.start();
        TextView textView = (TextView) s.findViewById(R.id.textView);
        textView.performClick();
        controller.stop();
        assertEquals(textView.getText().toString(), "Hello Test!");
    }
}
