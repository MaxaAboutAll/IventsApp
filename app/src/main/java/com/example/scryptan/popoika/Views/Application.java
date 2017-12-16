package com.example.scryptan.popoika.Views;

import com.vk.sdk.VKSdk;

public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
