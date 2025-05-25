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
import com.invirgance.convirgance.wiring.annotation.Wiring;

/** 
 * Provides support for quantitative values of data.
 * @author jbanes
 */
@Wiring
public class Metric
{
    private Star star;
    private Table table;
    private String column;

    /**
     * Default constructor initializes an empty Metric with no 
     * assigned attributes.
     */
    public Metric()
    {
    }

    /**
     * Constructs a Metric with an associated database table and column.
     * @param table Database table associated with the metric.
     * @param column Column associated with the metric.
     */
    public Metric(Table table, String column)
    {
        this.table = table;
        this.column = column;
    }

    /**
     * Returns the star schema associated with this metric.
     * @return the Star schema assigned to the metric.
     */
    public Star getStar()
    {
        return star;
    }

    /**
     * Assigns the star schema reference to this metric.
     * @param star The {@code Star} schema object.
     */
    void setStar(Star star)
    {
        this.star = star;
    }

    /**
     * Returns the database table associated with this metric.
     * @return Table object associated with this metric.
     */
    public Table getTable()
    {
        return table;
    }

    /**
     * Assigns a new table to this metric.
     * @param table the Table object containing the metric data.
     */
    public void setTable(Table table)
    {
        this.table = table;
    }

    /**
     * Returns the column representing this metric's data.
     * @return the Column object with the metric data.
     */
    public String getColumn()
    {
        return column;
    }

    /**
     * Sets the column associated with this metric.
     * @param column The Column object associated with this metric.
     */
    public void setColumn(String column)
    {
        this.column = column;
    }
}
