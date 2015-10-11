package com.nemezis.cloth.ui.fragment;

import android.support.v4.app.Fragment;

import com.nemezis.cloth.ui.activity.BaseActivity;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public abstract class BaseFragment<Component> extends Fragment {

	protected Component getActivityComponent() {
		return ((BaseActivity<Component>) getActivity()).getComponent();
	}
}
