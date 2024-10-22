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

import com.invirgance.convirgance.olap.measures.SumMeasure;
import com.invirgance.convirgance.olap.sql.Database;
import com.invirgance.convirgance.olap.sql.Table;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jbanes
 */
public class ReportGeneratorTest
{
    public Database getDatabase()
    {
        Database stardb = new Database("StarDB");
        
        Table sales = new Table("FactSales");
        Table franchise = new Table("DimFranchise", "id");
        Table store = new Table("DimStore", "id");
        
        stardb.addTable(sales);
        stardb.addTable(franchise);
        stardb.addTable(store);
        
        sales.addForeignKey("FranchiseId", franchise);
        sales.addForeignKey("StoreId", store);
        
        return stardb;
    }
    
    public Star getStar()
    {
        Database stardb = getDatabase();
        Star star = new Star(stardb.getTable("FactSales"));
        
        Dimension dimFranchise = new Dimension("Franchise Name", stardb.getTable("DimFranchise"), "FranchiseName");
        Dimension dimStore = new Dimension("Store Name", stardb.getTable("DimStore"), "StoreName");
        
        Metric quantity = new Metric(stardb.getTable("FactSales"), "Quantity");
        Measure measure = new SumMeasure("Products Sold", quantity);
        
        star.addDimension(dimFranchise);
        star.addDimension(dimStore);
        star.addMeasure(measure);
        
        return star;
    }

    @Test
    public void testAggregateNoDimension()
    {
        String expected = "select\n" +
                          "    sum(FactSales.Quantity) as \"Products Sold\"\n" + 
                          "from FactSales";
        
        Star star = getStar();
        ReportGenerator generator = new ReportGenerator(star);
        
        generator.addMeasure(star.getMeasure("Products Sold"));
        
        assertEquals(expected, generator.getSQL());
    }

    @Test
    public void testAggregateDimensions()
    {
        String expected = "select\n" + 
                          "    DimFranchise.FranchiseName as \"Franchise Name\",\n" +
                          "    DimStore.StoreName as \"Store Name\",\n" + 
                          "    sum(FactSales.Quantity) as \"Products Sold\"\n" +
                          "from FactSales\n" + 
                          "join DimFranchise on DimFranchise.id = FactSales.FranchiseId\n" + 
                          "join DimStore on DimStore.id = FactSales.StoreId\n" + 
                          "group by\n" +
                          "    DimFranchise.FranchiseName,\n" +
                          "    DimStore.StoreName";
        
        Star star = getStar();
        ReportGenerator generator = new ReportGenerator(star);
        
        generator.addDimension(star.getDimension("Franchise Name"));
        generator.addDimension(star.getDimension("Store Name"));
        generator.addMeasure(star.getMeasure("Products Sold"));
        
        assertEquals(expected, generator.getSQL());
    }
    
}
