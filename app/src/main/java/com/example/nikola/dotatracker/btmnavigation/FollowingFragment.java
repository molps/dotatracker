package com.example.nikola.dotatracker.btmnavigation;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nikola.dotatracker.R;
import com.example.nikola.dotatracker.data.DotaContract.DotaFollowing;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private TextView textView;

    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_following_textView);
        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                DotaFollowing.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        textView.setText("");
        if (data != null) {
            while (data.moveToNext())
                textView.append("\n\n" + data.getString(data.getColumnIndex(DotaFollowing.COLUMN_PLAYER_ID)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
