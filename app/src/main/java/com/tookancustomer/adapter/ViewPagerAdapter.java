package com.tookancustomer.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tookancustomer.fragments.NLevelWorkFlowFragment;
import com.tookancustomer.modules.merchantCatalog.fragment.MerchantCatalogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shwetaaggarwal on 28/06/17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void removeAllFragment() {
        mFragmentList.clear();
        mFragmentTitleList.clear();
    }

    public NLevelWorkFlowFragment getFragment(int pos) {
        return (NLevelWorkFlowFragment) mFragmentList.get(pos);
    }

    public MerchantCatalogFragment getMerchantCatalogFragment(int pos) {
        return (MerchantCatalogFragment) mFragmentList.get(pos);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}