package com.example.nikola.dotatracker.btmnavigation;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikola.dotatracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends Fragment {

    private static final String LOG_TAG = MatchesFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(LOG_TAG, "MatchesFragment: onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "MatchesFragment: onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "MatchesFragment: onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "MatchesFragment: onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "MatchesFragment: onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "MatchesFragment: onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "MatchesFragment: onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(LOG_TAG, "MatchesFragment: onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "MatchesFragment: onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(LOG_TAG, "MatchesFragment: onDetach");
    }

    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(LOG_TAG, "MatchesFragment: onCreateView");
        return inflater.inflate(R.layout.fragment_matches, container, false);
    }

}
