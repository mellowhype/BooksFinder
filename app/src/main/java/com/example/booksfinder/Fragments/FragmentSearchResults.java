package com.example.booksfinder.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booksfinder.BookAdapter;
import com.example.booksfinder.MainActivity;
import com.example.booksfinder.Model.Item;
import com.example.booksfinder.R;

import java.util.List;

public class FragmentSearchResults extends Fragment implements MainActivity.OnActivityDataListener {

    private RecyclerView recyclerViewFragment;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search_results, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialization();
    }

    private void initialization() {
        recyclerViewFragment = (RecyclerView) v.findViewById(R.id.recyclerViewFragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewFragment.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityDataListener(List<Item> volumeInfoList) {
        recyclerViewFragment.smoothScrollToPosition(0);
        BookAdapter adapter = new BookAdapter(getContext(), volumeInfoList);
        recyclerViewFragment.setAdapter(adapter);
    }
}
