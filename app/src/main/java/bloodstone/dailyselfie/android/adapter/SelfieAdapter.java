package bloodstone.dailyselfie.android.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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
    private boolean mIsDataValid=false;
    private SelfieDataSetObserver mDataSetObserver;
    private ImageLoader mImageLoader;

    public SelfieAdapter(Cursor cursor){
        this.mCursor=cursor;
        mIsDataValid=mCursor!=null;
        mDataSetObserver=new SelfieDataSetObserver();
        if(cursor!=null){
            //mDataSetObserver=new SelfieDataSetObserver();
            mCursor.registerDataSetObserver(mDataSetObserver);
        }

        mImageLoader=new ImageLoader();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_selfie_item,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mCursor.moveToPosition(position)){

            //holder.setImageTitle(title);
            holder.setImage(mCursor);
            /*int imageId=mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media._ID));

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize =4;

            Bitmap map=MediaStore.Images.Thumbnails.getThumbnail(holder.getContext().getContentResolver(),
                    imageId,MediaStore.Images.Thumbnails.MINI_KIND,bmOptions);

            //get the thumbnail
            //holder.getContext().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI)

            holder.setImage(map);*/
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor!=null &&mIsDataValid){
            return mCursor.getCount();
        }
        else{
            return 0;
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public Cursor swapCursor(Cursor newCursor){
        if(newCursor==mCursor){
            return null;
        }
        Cursor oldCursor=mCursor;
        if(oldCursor!=null &&mDataSetObserver!=null){
            //unregister data observer
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor=newCursor;
        if(mCursor!=null){
            //register the observer
            if(mDataSetObserver!=null){
                mCursor.registerDataSetObserver(mDataSetObserver);
                mIsDataValid=true;
                notifyDataSetChanged();
            }
        }else{
            //the new cursor is null, unrigester the dataobserver and invalidate the data set
            mIsDataValid=false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtTitle;
        private ImageView mImg;
        public ViewHolder(View view) {
            super(view);
            mTxtTitle=(TextView)view.findViewById(R.id.selfie_title);
            mImg=(ImageView)view.findViewById(R.id.selfie_image);
        }

        public Context getContext(){
            return mTxtTitle.getContext();
        }

        public void setImage(Cursor cursor){
            mImageLoader.displayImage(mCursor,this.mImg,this.mTxtTitle);
        }

        public void setImageTitle(String title){
            mTxtTitle.setText(title);
        }
    }


    private class SelfieDataSetObserver extends DataSetObserver{

        @Override
        public void onChanged() {
            super.onChanged();
            mIsDataValid=true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mIsDataValid=false;
            notifyDataSetChanged();
        }
    }
}
