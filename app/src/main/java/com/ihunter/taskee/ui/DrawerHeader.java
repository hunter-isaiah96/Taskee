package com.ihunter.taskee.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ihunter.taskee.R;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.model.BaseDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerHeader extends BaseDrawerItem {

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.drawer_header;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public ViewHolderFactory getFactory() {
        return new ItemFactory();
    }

    private class ItemFactory implements ViewHolderFactory<ViewHolder> {
        @Override
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.header_image)
        AppCompatImageView imageView;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Glide.with(view.getContext()).load(R.drawable.icon).into(imageView);
        }
    }

}