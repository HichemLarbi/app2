package com.example.hlarbi.firebaseappbeta1.AccountActivity.ViewClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hlarbi.firebaseappbeta1.R;

public class GridAdapter extends BaseAdapter {
    Context gcontext;
    private final int[] IMAGES ;

    private final String[] NAMES ;
    private final String[] NUMBERS;
    private final String[] UNITIES;

    View gview;
    LayoutInflater glayoutInflater;

    public GridAdapter(Context context, int[] images, String[] names, String[] numbers, String[] unities) {
        this.IMAGES = images;
        this.NAMES = names;
        this.NUMBERS = numbers;
        this.UNITIES = unities;
    }


    @Override
    public int getCount() {
        return NAMES.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        glayoutInflater = (LayoutInflater) gcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null){
    gview = new View(gcontext);
    gview = glayoutInflater.inflate(R.layout.item_sample_gridview, null);
    ImageView g_icon = (ImageView)gview.findViewById(R.id.imageView_item_grid);
    TextView g_subject  = (TextView)gview.findViewById(R.id.textView_subject_item_grid);
    TextView g_num = (TextView)gview.findViewById(R.id.textView_number_item);
    TextView g_uni = (TextView)gview.findViewById(R.id.textView_unity_item);

    g_icon.setImageResource(IMAGES[position]);
    g_subject.setText(NAMES[position]);
    g_num.setText(NUMBERS[position]);
    g_uni.setText(UNITIES[position]);

}
return gview;
    }
}
