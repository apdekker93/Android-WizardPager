/*
 * Copyright 2013 Google Inc.
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

package com.example.android.wizardpager.wizard.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.model.MultipleFixedChoicePage;
import com.example.android.wizardpager.wizard.model.Page;

public class MultipleChoiceFragment extends WizardListFragment {

    public static MultipleChoiceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MultipleChoiceFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MultipleFixedChoicePage fixedChoicePage = (MultipleFixedChoicePage) mPage;
        mChoices = new ArrayList<String>();
        for (int i = 0; i < fixedChoicePage.getOptionCount(); i++) {
            mChoices.add(fixedChoicePage.getOptionAt(i));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_list, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        // Pre-select currently selected items.
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                ArrayList<String> selectedItems = mPage.getData().getStringArrayList(
                        Page.SIMPLE_DATA_KEY);
                if (selectedItems == null || selectedItems.size() == 0) {
                    return;
                }

                Set<String> selectedSet = new HashSet<String>(selectedItems);

                for (int i = 0; i < mChoices.size(); i++) {
                    if (selectedSet.contains(mChoices.get(i))) {
                        mListView.setItemChecked(i, true);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
        SparseBooleanArray checkedPositions = mListView.getCheckedItemPositions();
        ArrayList<String> selections = new ArrayList<String>();
        for (int i = 0; i < checkedPositions.size(); i++) {
            if (checkedPositions.valueAt(i)) {
                selections.add(
                        mListView.getAdapter().getItem(checkedPositions.keyAt(i)).toString());
            }
        }

        mPage.getData().putStringArrayList(Page.SIMPLE_DATA_KEY, selections);
        mPage.notifyDataChanged();
    }
}
