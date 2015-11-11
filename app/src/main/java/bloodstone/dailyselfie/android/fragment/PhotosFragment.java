package bloodstone.dailyselfie.android.fragment;


import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.adapter.SelfieAdapter;
import bloodstone.dailyselfie.android.utils.PhotoUtils;

/**
 * Created by minsamy on 11/4/2015.
 */
public class PhotosFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    static private final String ARG_PHOTO_TYPE = "photo_type";

    private Cursor mImageFileCursor;
    static private final int IMAGE_FILE_LOADER_ID = 0;

    //widgets
    private RecyclerView mRecyclerView;
    private SelfieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    

    static public PhotosFragment newInstance(int photosType) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PHOTO_TYPE, photosType);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main,container,false);
        mRecyclerView=(RecyclerView)v.findViewById(R.id.selfies_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter=new SelfieAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        getLoaderManager().initLoader(IMAGE_FILE_LOADER_ID, null, this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = PhotoUtils.getImageFileCursorLoader(getActivity());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //mAdapter=new SelfieAdapter(mCursor);
        //mRecyclerView.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();

        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });*/

        mAdapter.swapCursor(data);
        Log.e("FINISHED", String.valueOf(data.getCount()));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //swap cursor adapter
    }

    public void refresh(){
        mAdapter.notifyDataSetChanged();
    }
}
