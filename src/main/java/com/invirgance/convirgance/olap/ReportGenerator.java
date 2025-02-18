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
 * Provides support for the SQL query generation from constructed star schemas.
 * @author jbanes
 */
public class ReportGenerator
{
    private Star star;
    private List<Dimension> dimensions = new ArrayList<>();
    private List<Measure> measures = new ArrayList<>();
    
    private boolean caseSensitive;

    /**
     * Constructs a ReportGenerator with a specified star schema.
     * @param star The Star object representing the star schema.
     */
    public ReportGenerator(Star star)
    {
        this.star = star;
    }

    /**
     * Returns true if the ReportGenerator is case sensitive
     * @return boolean.
     */
    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    /**
     * Sets the case sensitivity for the ReportGenerator.
     * @param caseSensitive boolean value.
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }
    
    /**
     * Adds a new dimension to the ReportGenerator object. The dimension provided must
     * be in the associated star schema to be added to the report generator.
     * @param dimension the dimension to be added to the report generator.
     */
    public void addDimension(Dimension dimension)
    {
        if(dimension.getStar() != star) throw new IllegalArgumentException("Dimensions must be part of Star");
        
        if(!dimensions.contains(dimension)) this.dimensions.add(dimension);
    }
    
    /**
     * Adds a measure to this report generator. The measure provided must already 
     * be in the associated star schema.
     * @param measure the Measure to be added to the report generator.
     */
    public void addMeasure(Measure measure)
    {
        if(measure.getStar() != star) throw new IllegalArgumentException("Measures must be part of Star");
        
        if(!this.measures.contains(measure)) this.measures.add(measure);
    }
    
    /**
     * Generates the SQL query as a String using the dimensions and measures
     * from this report generator.
     * @return the SQL query as a string.
     */
    public String getSQL()
    {
        SQLGenerator generator = new SQLGenerator();
        
        generator.setCaseSensitive(caseSensitive);
        generator.setForceGroupBy(true);
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
