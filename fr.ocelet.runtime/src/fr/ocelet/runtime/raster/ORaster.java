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

import com.sun.media.jai.codecimpl.util.RasterFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import fr.ocelet.runtime.geom.SpatialManager;

import java.awt.image.DataBufferDouble;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.Operations;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

// Referenced classes of package fr.ocelet.runtime.raster:
//            Grid

public class ORaster {

	
    int range[];
    private double pX;
    private double pY;  
    private Raster raster;
    int cpt;
    private String path;
    private AbstractGridCoverage2DReader reader;
    private GridGeometry2D geometry2D;
    private Double[] bounds = new Double[4];
    private CoordinateReferenceSystem crs;
    private GridCoverage2D coverage;
    private int minGridX;
    private int minGridY;
    private int maxGridX;
    private int maxGridY;
    private int gridWidth;
    private int gridHeight;

    
    public int getGridMinX() {
    	return minGridX;
    }
    
    public int getGridMinY() {
    	return minGridY;
    }
    
    public int getGridMaxX() {
    	return maxGridX;
    }
    
    public int getGridMaxY() {
    	return maxGridY;
    }
    
    public int getGridWidth() {
    	return gridWidth;
    }
    
    public int getGridHeight() {
    	return gridHeight;
    }
    public ORaster(File file){
    
        range = new int[4];
        cpt = 0;
        AbstractGridFormat format = GridFormatFinder.findFormat(file);
        
        reader = format.getReader(file);
        bounds[0] = getMinimum(0);
        bounds[1] = getMinimum(1);
        bounds[2] = getMaximum(0);
        bounds[3] = getMaximum(1);
          
        setPixelsize();
    }
    
    public void setCRS(CoordinateReferenceSystem crs) {
    	this.crs = crs;
    }
    
    public CoordinateReferenceSystem getCRS() {
    	return crs;
    }
   
    public ORaster(String path){
    
        range = new int[4];
        cpt = 0;

        
        File file = new File(path);
        this.path = path;
       
        AbstractGridFormat format = GridFormatFinder.findFormat(file);
      
     
        
        reader = format.getReader(file);
     
       
            						   
        getRaster();
        setPixelsize();
    }
    
    public ORaster(String path, Double[] bounds){
        
        range = new int[4];
        cpt = 0;

        
        File file = new File(path);
        this.path = path;
       
        AbstractGridFormat format = GridFormatFinder.findFormat(file);
      
     
        
        reader = format.getReader(file);
     
       
            						   
        getRaster(bounds[0], bounds[1], bounds[2], bounds[3]);
       // setPixelsize();
    }

    private ORaster(AbstractGridCoverage2DReader reader, Double bounds[], double pX, double pY){
    
        range = new int[4];
        cpt = 0;
        this.reader = reader;
        this.bounds = bounds;
        this.pX = pX;
        this.pY = pY;
 						   
    }
    
   
   public Double[] getWorldBounds() {
	   return bounds;
   }
    public double[] gridToWorld(int x, int y){
    	GridCoordinates2D gc = new GridCoordinates2D(x, y);
    	DirectPosition dp = null;
    	try {
			dp = geometry2D.gridToWorld(gc);
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(dp == null) {
    		return null;
    	}
    	return dp.getCoordinate();
    }
    public int[] worldToGrid(double x, double y){
    	
    	DirectPosition dp = new DirectPosition2D(x, y);
    	GridCoordinates2D gridCoord = null;
    	try {
		 gridCoord = geometry2D.worldToGrid(dp);
		} catch (InvalidGridGeometryException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    	 if(gridCoord == null) {
    		 return null;
    	 }
    	 return new int[]{gridCoord.x, gridCoord.y};
    }

   

    public double[] getDoubleValue(int x, int y, int width, int height)
    {
        //Raster raster = getRaster();
        double pixels[] = new double[1];
        return raster.getPixels(x, y, width, height, pixels);
    }

    public int[] getIntValue(int x, int y, int width, int height)
    {
        //Raster raster = getRaster();
        int pixels[] = new int[1];
        return raster.getPixels(x, y, width, height, pixels);
    }

    public float[] getFloatValue(int x, int y, int width, int height)
    {
        //Raster raster = getRaster();
        float pixels[] = new float[1];
        return raster.getPixels(x, y, width, height, pixels);
    }

    public double getDoubleValue(int x, int y, int band)
    {
        return raster.getSampleDouble(x, y, band);
    }

    public float getFloat(int x, int y, int band)
    {
        return raster.getSampleFloat(x, y, band);
    }

    public double getIntValue(int x, int y, int band)
    {
        return (double)raster.getSample(x, y, band);
    }

    private Raster getRaster()
    {
       
        //if(raster != null)
           // return raster;
       // GridCoverage2D coverage = null;
        try
        {
            coverage = reader.read(null);
           
        }
        catch(IllegalArgumentException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        /*System.out.println("resample");
    	if(!coverage.getCoordinateReferenceSystem().getName().equals(SpatialManager.getCrs().getName())) {
    			
    			coverage = (GridCoverage2D) Operations.DEFAULT.resample(coverage,SpatialManager.getCrs());
    		}
    	   System.out.println("end resample");*/
	 geometry2D = coverage.getGridGeometry();
        RenderedImage rendImage = coverage.getRenderedImage();
        bounds[0] = geometry2D.getEnvelope2D().getMinX();
                bounds[1] = geometry2D.getEnvelope2D().getMinY();
                bounds[2] =geometry2D.getEnvelope2D().getMaxX();
                bounds[3] = geometry2D.getEnvelope2D().getMaxY();
        raster = rendImage.getData();
        this.minGridX = raster.getMinX();
        this.minGridY = raster.getMinY();
        this.maxGridX = raster.getMinX() + raster.getWidth() - 1;
        this.maxGridY = raster.getMinY() + raster.getHeight() - 1;
        this.gridWidth = raster.getWidth();
        this.gridHeight = raster.getHeight();
        
     
        return raster;
    }
    
    private Raster getRaster(Double bMinX, Double bMinY, Double bMaxX, Double bMaxY){
    	
    
    
    	Double minX = bMinX;
    	Double minY = bMinY;
    	Double maxX = bMaxX;
    	Double maxY = bMaxY;
    	
      
       // if(raster != null)
          //  return raster;
       // GridCoverage2D coverage = null;
        try
        {
            coverage = reader.read(null);
           
        }
        catch(IllegalArgumentException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
      /*  System.out.println("resample");
	if(!coverage.getCoordinateReferenceSystem().getName().equals(SpatialManager.getCrs().getName())) {
			
			coverage = (GridCoverage2D) Operations.DEFAULT.resample(coverage,SpatialManager.getCrs());
		}
	   System.out.println("end resample");*/
	 geometry2D = coverage.getGridGeometry();
    	double x = geometry2D.getEnvelope().getLowerCorner().getCoordinate()[0];
    	double y = geometry2D.getEnvelope().getLowerCorner().getCoordinate()[1];
    	double x2 = geometry2D.getEnvelope().getUpperCorner().getCoordinate()[0];
    	double y2 = geometry2D.getEnvelope().getUpperCorner().getCoordinate()[1];
    	
    	double xRes = (x2 - x) / geometry2D.getGridRange2D().getBounds().getWidth();
    	double yRes = (y2 - y) /geometry2D.getGridRange2D().getBounds().getHeight();
    	double precisionScale = 0.05;
    	double precision1 = 1.0 - precisionScale;
    	double precision2 = 1.0+ precisionScale;
    	
    	
		
		
    	
       if(x >= minX || Math.abs(x - minX) < Math.abs(xRes / 2) ){
    	   
    	   
        	minX = x + xRes / 2;
        	minGridX = coverage.getGridGeometry().getGridRange2D().x;
        	// minX = geometry2D.getEnvelope().getLowerCorner().getCoordinate()[0];
        	
        }
        if(y >= minY ||  Math.abs(y - minY) < Math.abs(yRes / 2)) {
        
        	minY = y + yRes / 2;
        	minGridY = coverage.getGridGeometry().getGridRange2D().y;
        	
        }
        if(x2 <= maxX ||  Math.abs(x2 - maxX) < Math.abs(xRes / 2)	) {
        	maxX = x2 - xRes / 2;
        	maxGridX = coverage.getGridGeometry().getGridRange2D().x + coverage.getGridGeometry().getGridRange2D().width - 1;
        	//maxX = geometry2D.getEnvelope().getUpperCorner().getCoordinate()[0] ;
        }
        if(y2 <= maxY || Math.abs(y2 - maxY) < Math.abs(yRes / 2)) {
        	maxY = y2 - yRes / 2;
        	maxGridY = coverage.getGridGeometry().getGridRange2D().y + coverage.getGridGeometry().getGridRange2D().height - 1;
        	//maxY = geometry2D.getEnvelope().getUpperCorner().getCoordinate()[1] ;
        }
       
  
        java.awt.Rectangle rect = new java.awt.Rectangle();
        if(minX <= x) {
        	minX =  x + xRes / 2;
        }
        if(minY <= y) {
        	minY =  y + yRes / 2;
        }
        if(maxX <= x2) {
        	maxX = x2 - xRes / 2;
        }
        if(maxY <= y2) {
        	maxY =  y2 - yRes / 2;
        }
        
        DirectPosition2D min = new DirectPosition2D(minX, minY);
        DirectPosition2D max = new DirectPosition2D(maxX, maxY);
        
      
        GridCoordinates2D dp1 = null;
		try {
			dp1 = geometry2D.worldToGrid(min);
			
		} catch (InvalidGridGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        GridCoordinates2D dp2 = null;
		try {
			dp2 = geometry2D.worldToGrid(max);
			
		} catch (InvalidGridGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DirectPosition newMin = null;
		try {
			newMin = geometry2D.gridToWorld(dp1);
		} catch (TransformException e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
		DirectPosition newMax = null;
		try {
			 newMax = geometry2D.gridToWorld(dp2);
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		this.bounds[0] = newMin.getCoordinate()[0] - xRes / 2;
	    this.bounds[1] = newMin.getCoordinate()[1] - yRes / 2;
	    this.bounds[2] = newMax.getCoordinate()[0] + xRes / 2;
	    this.bounds[3] = newMax.getCoordinate()[1] + yRes / 2;
		this.pX = xRes;
		this.pY = yRes;
		 min = new DirectPosition2D(bounds[0] + xRes / 2, bounds[1] + xRes / 2);
	     max = new DirectPosition2D(bounds[2] - xRes / 2, bounds[3] - yRes / 2);
		try {
			dp1 = geometry2D.worldToGrid(min);
		} catch (InvalidGridGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		try {
			dp2 = geometry2D.worldToGrid(max);
		} catch (InvalidGridGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       this.minGridX = dp1.x;
       this.minGridY = dp2.y;
       this.maxGridX = dp2.x;
       this.maxGridY = dp1.y;
       this.gridWidth = maxGridX - minGridX + 1;
       this.gridHeight = maxGridY - minGridY + 1;
    
        rect.setBounds(minGridX, minGridY, gridWidth, gridHeight);   
       
        RenderedImage rendImage = coverage.getRenderedImage();   
        raster = rendImage.getData(rect);
        
        return raster;
    }
    public void printWorldBounds() {
		//System.out.println(bounds[0]+ " "+bounds[1]+ " "+bounds[2]+ " "+bounds[3]);
	}
    public int numBands()
    {
        return raster.getNumBands();
    }

    

    public int[] getPixelBounds(Polygon polygon)
    {
        Coordinate scaleCoor[] = scalePolygon(polygon);
        int bounds[] = getBounds(scaleCoor);
        boolean inBounds = false;
        for(int i = bounds[0]; i < bounds[2]; i++)
        {
            for(int j = bounds[1]; j < bounds[3]; j++)
                if(polygon.intersects(getPoint(new Coordinate(i, j))))
                    inBounds = true;

        }

        if(!inBounds)
            return scaledCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
        else
            return bounds;
    }

    public Integer[] getIntegerValue(Polygon polygon, int band){
    
       
        ArrayList values = new ArrayList();
        Coordinate scaleCoor[] = scalePolygon(polygon);
        int bounds[] = getBounds(scaleCoor);
        for(int i = bounds[0]; i < bounds[2]; i++){
        
            for(int j = bounds[1]; j < bounds[3]; j++)
                if(polygon.intersects(getPoint(new Coordinate(i, j))))
                    values.add(Integer.valueOf(raster.getSample(i, j, band)));

        }

        if(values.isEmpty())
        {
            int scaledCentroid[] = scaledCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
            values.add(Integer.valueOf(raster.getSample(scaledCentroid[0], scaledCentroid[1], band)));
        }
        return (Integer[])values.toArray(new Integer[values.size()]);
    }

    public Float[] getFloatValue(Polygon polygon, int band)
    {
      
        ArrayList values = new ArrayList();
        Coordinate scaleCoor[] = scalePolygon(polygon);
        int bounds[] = getBounds(scaleCoor);
        for(int i = bounds[0]; i < bounds[2]; i++)
        {
            for(int j = bounds[1]; j < bounds[3]; j++)
                if(polygon.intersects(getPoint(new Coordinate(i, j))))
                    values.add(Float.valueOf(raster.getSampleFloat(i, j, band)));

        }

        if(values.isEmpty())
        {
            int scaledCentroid[] = scaledCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
            values.add(Float.valueOf(raster.getSampleFloat(scaledCentroid[0], scaledCentroid[1], band)));
        }
        return (Float[])values.toArray(new Float[values.size()]);
    }

    public double[] getDoubleBounds(Coordinate coordinates[])
    {
        double bounds[] = new double[4];
        double minX = 1.7976931348623157E+308D;
        double minY = 1.7976931348623157E+308D;
        double maxX = 0.0D;
        double maxY = 0.0D;
        Coordinate acoordinate[];
        int j = (acoordinate = coordinates).length;
        for(int i = 0; i < j; i++)
        {
            Coordinate c = acoordinate[i];
            if(c.x > maxX)
                maxX = (int)c.x;
            if(c.y > maxY)
                maxY = (int)c.y;
            if(c.x < minX)
                minX = (int)c.x;
            if(c.y < minY)
                minY = (int)c.y;
        }

        bounds[0] = minX;
        bounds[1] = minY;
        bounds[2] = maxX;
        bounds[3] = maxY;
        return bounds;
    }

    public int[] getBounds(Coordinate coordinates[])
    {
        int bounds[] = new int[4];
        int minX = 0x7fffffff;
        int minY = 0x7fffffff;
        int maxX = 0;
        int maxY = 0;
        Coordinate acoordinate[];
        int j = (acoordinate = coordinates).length;
        for(int i = 0; i < j; i++)
        {
            Coordinate c = acoordinate[i];
            if(c.x > (double)maxX)
                maxX = (int)c.x;
            if(c.y > (double)maxY)
                maxY = (int)c.y;
            if(c.x < (double)minX)
                minX = (int)c.x;
            if(c.y < (double)minY)
                minY = (int)c.y;
        }

        bounds[0] = minX - 1;
        bounds[1] = minY - 1;
        bounds[2] = maxX + 1;
        bounds[3] = maxY + 1;
        return bounds;
    }

    private Point getPoint(Coordinate coordinate)
    {
        Coordinate c[] = new Coordinate[1];
        c[0] = coordinate;
        com.vividsolutions.jts.geom.CoordinateSequence cs = new CoordinateArraySequence(c);
        return new Point(cs, new GeometryFactory());
    }

    public double getMaximum(int dimension)
    {
        return reader.getOriginalEnvelope().getMaximum(dimension);
    }

    public double getMinimum(int dimension)
    {
        return reader.getOriginalEnvelope().getMinimum(dimension);
    }

    public int getMinPixel(int dimension)
    {
    	
        return reader.getOriginalGridRange().getLow().getCoordinateValue(dimension);
    }

    public int getMaxPixel(int dimension)
    {
        return reader.getOriginalGridRange().getHigh().getCoordinateValue(dimension);
    }

    public double rangeFactor(int dim)
    {
    	
    	
        double range = getMaximum(dim) - getMinimum(dim);
        double pxRange = getMaxPixel(dim) - getMinPixel(dim);
        return range / pxRange;
    }

    private void setPixelsize()
    {
    	Raster r = raster;
    	int h = r.getHeight();
    	int w = r.getWidth();
    	int minX = r.getMinX();
    	int minY = r.getMinX();
    			DirectPosition px1 = null;
    	DirectPosition px2 = null;
    	try {
			px1 = geometry2D.gridToWorld(new GridCoordinates2D(minX, minY));
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			px2 = geometry2D.gridToWorld(new GridCoordinates2D(minX + 1 , minY + 1));
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//System.out.println("pix diff "+(px2.getCoordinate()[0] - px1.getCoordinate()[0])+" "+(px1.getCoordinate()[1] - px2.getCoordinate()[1]));

    	double x = geometry2D.getEnvelope().getLowerCorner().getCoordinate()[0];
    	double y = geometry2D.getEnvelope().getLowerCorner().getCoordinate()[1];
    	double x2 = geometry2D.getEnvelope().getUpperCorner().getCoordinate()[0];
    	double y2 = geometry2D.getEnvelope().getUpperCorner().getCoordinate()[1];
    	
    	double xRes = (x2 - x) / r.getWidth();
    	double yRes = (y2 - y) / r.getHeight();
    	Integer precision = 10000;
    	Integer scaleX = (int)Math.round(xRes * precision);
    	Integer scaleY = (int)Math.round(yRes * precision);
    	double lx = scaleX.doubleValue() / precision.doubleValue();
    	double ly = scaleY.doubleValue() / precision.doubleValue();
    	//System.out.println("scaling res "+xRes+" "+yRes);
    	pX = xRes;
    	pY =yRes;

    }

    public int[] scaledCoordinate(double x, double y)
    {
        int iX = (int)((x - bounds[0]) / pX);
        int iY = (int)((y - bounds[1]) / pY);
        return (new int[] {
            iX, iY
        });
    }

    public Coordinate inverseScale(int x, int y)
    {
        double dX = (double)x * pX + bounds[0];
        double dY = (double)y * pY + bounds[1];
        return new Coordinate(dX, dY);
    }

    public double dimensionScale(int dimension)
    {
        double range = getMaximum(dimension) - getMinimum(dimension);
        int pixelRange = getMaxPixel(dimension) - getMinPixel(dimension);
        return range / (double)pixelRange;
    }

    public Coordinate[] scalePolygon(Polygon polygon)
    {
        Coordinate extCoordinates[] = polygon.getExteriorRing().getCoordinates();
        Coordinate coordinates[] = new Coordinate[extCoordinates.length];
        int index = 0;
        Coordinate acoordinate[];
        int j = (acoordinate = extCoordinates).length;
        for(int i = 0; i < j; i++)
        {
            Coordinate c = acoordinate[i];
            int scaled[] = scaledCoordinate(c.x, c.y);
            coordinates[index] = new Coordinate(scaled[0], scaled[1]);
            index++;
        }

        return coordinates;
    }

    public Polygon scaleTo(Polygon polygon)
    {
        Coordinate coords[] = scalePolygon(polygon);
        com.vividsolutions.jts.geom.CoordinateSequence cs = new CoordinateArraySequence(coords);
        LinearRing lr = new LinearRing(cs, new GeometryFactory());
        return new Polygon(lr, null, new GeometryFactory());
    }

    public void change(String fileName)
        throws IOException
    {
        File file = new File(fileName);
     
        WritableRaster create = raster.createCompatibleWritableRaster();
        RasterFactory.createRaster(raster.getSampleModel(), raster.getDataBuffer(), null);
        GeoTiffWriter writer = new GeoTiffWriter(file);
        CharSequence cs = new String("one");
        (new GridCoverageFactory()).create(cs, create, reader.getOriginalEnvelope());
        getCoverage();
        writer.dispose();
    }

    public GridCoverage2D getCoverage()
    {
       if(coverage == null) {
        try
        {
            coverage = reader.read(null);
        }
        catch(IllegalArgumentException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            //Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
       }
        return coverage;
    }

   

 
    



   
   

    public Coordinate[] cboundary(Polygon polygon)
    {
        Coordinate bounds[] = new Coordinate[2];
        double minX = (1.0D / 0.0D);
        double minY = (1.0D / 0.0D);
        double maxX = 0.0D;
        double maxY = 0.0D;
        Coordinate acoordinate[];
        int j = (acoordinate = polygon.getExteriorRing().getCoordinates()).length;
        for(int i = 0; i < j; i++)
        {
            Coordinate c = acoordinate[i];
            if(c.x < minX)
                minX = c.x;
            if(c.x > maxX)
                maxX = c.x;
            if(c.y < minY)
                minY = c.y;
            if(c.y > maxY)
                maxY = c.y;
        }

        bounds[0] = new Coordinate(minX, minY);
        bounds[1] = new Coordinate(maxX, maxY);
        return bounds;
    }

    public int reverse(int coord)
    {
        return getMaxPixel(1) - coord;
    }

    /*public ORaster toZone(int x, int y, int width, int height, double bounds[])
    {
        ORaster raster = new ORaster(reader, bounds, pX, pY);
        raster.zoneRaster(getRaster(), x, y, width, height);
        return raster;
    }*/

    public int[] getRGB()
    {
        GridCoverage2D cov = null;
        try
        {
            cov = reader.read(null);
        }
        catch(IOException giveUp)
        {
            throw new RuntimeException(giveUp);
        }
        int numBands = cov.getNumSampleDimensions();
        if(numBands < 3)
            return null;
        String sampleDimensionNames[] = new String[numBands];
        for(int i = 0; i < numBands; i++)
        {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }

        int channelNum[] = {
            -1, -1, -1
        };
        for(int i = 0; i < numBands; i++)
        {
            String name = sampleDimensionNames[i].toLowerCase();
            if(name != null)
                if(name.matches("red.*"))
                    channelNum[0] = i + 1;
                else
                if(name.matches("green.*"))
                    channelNum[1] = i + 1;
                else
                if(name.matches("blue.*"))
                    channelNum[2] = i + 1;
        }

        System.out.println((new StringBuilder("RED ")).append(channelNum[0]).toString());
        System.out.println((new StringBuilder("GR ")).append(channelNum[1]).toString());
        System.out.println((new StringBuilder("BL")).append(channelNum[2]).toString());
        if(channelNum[0] < 0 || channelNum[1] < 0 || channelNum[2] < 0)
        {
            channelNum[0] = 1;
            channelNum[1] = 2;
            channelNum[2] = 3;
        }
        return channelNum;
    }

    

 

   

    public GridGeometry2D getGridGeometry()
    {
        return getCoverage().getGridGeometry();
    }

  

    public double getXRes(){
    	return pX;
    }
    
    public double getYRes(){
    	return pY;
    }
    
    @Override
    public String toString() {
    	
    	String s = "";
    	s = path+"\n";
    	s = s+ "Grid bounds "+minGridX+" "+minGridY+" "+maxGridX+" "+maxGridY+" w "+gridWidth+" h "+gridHeight+" \n";
    	s = s +" world bounds "+bounds[0]+" "+bounds[1]+" "+bounds[2]+" "+bounds[3];
    	return s;
    }

}
