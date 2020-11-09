package com.example.booksfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booksfinder.Model.Item;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>  {

    private final List<Item> volumeInfo;
    private final Context mCtx;
    private OnItemClickListener itemClickListener;

    public BookAdapter(Context context, List<Item> volumeInfo) {
        this.volumeInfo = volumeInfo;
        this.mCtx = context;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvTitle, tvAuthor, tvDescription, tvPagesCount;
        RatingBar ratingBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.imgThumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPagesCount = (TextView) itemView.findViewById(R.id.tvPagesCount);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }

        void bind(final Item item) {

            ratingBar.setRating(item.getVolumeInfo().getAverageRating());

            if (item.getVolumeInfo().getTitle() == null) {
                tvTitle.setText(R.string.noTitle);
            } else {
                tvTitle.setText(item.getVolumeInfo().getTitle());
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

            if (item.getVolumeInfo().getPageCount() == null) {
                tvPagesCount.setText(R.string.noPagesCount);
            } else {
                tvPagesCount.setText(item.getVolumeInfo().getPageCount() + " pages");
            }

            try {
                Glide.with(mCtx).load(item.getVolumeInfo().getImageLinks().getSmallThumbnail()).into(imgThumbnail);
            } catch (NullPointerException e) {
                Glide.with(mCtx).load("https://image.makewebeasy.net/noimage.png").into(imgThumbnail);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener = (OnItemClickListener) mCtx;
                    itemClickListener.onItemClickListener(item);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(Item item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(volumeInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return volumeInfo == null ? 0 : volumeInfo.size();
    }
}
