package com.whattabiz.legall.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whattabiz.legall.GsonConverter;
import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.adapters.ParentCardRecyclerAdapter;
import com.whattabiz.legall.R;

/**
 * Created by Rumaan on 11-11-2016.
 */

public class CasesFragment extends Fragment {
    // TODO: 11-11-2016 Get details from BackEnd

    public static final String SHARED_PREF_KEY_CASE = "CASES";

    private ParentCardRecyclerAdapter parentCardRecyclerAdapter;
    private TextView emptyTextView;
    private RecyclerView parentCardView;


    public static CasesFragment newInstance() {
        Bundle args = new Bundle();
        CasesFragment fragment = new CasesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static void saveToSharedPrefs(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_KEY_CASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // get the JSON STRING of Case Models
        String jsonString = GsonConverter.fromPOJOtoJson(Lawyers.getInstance().caseModelArrayList);
        editor.putString("CASE_JSON", jsonString);
        editor.apply();

        Log.d("JSON to sharedpref", jsonString);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_lawyer_cases, container, false);
        // set up recycler here
        parentCardView = (RecyclerView) parentView.findViewById(R.id.recycler_parent_card_view);
        emptyTextView = (TextView) parentView.findViewById(R.id.empty_list_tv);

        // instantiate the Recycler View
        parentCardRecyclerAdapter = new ParentCardRecyclerAdapter(getContext());

        parentCardView.setLayoutManager(new LinearLayoutManager(getContext()));

        // if list is empty show empty text

        Log.i("Legall","here");



        if (Lawyers.caseModelArrayList.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            parentCardView.setVisibility(View.GONE);

            Log.i("Legall","on it !");
        } else {
            emptyTextView.setVisibility(View.GONE);
            parentCardView.setVisibility(View.VISIBLE);



            parentCardView.setItemAnimator(new DefaultItemAnimator());
            parentCardRecyclerAdapter.notifyDataSetChanged();
        }
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // stack overflow solution
        parentCardView.setAdapter(parentCardRecyclerAdapter);

        // get the details from Singleton and notify
        if (Lawyers.getInstance().caseModelArrayList.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            parentCardView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            parentCardView.setVisibility(View.VISIBLE);
            parentCardRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (!Lawyers.getInstance().caseModelArrayList.isEmpty()) {
            saveToSharedPrefs(getContext());
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if (!Lawyers.getInstance().caseModelArrayList.isEmpty()) {
            saveToSharedPrefs(getContext());
        }*/
    }
}
