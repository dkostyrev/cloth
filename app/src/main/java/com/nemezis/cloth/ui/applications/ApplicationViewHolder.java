package com.nemezis.cloth.ui.applications;

import android.view.View;
import android.widget.TextView;

import com.nemezis.cloth.R;
import com.nemezis.cloth.model.Application;
import com.nemezis.cloth.ui.adapter.BaseViewHolder;

import butterknife.Bind;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class ApplicationViewHolder extends BaseViewHolder<Application> {

    @Bind(R.id.applicationNameTextView)
    TextView applicationNameTextView;

    public ApplicationViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bind(Application item) {
        applicationNameTextView.setText(item.getName());
    }
}
