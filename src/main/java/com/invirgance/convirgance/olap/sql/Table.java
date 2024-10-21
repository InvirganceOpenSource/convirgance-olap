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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbanes
 */
public class Table
{
    private Database database;
    private String name;
    private String primaryKey;
    private List<ForeignKey> foreignKeys = new ArrayList<>();

    public Table()
    {
    }

    public Table(String name)
    {
        this.name = name;
    }

    public Table(String name, String primaryKey)
    {
        this.name = name;
        this.primaryKey = primaryKey;
    }

    public Database getDatabase()
    {
        return database;
    }

    void setDatabase(Database database)
    {
        this.database = database;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPrimaryKey()
    {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey)
    {
        this.primaryKey = primaryKey;
    }

    public List<ForeignKey> getForeignKeys()
    {
        return foreignKeys;
    }
    
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

    public void setForeignKeys(List<ForeignKey> foreignKeys)
    {
        this.foreignKeys = foreignKeys;
        
        for(ForeignKey key : foreignKeys) key.setSource(this);
    }
}
