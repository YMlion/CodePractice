package com.xyxg.android.unittestexample.dagger;

import com.xyxg.android.unittestexample.Request;

import dagger.Module;
import dagger.Provides;

/**
 * @author YML
 * @date 2016/9/20
 */
@Module
public class AppModules {

    @Provides
    public Request provideRequest() {
        return new Request();
    }
}
