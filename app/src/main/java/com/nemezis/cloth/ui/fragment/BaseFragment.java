package com.nemezis.cloth.ui.fragment;

import android.support.v4.app.Fragment;

import com.nemezis.cloth.ui.activity.BaseActivity;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public abstract class BaseFragment<T> extends Fragment {

	protected T getActivityComponent() {
		return ((BaseActivity<T>) getActivity()).getComponent();
	}
}
