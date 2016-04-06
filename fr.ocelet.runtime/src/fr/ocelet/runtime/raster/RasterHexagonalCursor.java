package fr.ocelet.runtime.raster;

public class RasterHexagonalCursor extends RasterCursor {

	private boolean toMove = false;
	private int index = 0;
	public RasterHexagonalCursor(int numGrid) {
		super(numGrid);
		// TODO Auto-generated constructor stub
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
          
         //   gridManager.reset();
            return false;
        } else
        {
            return true;
        }
	}

	@Override
	public void next() {
		 
		if(toMove){
			move();
		}
		if(x%2 == 0){
			setPair();
		}else{
			setImpair();
		}
	}
	
	public void setPair(){
		
		 if(direction == 0){
			 toMove = false;
			  if(inbounds(x + 1, y - 1)){
				  x2 = x + 1;
	              y2 = y - 1;
	              direction = 1;
			  }else{
				direction = 1;
				setPair();
			  }
		 }else {
			 
			 
			 if(direction == 1){
				 toMove = false;
				 if(inbounds(x + 1, y)){
					 x2 = x + 1;
					 y2 = y;
					 direction = 2;
				 }else{
					 direction = 2;
					 setPair();
				 
				 }
			 }else{
				 if(direction == 2){
					
					 if(inbounds(x, y + 1)){
						 x2 = x;
						 y2 = y + 1;
						 direction = 0;
						 toMove = true;
					 }else{
						 direction = 0;
						 toMove = false;
						 move();
						 if(hasNext())
				    	setImpair();
				 	}				
				 }
			 }
		 }
	}
		 
	public void setImpair(){
	
		 if(direction == 0){
			 toMove = false;
			  if(inbounds(x + 1, y)){
				  x2 = x + 1;
	              y2 = y;
	              direction = 1;
			  }else{
				direction = 1;
				setImpair();
			  }
		 }else {
			 
			 
			 if(direction == 1){
				 
				 toMove = false;
				 if(inbounds(x + 1, y + 1)){
					 x2 = x + 1;
					 y2 = y + 1;
					 direction = 2;
				 }else{
					 direction = 2;
					 setImpair();
				 
				 }
			 }else{
				 if(direction == 2){
					
					 if(inbounds(x, y + 1)){
						 toMove = true;
						 x2 = x;
						 y2 = y + 1;
						 direction = 0;
					
					 }else{
						 direction = 0;
						 toMove = false;
						 move();
						 if(hasNext())
					     setPair();
				 	}				
				 }
			 }
		 }
	}

	 public boolean setEnd2(){
	    
		 
		 if(x%2 == 0){//pair
			 
	
			 
			 if(direction == 0){
				
				 increment2();
				 
		            if(inbounds(x + 1, y)){
		            
		                x2 = x + 1;
		                y2 = y;
		               
		                return true;
		            } else {
		            	
		                return false;
		            }
		        }
			  if(direction == 1){
				
				  increment2();
		            if(inbounds(x, y + 1)){
		            	
		            
		                x2 = x;
		                y2 = y + 1;
		              
		                return true;
		            } else {
		            	
		                return false;
		            }
		        }
			  if(direction == 2){
				
				  increment2();
		            if(inbounds(x + 1, y - 1)) {
		            
		                x2 = x + 1;
		                y2 = y - 1;
		               
		                return true;
		            } else{
		            	
		                return false;
		            }
		        }
		 }else{
			
			 if(direction == 0){
				 increment2();
		            
		            if(inbounds(x + 1, y)){
		            
		                x2 = x + 1;
		                y2 = y;
		               
		                return true;
		            } else {
		            	
		                return false;
		            }
		        }
			  if(direction == 1){
				  increment2();
		            
		            if(inbounds(x + 1, y + 1)){
		            
		                x2 = x + 1;
		                y2 = y + 1;
		               
		                return true;
		            } else {
		            	
		                return false;
		            }
		        }
			  if(direction == 2){
			
				  increment2();
		            if(inbounds(x, y + 1)) {
		            
		                x2 = x;
		                y2 = y + 1;
		               
		                return true;
		            } else{
		            	
		                return false;
		            }
		        }
		 }
		 return false;

	    }
	 
	 public void increment2(){
	    
	        if(direction == 2){
	        
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
	            index ++;
	          
	            
	            gridManager.increment();
	            gridManager.yIncrement();
	           
	        } else {				        
	            x++;
	        }
	}
	
	public boolean inbounds(int x, int y){
 
     return x >= 0 && y >= 0 && x < width && y < height;
 }


	

}
