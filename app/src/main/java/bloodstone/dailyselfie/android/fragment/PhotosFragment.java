package bloodstone.dailyselfie.android.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.SelfieDetailsActivity;
import bloodstone.dailyselfie.android.adapter.SelfieAdapter;
import bloodstone.dailyselfie.android.utils.PhotoUtils;

/**
 * Created by minsamy on 11/4/2015.
 */
public class PhotosFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SelfieAdapter.OnRecyclerViewItemClickListener {


    static private final String ARG_PHOTO_TYPE = "photo_type";
    static private final String ARG_USER_ID = "user_id";

    private Cursor mImageFileCursor;
    static private final int IMAGE_FILE_LOADER_ID = 100;

    //widgets
    private RecyclerView mRecyclerView;
    private SelfieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int mSelfieType;
    private String mUserId;

    static public PhotosFragment newInstance(int photosType, String userId) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PHOTO_TYPE, photosType);
        b.putString(ARG_USER_ID, userId);
        fragment.setArguments(b);
        return fragment;
    }


    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.selfies_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SelfieAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        if (getArguments() != null) {
            mSelfieType = getArguments().getInt(ARG_PHOTO_TYPE);
            mUserId = getArguments().getString(ARG_USER_ID);
        }


        if(getLoaderManager()!=null){
            getLoaderManager().initLoader(IMAGE_FILE_LOADER_ID, null, this);
        }


        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id==IMAGE_FILE_LOADER_ID){
            CursorLoader loader = PhotoUtils.getImageFileCursorLoader(getActivity(), mUserId, mSelfieType);
            return loader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        //mAdapter.swapCursor(null);
        mAdapter.changeCursor(null);
    }

    @Override
    public void onRecyclerViewItemClick(long itemId) {
        //Snackbar.make(mRecyclerView, String.valueOf(itemId), Snackbar.LENGTH_LONG).show();
        Intent intent = SelfieDetailsActivity.makeIntent(getActivity(), itemId, mSelfieType, mUserId);
        startActivity(intent);
    }
}
