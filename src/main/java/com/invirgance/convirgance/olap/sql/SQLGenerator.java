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
 * Provides support for creating and outputting SQL queries for working
 * with OLAP.
 * @author jbanes
 */
public class SQLGenerator
{
    private List<Column> selects = new ArrayList<>();
    private List<Table> tables = new ArrayList<>();
    
    private boolean caseSensitive;
    private boolean forceGroupBy;

    /**
     * Returns true if the SQLGenerator is case sensitive.
     * @return boolean.
     */
    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    /**
     * Sets the case sensitivity for the SQLGenerator as true or false.
     * @param caseSensitive the boolean.
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }
    
    /**
     * Returns true if the SQLGenerator is set to force the GROUP BY command in
     * queries, false otherwise.
     * @return boolean.
     */
    public boolean isForceGroupBy()
    {
        return forceGroupBy;
    }

    /** 
     * Sets the forcing of GROUP BY command.
     * @param forceGroupBy boolean.
     */
    public void setForceGroupBy(boolean forceGroupBy)
    {
        this.forceGroupBy = forceGroupBy;
    }
    
    /**
     * Adds the provided Table to the table list of the SQLGenerator
     * @param table a Table to include in SQLGenerator.
     */
    public void addTable(Table table)
    {
        if(!tables.contains(table)) this.tables.add(table);
    }
    
    /**
     * Adds a Column object into the list of Columns 
     * selected for this SQLGenerator.
     * @param column String with the column name to be added.
     * @param table Table associated with the column.
     */
    public void addSelect(String column, Table table)
    {
        selects.add(new Column(column, table));
        
        addTable(table);
    }
    
    /**
     * Adds a Column object with the specified alias name into the list of 
     * Columns for this SQL Generator.
     * The Column is constructed using string name provided, the associated table, 
     * and the specified alias.
     * @param column the string with the column name.
     * @param table Table associated with the column.
     * @param alias the string representing the alias for the column name.
     */
    public void addSelect(String column, Table table, String alias)
    {
        selects.add(new Column(column, table, alias));
        
        addTable(table);
    }
    
    /**
     * Adds an Aggregate object into the list of Columns selected for
     * this SQLGenerator.
     * @param function the aggregate's function.
     * @param column the aggregate's name.
     * @param table table associated with the aggregate.
     */
    public void addAggregate(String function, String column, Table table)
    {
        selects.add(new Aggregate(function, column, table));
        
        addTable(table);
    }
    
    /**
     * Adds an Aggregate object (with specified alias) into the list of Columns selected for
     * this SQLGenerator.
     * @param function the aggregate's function.
     * @param column the aggregate's name.
     * @param table table associated with the aggregate.
     * @param alias the aggregate's alias.
     */
    public void addAggregate(String function, String column, Table table, String alias)
    {
        selects.add(new Aggregate(function, column, table, alias));
        
        addTable(table);
    }
    
    /**
     * Handles the generation of JOIN clauses among all the tables
     * selected for the SQLGenerator and the specified FROM table. 
     * @param from the FROM table, from which columns are selected.
     * @return a SQL string component with JOIN clauses.
     */
    private String generateJoins(Table from)
    {
        StringBuffer buffer = new StringBuffer();
        String quotes = caseSensitive ? "\"" : "";
        
        for(Table table : this.tables)
        {
            if(table.equals(from)) continue;
            
            for(ForeignKey key : from.getForeignKeys())
            {
                if(key.getTarget().equals(table))
                {
                    buffer.append('\n');
                    buffer.append("join ");
                    buffer.append(quotes);
                    buffer.append(table.getName());
                    buffer.append(quotes);
                    buffer.append(" on ");
                    buffer.append(quotes);
                    buffer.append(table.getName());
                    buffer.append(quotes);
                    buffer.append('.');
                    buffer.append(quotes);
                    buffer.append(table.getPrimaryKey());
                    buffer.append(quotes);
                    buffer.append(" = ");
                    buffer.append(quotes);
                    buffer.append(from.getName());
                    buffer.append(quotes);
                    buffer.append('.');
                    buffer.append(quotes);
                    buffer.append(key.getSourceKey());
                    buffer.append(quotes);
                }
            }
        }
        
        // TODO: Handle joins between tables that are not the FROM table
        
        return buffer.toString();
    }
    
    /** 
     * Handles the generation of the GROUP BY clause.
     * @return the GROUP BY clause for the SQL query.
     */
    private String generateGroupBy()
    {
        StringBuffer buffer = new StringBuffer();
        int aggregates = 0;
        int index = 0;
        
        for(Column column : selects)
        {
            if(column instanceof Aggregate) 
            {
                aggregates++;
                continue;
            }
            
            if(index > 0) buffer.append(",\n");
            else buffer.append("\ngroup by\n");
              
            buffer.append("    ");
            buffer.append(column.getGroupBySQL());
            
            index++;
        }
        
        if(aggregates < 1 && !forceGroupBy) return "";
        
        return buffer.toString();
    }
    
    /**
     * Generates the String with the full SQL query.
     * @return the SQL query string
     */
    public String getSQL()
    {
        StringBuffer buffer = new StringBuffer();
        Table from = tables.get(0);
        int index = 0;
        
        buffer.append("select\n");
        
        for(Column column : selects)
        {
            if(index > 0) buffer.append(",\n");
              
            buffer.append("    ");
            buffer.append(column.getSQL());
            
            index++;
        }
        
        buffer.append("\n");
        buffer.append("from ");
        
        if(caseSensitive) buffer.append('"');
        
        buffer.append(from.getName());
        
        if(caseSensitive) buffer.append('"');
        
        buffer.append(generateJoins(from));
        buffer.append(generateGroupBy());
        
        return buffer.toString();
    }
    
    /**
     * Private class that provides the Column support for 
     * the SQLGenerator class. Instance fields include the name, table, 
     * and alias.
     */
    private class Column
    {
        protected String name;
        protected Table table;
        protected String alias;
        
        /**
         * Creates a new instance of the Column object with the
         * specified name for the column and the provided table.
         * @param name the name of the column.
         * @param table the table containing the column.
         */
        public Column(String name, Table table)
        {
            this.name = name;
            this.table = table;
        }
        
        /**
         * Creates a new instance of the Column object with the
         * specified name, table, and alias.
         * @param name
         * @param table
         * @param alias 
         */
        public Column(String name, Table table, String alias)
        {
            this.name = name;
            this.table = table;
            this.alias = alias;
        }

        /**
         * Returns the name of the Column object.
         * @return String representing the column's name
         */
        public String getName()
        {
            return name;
        }
        
        /**
         * Returns the Table object associated with the column.
         * @return the table containing the column.
         */
        public Table getTable()
        {
            return table;
        }
        
        /**
         * Returns the SQL component for the column, with the specified alias 
         * name if one exists.
         * @return SQL string component.
         */
        public String getSQL()
        {
            StringBuffer buffer = new StringBuffer();
            String quotes = caseSensitive ? "\"" : "";
            
            buffer.append(quotes);
            buffer.append(table.getName());
            buffer.append(quotes);
            buffer.append('.');
            buffer.append(quotes);
            buffer.append(this.name);
            buffer.append(quotes);
            
            if(this.alias != null)
            {
                buffer.append(" as ");
                buffer.append("\"");
                buffer.append(this.alias);
                buffer.append("\"");
            }
            
            return buffer.toString();
        }
        
        /**
         * Returns the SQL component for the column to use in GROUP BY
         * clause generation .
         * @return SQL String component
         */
        public String getGroupBySQL()
        {
            StringBuffer buffer = new StringBuffer();
            String quotes = caseSensitive ? "\"" : "";
            
            buffer.append(quotes);
            buffer.append(table.getName());
            buffer.append(quotes);
            buffer.append('.');
            buffer.append(quotes);
            buffer.append(this.name);
            buffer.append(quotes);
                
            return buffer.toString();
        }
    }
    
    /**
     * Private class Aggregate, extends the Column object to include
     * the String field for a function run on the column to create an aggregate.
     */
    private class Aggregate extends Column
    {
        private String function;
        
        /**
         * Creates a new instance of an Aggregate object with the specified
         * function, name, and table.
         * @param function a String representing a function.
         * @param name the String with the aggregate's name.
         * @param table Table associated with the Aggregate object.
         */
        public Aggregate(String function, String name, Table table)
        {
            super(name, table);
            
            this.function = function;
        }
        /**
         * Creates a new instance of an Aggregate object with the specified 
         * function, name, and table, and the alias.
         * @param function a String representing a function.
         * @param name the String with the aggregate's name,
         * @param table Table associated with the Aggregate object.
         * @param alias the String with the aggregate's alias.
         */
        public Aggregate(String function, String name, Table table, String alias)
        {
            super(name, table, alias);
            
            this.function = function;
        }
        
        /**
         * Returns the function of the Aggregate object
         * @return the String representing the object's function.
         */
        public String getFunction()
        {
            return function;
        }

        /**
         * Returns the SQL component for getting the Aggregate, with the 
         * specified alias name if one exists.
         * @return SQL string component.
         */
        @Override
        public String getSQL()
        {
            StringBuffer buffer = new StringBuffer();
            String quotes = caseSensitive ? "\"" : "";
            
            buffer.append(function);
            buffer.append('(');
            buffer.append(quotes);
            buffer.append(table.getName());
            buffer.append(quotes);
            buffer.append('.');
            buffer.append(quotes);
            buffer.append(this.name);
            buffer.append(quotes);
            buffer.append(')');
            
            if(this.alias != null)
            {
                buffer.append(" as ");
                buffer.append("\"");
                buffer.append(this.alias);
                buffer.append("\"");
            }
            
            return buffer.toString();
        }
    }
}
