/*
 * Copyright (C) 2020 Komodo-OS-Rom
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
package com.komodo.settings;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Surface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;
import com.android.settings.search.BaseSearchIndexProvider;

import com.android.internal.logging.nano.MetricsProto;

import com.komodo.settings.categories.About;
import com.komodo.settings.categories.Interface;
import com.komodo.settings.categories.Gestures;
import com.komodo.settings.categories.Notifications;
import com.komodo.settings.categories.Misc;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class KomodoSettings extends SettingsPreferenceFragment {

    BottomNavigationView bottomNavigation;
    private MenuItem menuitem;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.komodo_settings, container, false);
        final BottomNavigationView bottomNavigation = (BottomNavigationView) view.findViewById(R.id.nav_bottom);
        mContext = getActivity();
        getActivity().setTitle(R.string.komodo_settings_title);

        final ViewPager viewPager = view.findViewById(R.id.viewpager);
        PagerAdapter mPagerAdapter = new PagerAdapter(getFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

    bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == bottomNavigation.getSelectedItemId()) {
        return false;
        } else {
             if (item.getItemId() == R.id.navigation_about) {
                viewPager.setCurrentItem(0);
             } else if (item.getItemId() == R.id.navigation_theming) {
                viewPager.setCurrentItem(1);
             } else if (item.getItemId() == R.id.navigation_notifications) {
                viewPager.setCurrentItem(2);
             } else if (item.getItemId() == R.id.navigation_gestures) {
                viewPager.setCurrentItem(3);
             } else if (item.getItemId() == R.id.navigation_misc) {
               viewPager.setCurrentItem(4);
             }
             return true;
            }
         }
     });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(menuitem != null) {
                    menuitem.setChecked(false);
                } else {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                menuitem = bottomNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        bottomNavigation.setSelectedItemId(R.id.navigation_about);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        PagerAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new About();
            frags[1] = new Interface();
            frags[2] = new Notifications();
            frags[3] = new Gestures();
            frags[4] = new Misc();
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private String[] getTitles() {
        String titleString[];
        titleString = new String[]{
                getString(R.string.navigation_about_title),
                getString(R.string.navigation_theming_title),
                getString(R.string.navigation_gesture_title),
                getString(R.string.navigation_notification_title),
                getString(R.string.navigation_misc_title)};

        return titleString;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /* Dont add super. to fix duplicate Search and Help & Feedback */
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.KOMODO_SETTINGS;
    }

    public static void lockCurrentOrientation(Activity activity) {
        int currentRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        int frozenRotation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        switch (currentRotation) {
            case Surface.ROTATION_0:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case Surface.ROTATION_270:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }
        activity.setRequestedOrientation(frozenRotation);
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.layout.komodo_settings);
}
