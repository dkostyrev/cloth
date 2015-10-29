package com.nemezis.cloth.ui.applications;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nemezis.cloth.R;
import com.nemezis.cloth.di.component.MainActivityComponent;
import com.nemezis.cloth.model.Application;
import com.nemezis.cloth.presenter.ApplicationsPresenter;
import com.nemezis.cloth.ui.adapter.BaseAdapter;
import com.nemezis.cloth.ui.fragment.BaseFragment;
import com.nemezis.cloth.view.ApplicationsView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
public class ApplicationsFragment extends BaseFragment<MainActivityComponent> implements ApplicationsView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    ApplicationsPresenter applicationsPresenter;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private BaseAdapter<ApplicationViewHolder, Application> applicationsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.applications_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        applicationsAdapter = new BaseAdapter<>(ApplicationViewHolder.class, R.layout.application_item);
        recyclerView.setAdapter(applicationsAdapter);

        applicationsPresenter.attachView(this);
    }

    @Override public void displayApplications(List<Application> applications) {
        applicationsAdapter.setItems(applications);
    }

    @Override public void onRefresh() {
        applicationsPresenter.refreshApplications();
    }

    @Override
    public void onDestroyView() {
        applicationsPresenter.detachView();
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
}
