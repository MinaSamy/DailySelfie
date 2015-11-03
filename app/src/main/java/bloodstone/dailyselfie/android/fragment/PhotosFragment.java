package bloodstone.dailyselfie.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by minsamy on 11/4/2015.
 */
public class PhotosFragment extends Fragment {

    static public final int PHOTO_TYPE_NORMAL_SELFIE=0;
    static public final int PHOTO_TYPE_EFFECTS_SELFIE=1;
    static private final String ARG_PHOTO_TYPE="photo_type";


    static public PhotosFragment newInstance(int photosType){
        PhotosFragment fragment=new PhotosFragment();
        Bundle b=new Bundle();
        b.putInt(ARG_PHOTO_TYPE,photosType);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
