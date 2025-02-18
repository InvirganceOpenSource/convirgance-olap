/*
 * Copyright 2024 INVIRGANCE LLC

Permission is hereby granted, free of charge, to any person obtaining a copy 
of this software and associated documentation files (the “Software”), to deal 
in the Software without restriction, including without limitation the rights to 
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
of the Software, and to permit persons to whom the Software is furnished to do 
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
SOFTWARE.
 */
package com.invirgance.convirgance.olap;

import com.invirgance.convirgance.olap.sql.Table;

/**
 * Provides support for qualitative descriptions of data.
 * @author jbanes
 */
public class Dimension
{
    private Star star;
    private String name;
    private Table table;
    private String column;

    /**
     * Initializes an empty Dimension with no assigned attributes.
     */
    public Dimension()
    {
    }
    
    /**
     * Constructs a Dimension with a specified name,
     * associated database table, and column.
     * @param name The name of the Dimension (e.g. "Region", "Item Category").
     * @param table The database table containing the dimension data.
     * @param column The column in the database table that represents the dimension.
     */
    public Dimension(String name, Table table, String column)
    {
        this.name = name;
        this.table = table;
        this.column = column;
    }

    /**
     * Returns the star schema associated with this dimension.
     * @return the Star schema assigned to the dimension.
     */
    public Star getStar()
    {
        return star;
    }

    /**
     * Assigns the star schema reference to this dimension. 
    * @param star The {@code Star} schema object.
     */
    void setStar(Star star)
    {
        this.star = star;
    }

    /**
     * Returns the name of the dimension.
     * @return the name of the Dimension as a String.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Sets the name of this Dimension.
     * @param name the new name as a String.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns the database table associated with this dimension.
     * @return the Table object associated with this dimension.
     */
    public Table getTable()
    {
        return table;
    }

    /** 
     * Assigns a new table to this dimension.
     * @param table the Table object containing the dimension data.
     */
    public void setTable(Table table)
    {
        this.table = table;
    }

    /**
     * Returns the column representing this dimension.
     * @return the Column object representing the dimension.
     */
    public String getColumn()
    {
        return column;
    }

    /**
     * Sets the column associated with this dimension.
     * @param column the Column object associated with the dimension.
     */
    public void setColumn(String column)
    {
        this.column = column;
    }
}
