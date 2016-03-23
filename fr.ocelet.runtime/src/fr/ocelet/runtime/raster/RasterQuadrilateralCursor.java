package fr.ocelet.runtime.raster;

public class RasterQuadrilateralCursor  extends RasterCursor{

	
	/*protected Incrementor incrementor;
	public RasterQuadrilateralCursor(int numGrid) {
		super(numGrid);
		incrementor = new IncrementorHeight();
		// TODO Auto-generated constructor stub
	}
	
	public void setMode(String mode){
		
		if(mode.equals("Four")){
			incrementor = new IncrementorFour();
		}else{
			incrementor = new IncrementorHeight();
		}
	}

	@Override
	public boolean hasNext() {

		if(x == width - 2 && y == height - 1 && x2 == width - 1 && y2 == height - 1)
        {
            x = 0;
            y = 0;
            x2 = 0;
            y2 = 0;
            direction = 0;        
          
            gridManager.reset();
            return false;
        } else
        {
            return true;
        }
	}

	@Override
	public void next() {		
		 while(!setEnd2()) ;
	       		
	}
	
	 public boolean setEnd2()
	    {
		 
	        if(isRight())
	        {
	        	incrementor.increment();
	            //increment2();
	            if(inbounds(x + 1, y))
	            {
	                x2 = x + 1;
	                y2 = y;
	                return true;
	            } else
	            {
	                return false;
	            }
	        }
	        if(isRightDown())
	        {
	        	incrementor.increment();
	           // increment2();
	            if(inbounds(x + 1, y + 1))
	            {
	                x2 = x + 1;
	                y2 = y + 1;
	                return true;
	            } else
	            {
	                return false;
	            }
	        }
	        if(isDown())
	        {
	           // increment2();
	        	incrementor.increment();
	            if(inbounds(x, y + 1))
	            {
	                x2 = x;
	                y2 = y + 1;
	                return true;
	            } else
	            {
	                return false;
	            }
	        }
	        if(isDownLeft())
	        {
	          //  increment2();
	        	incrementor.increment();
	            if(inbounds(x - 1, y + 1))
	            {
	                x2 = x - 1;
	                y2 = y + 1;
	                return true;
	            } else
	            {
	                return false;
	            }
	        } else
	        {
	            return false;
	        }
	    }
	public void cursor(){
		direction = direction + 1;
		if(direction == 0){
			 move();
			 if(inbounds(x + 1, y)){
				 x2 = x + 1;
	             y2 = y;
			 }else{
				 direction++;
			 }
		 }
		 
		 if(direction == 1){
			 if(inbounds(x + 1, y + 1)){
				 x2 = x + 1;
				 y2 = y + 1;
			 }else{
				 direction++;
			 }
		 }
		 
		 if(direction == 2){
			 if(inbounds(x, y + 1)){
				 x2 = x;
				 y2 = y + 1;
			 }else{
				 direction++;	
			 }
		 }
		 
		 if(direction == 3){
			 if(inbounds(x - 1, y + 1)){
				 x2 = x - 1;
				 y2 = y + 1;
             direction = -1;
			
			 }else{
				 direction = -1;
				 next();
			 }
		 }
	} 
	 public void increment2(){
	    
	        if(direction == 3){
	        
	            direction = 0;
	            move();
	        } else {
	        
	            direction++;
	        }
	    }

	@Override
	public void move() {
		 if(x == width - 1){
	        
	          
	            x = 0;
	            y++;
	            gridManager.increment();
	        } else {
	        
	            x++;
	        }
		
	}
	
	public boolean inbounds(int x, int y){
    
        return x >= 0 && y >= 0 && x < width && y < height;
    }

	public class IncrementorFour extends Incrementor{
		
		public void increment(){
			if(direction == 2){
		        
	            direction = 0;
	            move();
	        } else {
	        
	            direction = direction + 2;
	        }
		}
	}

	
	
public class IncrementorHeight extends Incrementor{
	 public void increment(){
		    
	        if(direction == 3){
	        
	            direction = 0;
	            move();
	        } else {
	        
	            direction++;
	        }
	    }
	}

public abstract class Incrementor{
	
	public abstract void increment();
	
	
}*/
	private NeighbourSide ns;
	private NeighbourSideRight nsr;
	private NeighbourSideRightBot nsrb;
	private NeighbourSideBot nsb;
	private NeighbourSideBotLeft nsbl;
	
	
	
	protected Incrementor incrementor;
	public RasterQuadrilateralCursor(int numGrid) {
		super(numGrid);
		incrementor = new IncrementorHeight();
		// TODO Auto-generated constructor stub
		direction = -1;
		x = -1;
		
		nsr = new NeighbourSideRight();
		nsrb = new NeighbourSideRightBot();
		nsb = new NeighbourSideBot();
		nsbl = new NeighbourSideBotLeft();
		ns = nsr;
	}
	
	public void setMode(String mode){
		
		if(mode.equals("Four")){
			incrementor = new IncrementorFour();
		}else{
			incrementor = new IncrementorHeight();
		}
	}

	@Override
	public boolean hasNext() {

		if(x == width - 2 && y == height - 1 && x2 == width - 1 && y2 == height - 1)
        {
            x = -1;
            y = 0;
            x2 = 0;
            y2 = 0;
            direction = -1;        
          
            gridManager.reset();
            return false;
        } else
        {
            return true;
        }
	}

	@Override
	public void next() {		
		// while(!setEnd2()) ;
		 ns.next();
		//cursor();
	       		
	}
	
	
	@Override
	public void move() {
		 if(x == width - 1){
	            x = 0;
	            y++;
	            gridManager.increment();
	        } else {
	        
	            x++;
	        }
		
	}
	
	public boolean inbounds(int x, int y){
    
        return x >= 0 && y >= 0 && x < width && y < height;
    }

	public class IncrementorFour extends Incrementor{
		
		public void increment(){
			if(direction == 2){
		        
	            direction = 0;
	            move();
	        } else {
	        
	            direction = direction + 2;
	        }
		}
	}

	
	
public class IncrementorHeight extends Incrementor{
	 public void increment(){
		    
	        if(direction == 3){
	        
	            direction = 0;
	            move();
	        } else {
	        
	            direction++;
	        }
	    }
	}

public abstract class Incrementor{
	
	public abstract void increment();
	
	
}

public abstract class NeighbourSide{
	public abstract void next();
		
	
}

public class NeighbourSideRight extends NeighbourSide{
	
	public void next(){
		 move();
		 if(inbounds(x + 1, y)){
			 x2 = x + 1;
             y2 = y;
             ns = nsrb;
		 }else{
			ns = nsrb;
			ns.next();
		 }
	}
		
	
}

public class NeighbourSideRightBot extends NeighbourSide{
	
	public void next(){
		 if(inbounds(x + 1, y + 1)){
			 x2 = x + 1;
			 y2 = y + 1;
			 ns = nsb;

		 }else{
			ns = nsb;
			ns.next();
		 }
	}
		
	
}

public class NeighbourSideBot extends NeighbourSide{
	public void next(){
		 if(inbounds(x, y + 1)){
			 x2 = x;
			 y2 = y + 1;
			 ns = nsbl;

		 }else{
			ns = nsbl;
			ns.next();
		 }
	}
		
	
}

public class NeighbourSideBotLeft extends NeighbourSide{
	public void next(){
		if(inbounds(x - 1, y + 1)){
			 x2 = x - 1;
			 y2 = y + 1;
		ns = nsr;

		 }else{
			 ns = nsr;
			 ns.next();
		 }
	}
		
	
}


}
