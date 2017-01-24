package com.example.valentin.nexusinventory;

/**
 * Created by Valentin on 17/01/2017.
 */

public class Item
{

    private  int id;
    private String brand;
    private String model;
    private String description;
    private String price;
    private String stock;
    private byte[] image;


    public Item(int id, String brand, String model, String description, String price, String stock, byte[] image)
    {
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.id = id;
        this.image = image;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public void setStock(String stock)
    {
        this.stock = stock;
    }

    public int getId()
    {
        return id;
    }

    public byte[] getImage()
    {
        return image;
    }

    public String getBrand()
    {
        return brand;
    }

    public String getModel()
    {
        return model;
    }

    public String getDescription()
    {
        return description;
    }

    public String getPrice()
    {
        return price;
    }

    public String getStock()
    {
        return stock;
    }
}
