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
package com.invirgance.convirgance.olap.sql;

import com.invirgance.convirgance.wiring.annotation.Wiring;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides support for Database structure representation.
 * @author jbanes
 */
@Wiring
public class Database
{
    private String name;
    private List<Table> tables = new ArrayList<>();
    
    /**
     * Creates a new instance of the Database object.
     * 
     */
    public Database()
    {
    }

    /** 
     * Creates a new instance of the Database with the specified name.
     * @param name the String representing the name.
     */
    public Database(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns the name of the database.
     * @return name as a String.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the Database to the provided string.
     * @param name The new name to use.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the list of tables stored in the database.
     * @return list of Table objects.
     */
    public List<Table> getTables()
    {
        return tables;
    }
    
    /**
     * Adds the provided table to the Database if it already doesn't exist. If
     * the Database already contains the table passed in as the parameter, 
     * does nothing. This ensures that table names are not repeated when adding
     * to the database.
     * @param table The table to add.
     */
    public void addTable(Table table)
    {
        if(!this.tables.contains(table)) 
        {
            table.setDatabase(this);
            this.tables.add(table);
        }
    }
    
    /**
     * Returns the table whose name matches the passed in String. If the 
     * Database does not contain a table under such name, returns null.
     * @param name String representing the name of the table to return.
     * @return Table with the specified name.
     */
    public Table getTable(String name)
    {
        for(Table table : this.tables)
        {
            if(table.getName().equals(name)) return table;
        }
        
        return null;
    }
    
    /**
     * Removes the table from the Database.
     * @param table The table to remove.
     */
    public void removeTable(Table table)
    {
        table.setDatabase(null);
        this.tables.remove(table);
    }
    
    /**
     * Sets the tables to the Database and sets the Database to each table
     * in the list.
     * @param tables A List of Table objects to assign to the database
     */
    public void setTables(List<Table> tables)
    {
        this.tables = tables;
        
        for(Table table : tables) table.setDatabase(this);
    }
    
    /**
     * Compares the Database to another object first based on 
     * reference and then on name of the Database.
     * @param obj A database to compare to.
     * @return true if the databases are the same by memory reference or 
     * by name, false otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        Database other;
        
        if(obj == this) return true;
        if(!(obj instanceof Database)) return false;
        
        other = (Database)obj;
        
        if(!other.name.equals(name)) return false;
        
        return true;
    }

    /**
     * Returns the hashCode for the Database's name.
     * @return int representing the hashCode.
     */
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
