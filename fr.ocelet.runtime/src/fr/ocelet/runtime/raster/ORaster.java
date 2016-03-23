// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ORaster.java

package fr.ocelet.runtime.raster;

import com.sun.media.jai.codecimpl.util.RasterFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

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

import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

// Referenced classes of package fr.ocelet.runtime.raster:
//            Grid

public class ORaster {

	
    int range[];
    private double pX;
    private double pY;
    private WritableRaster wRaster;
    private Raster raster;
    int cpt;
    private AbstractGridCoverage2DReader reader;
    private GridGeometry2D geometry2D;
    private Double[] bounds = new Double[4];

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

    public ORaster(String path){
    
        range = new int[4];
        cpt = 0;

        
        File file = new File(path);
     
        AbstractGridFormat format = GridFormatFinder.findFormat(file);
        
        reader = format.getReader(file);
     
        bounds[0] = getMinimum(0);
        bounds[1] = getMinimum(1);
        bounds[2] = getMaximum(0);
        bounds[3] = getMaximum(1);
            						   
        getRaster();
        setPixelsize();
    }

    private ORaster(AbstractGridCoverage2DReader reader, Double bounds[], double pX, double pY){
    
        range = new int[4];
        cpt = 0;
        this.reader = reader;
        this.bounds = bounds;
        this.pX = pX;
        this.pY = pY;
 						   
    }

    public Grid getGrid(double minX, double minY, double maxX, double maxY)
    {
        Grid grid = new Grid(minX, minY, maxX, maxY, getMaxPixel(1), getCoverage().getGridGeometry());
        bounds = (new Double[] {
            getMinimum(0), getMinimum(1), getMaximum(0), getMaximum(1)
        });
        setPixelsize();
        grid.setXRes((int)pX);
        grid.setYRes((int)pY);
        return grid;
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
    	return dp.getCoordinate();
    }
    public int[] worldToGrid(double x, double y){
    	
    	DirectPosition dp = new DirectPosition2D(x, y);
    	GridCoordinates2D gridCoord = null;
    	try {
		 gridCoord = geometry2D.worldToGrid(dp);
		} catch (InvalidGridGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    	 return new int[]{gridCoord.x, gridCoord.y};
    }

    /*private void fillGrid(Grid grid, double minX, double minY){
    	
    	raster = getRaster();
    	
    	DirectPosition dp = new DirectPosition2D(minX, minY);
    	GridCoordinates2D coords = null;
    	try {
			coords = geometry2D.worldToGrid(dp);
		} catch (InvalidGridGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	for(int i = 0; i < grid.getWidth(); i ++){
    		
    		for(int j = 0; j < grid.getHeight(); j ++){
    			
    			for(String name : grid.getInitialBands().keySet()){
    			grid.setCellValue(name, i, j, raster.getSampleDouble(i + coords.x, j + coords.y, grid.getInitialBands().get(name)));
    			}
    			
    		}
    		
    		
    		
    	}
    	
    	
    }*/
   /* public Grid getGrid(Double bounds[], String name, Grid grid)
    {
    	
    	
    	double minX = bounds[0];
    	double minY = bounds[1];
    	
    	int cellWidth =(int) Math.round((bounds[2] - bounds[0]) / pX);
    	int cellHeight = (int)Math.round(bounds[3] - bounds[1] / pY);
    	
    	double newMaxX = minX + (cellWidth * pX);
    	double newMaxY = minX + (cellHeight * pY);
    	
    	WritableRaster wr = GridGenerator.createRaster(grid.getInitialBands().keySet().size(), cellWidth, cellHeight);
    	Envelope2D env = GridGenerator.createEnvelope(minX, minY, newMaxX, newMaxY);
    	GridCoverage2D coverage = GridGenerator.createCoverage(name, wr, env);
    	
    	grid.setRaster(wr);
    	grid.setGridGeometry(coverage.getGridGeometry());
    	fillGrid(grid, minX, minY);
       
        return grid;
    }*/

    public Grid getFullGrid(){
    	GridGeometry2D gridGeom = getCoverage().getGridGeometry();
    	 this.bounds = (new Double[] {
                 getMinimum(0), getMinimum(1), getMaximum(0), getMaximum(1)
             });
    	 Grid grid = new Grid(bounds, gridGeom, this);
        
         setPixelsize();
         grid.setXRes((int)pX);
         grid.setYRes((int)pY);
         return grid;
    }
    private void zoneRaster(Raster raster, int x, int y, int width, int height)
    {
        wRaster = raster.createCompatibleWritableRaster(x, y, width + 1, height + 1);
    }

    public WritableRaster getRaster(int minX, int minY, int maxX, int maxY)
    {
        Raster iRaster = getRaster();
        WritableRaster r = null;
        if(minX > 2 && minY > 2)
        {
            r = getRaster().createCompatibleWritableRaster(minX - 2, minY - 2, maxX - minX - 2, maxY - minY - 2);
            for(int i = minX - 2; i < maxX + 2; i++)
            {
                for(int j = minY - 2; j < maxY + 2; j++)
                {
                    for(int b = 0; b < iRaster.getNumBands(); b++)
                        r.setSample(i, j, b, iRaster.getSampleDouble(i, j, b));

                }

            }

        } else
        {
            r = getRaster().createCompatibleWritableRaster(minX, minY, maxX - minX, maxY - minY);
            for(int i = minX; i < maxX; i++)
            {
                for(int j = minY; j < maxY; j++)
                {
                    for(int b = 0; b < iRaster.getNumBands(); b++)
                        r.setSample(i, j, b, iRaster.getSampleDouble(i, j, b));

                }
            }
        }
        return r;
    }

    public WritableRaster getRaster(int bounds[])
    {
        Raster iRaster = getRaster();
        int minX = bounds[0];
        int minY = bounds[1];
        int maxX = bounds[2];
        int maxY = bounds[3];
        WritableRaster r;
        if(minX > 2 && minY > 2)
        {
            r = getRaster().createCompatibleWritableRaster(minX - 2, minY - 2, (maxX - minX) + 4, (maxY - minY) + 4);
            for(int i = minX - 2; i < maxX + 2; i++)
            {
                for(int j = minY - 2; j < maxY + 2; j++)
                {
                    for(int b = 0; b < iRaster.getNumBands(); b++)
                        r.setSample(i, j, b, iRaster.getSampleDouble(i, j, b));

                }
            }

        } else
        {
            r = getRaster().createCompatibleWritableRaster(minX - 2, minY - 2, maxX - minX + 4, maxY - minY + 4);
            for(int i = minX; i < maxX; i++)
            {
                for(int j = minY; j < maxY; j++)
                {
                    for(int b = 0; b < iRaster.getNumBands(); b++)
                        r.setSample(i, j, b, iRaster.getSampleDouble(i, j, b));

                }

            }

        }
        return r;
    }
    
   

    public WritableRaster createRaster(int numBand, int width, int height)
    {
        java.awt.image.DataBuffer db2 = new DataBufferDouble(1, numBand);
        SampleModel sample = RasterFactory.createBandedSampleModel(4, width + 4, height + 4, numBand);
        Raster r = Raster.createRaster(sample, db2, null);
        return r.createCompatibleWritableRaster();
    }

    public void setCRS()
        throws FactoryException
    {
        String wkt = "GEOGCS[\"WGS 84\",  DATUM[    \"WGS_1984\",    SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],    TOWGS84[0,0,0,0,0,0,0],    AUTHORITY[\"EPSG\",\"6326\"]],  PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],  UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]],  AXIS[\"Lat\",NORTH],  AXIS[\"Long\",EAST],  AUTHORITY[\"EPSG\",\"4326\"]]";
        CRS.parseWKT(wkt);
    }
public double[] worldBounds(){
	Raster raster = getRaster();
	double[] worldBounds = new double[4];
	int[] bounds = getBounds();
	
	double[] min = gridToWorld(bounds[0], bounds[1]);
	double[] max = gridToWorld(bounds[2] - 1, bounds[3] - 1);


	double minX = min[0];
	double minY = max[1];	
	double maxX = max[0];
	double maxY = min[1];
	
System.out.println("bounds" +minX+" "+minY+" "+maxX+" "+maxY);

worldBounds[0] = minX;
worldBounds[1] = minY;
worldBounds[2] = maxX;
worldBounds[3] = maxY;
return worldBounds;

}
    public int[] getBounds()
    {
        Raster raster = getRaster();
        int bounds[] = {
            raster.getMinX(), raster.getMinY(), raster.getWidth() + raster.getMinX(), raster.getMinY() + raster.getHeight()
        };
        return bounds;
    }

    public double[] getDoubleValue(int x, int y, int width, int height)
    {
        Raster raster = getRaster();
        double pixels[] = new double[1];
        return raster.getPixels(x, y, width, height, pixels);
    }

    public int[] getIntValue(int x, int y, int width, int height)
    {
        Raster raster = getRaster();
        int pixels[] = new int[1];
        return raster.getPixels(x, y, width, height, pixels);
    }

    public float[] getFloatValue(int x, int y, int width, int height)
    {
        Raster raster = getRaster();
        float pixels[] = new float[1];
        return raster.getPixels(x, y, width, height, pixels);
    }

    public double getDoubleValue(int x, int y, int band)
    {
        return getRaster().getSampleDouble(x, y, band);
    }

    public float getFloat(int x, int y, int band)
    {
        return getRaster().getSampleFloat(x, y, band);
    }

    public double getIntValue(int x, int y, int band)
    {
        return (double)getRaster().getSample(x, y, band);
    }

    private Raster getRaster()
    {
        if(wRaster != null)
            return wRaster;
        if(raster != null)
            return raster;
        GridCoverage2D coverage = null;
        try
        {
            coverage = reader.read(null);
            geometry2D = coverage.getGridGeometry();
        }
        catch(IllegalArgumentException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        RenderedImage rendImage = coverage.getRenderedImage();
        raster = rendImage.getData();
        return raster;
    }

    public int numBands()
    {
        return getRaster().getNumBands();
    }

    public Double[] getDoubleValue(Polygon polygon, int band)
    {
        Raster raster = getRaster();
        ArrayList values = new ArrayList();
        Coordinate scaleCoor[] = scalePolygon(polygon);
        int bounds[] = getBounds(scaleCoor);
        for(int i = bounds[0]; i < bounds[2]; i++)
        {
            for(int j = bounds[1]; j < bounds[3]; j++)
                if(polygon.intersects(getPoint(new Coordinate(i, j))))
                    values.add(raster.getSampleDouble(i, j, band));

        }

        if(values.isEmpty())
        {
            int scaledCentroid[] = scaledCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
            values.add(Double.valueOf(raster.getSampleDouble(scaledCentroid[0], scaledCentroid[1], band)));
        }
        return (Double[])values.toArray(new Double[values.size()]);
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
    
        Raster raster = getRaster();
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
        Raster raster = getRaster();
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
        pX = rangeFactor(0);
        pY = rangeFactor(1);
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
        Raster raster = getRaster();
        WritableRaster create = raster.createCompatibleWritableRaster();
        RasterFactory.createRaster(raster.getSampleModel(), raster.getDataBuffer(), null);
        GeoTiffWriter writer = new GeoTiffWriter(file);
        CharSequence cs = new String("one");
        (new GridCoverageFactory()).create(cs, create, reader.getOriginalEnvelope());
        getCoverage();
        writer.dispose();
    }

    private GridCoverage2D getCoverage()
    {
        GridCoverage2D coverage = null;
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
        return coverage;
    }

    private void writeDoubleValue(int x, int y, int band, double value)
    {
        wRaster.setSample(x, y, band, value);
    }

    private void writeIntValue(int x, int y, int band, int value)
    {
        wRaster.setSample(x, y, band, value);
    }

    private void writeFloatValue(int x, int y, int band, float value)
    {
        wRaster.setSample(x, y, band, value);
    }

    public WritableRaster getWritableRaster()
    {
        return getCoverage().getRenderedImage().getData().createCompatibleWritableRaster();
    }

    public void writeValue(int x, int y, int band, Object value)
    {
        if(value instanceof Integer)
            writeIntValue(x, y, band, ((Integer)value).intValue());
        else
        if(value instanceof Double)
            writeDoubleValue(x, y, band, ((Double)value).doubleValue());
        else
        if(value instanceof Float)
            writeFloatValue(x, y, band, ((Float)value).floatValue());
        else
            System.out.println(" ERROR VALUE TYPE NOT ALLOWED");
    }

    public void edit()
    {
        wRaster = getWritableRaster();
        Raster raster = getCoverage().getRenderedImage().getData();
        wRaster.setRect(raster);
    }

    public void write(String fileName, String imageName)
    {
        GeneralParameterValue paramValues[] = getInitialParameters();
        File file = new File(fileName);
        GeoTiffWriter writer = null;
        try
        {
            writer = new GeoTiffWriter(file);
        }
        catch(IOException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        GridCoverage2D cov = (new GridCoverageFactory()).create(imageName, wRaster, reader.getOriginalEnvelope());
        try
        {
            writer.write(cov, paramValues);
        }
        catch(IllegalArgumentException ex)
        {
          //  Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
          //  Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IndexOutOfBoundsException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println((new StringBuilder(" File created in : ")).append(fileName).toString());
        writer.dispose();
    }

    private GeneralParameterValue[] getInitialParameters()
    {
        java.util.List paramValues = reader.getFormat().getReadParameters().values();
        return (GeneralParameterValue[])paramValues.toArray(new GeneralParameterValue[paramValues.size()]);
    }

    public int[] getBounds(Polygon polygon)
    {
        Polygon scalePoly = scaleTo(polygon);
        int bounds[] = getBounds(scalePoly.getCoordinates());
        int reverseB[] = new int[4];
        reverseB[0] = bounds[0];
        reverseB[1] = reverse(bounds[3]);
        reverseB[2] = bounds[2];
        reverseB[3] = reverse(bounds[1]);
        Coordinate b[] = cboundary(polygon);
        GridGeometry2D gridGeometry = getCoverage().getGridGeometry();
        Coordinate acoordinate[];
        int j = (acoordinate = b).length;
        for(int i = 0; i < j; i++)
        {
            Coordinate c = acoordinate[i];
            try
            {
                GridCoordinates2D gc = gridGeometry.worldToGrid(new DirectPosition2D(c.x, c.y));
               // System.out.println((new StringBuilder(String.valueOf(gc.x))).append(" ").append(gc.y).toString());
            }
            catch(TransformException ex)
            {
              //  Logger.getLogger(fr/ocelet/runtime/raster/ORaster.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return reverseB;
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

    public void plus(ORaster oRaster)
    {
        for(int i = 0; i < numBands(); i++)
            plusBand(oRaster, i);

    }

    public void minus(ORaster oRaster)
    {
        for(int i = 0; i < numBands(); i++)
            minusBand(oRaster, i);

    }

    public void star(ORaster oRaster)
    {
        for(int i = 0; i < numBands(); i++)
            starBand(oRaster, i);

    }

    public void divide(ORaster oRaster)
    {
        for(int i = 0; i < numBands(); i++)
            divideBand(oRaster, i);

    }

    public void plusBand(ORaster oRaster, int band)
    {
        for(int x = 0; x < getMinPixel(0); x++)
        {
            for(int y = 0; y < getMinPixel(0); y++)
            {
                double thisVal = getDoubleValue(x, y, band);
                double value = oRaster.getDoubleValue(x, y, band);
                wRaster.setSample(x, y, band, thisVal + value);
            }

        }

    }

    public void minusBand(ORaster oRaster, int band)
    {
        for(int x = 0; x < getMinPixel(0); x++)
        {
            for(int y = 0; y < getMinPixel(0); y++)
            {
                double thisVal = getDoubleValue(x, y, band);
                double value = oRaster.getDoubleValue(x, y, band);
                wRaster.setSample(x, y, band, thisVal - value);
            }

        }

    }

    public void starBand(ORaster oRaster, int band)
    {
        for(int x = 0; x < getMinPixel(0); x++)
        {
            for(int y = 0; y < getMinPixel(0); y++)
            {
                double thisVal = getDoubleValue(x, y, band);
                double value = oRaster.getDoubleValue(x, y, band);
                wRaster.setSample(x, y, band, thisVal * value);
            }

        }

    }

    public void divideBand(ORaster oRaster, int band)
    {
        for(int x = 0; x < getMinPixel(0); x++)
        {
            for(int y = 0; y < getMinPixel(0); y++)
            {
                double thisVal = getDoubleValue(x, y, band);
                double value = oRaster.getDoubleValue(x, y, band);
                wRaster.setSample(x, y, band, thisVal / value);
            }

        }

    }

    public GridGeometry2D getGridGeometry()
    {
        return getCoverage().getGridGeometry();
    }

    private void createNewBand()
    {
        wRaster.getSampleModel().getNumBands();
    }

    public double getXRes(){
    	return pX;
    }
    
    public double getYRes(){
    	return pY;
    }
    

}
