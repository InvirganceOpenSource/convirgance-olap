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
package com.invirgance.convirgance.sql;

import com.invirgance.convirgance.olap.sql.Database;
import com.invirgance.convirgance.olap.sql.SQLGenerator;
import com.invirgance.convirgance.olap.sql.Table;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jbanes
 */
public class SQLGeneratorTest
{
    @Test
    public void testSingleTable()
    {
        String expected = "select\n" +
                          "    DimFranchise.FranchiseName\n" + 
                          "from DimFranchise";
        
        SQLGenerator generator = new SQLGenerator();
        Database stardb = new Database("StarDB");
        Table franchise = new Table("DimFranchise", "id");
        
        stardb.addTable(franchise);
        generator.addSelect("FranchiseName", franchise);
        
        assertEquals(expected, generator.getSQL());
    }
    
    @Test
    public void testOneDimension()
    {
        String expected = "select\n" +
                          "    DimFranchise.FranchiseName\n" + 
                          "from FactSales\n" + 
                          "join DimFranchise on DimFranchise.id = FactSales.FranchiseId";
        
        SQLGenerator generator = new SQLGenerator();
        Database stardb = new Database("StarDB");
        Table sales = new Table("FactSales", "id");
        Table franchise = new Table("DimFranchise", "id");
        
        stardb.addTable(sales);
        stardb.addTable(franchise);
        
        sales.addForeignKey("FranchiseId", franchise);
        
        generator.addTable(sales);
        generator.addSelect("FranchiseName", franchise);
        
        assertEquals(expected, generator.getSQL());
    }
    
    @Test
    public void testTwoDimension()
    {
        String expected = "select\n" +
                          "    DimFranchise.FranchiseName,\n" +
                          "    DimStore.StoreName\n" + 
                          "from FactSales\n" + 
                          "join DimFranchise on DimFranchise.id = FactSales.FranchiseId\n" + 
                          "join DimStore on DimStore.id = FactSales.StoreId";
        
        SQLGenerator generator = new SQLGenerator();
        Database stardb = new Database("StarDB");
        Table sales = new Table("FactSales", "id");
        Table franchise = new Table("DimFranchise", "id");
        Table store = new Table("DimStore", "id");
        
        stardb.addTable(sales);
        stardb.addTable(franchise);
        
        sales.addForeignKey("FranchiseId", franchise);
        sales.addForeignKey("StoreId", store);
        
        generator.addTable(sales);
        generator.addSelect("FranchiseName", franchise);
        generator.addSelect("StoreName", store);
        
        assertEquals(expected, generator.getSQL());
    }
    
    @Test
    public void testAggregate()
    {
        String expected = "select\n" + 
                          "    DimFranchise.FranchiseName,\n" +
                          "    DimStore.StoreName,\n" + 
                          "    sum(FactSales.Quantity) as \"Products Sold\"\n" +
                          "from FactSales\n" + 
                          "join DimFranchise on DimFranchise.id = FactSales.FranchiseId\n" + 
                          "join DimStore on DimStore.id = FactSales.StoreId\n" + 
                          "group by\n" +
                          "    DimFranchise.FranchiseName,\n" +
                          "    DimStore.StoreName";
        
        SQLGenerator generator = new SQLGenerator();
        Database stardb = new Database("StarDB");
        Table sales = new Table("FactSales", "id");
        Table franchise = new Table("DimFranchise", "id");
        Table store = new Table("DimStore", "id");
        
        stardb.addTable(sales);
        stardb.addTable(franchise);
        
        sales.addForeignKey("FranchiseId", franchise);
        sales.addForeignKey("StoreId", store);
        
        generator.addTable(sales);
        generator.addSelect("FranchiseName", franchise);
        generator.addSelect("StoreName", store);
        generator.addAggregate("sum", "Quantity", sales, "Products Sold");
        
        assertEquals(expected, generator.getSQL());
    }
    
    @Test
    public void testDuplicates()
    {
        SQLGenerator generator = new SQLGenerator();
        Database stardb = new Database("StarDB");
        Table sales = new Table("FactSales", "id");
        Table franchise = new Table("DimFranchise", "id");
        Table store = new Table("DimStore", "id");
        
        assertEquals(0, stardb.getTables().size());
        
        stardb.addTable(sales);
        stardb.addTable(franchise);
        
        assertEquals(2, stardb.getTables().size());
        
        stardb.addTable(sales);
        stardb.addTable(franchise);
        
        assertEquals(2, stardb.getTables().size());
        assertEquals(0, sales.getForeignKeys().size());
        
        sales.addForeignKey("FranchiseId", franchise);
        sales.addForeignKey("StoreId", store);
        
        assertEquals(2, sales.getForeignKeys().size());
        
        sales.addForeignKey("FranchiseId", franchise);
        sales.addForeignKey("StoreId", store);
        
        assertEquals(2, sales.getForeignKeys().size());
        
        assertEquals(new Table("FactSales", "id"), new Table("FactSales", "id"));
        assertNotEquals(new Table("FactSales", "id"), new Table("DimFranchise", "id"));
        assertNotEquals(new Table("FactSales", "id"), new Table("FactSales", "FranchiseId"));
    }
}
