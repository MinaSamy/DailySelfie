package bloodstone.dailyselfie.android.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.utils.ImageLoader;

/**
 * Created by minsamy on 11/11/2015.
 */
public class SelfieAdapter extends RecyclerView.Adapter<SelfieAdapter.ViewHolder> {

    private Cursor mCursor;

    //recyclerview adapter does not have the notifyDataSetInvalidated() method
    //so we use this boolean to track the data status
    private boolean mIsDataValid = false;
    private SelfieDataSetObserver mDataSetObserver;
    private ImageLoader mImageLoader;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public SelfieAdapter(Cursor cursor) {
        this.mCursor = cursor;
        mIsDataValid = mCursor != null;
        mDataSetObserver = new SelfieDataSetObserver();
        if (cursor != null) {
            //mDataSetObserver=new SelfieDataSetObserver();
            mCursor.registerDataSetObserver(mDataSetObserver);
        }

        mImageLoader = new ImageLoader();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_selfie_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            int imageId = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media._ID));
            holder.setImage(mCursor);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null && mIsDataValid) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            //unregister data observer
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            //register the observer
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
                mIsDataValid = true;
                notifyDataSetChanged();
            }
        } else {
            //the new cursor is null, unrigester the dataobserver and invalidate the data set
            mIsDataValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTxtTitle;
        private ImageView mImg;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mTxtTitle = (TextView) view.findViewById(R.id.selfie_title);
            mImg = (ImageView) view.findViewById(R.id.selfie_image);
            setIsRecyclable(true);


        }

        public Context getContext() {
            return mTxtTitle.getContext();
        }

        public void setImage(Cursor cursor) {
            mImageLoader.displayImage(mCursor, this.mImg, this.mTxtTitle);
        }

        /*public void setTag(){
            this.getItemId()
        }*/


        @Override
        public void onClick(View v) {
            if (mCursor.moveToPosition(getAdapterPosition())) {
                int imageId = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media._ID));
                if (mOnRecyclerViewItemClickListener != null) {
                    mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(imageId);
                }
            }

        }
    }


    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(int itemId);
    }


    private class SelfieDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            mIsDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mIsDataValid = false;
            notifyDataSetChanged();
        }
    }
}
