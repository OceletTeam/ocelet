// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CellAggregOperator.java

package fr.ocelet.runtime.raster;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;

public class CellAggregOperator
{

    public CellAggregOperator()
    {
    }
    
  

    public CellAggregOperator(AggregOperator<Double, List<Double>> operator, String name)
    {
        this.operator = operator;
        this.name = name;
    }

    public void setOperator(AggregOperator<Double, List<Double>> operator)
    {
        this.operator = operator;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Double apply(List<Double> values, Double val)
    {
        return (Double)operator.compute(values, val);
    }

    private AggregOperator<Double, List<Double>> operator;
    private String name;
}
