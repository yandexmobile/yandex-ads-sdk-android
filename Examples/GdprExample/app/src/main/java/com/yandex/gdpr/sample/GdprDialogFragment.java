/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.gdpr.sample;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;

import com.yandex.gdpr.sample.settings.SettingsFragment;

public class GdprDialogFragment extends DialogFragment {

    private NoticeDialogListener mNoticeDialogListener;

    public interface NoticeDialogListener {
        void onDialogClick();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try {
            mNoticeDialogListener = (NoticeDialogListener) getActivity();
        } catch (final ClassCastException ignored) {
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getContext();

        // This is sample GDPR dialog which is used to demonstrate the GDPR user consent retrieval logic.
        // Please, do not use this dialog in production app.
        // Replace it with one which is suitable for the app.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.gdpr_title)
                .setMessage(R.string.gdpr_message)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onButtonClicked(context, true);
                    }
                })
                .setNeutralButton(R.string.about, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        openPrivacyPolicy();
                    }
                })
                .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onButtonClicked(context, false);
                    }
                });
        return builder.create();
    }

    private void openPrivacyPolicy() {
        final String url = getString(R.string.privacy_policy_url);
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void onButtonClicked(final Context context,
                                 final boolean userConsent) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(SettingsFragment.USER_CONSENT_KEY, userConsent)
                .putBoolean(SettingsFragment.DIALOG_SHOWN_KEY, true)
                .apply();

        mNoticeDialogListener.onDialogClick();
    }
}
