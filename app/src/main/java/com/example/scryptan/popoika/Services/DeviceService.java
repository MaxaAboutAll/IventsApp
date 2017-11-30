package com.example.scryptan.popoika.Services;

import android.content.ContentResolver;
import android.provider.Settings;

import java.util.Locale;

public class DeviceService implements IDeviceService {

    private ContentResolver resolver;

    public DeviceService(ContentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public String getDeviceID() {
        return Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
    }

    @Override
    public String getDeviceCountry() {
        return Locale.getDefault().getDisplayCountry();
    }

}
