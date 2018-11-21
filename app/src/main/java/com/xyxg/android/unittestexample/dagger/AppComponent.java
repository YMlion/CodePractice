package com.xyxg.android.unittestexample.dagger;

import com.xyxg.android.unittestexample.Login;

import dagger.Component;

/**
 * @author YML
 * @date 2016/9/20
 */
@Component(modules = AppModules.class)
public interface AppComponent {
    void inject(Login login);
}
