package fr.ocelet.runtime.relation;

import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.*;

import java.io.PrintStream;

// Referenced classes of package fr.ocelet.runtime.relation:
//            OcltEdge, GridCellManager, OcltRole, AggregOperator

public abstract class CursorHexagonalEdge extends OcltEdge{

	
	 protected int x;
	    protected int y;
	    protected int x2;
	    protected int y2;
	    private int numGrid;
	    protected Grid grid;
	    private int direction;
	    private boolean state;
	    private GridCellManager gridManager;
	    private int index;
	    
	    
    public CursorHexagonalEdge(int numGrid){
        x = 0;
        y = 0;
        x2 = 0;
        y2 = 0;
        direction = 0;
        state = true;
        index = 0;
        grid = GridManager.getInstance().get(numGrid);
        gridManager = grid.getGridCellManager();
        update();
    }

    public CursorHexagonalEdge(Grid grid){
        x = 0;
        y = 0;
        x2 = 0;
        y2 = 0;
        direction = 0;
        state = true;
        index = 0;
        this.grid = grid;
        gridManager = grid.getGridCellManager();
    }

    public abstract void update();

    public boolean hasNext(){
        if(x == grid.getWidth() - 2 && y == grid.getHeight() - 1 && x2 == grid.getWidth() - 1 && y2 == grid.getHeight() - 1)
        {
            x = 0;
            y = 0;
            x2 = 0;
            y2 = 0;
            direction = 0;        
            update();
            gridManager.reset();
            return false;
        } else{
            return true;
        }
    }

    public void next(){
        while(!setEnd2()) ;
        update();
    }

    public void move(){
        if(x == grid.getWidth() - 1)
        {
            index++;
            x = 0;
            y++;
            gridManager.increment();
        } else
        {
            x++;
        }
    }

    public boolean isRight(){
        return direction == 0;
    }

    public boolean isRightDown(){
        return direction == 1;
    }

    public boolean isDown(){
        return direction == 2;
    }

    public boolean isDownLeft(){
        return direction == 3;
    }

    public boolean isLeft(){
        return direction == 4;
    }

    public boolean isUpLeft(){
        return direction == 5;
    }

    public boolean isUp(){
        return direction == 6;
    }

    public boolean isUpRight(){
        return direction == 7;
    }

    public void increment(){
        if(direction == 7){
            direction = 0;
            move();
        } else {
            direction++;
        }
    }

    public boolean inbounds(int x, int y){
        return x >= 0 && y >= 0 && x < grid.getWidth() && y < grid.getHeight();
    }

    public boolean state(){
        return state;
    }

    public boolean setEnd(){
        if(isRight()){
            increment();
            if(inbounds(x + 1, y)){
                x2 = x + 1;
                y2 = y;
                return true;
            } else {
                return false;
            }
        }
        if(isRightDown()){
            increment();
            if(inbounds(x + 1, y + 1)){
                x2 = x + 1;
                y2 = y + 1;
                return true;
            } else {
                return false;
            }
        }
        if(isDown()){
            increment();
            if(inbounds(x, y + 1)){
                x2 = x;
                y2 = y + 1;
                return true;
            } else {
                return false;
            }
        }
        if(isDownLeft()){
            increment();
            if(inbounds(x - 1, y + 1)){
                x2 = x - 1;
                y2 = y + 1;
                return true;
            } else {
                return false;
            }
        }
        if(isLeft()) {
            increment();
            if(inbounds(x - 1, y)){
                x2 = x - 1;
                y2 = y;
                return true;
            } else {
                return false;
            }
        }
        if(isUpLeft()){
            increment();
            if(inbounds(x - 1, y - 1)) {
                x2 = x - 1;
                y2 = y - 1;
                return true;
            } else {
                return false;
            }
        }
        if(isUp()) {
            increment();
            if(inbounds(x, y - 1)) {
                x2 = x;
                y2 = y - 1;
                return true;
            } else {
                return false;
            }
        }
        if(isUpRight()){
            increment();
            if(inbounds(x + 1, y - 1)){
                x2 = x + 1;
                y2 = y - 1;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int[] getEndPos(){
        return (new int[] {x2, y2});
    }

    public int[] getFirstPos(){
        return (new int[] {x, y});
    }

    public OcltRole getEnd(){
        return (OcltRole)gridManager.get(x2, y2);
    }

    public OcltRole getFirst(){
        return (OcltRole)gridManager.get(x, y);
    }

    public void clearProperties(){
        gridManager.clearProperties();
    }

    public int[] getPos1(){
        return (new int[] {x, y});
    }

    public int[] getPos2(){
        return (new int[] {x2, y2});
    }

    public void setCellOperator(CellAggregOperator operator){
        gridManager.addOperator(operator, operator.getName());
    }
public void setCellOperator(String name, AggregOperator operator, KeyMap<String, String> typeProps)
    {
        CellAggregOperator cao = new CellAggregOperator();
        if(typeProps.get(name).equals("Double")){
        	setAggregOpDouble(name, operator, false);
        }else if(typeProps.get(name).equals("Integer")){
        	setAggregOpInteger(name, operator, false);

        }else if(typeProps.get(name).equals("Float")){
        	setAggregOpFloat(name, operator, false);

        }else if(typeProps.get(name).equals("Byte")){
        	setAggregOpByte(name, operator, false);

        }else if(typeProps.get(name).equals("Boolean")){
        	setAggregOpBoolean(name, operator, false);

        }
    }
    public void setAggregOpDouble(String name, AggregOperator<Double, List<Double>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorDouble(agg);
        gridManager.addOperator(cao, name);
    }
public void setAggregOpInteger(String name, AggregOperator<Integer, List<Integer>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorInteger(agg);
        gridManager.addOperator(cao, name);
    }

public void setAggregOpFloat(String name, AggregOperator<Float, List<Float>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorFloat(agg);
        gridManager.addOperator(cao, name);
    }

public void setAggregOpByte(String name, AggregOperator<Byte, List<Byte>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorByte(agg);
        gridManager.addOperator(cao, name);
    }

public void setAggregOpBoolean(String name, AggregOperator<Boolean, List<Boolean>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorBoolean(agg);
        gridManager.addOperator(cao, name);
    }
    public void cleanOperator(){
        gridManager.clearAggregMap();
    }

    public void increment2(){
        if(direction == 3){
            direction = 0;
            move();
        } else {
            direction++;
        }
    }

    public boolean setEnd2(){
        if(isRight()){
            increment2();
            if(inbounds(x + 1, y)){
                x2 = x + 1;
                y2 = y;
                return true;
            } else {
                return false;
            }
        }
        if(isRightDown()){
            increment2();
            if(inbounds(x + 1, y + 1)){
                x2 = x + 1;
                y2 = y + 1;
                return true;
            } else {
                return false;
            }
        }
        if(isDown()){
            increment2();
            if(inbounds(x, y + 1)){
                x2 = x;
                y2 = y + 1;
                return true;
            } else {
                return false;
            }
        }
        if(isDownLeft()) {
            increment2();
            if(inbounds(x - 1, y + 1)){
                x2 = x - 1;
                y2 = y + 1;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public OcltRole getRole(int arg){
    	return null;
    }

   
}
