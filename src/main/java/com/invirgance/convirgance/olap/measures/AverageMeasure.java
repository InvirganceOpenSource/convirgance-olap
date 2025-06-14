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
package com.invirgance.convirgance.olap.measures;

import com.invirgance.convirgance.olap.Measure;
import com.invirgance.convirgance.olap.Metric;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * Support for constructing average measures from metrics.
 * @author jbanes
 */
@Wiring
public class AverageMeasure extends Measure
{
    /**
     * Default constructor that specifies the averaging function but leaves
     * the name and the metric fields unassigned.
     */
    public AverageMeasure()
    {
        setFunction("avg");
    }
    
    /**
     * Constructs an AverageMeasure with a specified name and metric.
     * @param name the name assigned to the AverageMeasure as a String
     * @param metric the metric being averaged.
     */
    public AverageMeasure(String name, Metric metric)
    {
        super(name, metric, "avg");
    }
    
}
