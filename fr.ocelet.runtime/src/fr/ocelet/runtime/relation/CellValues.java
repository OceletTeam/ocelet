// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CellValues.java

package fr.ocelet.runtime.relation;

import fr.ocelet.runtime.ocltypes.List;
import java.util.*;

public class CellValues
{

    public CellValues()
    {
        values = new HashMap<String, List<Double>>();
    }

    public void set(String name)
    {
        List<Double> aList = new List<Double>();
        values.put(name, aList);
    }

    public void add(String name, Double value)
    {
        ((List<Double>)values.get(name)).add(value);
    }

    public void clear(String name)
    {
        ((List<Double>)values.get(name)).clear();
    }

    public List<Double> getValues(String name)
    {
        return (List<Double>)values.get(name);
    }

    public void clearAll()
    {
        String name;
        for(Iterator<String> iterator = values.keySet().iterator(); iterator.hasNext(); ((List<Double>)values.get(name)).clear())
            name = (String)iterator.next();

    }
    
    @Override
    public String toString(){
    	String s = "";
    	for(String name : values.keySet()){
    		s += values.get(name).toString();
    	}
    	return s;
    }

    private HashMap<String, List<Double>> values;
}
