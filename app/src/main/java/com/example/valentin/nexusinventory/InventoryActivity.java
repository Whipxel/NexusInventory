package com.example.valentin.nexusinventory;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class InventoryActivity extends ListActivity
{

    public static ArrayList<Item> list;
    public static ItemAdapter adapter = null;
    public static SQLiteHelper database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ListView listView = getListView();

        //SQLiteDatabase database= DataActivity.sqLiteHelper.getReadableDatabase();

        database = new SQLiteHelper(this, "ItemDB.sqlite", null, 1);
        database.queryData("CREATE TABLE IF NOT EXISTS ITEM (Id INTEGER PRIMARY KEY AUTOINCREMENT, brand VARCHAR, model VARCHAR, description VARCHAR, price VARCHAR, stock VARCHAR, image BLOG)");


        //listView = (ListView) findViewById(R.id.list);
        list = new ArrayList<>();
        adapter = new ItemAdapter(this, R.layout.item_list, list);
        listView.setAdapter(adapter);

        //get data from sqlite
        Cursor cursor = database.getData("SELECT * FROM ITEM");//DataActivity.sqLiteHelper.getData("SELECT * FROM ITEM");
        list.clear();

        while(cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String brand = cursor.getString(1);
            String model = cursor.getString(2);
            String description = cursor.getString(3);
            String price = cursor.getString(4);
            String stock = cursor.getString(5);
            byte[] image = cursor.getBlob(6);

            list.add(new Item(id, brand, model, description, price, stock,image));
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        int itemlist = list.get(position).getId();
        Intent intent = new Intent(this, DataActivity.class);
        Bundle dataBundle = new Bundle();

        //Enpaquetar datos de la lista
        dataBundle.putInt("dataId",list.get(position).getId());
        dataBundle.putByteArray("dataImage",list.get(position).getImage());
        dataBundle.putString("dataBrand",list.get(position).getBrand());
        dataBundle.putString("dataModel",list.get(position).getModel());
        dataBundle.putString("dataDescription",list.get(position).getDescription());
        dataBundle.putString("dataPrice",list.get(position).getPrice());
        dataBundle.putString("dataStock",list.get(position).getStock());

        intent.putExtras(dataBundle);

        //Toast.makeText(getApplicationContext(), Integer.toString(itemlist), Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }

    public void toAddActivity(View view)
    {
        Intent intent = new Intent(this,DataActivity.class);
        startActivity(intent);
    }

}
