package com.robotoole.flickrlist.app.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robotoole.flickrlist.R;
import com.robotoole.flickrlist.app.event.EventManager;
import com.robotoole.flickrlist.app.event.RecyclerClickEvent;
import com.robotoole.flickrlist.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * RecyclerView adapter to load the Flickr pictures in a
 * list of card views.
 * Created by robert on 2/19/15.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private final RecyclerView mListView;

    private List<Picture> pictures;

    /**
     * Constructor for PictureAdapter.
     *
     * @param listView ViewGroup used for layout params and children count
     * @param pictures a list of Pictures
     */
    public PictureAdapter(
            @NonNull List<Picture> pictures, RecyclerView listView) {
        this.pictures = pictures;
        mListView = listView;
    }

    /**
     * Create a newInstance of the PictureAdapter with blank data.
     *
     * @param listView the ViewGroup this adapter attaches to
     * @return a PictureAdapter with no data
     */
    public static PictureAdapter newInstance(RecyclerView listView) {
        return new PictureAdapter(new ArrayList<Picture>(), listView);
    }

    public void updatePictures(List<Picture> pictures) {
        this.pictures.clear();
        this.pictures.addAll(pictures);
        notifyDataSetChanged();
        mListView.smoothScrollToPosition(0);
        Log.d("adapter", "adapter notified!");
    }

    /**
     * Get total amount of pictures.
     *
     * @return picture count.
     */
    @Override
    public int getItemCount() {
        return pictures.size();
    }

    /**
     * Get the desired {@link Picture} based off its position in a list.
     *
     * @param position the position in the list
     * @return the desired {@link Picture}
     */
    public Picture getPictureData(int position) {
        return pictures.get(position);
    }

    /**
     * Create a new view holder.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return ViewHolder.newInstance(view);
    }

    /**
     * Bind data to a view holder and populate the view.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picture picture = getPictureData(position);
        setPictureViewData(holder, picture);
    }

    /**
     * Set this ViewHolders underlying Picture data.
     *
     * @param holder  {@link Picture} {@link ViewHolder}
     * @param picture the {@link Picture} data object
     */
    private void setPictureViewData(ViewHolder holder, Picture picture) {
        holder.title.setText(picture.getTitle());

        holder.author.setText(Html.fromHtml("<a href=\""
                + picture.getAuthor_id()
                + "\">" + mListView.getContext().getString(R.string.by,
                picture.getAuthor()) + "</a>"));
        holder.author.setMovementMethod(LinkMovementMethod.getInstance());

        Picasso.with(mListView.getContext())
                .load(picture.getLink())
                .into(holder.imageView);
    }

    /**
     * ViewHolder for the {@link Picture} and title.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @InjectView(R.id.fragment_list_item_image)
        public ImageView imageView;
        @InjectView(R.id.fragment_list_item_title)
        public TextView title;
        @InjectView(R.id.fragment_list_item_author)
        public TextView author;

        /**
         * Create a new Instance of the ViewHolder.
         *
         * @param view inflated in {@link #onCreateViewHolder}
         * @return an Order ViewHolder instance
         */
        public static ViewHolder newInstance(View view) {
            return new ViewHolder(view);
        }

        /**
         * Constructor for the holder.
         *
         * @param view the inflated view
         */

        private ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(this);
        }

        /**
         * Handle the onClick of the item and post the event to
         * whoever is listening.
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            view.setTag(getPosition());
            EventManager.post(new RecyclerClickEvent(view));
        }
    }
}
