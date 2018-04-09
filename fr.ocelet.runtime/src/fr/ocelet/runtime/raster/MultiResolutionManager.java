/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
*
*  This software is a domain specific programming language dedicated to writing
*  spatially explicit models and performing spatial dynamics simulations.
*
*  This software is governed by the CeCILL license under French law and
*  abiding by the rules of distribution of free software.  You can  use,
*  modify and/ or redistribute the software under the terms of the CeCILL
*  license as circulated by CEA, CNRS and INRIA at the following URL
*  "http://www.cecill.info".
*  As a counterpart to the access to the source code and  rights to copy,
*  modify and redistribute granted by the license, users are provided only
*  with a limited warranty  and the software's author,  the holder of the
*  economic rights,  and the successive licensors  have only limited
*  liability.
*  The fact that you are presently reading this means that you have had
*  knowledge of the CeCILL license and that you accept its terms.
*/
package fr.ocelet.runtime.raster;

import java.util.ArrayList;

import fr.ocelet.runtime.relation.CellValues;

public class MultiResolutionManager {
	
    private CellValues[] firstLine;
    private CellValues[] secondLine;
    private CellValues[] thirdLine;
    private ArrayList<String> properties;
    private int width;
    private int start;
    private int end;
    private int currentY;
    
    public void add(int x, int y, String name, double value){
    	
    	if(y == currentY){
    		//System.out.println("add 1");
    		addFirstLine(x, name, value);
    	}
    	else if(y == currentY + 1){
    		addSecondLine(x, name, value);
    		//System.out.println("add 2");
    	}
    	else if(y == currentY + 2){
    		addThirdLine(x, name, value);
    		//System.out.println("add 3");
    	}
    else{
    		//System.out.println("WARNING : Unable to reference world coordinate "+x +" "+y+" "+currentY );
    	}
    }
    
    
    public void setFirstLine(int x, String name, double value){
    	
    }
    
    public void switchLine(){
    	
    	currentY = currentY + 1;
    	CellValues[] temp1 = secondLine;
    	CellValues[] temp2 = thirdLine;
    	
    	thirdLine = firstLine;
    	firstLine = temp1;
    	secondLine = temp2;
    	
    	//clearFirstLineProperties();
    	//thirdLine = firstLine;
    	//firstLine = temp1;
    	//secondLine = temp2;
    	//clearThirdLineProperties();
    }
   
   public MultiResolutionManager(int width, ArrayList<String> properties){
    	this.width = width + 1;
    	this.properties = properties;
    	init();
    }
    
    public MultiResolutionManager(int start, int end, int currentY,ArrayList<String> properties){
    	this.start = start;
    	this.end = end;
    	this.width = end - start + 1;
    	this.properties = properties;
    	
    	this.currentY = currentY;
    	init();
    }
    public void addFirstLine(int x, String name, Double value){
    	firstLine[x - start].add(name,  value);
    }
    
    public void addSecondLine(int x, String name, Double value){
    	secondLine[x - start].add(name,  value);
    }
    
    public void addThirdLine(int x, String name, Double value){
    	thirdLine[x - start].add(name,  value);
    }
    
    public CellValues getFirstLineValue(int x){
    	return firstLine[x - start];
    }
    
    public CellValues getSecondLineValue(int x){
    	return secondLine[x - start];
    }
    
    public CellValues getThirdLineValue(int x){
    	return thirdLine[x - start];
    }
    
    
    
    public void resetAllTempValues(){
    	for(int i = 0; i < width; i ++){
    		firstLine[i] = new CellValues();
    		secondLine[i] = new CellValues();
    		thirdLine[i] = new CellValues();
    		for(String name : properties){
    			firstLine[i].set(name);
    			secondLine[i].set(name);
    			thirdLine[i].set(name);

    		}
    	}
    }
    public void init(){
    	 firstLine = new CellValues[width];
    	secondLine = new CellValues[width];
    	thirdLine = new CellValues[width];

    	for(int i = 0; i < width; i ++){
    		firstLine[i] = new CellValues();
    		secondLine[i] = new CellValues();
    		thirdLine[i] = new CellValues();
    		for(String name : properties){
    			firstLine[i].set(name);
    			secondLine[i].set(name);
    			thirdLine[i].set(name);
    		}
    	}
    }
    
    
    public void clearFirstLineName(String name){
    	for(int i = 0; i < width; i ++){
    		firstLine[i].clear(name);
    	}
    }
    public void clearFirstLineProperties(){
    	for(int i = 0; i < width; i ++){
    		for(String name : properties){
    			firstLine[i].clear(name);
    		}
    	}
    }
    
    public void clearSecondLineName(String name){
    	for(int i = 0; i < width; i ++){
    		secondLine[i].clear(name);
    	}
    }
    public void clearSecondLineProperties(){
    	for(int i = 0; i < width; i ++){
    		for(String name : properties){
    			secondLine[i].clear(name);
    		}
    	}
    }
    
    public void clearThirdLineName(String name){
    	for(int i = 0; i < width; i ++){
    		thirdLine[i].clear(name);
    	}
    }
    public void clearThirdLineProperties(){
    	for(int i = 0; i < width; i ++){
    		for(String name : properties){
    			thirdLine[i].clear(name);
    		}
    	}
    }
   public ArrayList<String> getProperties(){
	   return properties;
   }
   /* public MultiResolutionManager(int width, ArrayList<String> properties){
    	this.width = width + 1;
    	this.properties = properties;
    	init();
    }
    
    public MultiResolutionManager(int start, int end, ArrayList<String> properties){
    	this.start = start;
    	this.end = end + 1;
    	this.width = end - start;
    	this.properties = properties;
    	init();
    }
    
    public void add(int x, String name, Double value){
        get(x).add(name, value);       
    }
    
    public CellValues get(int x){
    	return globalLine[x];
    }
    public void init(){
    	globalLine = new CellValues[width];
    	for(int i = 0; i < width; i ++){
    		globalLine[i] = new CellValues();
    		for(String name : properties){
    			globalLine[i].set(name);
    		}
    	}
    }
    public void clear(String name){
    	for(int i = 0; i < width; i ++){
    		globalLine[i].clear(name);
    	}
    }
    public void clear(){
    	for(int i = 0; i < width; i ++){
    		for(String name : properties){
    			globalLine[i].clear(name);
    		}
    	}
    }
   public ArrayList<String> getProperties(){
	   return properties;
   }*/
    
   /* public void switchLine(){
    	CellValues[] temp = tempLine;
    	tempLine = globalLine;
    	globalLine = tempLine;
    }
   
   public MultiResolutionManager(int width, ArrayList<String> properties){
    	this.width = width + 1;
    	this.properties = properties;
    	init();
    }
    
    public MultiResolutionManager(int start, int end, ArrayList<String> properties){
    	this.start = start;
    	this.end = end;
    	this.width = end - start + 1;
    	this.properties = properties;
    	init();
    }
    public void addTempLine(int x, String name, Double value){
    	getTempValue(x).add(name, value);
    }
    public void add(int x, String name, Double value){
        get(x).add(name, value);       
    }
    public CellValues getTempValue(int x){
    	return tempLine[x - start];
    }
    public CellValues get(int x){
    			
    	return globalLine[x - start];
    }
    
    public void resetTempValues(){
    	for(int i = 0; i < width; i ++){
    		globalLine[i] = new CellValues();
    		tempLine[i] = new CellValues();
    		
    		for(String name : properties){
    			globalLine[i].set(name);
    			tempLine[i].set(name);
    		}
    	}
    }
    public void init(){
    	globalLine = new CellValues[width];
    	tempLine = new CellValues[width];
    	for(int i = 0; i < width; i ++){
    		globalLine[i] = new CellValues();
    		tempLine[i] = new CellValues();
    		
    		for(String name : properties){
    			globalLine[i].set(name);
    			tempLine[i].set(name);
    		}
    	}
    }
    
    
    public void clear(String name){
    	for(int i = 0; i < width; i ++){
    		globalLine[i].clear(name);
    	}
    }
    public void clear(){
    	for(int i = 0; i < width; i ++){
    		for(String name : properties){
    			globalLine[i].clear(name);
    		}
    	}
    }
   public ArrayList<String> getProperties(){
	   return properties;
   }*/
  
}
