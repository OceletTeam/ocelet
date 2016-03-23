package fr.ocelet.runtime.raster;


public abstract class RasterCursor {
	
	protected int x;
	protected int y;
	protected int x2;
	protected int y2;
	protected int width;
	protected int height;
	protected int direction = 0;
	protected GridCellManager gridManager;
	protected double xRes;
	protected double yRes;
	
	public RasterCursor(int numGrid){
		
		Grid grid = GridManager.getInstance().get(numGrid);
		this.width = grid.getWidth();
		this.height = grid.getHeight();
		xRes = grid.getXRes();
		yRes = grid.getYRes();
		this.x = 0;
		this.y = 0;
		this.x2 = 0;
		this.y2 = 0;
		this.gridManager = grid.getGridCellManager();
		
	}
	public void setGridCellManager(GridCellManager gridManager){
		this.gridManager = gridManager;
	}
	
	public abstract boolean hasNext();
	
	public abstract void next();
	
	 public boolean isRight()
	    {
	        return direction == 0;
	    }

	    public boolean isRightDown()
	    {
	        return direction == 1;
	    }

	    public boolean isDown()
	    {
	        return direction == 2;
	    }

	    public boolean isDownLeft()
	    {
	        return direction == 3;
	    }

	    public boolean isLeft()
	    {
	        return direction == 4;
	    }

	    public boolean isUpLeft()
	    {
	        return direction == 5;
	    }

	    public boolean isUp()
	    {
	        return direction == 6;
	    }

	    public boolean isUpRight()
	    {
	        return direction == 7;
	    }
	    
	    public abstract void move();
	    
	    
	    public int getX(){
	    	return x;
	    }
	    
	    public int getY(){
	    	return y;
	    }
	    
	    public int getX2(){
	    	return x2;
	    }
	    
	    public int getY2(){
	    	return y2;
	    }
	    
	    
	    public void reset(){
	    	  x = 0;
	            y = 0;
	            x2 = 0;
	            y2 = 0;
	            direction = 0;        
	           
	            gridManager.reset();
	    }
		
}
