package com.yandex.gdpr.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yandex.gdpr.sample.settings.SettingsActivity;
import com.yandex.gdpr.sample.settings.SettingsFragment;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.MobileAds;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration;
import com.yandex.mobile.ads.nativeads.NativeAppInstallAd;
import com.yandex.mobile.ads.nativeads.NativeContentAd;
import com.yandex.mobile.ads.nativeads.NativeGenericAd;
import com.yandex.mobile.ads.nativeads.NativeImageAd;
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
        /*
        * Replace demo R-M-DEMO-native-i with actual Block ID
        * Please, note, that configured image sizes don't affect demo ads.
        * Following demo Block IDs may be used for testing:
        * app install: R-M-DEMO-native-i
        * content: R-M-DEMO-native-c
        */
        final NativeAdLoaderConfiguration adLoaderConfiguration =
                new NativeAdLoaderConfiguration.Builder("R-M-DEMO-native-i", true)
                        .setImageSizes(NativeAdLoaderConfiguration.NATIVE_IMAGE_SIZE_MEDIUM).build();
        mNativeAdLoader = new NativeAdLoader(this, adLoaderConfiguration);
        mNativeAdLoader.setNativeAdLoadListener(new NativeAdLoadListener());
    }

    private void bindNativeAd(@NonNull final NativeGenericAd nativeAd) {
        mNativeBannerView.setAd(nativeAd);
        mNativeBannerView.setVisibility(View.VISIBLE);
    }

    private void refreshNativeAd() {
        mNativeBannerView.setVisibility(View.GONE);
        mNativeAdLoader.loadAd(AdRequest.builder().build());
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

    private class NativeAdLoadListener implements NativeAdLoader.OnImageAdLoadListener {

        @Override
        public void onAppInstallAdLoaded(@NonNull final NativeAppInstallAd nativeAppInstallAd) {
            bindNativeAd(nativeAppInstallAd);
        }

        @Override
        public void onContentAdLoaded(@NonNull NativeContentAd nativeContentAd) {
            bindNativeAd(nativeContentAd);
        }

        @Override
        public void onImageAdLoaded(@NonNull NativeImageAd nativeImageAd) {
            bindNativeAd(nativeImageAd);
        }

        @Override
        public void onAdFailedToLoad(@NonNull AdRequestError error) {
            Log.d(SAMPLE_TAG, error.getDescription());
            Toast.makeText(MainActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
        }
    }
}
