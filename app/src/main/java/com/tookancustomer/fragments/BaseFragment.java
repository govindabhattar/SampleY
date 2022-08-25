package com.tookancustomer.fragments;

import androidx.fragment.app.Fragment;

import com.tookancustomer.utility.FragmentBackPressed.BackPressImpl;
import com.tookancustomer.utility.FragmentBackPressed.OnBackPressListener;

/**
 * Created by Shweta on 12/22/17.
 */

public class BaseFragment extends Fragment implements OnBackPressListener{

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImpl(this).onBackPressed();
    }
}
