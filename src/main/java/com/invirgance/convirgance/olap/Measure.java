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

/**
 * Provides support for aggregated quantitative values of data.
 * @author jbanes
 */
public class Measure
{
    private String name;
    private Metric metric;
    private String function;

    /**
     * Default constructor initializes an empty Measure with no assigned
     * attributes.
     */
    public Measure()
    {
    }

    /** 
     * Constructs a Measure with a specified name, associated Metric, and the 
     * function that generates the measure.
     * @param name the name of the measure as a String.
     * @param metric the associated Metric object.
     * @param function the name of the aggregating SQL function as a String.
     */
    public Measure(String name, Metric metric, String function)
    {
        this.name = name;
        this.metric = metric;
        this.function = function;
    }

    /**
     * Returns the star schema associated with the Measure.
     * @return Star object associated with the Measure.
     */
    public Star getStar()
    {
        return metric.getStar();
    }

    /**
     * Returns the name of the Measure as a String.
     * @return name of the Measure as a String.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Assigned a name to this Measure.
     * @param name the name to be assigned.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns the Metric associated with this Measure.
     * @return The Metric behind the Measure.
     */
    public Metric getMetric()
    {
        return metric;
    }

    /**
     * Sets the Metric associated with this Measure.
     * @param metric the Metric associated with the Measure.
     */
    public void setMetric(Metric metric)
    {
        this.metric = metric;
    }

    /**
     * Returns the function associated with the Measure.
     * @return the function associated with the Measure as a String.
     */
    public String getFunction()
    {
        return function;
    }

    /**
     * Assigns a function to the Measure object.
     * @param function the function associated with the measure as a String.
     */
    public void setFunction(String function)
    {
        this.function = function;
    }
}
