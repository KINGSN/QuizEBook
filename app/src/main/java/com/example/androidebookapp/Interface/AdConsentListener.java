package com.example.androidebookapp.Interface;

import com.google.ads.consent.ConsentStatus;

public interface AdConsentListener {
    void onConsentUpdate(ConsentStatus consentStatus);
}
