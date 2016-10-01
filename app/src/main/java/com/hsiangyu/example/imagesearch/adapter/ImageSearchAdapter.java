package com.hsiangyu.example.imagesearch.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hsiangyu.example.imagesearch.R;
import com.hsiangyu.example.imagesearch.pojo.PhotoInfo;

import java.util.ArrayList;

/**
 * Created by HsiangYu on 2016/9/30.
 */
public class ImageSearchAdapter extends RecyclerView.Adapter<ImageSearchAdapter.ImageSearchViewHolder> {
    public ArrayList<PhotoInfo> photoInfoArrayList;
    public boolean isGrid;

    public ImageSearchAdapter() {
        photoInfoArrayList = new ArrayList<>();
        isGrid = false;
    }

    @Override
    public ImageSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem;
        viewItem = LayoutInflater.from(parent.getContext()).inflate(isGrid ? R.layout.image_search_grid_item : R.layout.image_search_list_item, parent, false);
        ImageSearchViewHolder imageSearchViewHolder = new ImageSearchViewHolder(viewItem);
        return imageSearchViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageSearchViewHolder holder, int position) {
        holder.tagText.setText(photoInfoArrayList.get(position).getTags());
        Glide.with(holder.itemView.getContext())
                .load(photoInfoArrayList.get(position).getPreviewURL())
                .into(holder.photoImage);
        if (isGrid) {
            if (position % 2 == 0) {
                holder.photoImage.getLayoutParams().height = dpToPx(120);
            } else {
                holder.photoImage.getLayoutParams().height = dpToPx(180);

            }

        }
    }

    @Override
    public int getItemCount() {
        return photoInfoArrayList.size();
    }

    public class ImageSearchViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImage;
        TextView tagText;

        public ImageSearchViewHolder(View itemView) {
            super(itemView);
            photoImage = (ImageView) itemView.findViewById(R.id.imageview_photo);
            tagText = (TextView) itemView.findViewById(R.id.textview_tags);
        }
    }



    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
