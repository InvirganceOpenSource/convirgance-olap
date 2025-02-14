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

/**
 * Captures the relationship between a source table, a column on the source 
 * table, and a target table.
 * @author jbanes
 */
public class ForeignKey
{
    private Table source;
    private String key;
    private Table target;

    /**
     * Returns the source table.
     * @return the source Table.
     */
    Table getSource()
    {
        return source;
    }

    /**
     * Sets the provided source table to the key.
     * @param source the source Table.
     */
    public void setSource(Table source)
    {
        this.source = source;
    }

    /**
     * Returns the String representing the key
     * @return the key String
     */
    public String getSourceKey()
    {
        return key;
    }

    /**
     * Sets the provided String as the key for this ForeignKey object.
     * @param sourceKey the String for the key.
     */
    public void setSourceKey(String sourceKey)
    {
        this.key = sourceKey;
    }

    /**
     * Returns the target table associated with the foreignKey
     * @return the target Table.
     */
    public Table getTarget()
    {
        return target;
    }

    /**
     * Sets the provided target table to be associated with this foreignKey.
     * @param target 
     */
    public void setTarget(Table target)
    {
        this.target = target;
    }

    /**
     * Compares this ForeignKey to another first by reference, then by
     * instance fields.
     * @param obj a ForeignKey object to compare to.
     * @return true if the objects are the same by reference or by 
     * instance fields.
     */
    @Override
    public boolean equals(Object obj)
    {
        ForeignKey other;
        
        if(obj == this) return true;
        if(!(obj instanceof ForeignKey)) return false;
        
        other = (ForeignKey)obj;
        
        if(!other.key.equals(key)) return false;
        if(!other.source.equals(source)) return false;
        if(!other.target.equals(target)) return false;
        
        return true;
    }

    /**
     * Returns the hashCode for the ForeignKey object based on all of 
     * its instance fields.
     * @return int representing the hashCode.
     */
    @Override
    public int hashCode()
    {
        return key.hashCode() + source.hashCode() + target.hashCode();
    }
}
