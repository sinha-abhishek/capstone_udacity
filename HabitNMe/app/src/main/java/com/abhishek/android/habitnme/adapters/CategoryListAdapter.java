package com.abhishek.android.habitnme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhishek.android.habitnme.BaseApplication;
import com.abhishek.android.habitnme.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoryListAdapter extends BaseAdapter {
    @Inject
    protected Context context;

    private LayoutInflater layoutInflater;

    public CategoryListAdapter() {
        BaseApplication.getDataComponent().inject(this);
        layoutInflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        String[] items = context.getResources().getStringArray(R.array.category_array);
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return context.getResources().getStringArray(R.array.category_array)[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryViewHolder viewHolder ;
        if (convertView != null) {
            viewHolder = (CategoryViewHolder)convertView.getTag();
        } else {
            convertView = layoutInflater.inflate(R.layout.choose_list_item, null);
            viewHolder = new CategoryViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        Integer img = context.getResources().obtainTypedArray(R.array.category_icons).getResourceId(position, -1);
        viewHolder.imageView.setImageResource(img);
        viewHolder.imageView.setContentDescription(context.getResources().getStringArray(R.array.category_array)[position]);
        viewHolder.textView.setText(context.getResources().getStringArray(R.array.category_array)[position]);
        return convertView;
    }

    static class CategoryViewHolder {
        @BindView(R.id.category_image)
        ImageView imageView;
        @BindView(R.id.categroy_text)
        TextView textView;

        public CategoryViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
