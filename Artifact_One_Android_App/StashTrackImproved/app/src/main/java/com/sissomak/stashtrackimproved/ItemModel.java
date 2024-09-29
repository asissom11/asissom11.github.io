package com.sissomak.sqldemo;

/**
 * ItemModel Class
 * Functionality:
 *      - Template for item information in the Items database
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class ItemModel
{
    private int id;
    private String name;
    private String description;
    private String quantity;
    private String units;

    public ItemModel()
    {
    }

    public ItemModel(int id, String name, String description, String quantity, String units)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.units = units;
    }

    public ItemModel(String description, String quantity, String units)
    {
        this.description = description;
        this.quantity = quantity;
        this.units = units;
    }

    public ItemModel(int itemID, String itemName, String itemDescription, String itemQuantity)
    {
        this.id = itemID;
        this.name = itemName;
        this.description = itemDescription;
        this.quantity = itemQuantity;
    }

    // toString method
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity='" + quantity +
                ", units='" + units + '\'' +
                '}';
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnits()
    {
        return units;
    }

    public void setUnits(String units)
    {
        this.units = units;
    }
}
