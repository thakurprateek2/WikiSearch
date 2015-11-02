package acompli.wikisearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by thakurprateek on 30-10-2015.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    public ImageListAdapter(Context context, List<Page> pages) {
        this.context = context;
        this.pages = pages;
    }

    private Context context;
    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    private List<Page> pages;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvImageTitle;
        public ImageView ivIamge;
        public ViewHolder(View v) {
            super(v);
            tvImageTitle = (TextView) v.findViewById(R.id.tvImageTitle);
            ivIamge = (ImageView) v.findViewById(R.id.ivIamge);
        }
    }

    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_element_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Page currentPage = pages.get(position);
        holder.tvImageTitle.setText(currentPage.getTitle());

        Glide.with(getContext())
                .load(currentPage.getThumbnail() != null ? currentPage.getThumbnail().getSource() : null)
                .placeholder(R.drawable.file_picture)
                .crossFade()
                .into(holder.ivIamge);

    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public void removeAt(int position) {
        pages.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pages.size());
    }

    public void addAt(int position, Page page) {
        pages.add(position, page);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, pages.size());
    }

}