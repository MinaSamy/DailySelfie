package bloodstone.dailyselfie.android.adapter;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bloodstone.dailyselfie.android.R;

/**
 * Created by minsamy on 11/11/2015.
 */
public class SelfieAdapter extends RecyclerView.Adapter<SelfieAdapter.ViewHolder> {

    private Cursor mCursor;

    public SelfieAdapter(Cursor cursor){
        this.mCursor=cursor;
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
            String title=mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.TITLE));
            holder.setImageTitle(title);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtTitle;
        public ViewHolder(View view) {
            super(view);
            mTxtTitle=(TextView)view.findViewById(R.id.selfie_title);
        }

        public void setImageTitle(String title){
            mTxtTitle.setText(title);
        }
    }
}
