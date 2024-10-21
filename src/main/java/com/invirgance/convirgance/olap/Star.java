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
 *
 * @author jbanes
 */
public class Star
{
    private Table fact;
    private List<Dimension> dimensions = new ArrayList<>();
    private List<Metric> metrics = new ArrayList<>();
    private List<Measure> measures = new ArrayList<>();

    public Star()
    {
    }

    public Star(Table fact)
    {
        this.fact = fact;
    }

    public Table getFact()
    {
        return fact;
    }

    public void setFact(Table fact)
    {
        this.fact = fact;
    }
    
    public Dimension getDimension(String name)
    {
        for(Dimension dimension : this.dimensions)
        {
            if(dimension.getName().equals(name)) return dimension;
        }
        
        return null;
    }

    public List<Dimension> getDimensions()
    {
        return dimensions;
    }
    
    public void addDimension(Dimension dimension)
    {
        if(!this.dimensions.contains(dimension))
        {
            this.dimensions.add(dimension);
            dimension.setStar(this);
        }
    }

    public void setDimensions(List<Dimension> dimensions)
    {
        this.dimensions = dimensions;
        
        for(Dimension dimension : dimensions) dimension.setStar(this);
    }

    public List<Metric> getMetrics()
    {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics)
    {
        this.metrics = metrics;
        
        for(Metric metric : metrics) metric.setStar(this);
    }

    public Measure getMeasure(String name)
    {
        for(Measure measure : this.measures)
        {
            if(measure.getName().equals(name)) return measure;
        }
        
        return null;
    }
    
    public List<Measure> getMeasures()
    {
        return measures;
    }
    
    public void addMeasure(Measure measure)
    {
        if(!this.measures.contains(measure)) 
        {
            this.measures.add(measure);
            measure.getMetric().setStar(this);
        }
    }

    public void setMeasures(List<Measure> measures)
    {
        this.measures = measures;
        
        for(Measure measure : measures) measure.getMetric().setStar(this);
    }
    
}
