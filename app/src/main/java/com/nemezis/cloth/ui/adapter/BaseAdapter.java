package com.nemezis.cloth.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class BaseAdapter<BaseVH extends BaseViewHolder<ItemType>, ItemType> extends RecyclerView.Adapter<BaseVH> {

    private List<ItemType> items;
    private LayoutInflater layoutInflater;
    private @LayoutRes int layoutRes;
    private Constructor<BaseVH> constructor;

    public BaseAdapter(@NonNull Class<BaseVH> viewHolderClass, @LayoutRes int layoutRes) {
        this(viewHolderClass, layoutRes, Collections.<ItemType>emptyList());
    }

    public BaseAdapter(@NonNull Class<BaseVH> viewHolderClass, @LayoutRes int layoutRes, @NonNull List<ItemType> items) {
        try {
            this.constructor = viewHolderClass.getConstructor(View.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(viewHolderClass.getSimpleName() + " must declare single argument (View view) constructor");
        }
        this.items = items;
    }

    public void setItems(@NonNull List<ItemType> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        layoutInflater = LayoutInflater.from(recyclerView.getContext());
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Override public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            return constructor.newInstance(layoutInflater.inflate(layoutRes, parent, false));
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to create an instance of " + constructor.getDeclaringClass().getSimpleName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to create an instance of " + constructor.getDeclaringClass().getSimpleName());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to create an instance of " + constructor.getDeclaringClass().getSimpleName());
        }
    }

    @Override public void onBindViewHolder(BaseVH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override public int getItemCount() {
        return items.size();
    }
}
