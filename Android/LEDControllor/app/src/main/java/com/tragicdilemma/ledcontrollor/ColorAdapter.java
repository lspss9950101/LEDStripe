package com.tragicdilemma.ledcontrollor;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ColorAdapter extends BaseAdapter {

    private float sat, val;

    public ColorAdapter(float sat, float val){
        this.sat = sat;
        this.val = val;
    }

    @Override
    public int getCount() {
        return 120;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color_picker, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else viewHolder = (ViewHolder) view.getTag();

        viewHolder.img_grid.setBackgroundColor(Color.HSVToColor(new float[]{i*3, sat, val}));
        WindowManager windowManager = (WindowManager) viewGroup.getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = (int) (windowManager.getDefaultDisplay().getWidth() * 0.75 / 6);
        viewHolder.img_grid.getLayoutParams().height =viewHolder.img_grid.getLayoutParams().width = width;

        return view;
    }

    private class ViewHolder{
        ImageView img_grid;
        public ViewHolder(View view){
            img_grid = view.findViewById(R.id.img_grid);
        }
    }

    public void updateSV(@Nullable Float sat, @Nullable Float val){
        if(sat != null)this.sat = (float)sat;
        if(val != null)this.val = (float)val;
        notifyDataSetChanged();
    }
}
