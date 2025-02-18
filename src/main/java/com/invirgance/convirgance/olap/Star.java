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
import java.util.ArrayList;
import java.util.List;

/**
 * Provides support for the central Star Schema container to manage
 * relationships among Dimensions, Metrics, and aggregated Measures.
 * @author jbanes
 */
public class Star
{
    private Table fact;
    private List<Dimension> dimensions = new ArrayList<>();
    private List<Metric> metrics = new ArrayList<>();
    private List<Measure> measures = new ArrayList<>();

    /**
     * Initializes am empty Star object with no assigned attributes.
     */
    public Star()
    {
    }

    /**
     * Creates a new Star object with the specified table as the fact table.
     * @param fact The fact table.
     */
    public Star(Table fact)
    {
        this.fact = fact;
    }

    /**
     * Returns the fact table of the Star.
     * @return the fact table.
     */
    public Table getFact()
    {
        return fact;
    }

    /**
     * Sets the provided Table as the fact table for this Star object.
     * @param fact the table to be set as the fact table.
     */
    public void setFact(Table fact)
    {
        this.fact = fact;
    }
    
    /**
     * Returns the requested Dimension from the Star or null if 
     * no such Dimension found.
     * @param name the String with the Dimension's name.
     * @return The requested Dimension object or null.
     */
    public Dimension getDimension(String name)
    {
        for(Dimension dimension : this.dimensions)
        {
            if(dimension.getName().equals(name)) return dimension;
        }
        
        return null;
    }

    /**
     * Returns all Dimensions contained in the Star.
     * @return A list of Dimensions.
     */
    public List<Dimension> getDimensions()
    {
        return dimensions;
    }
    
    /**
     * Adds the passed in Dimension object to the list of Dimensions 
     * in the Star. Sets this Star to be associated with the Dimension added.
     * @param dimension The Dimension to add.
     */
    public void addDimension(Dimension dimension)
    {
        if(!this.dimensions.contains(dimension))
        {
            this.dimensions.add(dimension);
            dimension.setStar(this);
        }
    }
    
    /**
     * Assigns the passed in List of Dimensions to this Star object. Also
     * associates this Star object with every Dimension in the list by 
     * adding the reference to the Star within each Dimension.
     * @param dimensions The list of Dimensions.
     */
    public void setDimensions(List<Dimension> dimensions)
    {
        this.dimensions = dimensions;
        
        for(Dimension dimension : dimensions) dimension.setStar(this);
    }

    /**
     * Returns all Metrics contained in the Star. 
    * @return the list of Metrics.
     */
    public List<Metric> getMetrics()
    {
        return metrics;
    }

    /**
     *  Assigns the passed in List of Metrics to this Star object. Also
     * associates this Star object with every Metric in the list by adding the 
     * reference to the Star within each Metric.
    * @param metrics The list of Metrics.
     */
    public void setMetrics(List<Metric> metrics)
    {
        this.metrics = metrics;
        
        for(Metric metric : metrics) metric.setStar(this);
    }

    /**
     * Returns the requested Measure from the Star or null if 
     * no such Measure found.
     * @param name the String representing the Measure to be returned.
     * @return the requested Measure object.
     */
    public Measure getMeasure(String name)
    {
        for(Measure measure : this.measures)
        {
            if(measure.getName().equals(name)) return measure;
        }
        
        return null;
    }
    
    /**
     * Returns all Measures contained in the Star.
     * @return the list of Measures.
     */
    public List<Measure> getMeasures()
    {
        return measures;
    }
    
    /**
     * Adds the passed in Measure object to the list of Measures
     * contained in the Star. Also associates this Star to the Measure.
     * @param measure The Measure object to be added
     */
    public void addMeasure(Measure measure)
    {
        if(!this.measures.contains(measure)) 
        {
            this.measures.add(measure);
            measure.getMetric().setStar(this);
        }
    }

    /**
     * Assigns the passed in List of Measures to this Star object.
     * Associated every Measure with this Star by including the reference 
     * to Star for each Measure in the list.
     * @param measures The list of Measures to be assigned.
     */
    public void setMeasures(List<Measure> measures)
    {
        this.measures = measures;
        
        for(Measure measure : measures) measure.getMetric().setStar(this);
    }
    
}
