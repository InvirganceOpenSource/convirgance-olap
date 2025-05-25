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
 * Provides support for a database Table representation.
 * @author jbanes
 */
@Wiring
public class Table
{
    private Database database;
    private String name;
    private String primaryKey;
    private List<ForeignKey> foreignKeys = new ArrayList<>();

    /**
     * Creates a new instance of the Table object.
     */
    public Table()
    {
    }
    
    /** 
     * Creates a new instance of the Table with the specified name.
     * @param name the String representing the table's name.
     */
    public Table(String name)
    {
        this.name = name;
    }
    
    /** 
     * Creates a new instance of the Database with the specified name
     * and primaryKey.
     * @param name the String representing the name.
     * @param primaryKey the String representing the primaryKey.
     */
    public Table(String name, String primaryKey)
    {
        this.name = name;
        this.primaryKey = primaryKey;
    }

    /**
     * Returns the Database associated with the table.
     * @return the Database object.
     */
    public Database getDatabase()
    {
        return database;
    }

    /**
     * Sets the provided database to the table.
     * @param database The database object to set to the table.
     */
    void setDatabase(Database database)
    {
        this.database = database;
    }

    /**
     * Returns the name of the Table.
     * @return the String with the table's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name for the table.
     * @param name the String with the name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /** 
     * Returns the String with the primaryKey of the table.
     * @return String with the primaryKey.
     */
    public String getPrimaryKey()
    {
        return primaryKey;
    }

    /**
     * Sets the provided string as the primaryKey for the table.
     * @param primaryKey String representing the primary key.
     */
    public void setPrimaryKey(String primaryKey)
    {
        this.primaryKey = primaryKey;
    }

    /**
     * Returns the List of ForeignKey objects associated with the table.
     * @return the List of ForeignKey objects.
     */
    public List<ForeignKey> getForeignKeys()
    {
        return foreignKeys;
    }
    
    /**
     * Creates and adds the new foreignKey to this table's list of foreign keys.
     * If the table already contains the new key, does nothing.
     * @param foreignKey the String representing the foreign key.
     * @param target the target table associated with the foreign key.
     */
    public void addForeignKey(String foreignKey, Table target)
    {
        ForeignKey key = new ForeignKey();
        
        key.setSource(this);
        key.setSourceKey(foreignKey);
        key.setTarget(target);
        
        if(!this.foreignKeys.contains(key))
        {
            this.foreignKeys.add(key);
        }
    }

    /**
     * Sets the provided list of ForeignKey objects to this Table.
     * @param foreignKeys a list of ForeignKey objects.
     */
    public void setForeignKeys(List<ForeignKey> foreignKeys)
    {
        this.foreignKeys = foreignKeys;
        
        for(ForeignKey key : foreignKeys) key.setSource(this);
    }

    /**
     * Compares this Table to another Table first by reference, then
     * by instance fields (associated database, table name, and primary key),
     * @param obj A table to compare to.
     * @return true if the databases are the same by memory reference or 
     * by instance fields.
     */
    @Override
    public boolean equals(Object obj)
    {
        Table other;
        
        if(obj == this) return true;
        if(!(obj instanceof Table)) return false;
        
        other = (Table)obj;
        
        if(other.database == null && this.database != null) return false;
        if(other.database != null && !other.database.equals(database)) return false;
        
        if(!other.name.equals(name)) return false;
        
        if(other.primaryKey == null && this.primaryKey != null) return false;
        if(other.primaryKey != null && !other.primaryKey.equals(primaryKey)) return false;
        
        return true;
    }

    /**
     * Returns the hashCode for the Table. The hashCode is based on the
     * database associated with the table, the table's name, and its primary key.
     * @return int representing the hashCode.
     */
    @Override
    public int hashCode()
    {
        return database.hashCode() + name.hashCode() + primaryKey.hashCode();
    }
}
