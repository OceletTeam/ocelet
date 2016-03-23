package fr.ocelet.runtime.raster;

import java.util.ArrayList;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.relation.OcltRole;

/*public class IX<R2 extends OcltRole>{

	Integer x;	
	private ArrayList<IY<R2>> ys;

	private int pos = 0;
	public IX(Integer x){
		this.x = x;
		ys = new ArrayList<IY<R2>>();
	}
	
	public void add(IY<R2> y){
		ys.add(y);
	}
	
	public void add(Integer y, R2 r){
		
		boolean test = false;
		
		for(IY<R2> iy : ys){
			if(iy.y == y){
				iy.addEntity(r);
				test = true;
			}
		}
		if(!test){
			IY<R2> iy = new IY<R2>(y);
			iy.addEntity(r);
			ys.add(iy);
		}
	}
	
	public boolean hasNext(){
		
		if(pos == ys.size() - 1){
		
			return false;
		}
		return true;
	}
	
	
	public  void next(){
		pos++;
	}
	
	public IY<R2> get(){
		return ys.get(pos);
	}

	public void reset(){
		pos = 0;
	}
	
	public Integer getX(){
		return x;
	}
	
	
}*/

public class IX{

	Integer x;	
	//private ArrayList<IY<R2>> ys;
	private IY[] ys = null;
	private int pos = 0;
	public IX(Integer x){
		this.x = x;
		//ys = new IY[1];
	}
	
	/*public void add(IY y){
		
		if(ys == null){
			ys = new IY[1];
			IY iy = new IY(y);
		}
		IY[] temp = new IY[ys.length + 1];
		for(int i = 0; i < ys.length; i ++)
	}*/
	
	public void add(Integer y, AbstractEntity r){
		
		boolean test = false;
		
		if(ys == null){
			ys = new IY[1];
			IY iy = new IY(y);
			iy.addEntity(r);
			ys[0] = iy;
		}else{
		for(IY iy : ys){
			if(iy.y == y){
				iy.addEntity(r);
				test = true;
			}
		}
		if(!test){
			IY iy = new IY(y);
			iy.addEntity(r);
			IY[] temp = new IY[ys.length + 1];
			for(int i = 0; i < ys.length; i ++){
				temp[i] = ys[i];
			}
			temp[ys.length] = iy;
			ys = temp;
		}
		}
	}
	
	public boolean hasNext(){
		
		if(pos == ys.length - 1){
		
			return false;
		}
		return true;
	}
	
	
	public  void next(){
		pos++;
	}
	
	public IY get(){
		return ys[pos];
	}

	public void reset(){
		pos = 0;
	}
	
	public Integer getX(){
		return x;
	}
	
	
}
