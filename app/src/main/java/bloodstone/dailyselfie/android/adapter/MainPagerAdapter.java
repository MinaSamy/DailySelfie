package bloodstone.dailyselfie.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.fragment.PhotosFragment;
import bloodstone.dailyselfie.android.utils.CameraUtils;

/**
 * Created by minsamy on 11/3/2015.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return PhotosFragment.newInstance(CameraUtils.PHOTO_TYPE_NORMAL_SELFIE);
        }
        return PhotosFragment.newInstance(CameraUtils.PHOTO_TYPE_EFFECTS_SELFIE);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.selfies);
        }
        return mContext.getString(R.string.selfies_effects);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
