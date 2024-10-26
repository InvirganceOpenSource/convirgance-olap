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
public class SQLGenerator
{
    private List<Column> selects = new ArrayList<>();
    private List<Table> tables = new ArrayList<>();
    
    private boolean caseSensitive;
    private boolean forceGroupBy;

    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }

    public boolean isForceGroupBy()
    {
        return forceGroupBy;
    }

    public void setForceGroupBy(boolean forceGroupBy)
    {
        this.forceGroupBy = forceGroupBy;
    }
    
    public void addTable(Table table)
    {
        if(!tables.contains(table)) this.tables.add(table);
    }
    
    public void addSelect(String column, Table table)
    {
        selects.add(new Column(column, table));
        
        addTable(table);
    }
    
    public void addSelect(String column, Table table, String alias)
    {
        selects.add(new Column(column, table, alias));
        
        addTable(table);
    }
    
    public void addAggregate(String function, String column, Table table)
    {
        selects.add(new Aggregate(function, column, table));
        
        addTable(table);
    }
    
    public void addAggregate(String function, String column, Table table, String alias)
    {
        selects.add(new Aggregate(function, column, table, alias));
        
        addTable(table);
    }
    
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
    
    private class Column
    {
        protected String name;
        protected Table table;
        protected String alias;

        public Column(String name, Table table)
        {
            this.name = name;
            this.table = table;
        }

        public Column(String name, Table table, String alias)
        {
            this.name = name;
            this.table = table;
            this.alias = alias;
        }

        public String getName()
        {
            return name;
        }

        public Table getTable()
        {
            return table;
        }
        
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
    
    private class Aggregate extends Column
    {
        private String function;

        public Aggregate(String function, String name, Table table)
        {
            super(name, table);
            
            this.function = function;
        }

        public Aggregate(String function, String name, Table table, String alias)
        {
            super(name, table, alias);
            
            this.function = function;
        }

        public String getFunction()
        {
            return function;
        }

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
