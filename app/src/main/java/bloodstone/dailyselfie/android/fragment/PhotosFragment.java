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
    private Cursor mCursor=null;

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
        mCursor=new Cursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean move(int offset) {
                return false;
            }

            @Override
            public boolean moveToPosition(int position) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String columnName) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int columnIndex) {
                return new byte[0];
            }

            @Override
            public String getString(int columnIndex) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

            }

            @Override
            public short getShort(int columnIndex) {
                return 0;
            }

            @Override
            public int getInt(int columnIndex) {
                return 0;
            }

            @Override
            public long getLong(int columnIndex) {
                return 0;
            }

            @Override
            public float getFloat(int columnIndex) {
                return 0;
            }

            @Override
            public double getDouble(int columnIndex) {
                return 0;
            }

            @Override
            public int getType(int columnIndex) {
                return 0;
            }

            @Override
            public boolean isNull(int columnIndex) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver observer) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver observer) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void setNotificationUri(ContentResolver cr, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle extras) {
                return null;
            }
        };
        mAdapter=new SelfieAdapter(mCursor);
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
        mCursor=data;
        mAdapter=new SelfieAdapter(mCursor);
        mRecyclerView.setAdapter(mAdapter);

        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });*/

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
