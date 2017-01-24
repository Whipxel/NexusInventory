package com.example.valentin.nexusinventory;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DataActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int REQUEST_CODE_GALLERY = 999;
    ImageView mImageView;
    int itemId=0;
    byte[] image;
    String brand, model, description, price, stock;
    EditText edtBrand, edtModel, edtDescription, edtPrice, edtStock;
    Button btnChoose, btnSave, btnPhoto, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Bundle takeBundleData = getIntent().getExtras();

        //if are data in the bundle
        if(takeBundleData!=null)
        {
            itemId = takeBundleData.getInt("dataId");
            brand = takeBundleData.getString("dataBrand");
            model = takeBundleData.getString("dataModel");
            description = takeBundleData.getString("dataDescription");
            price = takeBundleData.getString("dataPrice");
            stock = takeBundleData.getString("dataStock");
            image = takeBundleData.getByteArray("dataImage");

            init();

            Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);

            mImageView.setImageBitmap(bitmap);//(Bitmap.createScaledBitmap(bitmap, mImageView.getWidth(),mImageView.getHeight(),false));
            edtBrand.setText(brand);
            edtModel.setText(model);
            edtDescription.setText(description);
            edtPrice.setText(price);
            edtStock.setText(stock);
        }
        else
        {
            init();
        }

        btnChoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ActivityCompat.requestPermissions(
                        DataActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY
                );
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //These fields have to be filled to store something
                if(edtBrand.getText().toString().matches("") && edtModel.getText().toString().matches("") && edtStock.getText().toString().matches(""))
                {
                    Toast.makeText(getApplicationContext(), "Error, you need to fill all the required data to store an item", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(itemId == 0)
                    {
                        try
                        {
                            InventoryActivity.database.insertData(
                                    edtBrand.getText().toString().trim(),
                                    edtModel.getText().toString().trim(),
                                    edtDescription.getText().toString().trim(),
                                    edtPrice.getText().toString().trim(),
                                    edtStock.getText().toString().trim(),
                                    imageViewToByte(mImageView)

                            );

                            Toast.makeText(getApplicationContext(), "Added item successfully!", Toast.LENGTH_SHORT).show();

                            //Update the ListView
                            refreshListView();

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        try
                        {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("brand", edtBrand.getText().toString());
                            contentValues.put("model", edtModel.getText().toString());
                            contentValues.put("description", edtDescription.getText().toString());
                            contentValues.put("price", edtPrice.getText().toString());
                            contentValues.put("stock", edtStock.getText().toString());
                            contentValues.put("image", imageViewToByte(mImageView));
                            InventoryActivity.database.update(Integer.toString(itemId), contentValues);

                            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();

                            //Update the ListView
                            refreshListView();

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }

        });

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int id = itemId;

                if(InventoryActivity.database.delete(Integer.toString(id)) > 0)
                {
                    Toast.makeText(getApplicationContext(), "Item deleted!!", Toast.LENGTH_SHORT).show();
                    refreshListView();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No hay dato que coincida con id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("brand",edtBrand.getText().toString());
        outState.putString("model",edtModel.getText().toString());
        outState.putString("description",edtDescription.getText().toString());
        outState.putString("price",edtPrice.getText().toString());
        outState.putString("stock",edtStock.getText().toString());
        outState.putByteArray("image",imageViewToByte(mImageView));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState)
    {
        super.onRestoreInstanceState(savedState);
        edtBrand.setText(savedState.getString("brand"));
        edtModel.setText(savedState.getString("model"));
        edtDescription.setText(savedState.getString("description"));
        edtPrice.setText(savedState.getString("price"));
        edtStock.setText(savedState.getString("stock"));

        byte[] img = savedState.getByteArray("image");

        Bitmap bitmap = BitmapFactory.decodeByteArray(img,0,img.length);

        mImageView.setImageBitmap(bitmap);


    }

    public void refreshListView()
    {

        //Update the ListView
        Cursor cursor = InventoryActivity.database.getData("SELECT * FROM ITEM");//DataActivity.sqLiteHelper.getData("SELECT * FROM ITEM");
        InventoryActivity.list.clear();

        while(cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String brand = cursor.getString(1);
            String model = cursor.getString(2);
            String description = cursor.getString(3);
            String price = cursor.getString(4);
            String stock = cursor.getString(5);
            byte[] image = cursor.getBlob(6);

            InventoryActivity.list.add(new Item(id, brand, model, description, price, stock,image));
        }

        InventoryActivity.adapter.notifyDataSetChanged();

        finish();
    }

    //return a image from the image view
    private byte[] imageViewToByte(ImageView image)
    {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        if(requestCode == REQUEST_CODE_GALLERY)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap resized = Bitmap.createScaledBitmap(imageBitmap, 700, 600, true);
            mImageView.setImageBitmap(resized);
        }

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();

            try
            {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 700, 600, true);
                mImageView.setImageBitmap(resized);

            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void init()
    {

        mImageView = (ImageView)findViewById(R.id.item_image);
        edtBrand = (EditText) findViewById(R.id.brand_edit);
        edtModel = (EditText) findViewById(R.id.model_edit);
        edtDescription = (EditText) findViewById(R.id.description_edit);
        edtPrice = (EditText) findViewById(R.id.price_edit);
        edtStock = (EditText) findViewById(R.id.stock_edit);
        btnChoose = (Button) findViewById(R.id.choose_btn);
        btnSave = (Button) findViewById(R.id.save_btn);
        btnPhoto = (Button) findViewById(R.id.photo_btn);
        btnDelete = (Button) findViewById(R.id.delete_btn);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        this.finish();
    }

    //get camera view
    public void takePhoto(View view)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
