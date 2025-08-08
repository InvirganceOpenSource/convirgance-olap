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
package com.invirgance.convirgance.olap.web;

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.olap.Dimension;
import com.invirgance.convirgance.olap.Measure;
import com.invirgance.convirgance.olap.ReportGenerator;
import com.invirgance.convirgance.olap.Star;
import com.invirgance.convirgance.source.FileSource;
import com.invirgance.convirgance.web.binding.Binding;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.servlet.ServiceState;
import com.invirgance.convirgance.wiring.XMLWiringParser;
import com.invirgance.convirgance.wiring.annotation.Wiring;
import java.io.File;
import java.util.List;

/**
 *
 * @author jbanes
 */
@Wiring
public class StarQueryBinding implements Binding
{
    private String jndiName;
    private String schema;
    private boolean caseSensitive;
    private boolean logQuery;
    
    private File file;
    private Star star;
    private long loaded;

    /**
     * Get the JNDI path to the configured database connection. e.g. jdbc/mydatabase
     * 
     * @return path to jdbc datasource
     */
    public String getJndiName()
    {
        return jndiName;
    }

    /**
     * Set the JNDI path to the configured database connection. e.g. jdbc/mydatabase
     * 
     * @param jndiName path to jdbc datasource
     */
    public void setJndiName(String jndiName)
    {
        this.jndiName = jndiName;
    }

    /**
     * Get the current classpath to the schema XML file
     * 
     * @return classpath to XML Wiring file
     */
    public String getSchema()
    {
        return schema;
    }

    /**
     * Sets the classpath to the Wiring XML file containing the database
     * structure. Under most circumstances, this path should start with a "/".
     * 
     * @param schema classpath to XML Wiring file
     */
    public void setSchema(String schema)
    {
        this.schema = schema;
    }

    /**
     * True if the underlying engine will quote the column and table names to 
     * ensure the correct object is queried.
     * 
     * @return true if quoting identifiers, false if relying on database match
     */
    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    /**
     * Set to true if the database is sensitive to the casing the names. The underlying
     * engine will quote the column and table names to ensure the correct object
     * is queried. Not necessary under most circumstances.
     * 
     * @param caseSensitive true to quote identifiers, false if allowed to let the database match
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }

    public boolean isLogQuery()
    {
        return logQuery;
    }

    public void setLogQuery(boolean logQuery)
    {
        this.logQuery = logQuery;
    }
    
    private void loadStar()
    {
        List list;

        // Already loaded
        if(star != null && this.file.lastModified() <= this.loaded) return;
        
        synchronized(this)
        {
            file = ((HttpRequest)ServiceState.get("request")).getFileByPath("WEB-INF/models/" + schema);

            if(file == null) throw new ConvirganceException("Schema " + schema + " not found under WEB-INF/models/");

            list = new XMLWiringParser<List>(new FileSource(file)).getRoot();
            loaded = file.lastModified();

            for(Object object : list)
            {
                if(object instanceof Star) this.star = (Star)object;
            }
        }
    }
    
    @Override
    public Iterable<JSONObject> getBinding(JSONObject parameters)
    {
        ReportGenerator generator;
        DBMS dbms;
        
        Measure measure;
        Dimension dimension;
        
        JSONArray<String> dimensions = (JSONArray<String>)parameters.getJSONArray("dimensions");
        JSONArray<String> measures = (JSONArray<String>)parameters.getJSONArray("measures");

        loadStar();
        
        if(parameters.getJSONArray("dimensions").isEmpty() && parameters.getJSONArray("measures").isEmpty()) return new JSONArray<>();
        
        generator = new ReportGenerator(star);
        dbms = DBMS.lookup(jndiName);
        
        for(String name : dimensions)
        {
            dimension = star.getDimension(name);
            
            if(dimension == null) throw new ConvirganceException("Dimension [" + name + "] not found!");
            
            generator.addDimension(dimension);
        }
        
        for(String name : measures)
        {
            measure = star.getMeasure(name);
            
            if(measure == null) throw new ConvirganceException("Measure [" + name + "] not found!");
            
            generator.addMeasure(measure);
        }
        
        generator.setCaseSensitive(caseSensitive);

        if(logQuery) System.out.println(generator.getSQL());
        
        return dbms.query(new Query(generator.getSQL()));
    }
    
}
