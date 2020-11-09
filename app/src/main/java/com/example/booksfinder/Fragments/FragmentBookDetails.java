package com.example.booksfinder.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.booksfinder.MainActivity;
import com.example.booksfinder.Model.Item;
import com.example.booksfinder.R;

public class FragmentBookDetails extends DialogFragment implements View.OnClickListener {

    private View v;
    private RatingBar ratingBar;
    private TextView tvTitle, tvSubtitle, tvAuthor, tvDescription, tvPublishDate, tvPagesCount;
    private ImageView imgThumbnail, imgButtonOpenStore, imgButtonClose;
    private Item item;

    private void initialization() {
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
        tvAuthor = (TextView) v.findViewById(R.id.tvAuthor);
        tvDescription = (TextView) v.findViewById(R.id.tvDescription);
        tvPublishDate = (TextView) v.findViewById(R.id.tvPublishDate);
        tvPagesCount = (TextView) v.findViewById(R.id.tvPagesCount);
        imgThumbnail = (ImageView) v.findViewById(R.id.imgThumbnail);
        imgButtonClose = (ImageView) v.findViewById(R.id.imgButtonClose);
        imgButtonOpenStore = (ImageView) v.findViewById(R.id.imgButtonOpenStore);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_book_details, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            item = getArguments().getParcelable(MainActivity.ITEM_KEY);
        }
        initialization();
    }

    @Override
    public void onStart() {
        super.onStart();
        ratingBar.setRating(item.getVolumeInfo().getAverageRating());

        if (item.getVolumeInfo().getTitle() == null) {
            tvTitle.setText(R.string.noTitle);
        } else {
            tvTitle.setText(item.getVolumeInfo().getTitle());
        }

        if(item.getVolumeInfo().getSubtitle() == null) {
            tvSubtitle.setText(R.string.noSubtitle);
        } else {
            tvSubtitle.setText(item.getVolumeInfo().getSubtitle());
        }

        if (item.getVolumeInfo().getAuthors() == null) {
            tvAuthor.setText(R.string.noAuthor);
        } else {
            tvAuthor.setText(item.getVolumeInfo().getAuthors().get(0));
        }

        if (item.getVolumeInfo().getDescription() == null) {
            tvDescription.setText(R.string.noDescription);
        } else {
            tvDescription.setText(item.getVolumeInfo().getDescription());
        }

        if (item.getVolumeInfo().getPublishedDate() == null) {
            tvPublishDate.setText(R.string.noPublishDate);
        } else {
            tvPublishDate.setText(item.getVolumeInfo().getPublishedDate());
        }

        if (item.getVolumeInfo().getPageCount() == null) {
            tvPagesCount.setText(R.string.noPagesCount);
        } else {
            tvPagesCount.setText(item.getVolumeInfo().getPageCount() + " pages");
        }

        try {
            Glide.with(getActivity()).load(item.getVolumeInfo().getImageLinks().getSmallThumbnail()).into(imgThumbnail);
        } catch (NullPointerException e) {
            Glide.with(getActivity()).load("https://image.makewebeasy.net/noimage.png").into(imgThumbnail);
        }

        imgButtonOpenStore.setOnClickListener(this);
        imgButtonClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgButtonOpenStore:
                if (item.getVolumeInfo().getInfoLink() == null) {
                    Toast.makeText(getActivity(), R.string.noLink, Toast.LENGTH_SHORT).show();
                } else {
                    Uri uri = Uri.parse(item.getVolumeInfo().getInfoLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            case R.id.imgButtonClose:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
                }
        }
    }
}
