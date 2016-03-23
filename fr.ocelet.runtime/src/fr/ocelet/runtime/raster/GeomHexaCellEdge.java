// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GeomCellEdge.java

package fr.ocelet.runtime.raster;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.MultiLine;
import fr.ocelet.runtime.geom.ocltypes.MultiPoint;
import fr.ocelet.runtime.geom.ocltypes.MultiPolygon;
import fr.ocelet.runtime.geom.ocltypes.Point;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;
import fr.ocelet.runtime.relation.OcltEdge;
import fr.ocelet.runtime.relation.OcltRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

// Referenced classes of package fr.ocelet.runtime.relation:
//            OcltEdge, OcltRole, AggregOperator

public abstract class GeomHexaCellEdge<R1 extends OcltRole, R2 extends OcltRole> extends OcltEdge {


	 private HashMap<String, CellAggregOperator> aggregMap = new HashMap<String, CellAggregOperator>();
	    private HashMap<Integer, HashMap<Integer , ArrayList<R2>>> matrice
	    = new HashMap<Integer, HashMap<Integer,ArrayList<R2>>>();
	    private List<R2> geomEntities;
	    private Grid grid;
	    private int currentxKey;
	    private int currentyKey;
	    private R2 currentRoleKey;
	    private Iterator<Integer> xIterator;
	    private Iterator<Integer> yIterator;
	    private Iterator<R2> roleIterator;
	    
	    private int type = 0; //0 = Flat hexagon, 1 = Angled hexagon
	    private double sideLength;
	    private int width;
	    private int height;
	    
    public void clearAggregMap()
    {
        aggregMap.clear();
    }

    public void addOperator(String name, CellAggregOperator operator)
    {
        aggregMap.put(name, operator);
    }

    public void setCellOperator(String name, AggregOperator operator)
    {
        CellAggregOperator cao = new CellAggregOperator(operator, name);
        aggregMap.put(name, cao);
    }

    public GeomHexaCellEdge(Grid grid, List<R2> geomEntities)
    {
      
        this.geomEntities = geomEntities;
        this.grid = grid;
        fill(this.geomEntities);
    }

    public void add(R2 e, int x, int y)
    {
        if(!matrice.keySet().contains(x)){
        
            ArrayList<R2> list = new ArrayList<R2>();
            list.add(e);
            HashMap<Integer, ArrayList<R2>> yHash = new HashMap<Integer, ArrayList<R2>>();
            yHash.put(y, list);
            matrice.put(x, yHash);
        } else
        if(!matrice.get(x).keySet().contains(y)){
        
            ArrayList<R2> list = new ArrayList<R2>();
            list.add(e);
           // HashMap<Integer, List<R2>> yHash = new HashMap<Integer, List<R2>>();
            //yHash.put(y, list);
            matrice.get(x).put(y, list);
        } else
        if(!(matrice.get(x).get(y)).contains(e))
            matrice.get(x).get(y).add(e);
    }

    public ArrayList<R2> get(int x, int y)
    {
        return (matrice.get(Integer.valueOf(x))).get(Integer.valueOf(y));
    }

    public void fill(List<R2> tities){
    
        for(R2 r2 : tities){
              OcltRole e = (OcltRole)r2;
              if(e.getSpatialType() instanceof Polygon)
                  setCells(r2, (Polygon)e.getSpatialType());
              if(e.getSpatialType() instanceof MultiPolygon)
              {
            
                  MultiPolygon mp = (MultiPolygon)e.getSpatialType();
                  for(int i = 0; i < mp.getNumGeometries(); i++)
                      setCells(r2, (Polygon)mp.getGeometryN(i));

              }
              if(e.getSpatialType() instanceof Line)
                  setCells(r2, (Line)e.getSpatialType());
              if(e.getSpatialType() instanceof MultiLine)
              {
                  MultiLine ml = (MultiLine)e.getSpatialType();
                  for(int i = 0; i < ml.getNumGeometries(); i++)
                      setCells(r2, (Line)ml.getGeometryN(i));

              }
              if(e.getSpatialType() instanceof Point)
                  setCells(r2, (Point)e.getSpatialType());
              if(e.getSpatialType() instanceof MultiPoint)
              {
                  MultiPoint mp = (MultiPoint)e.getSpatialType();
                  for(int i = 0; i < mp.getNumGeometries(); i++)
                      setCells(r2, (Point)mp.getGeometryN(i));

              }
              
        }
  

        xIterator = matrice.keySet().iterator();
        currentxKey = xIterator.next().intValue();
        yIterator = matrice.get(currentxKey).keySet().iterator();
        currentyKey = yIterator.next().intValue();
        roleIterator = matrice.get(currentxKey).get(currentyKey).iterator();
    }

    private void setCells(R2 r2, Polygon polygon)
    {
        int bounds[] = grid.intBounds(polygon);
        int index = 0;
        for(int i = bounds[0]; i < bounds[2]; i++)
        {
            for(int j = bounds[1]; j < bounds[3]; j++)
            {
                Coordinate c = grid.gridCoordinate(i, j);
                Point point = Point.xy(Double.valueOf(c.x), Double.valueOf(c.y));
                if(polygon.touches(point) || point.within(polygon))
                {
                    add(r2, i - grid.getMinX(), j - grid.getMinY());
                    index++;
                }
            }

        }

        if(index == 0)
        {
            int scaledCentroid[] = grid.gridCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
            add(r2, scaledCentroid[0] - grid.getMinX(), scaledCentroid[1] - grid.getMinY());
        }
    }

    private void setCells(R2 r2, Line line)
    {
        Coordinate coordinates[] = line.getCoordinates();
        for(int i = 0; i < coordinates.length - 1; i++)
        {
            int c1[] = grid.gridCoordinate(coordinates[i].x, coordinates[i].y);
            int c2[] = grid.gridCoordinate(coordinates[i + 1].x, coordinates[i + 1].y);
            int x = c1[0];
            int y = c1[1];
            int x2 = c2[0];
            int y2 = c2[1];
            bresenham(x, y, x2, y2, r2);
        }

    }
    
    private void setCells(R2 r2, Point point)
    {
        Coordinate coordinate = point.getCoordinates()[0];
       int[] coord =  grid.gridCoordinate(coordinate.x, coordinate.y);
        add(r2, coord[0] - grid.getMinX(), coord[1] - grid.getMinY());
       

    }

    public void bresenham(int x1, int y1, int x2, int y2, R2 role)
    {
        if(x1 == x2 && y1 == y2)
        {
            add(role, x1 - grid.getMinX(), y1 - grid.getMinY());
        } else
        {
            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);
            int rozdil = dx - dy;
            int posun_x;
            if(x1 < x2)
                posun_x = 1;
            else
                posun_x = -1;
            int posun_y;
            if(y1 < y2)
                posun_y = 1;
            else
                posun_y = -1;
            while(x1 != x2 || y1 != y2) 
            {
                int p = 2 * rozdil;
                if(p > -dy)
                {
                    rozdil -= dy;
                    x1 += posun_x;
                }
                if(p < dx)
                {
                    rozdil += dx;
                    y1 += posun_y;
                }
                add(role, x1 - grid.getMinX(), y1 - grid.getMinY());
            }
        }
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

    public boolean xHasNext()
    {
        return xIterator.hasNext();
    }

    public boolean yHasNext()
    {
        return yIterator.hasNext();
    }

    public boolean roleHasNext()
    {
        return roleIterator.hasNext();
    }

    public void cellSynchronisation()
    {
        for(Iterator iterator = grid.getTempName().iterator(); iterator.hasNext();)
        {
            String name = (String)iterator.next();
            List<Double> values = grid.getGeomTempValues(name);
            if(aggregMap.keySet().contains(name))
            {
                CellAggregOperator cao = (CellAggregOperator)aggregMap.get(name);
                Double d = cao.apply(values, Double.valueOf(0.0D));
                grid.setCellValue(name, currentxKey, currentyKey, d);
            } else
            {
                grid.setCellValue(name, currentxKey, currentyKey, (Double)values.get((int)(Math.random() * (double)values.size())));
            }
        }

    }

    public void xNext()
    {
        currentxKey = xIterator.next().intValue();
        yIterator = matrice.get(currentxKey).keySet().iterator();
        yNext();
    }

    public void yNext()
    {
        currentyKey = ((Integer)yIterator.next()).intValue();
        roleIterator = matrice.get(currentxKey).get(currentyKey).iterator();
        cellSynchronisation();
        grid.clearGeomTempVal();
        roleNext();
    }

    public void roleNext(){
    
        currentRoleKey = roleIterator.next();
    }

    public boolean hasNext()
    {
        return xHasNext() || yHasNext() || roleHasNext();
    }

    public void next()
    {
        if(roleHasNext())
            roleNext();
        else
        if(yHasNext())
            yNext();
        else
            xNext();
        update();
    }

    public List<R2> getGeomEntities()
    {
        return geomEntities;
    }

    public abstract void update();

    public Integer getX()
    {
        return Integer.valueOf(currentxKey);
    }

    public Integer getY()
    {
        return Integer.valueOf(currentyKey);
    }

    public R2 getGeomEntity()
    {
        return currentRoleKey;
    }

    public void setMode(int mode)
    {
        grid.setMode(mode);
    }

    
    public double[] gridToHexaCoordinate(int x, int y){
    	
    	double[] hexaCoord = new double[2];
    	
    	hexaCoord[0] = ((width / 2) + sideLength) * x;
    	if(x%2 == 1){ // impair
    		hexaCoord[1] = y * (2 * height) + height;
    	}else{ // pair
    		hexaCoord[1] = y * (2 * height);
    	}
    	
    	return hexaCoord;
    	
    }
    
   
    
   
}
