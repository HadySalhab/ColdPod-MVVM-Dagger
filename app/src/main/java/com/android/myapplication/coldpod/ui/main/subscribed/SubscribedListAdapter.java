package com.android.myapplication.coldpod.ui.main.subscribed;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.persistence.PodcastEntry;
import com.android.myapplication.coldpod.databinding.SubscribedPodcastsListItemBinding;

import static com.android.myapplication.coldpod.utils.Constants.DELETE;
import static com.android.myapplication.coldpod.utils.Constants.GROUP_ID_DELETE;
import static com.android.myapplication.coldpod.utils.Constants.ORDER_DELETE;

public class SubscribedListAdapter extends ListAdapter<PodcastEntry,SubscribedListAdapter.SubscribedListViewHolder>{
    private static final String TAG = "SubscribedListAdapter";

    interface Listener {
        void onItemClick(PodcastEntry podcastEntry);
        void onDeleteClick(PodcastEntry podcastEntry);
    }

    private final SubscribedListAdapter.Listener mListener;


    protected SubscribedListAdapter(@NonNull DiffUtil.ItemCallback<PodcastEntry> diffCallback, Listener listener) {
        super(diffCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public SubscribedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SubscribedPodcastsListItemBinding binding = SubscribedPodcastsListItemBinding.inflate(layoutInflater,parent,false);
        return new SubscribedListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribedListViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        PodcastEntry podcastEntry = getItem(position);
        holder.bind(podcastEntry);
    }


    public  class SubscribedListViewHolder extends RecyclerView.ViewHolder implements   View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private final SubscribedPodcastsListItemBinding binding;
        public SubscribedListViewHolder(@NonNull SubscribedPodcastsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnCreateContextMenuListener(this);
        }
        public void bind(PodcastEntry podcastEntry){
            Log.d(TAG, "bind: "+podcastEntry);
            binding.setPodcastEntry(podcastEntry);
            binding.setViewHolder(this);
            binding.executePendingBindings();
        }
        public void onItemClick(PodcastEntry podcastEntry){
            mListener.onItemClick(podcastEntry);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getTitle().toString()) {
                case DELETE:
                    int adapterPosition = item.getItemId();
                    PodcastEntry podcastEntry = getItem(adapterPosition);
                    mListener.onDeleteClick(podcastEntry);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            int adapterPosition = getAdapterPosition();
            // Set the itemId to adapterPosition to retrieve podcastEntry later
            MenuItem item = menu.add(GROUP_ID_DELETE, adapterPosition, ORDER_DELETE,
                    v.getContext().getString(R.string.action_delete));
            // Set OnMenuItemClickListener on the menu item
            item.setOnMenuItemClickListener(this);

        }
    }
}

