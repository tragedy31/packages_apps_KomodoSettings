/*
 * Copyright (C) 2018 Havoc-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.komodo.settings.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.List;

import com.komodo.settings.preferences.CustomSeekBarPreference;

public class Battery extends DashboardFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Battery";
    private static final String KEY_SMART_CHARGING_LEVEL = "smart_charging_level";

    private CustomSeekBarPreference mSmartChargingLevel;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        int mSmartChargingLevelDefaultConfig = getResources().getInteger(
                com.android.internal.R.integer.config_smartChargingBatteryLevel);

        mSmartChargingLevel = (CustomSeekBarPreference) findPreference(KEY_SMART_CHARGING_LEVEL);
        int currentLevel = Settings.System.getInt(getContentResolver(),
            Settings.System.SMART_CHARGING_LEVEL, mSmartChargingLevelDefaultConfig);
        mSmartChargingLevel.setValue(currentLevel);
        mSmartChargingLevel.setOnPreferenceChangeListener(this);
        mFooterPreferenceMixin.createFooterPreference().setTitle(R.string.smart_charging_footer);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mSmartChargingLevel) {
            int smartChargingLevel = (Integer) objValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SMART_CHARGING_LEVEL, smartChargingLevel);
            return true;
        } else {
            return false;
        }
    }

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();

                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.komodo_settings_battery;
                    result.add(sir);
                    return result;
                }
            };

    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.komodo_settings_battery;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.KOMODO_SETTINGS;
    }
}
