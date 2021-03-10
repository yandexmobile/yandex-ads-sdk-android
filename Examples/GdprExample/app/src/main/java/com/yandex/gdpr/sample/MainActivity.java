package com.yandex.gdpr.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.yandex.gdpr.sample.settings.SettingsActivity;
import com.yandex.gdpr.sample.settings.SettingsFragment;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.MobileAds;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;
import com.yandex.mobile.ads.nativeads.template.NativeBannerView;

public class MainActivity extends AppCompatActivity implements GdprDialogFragment.NoticeDialogListener {

    private static final String DIALOG_TAG = "dialog";
    private static final String SAMPLE_TAG = "GdprExample";

    private NativeAdLoader mNativeAdLoader;
    private NativeBannerView mNativeBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button nativeAdLoadButton = findViewById(R.id.native_load_button);
        nativeAdLoadButton.setOnClickListener(new NativeAdLoadClickListener());

        mNativeBannerView = findViewById(R.id.native_template);
        createNativeAdLoader();
        setUserConsent();
    }

    @Override
    public void onDialogClick() {
        setUserConsent();
        refreshNativeAd();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (R.id.settings == item.getItemId()) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNativeAdLoader() {
        mNativeAdLoader = new NativeAdLoader(this);
        mNativeAdLoader.setNativeAdLoadListener(new NativeLoadListener());
    }

    private void bindNativeAd(@NonNull final NativeAd nativeAd) {
        mNativeBannerView.setAd(nativeAd);
        mNativeBannerView.setVisibility(View.VISIBLE);
    }

    private void refreshNativeAd() {
        mNativeBannerView.setVisibility(View.GONE);

        /*
         * Replace demo R-M-DEMO-native-i with actual Block ID
         * Please, note, that configured image sizes don't affect demo ads.
         * Following demo Block IDs may be used for testing:
         * app install: R-M-DEMO-native-i
         * content: R-M-DEMO-native-c
         */
        final NativeAdRequestConfiguration nativeAdRequestConfiguration =
                new NativeAdRequestConfiguration.Builder("R-M-DEMO-native-i")
                        .setShouldLoadImagesAutomatically(true)
                        .build();
        mNativeAdLoader.loadAd(nativeAdRequestConfiguration);
    }

    private void setUserConsent() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean userConsent = preferences.getBoolean(SettingsFragment.USER_CONSENT_KEY, false);

        MobileAds.setUserConsent(userConsent);
    }

    private boolean isDialogShown() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        return preferences.getBoolean(SettingsFragment.DIALOG_SHOWN_KEY, false);
    }

    private void showDialog() {
        final GdprDialogFragment dialogFragment = new GdprDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    private class NativeAdLoadClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View v) {
            if (isDialogShown()) {
                refreshNativeAd();
            } else {
                showDialog();
            }
        }
    }

    private class NativeLoadListener implements NativeAdLoadListener {
        @Override
        public void onAdLoaded(@NonNull final NativeAd nativeAd) {
            bindNativeAd(nativeAd);
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
            Log.d(SAMPLE_TAG, adRequestError.getDescription());
            Toast.makeText(MainActivity.this, adRequestError.getDescription(), Toast.LENGTH_SHORT).show();
        }
    }
}
