package com.example.amit.android_topprchallenge;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.example.amit.android_topprchallenge.Database.EventsContract;
import com.example.amit.android_topprchallenge.Database.EventsLoader;


/**
 * Created by amit on 25-09-2016.
 */
public class EventsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public Context mContext;
    final String LOG_TAG = EventsFragment.class.getSimpleName();
    RecyclerView mRecyclerView;

    public onItemSelectedListener mListener;

    public interface onItemSelectedListener {
        void onItemSelected(long itemId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
        //instanceof checks whether activity is subtype of onItemSelectedListener,which is true since
        // activity implements onItemSelectedListener interface. This exercise assures that required
        //interface is implemented by parent activity for receiving callbacks to different methods.
        if (activity instanceof onItemSelectedListener) {
            //the mListener member holds a reference to activity's implementation of onItemSelectedListener,
            // so that this fragment can share events with the activity by calling methods defined by
            // OnArticleSelectedListener interface
            mListener = (onItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement EventsFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_events_list, container, false);


        if (savedInstanceState == null) {
            refresh();
        }
        return root;
    }

    private void refresh() {
        getActivity().startService(new Intent(mContext, UpdaterService.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        mContext.registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    public void onStop() {
        super.onStop();
        mContext.unregisterReceiver(mRefreshingReceiver);
    }

    private boolean mIsRefreshing = false;

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                    mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                    if (mIsRefreshing){
                        refresh();
                    }

                }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, EventsLoader.newAllArticlesInstance(mContext).toString());
        return  EventsLoader.newAllArticlesInstance(mContext);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int count = cursor.getCount();
        Log.v(LOG_TAG,Integer.toString(count));

       Adapter adapter = new Adapter(cursor);

        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(new Adapter(cursor));
  //      int columnCount = getResources().getInteger(R.integer.list_column_count);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private Cursor mCursor;
        private int selectedPos = 0;

        public Adapter(Cursor cursor) {
            mCursor = cursor;
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(EventsLoader.Query._ID);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.list_item_article,parent,false);
            final ViewHolder vh = new ViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Uri uri = EventsContract.Events.buildItemUri(getItemId(vh.getAdapterPosition()));
                    long itemId = getItemId(vh.getAdapterPosition());

                    Log.v(LOG_TAG,"event when clicked "+itemId);
                    mListener.onItemSelected(itemId);

                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
      //      String thumbnailString ="";
            mCursor.moveToPosition(position);
            /*
            holder.itemView.setTag(position);
            holder.itemView.setSelected(selectedPos == position);
            */
            holder.titleView.setText(mCursor.getString(EventsLoader.Query.NAME));
            holder.categoryView.setText(mCursor.getString(EventsLoader.Query.CATEGORY));

            String thumbnailString = mCursor.getString(EventsLoader.Query.IMAGE_URL);

            if (thumbnailString != null){

                try {
                    Picasso.with(mContext).load(thumbnailString).into(holder.thumbnailView);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }


        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailView;
        public TextView titleView;
        public TextView categoryView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (ImageView) view.findViewById(R.id.image);
            titleView = (TextView) view.findViewById(R.id.titleView);
            categoryView = (TextView) view.findViewById(R.id.categoryView);
        }
    }
}
