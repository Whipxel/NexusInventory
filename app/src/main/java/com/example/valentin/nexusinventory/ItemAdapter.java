package com.example.valentin.nexusinventory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Valentin on 17/01/2017.
 */

public class ItemAdapter extends BaseAdapter
{
    private Context context;
    private int layout;
    private ArrayList<Item> itemList;

    public ItemAdapter(Context context, int layout, ArrayList<Item> itemList)
    {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
    }

    @Override
    public int getCount()
    {
        return itemList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public class ViewHolder
    {
        ImageView imageView;
        TextView txtBrand, txtModel, txtStock;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtBrand = (TextView) row.findViewById(R.id.item_brand);
            holder.txtModel = (TextView) row.findViewById(R.id.item_model);
            holder.txtStock = (TextView) row.findViewById(R.id.stock_value);
            holder.imageView = (ImageView) row.findViewById(R.id.item_list_image);
            row.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }

        Item item = itemList.get(position);

        holder.txtBrand.setText(item.getBrand());
        holder.txtModel.setText(item.getModel());
        holder.txtStock.setText(item.getStock());

        byte[] itemImage = item.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage, 0, itemImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
