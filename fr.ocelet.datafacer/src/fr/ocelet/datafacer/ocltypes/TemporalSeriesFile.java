package fr.ocelet.datafacer.ocltypes;

import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.geotools.coverage.grid.GridGeometry2D;

import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.GridManager;
import fr.ocelet.runtime.raster.ORaster;
import fr.ocelet.runtime.relation.OcltRole;
import fr.ocelet.runtime.util.FileUtils;

public abstract class TemporalSeriesFile {
	
	
	protected int index = 0;
	protected String path;
	protected ORaster raster;
	protected Grid grid;
	protected File sourceFile;
	protected String[] names;
	protected Double[] bounds;
	protected HashMap<String, ParsedName> parsedNames = new HashMap<String, ParsedName>();
	protected int numGrid;
	
	protected KeyMap<String, Integer> matchedBand = new KeyMap<String, Integer>();

	  public TemporalSeriesFile(String fileName){
	    
	        path = fileName;
	      	File file = new File(FileUtils.applyOutput(path));
	        names = file.list();
	        File[] files = file.listFiles();
ArrayList<String> tempNames = new ArrayList<String>();
for(File f : files){
	if(!f.isDirectory()){
		tempNames.add(f.getName());

	}
}
Collections.sort(tempNames);
	names = tempNames.toArray(new String[tempNames.size()]);
	      init();
	    }

	    public void setProperties(Grid grid1, OcltRole ocltrole){
	    
	    }
	    
	   public void reset(){
		   index = 0;
	   }
	    public String getCurrentName(){
	    	return names[index];
	    }

	    public Double readDouble(int x, int y, int band){
	    
	        return Double.valueOf(raster.getDoubleValue(x, y, band));
	    }

	    public Double readDouble(int x, int y, String band) {
	    
	        int iBand = Integer.valueOf(band).intValue();
	        return Double.valueOf(raster.getDoubleValue(x, y, iBand));
	    }

	    public int getWidth()
	    {
	        return raster.getMaxPixel(0);
	    }

	    public int getHeight()
	    {
	        return raster.getMaxPixel(1);
	    }

	    public GridGeometry2D getGridGeometry()
	    {
	        return raster.getGridGeometry();
	    }

	    public WritableRaster getRaster()
	    {
	        return raster.getWritableRaster();
	    }
	    
	    public boolean hasNext(){
	    	if(index < names.length){
	    		return true;
	    	}
	    	return false;
	    }

	    public void next(){
	    	index ++;
	    }
	    
	    public void update(){
	    	currentGrid();
	    	Grid grid = GridManager.getInstance().get(numGrid);
	    	ORaster raster = new ORaster(FileUtils.applyOutput(path+"/"+names[index]));
	    	
	    	grid.copy(raster, matchedBand);
	    }
	    
	    public void mask(String mask){
	    
	    	int lastIndex = 0;
	    	
	    	String decoupe = mask.substring(1, mask.length());
	    	
	    	int count = 0;
	    	int startIndex = 0;
	    	while(decoupe.contains("%")){
	    		
	    		lastIndex = decoupe.indexOf("%");
	    		String val = decoupe.substring(startIndex, lastIndex);
	    		decoupe = decoupe.substring(lastIndex, decoupe.length());
	    		// = decoupe.indexOf("%");
	    		String ref = decoupe.substring(0, lastIndex);
	    	}
	    }
	    
	    public class ParsedName{
	    	
	    	private HashMap<String, String> parseMap = new HashMap<String, String>();
	    	
	    	public void add(String id, String value){
	    		
	    		parseMap.put(id, value);
	    	}
	    	
	    	public boolean isEqual(String params){
	    		return false;
	    	}
	    	
	    	
	    }
	    
	 public void setFileIndex(Integer index){
		 this.index = index;
	 }
	    public Integer getIndex(){
	    	return index;
	    }
	    
	    public Integer size(){
	    	return names.length;
	    }
	    
	    public void setFileName(String name){
	    	for(int i = 0; i < names.length; i ++){
	    		if(names[i].equals(name)){
	    			index = i;
	    		}
	    	}
	    }
	    
	    public abstract void currentGrid();
	    
	    public abstract void init();
	    
}
