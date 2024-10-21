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

import com.invirgance.convirgance.olap.sql.SQLGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbanes
 */
public class ReportGenerator
{
    private Star star;
    private List<Dimension> dimensions = new ArrayList<>();
    private List<Measure> measures = new ArrayList<>();

    public ReportGenerator(Star star)
    {
        this.star = star;
    }
    
    public void addDimension(Dimension dimension)
    {
        if(dimension.getStar() != star) throw new IllegalArgumentException("Dimensions must be part of Star");
        
        if(!dimensions.contains(dimension)) this.dimensions.add(dimension);
    }
    
    public void addMeasure(Measure measure)
    {
        if(measure.getStar() != star) throw new IllegalArgumentException("Measures must be part of Star");
        
        if(!this.measures.contains(measure)) this.measures.add(measure);
    }
    
    public String getSQL()
    {
        SQLGenerator generator = new SQLGenerator();
        
        generator.addTable(star.getFact());
        
        for(Dimension dimension : dimensions) 
        {
            generator.addSelect(dimension.getColumn(), dimension.getTable(), dimension.getName());
        }
        
        for(Measure measure : measures) 
        {
            generator.addAggregate(measure.getFunction(), measure.getMetric().getColumn(), measure.getMetric().getTable(), measure.getName());
        }
        
        return generator.getSQL();
    }
}
