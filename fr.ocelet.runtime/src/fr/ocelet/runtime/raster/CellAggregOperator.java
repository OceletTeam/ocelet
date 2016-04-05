package fr.ocelet.runtime.raster;

import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;

public class CellAggregOperator{
private Operator aggregOperator;
    private String name;

    public CellAggregOperator(){
    }


    public void setCellOperator(AggregOperator operator, KeyMap<String, String> typeProps){
    	if(typeProps.get(name).equals("Double")){
        	setOperatorDouble(operator);
        }else if(typeProps.get(name).equals("Integer")){
        	setOperatorInteger(operator);

        }else if(typeProps.get(name).equals("Float")){
        	setOperatorFloat(operator);

        }else if(typeProps.get(name).equals("Byte")){
        	setOperatorByte(operator);

        }else if(typeProps.get(name).equals("Boolean")){
        	setOperatorBoolean(operator);

        }
    	
    }
    public void setOperatorDouble(AggregOperator<Double, List<Double>> operator){
        OperatorDouble od = new OperatorDouble();
       od.setDoubleOperator(operator);
       aggregOperator = od;
    }
    
    public void setOperatorInteger(AggregOperator<Integer, List<Integer>> operator){
        OperatorInteger od = new OperatorInteger();
       od.setIntegerOperator(operator);
       aggregOperator = od;
    }
    
    public void setOperatorFloat(AggregOperator<Float, List<Float>> operator){
        OperatorFloat od = new OperatorFloat();
       od.setFloatOperator(operator);
       aggregOperator = od;
    }
    
    public void setOperatorBoolean(AggregOperator<Boolean, List<Boolean>> operator){
        OperatorBoolean od = new OperatorBoolean();
       od.setBooleanOperator(operator);
       aggregOperator = od;
    }
    
    public void setOperatorByte(AggregOperator<Byte, List<Byte>> operator){
        OperatorByte od = new OperatorByte();
       od.setByteOperator(operator);
       aggregOperator = od;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Double apply(List<Double> values, Double val)
    {
        return aggregOperator.apply(values, val);
    }

        
   private abstract class Operator{
	   public Operator(){
		   
	   }
	   public abstract Double apply(List<Double> values, Double val);
   }
 private class OperatorDouble extends Operator{
    	private AggregOperator<Double, List<Double>> operator;
    
    public OperatorDouble(){
    	
    }
    
    public void setDoubleOperator(AggregOperator<Double, List<Double>> operator){
    	this.operator = operator;
    }

	 public Double apply(List<Double> values, Double val)
    {
        return operator.compute(values, val);
    }
    	
    }
 private class OperatorInteger extends Operator{
	 
 	private AggregOperator<Integer, List<Integer>> operator;
    
    public void setIntegerOperator(AggregOperator<Integer, List<Integer>> operator){
    	this.operator = operator;
    }
 	public Double apply(List<Double> values, Double val)
    {
 		List<Integer> intList = new List<Integer>();
 		for(Double d : values){
 			intList.add(d.intValue());
 		}
        return operator.compute(intList, val.intValue()).doubleValue();
    }
 	
 }
 private class OperatorByte extends Operator{
 	private AggregOperator<Byte, List<Byte>> operator;

    
    public void setByteOperator(AggregOperator<Byte, List<Byte>> operator){
    	this.operator = operator;
    }
 	public Double apply(List<Double> values,  Double val)
    {
 		List<Byte> byteList = new List<Byte>();
 		for(Double b : values){
 			byteList.add(b.byteValue());
 		}
        return operator.compute(byteList, val.byteValue()).doubleValue();
    }
 	
 	
 }
 private class OperatorFloat extends Operator{
 	
 	private AggregOperator<Float, List<Float>> operator;

    
    
    public void setFloatOperator(AggregOperator<Float, List<Float>> operator){
    	this.operator = operator;
    }
 	public Double apply(List<Double> values,  Double val)
    {
 		List<Float> floatList = new List<Float>();
 		for(Double b : values){
 			floatList.add(b.floatValue());
 		}
        return operator.compute(floatList, val.floatValue()).doubleValue();
    }
 	
 }
 private class OperatorBoolean extends Operator{
 	
 	private AggregOperator<Boolean, List<Boolean>> operator;
    
    public void setBooleanOperator(AggregOperator<Boolean, List<Boolean>> operator){
    	this.operator = operator;
    }
 	public Double apply(List<Double> values,  Double val)
    {
 		
    Boolean v = false;
    if(val > 0.0)
    	v = true;
    
 		List<Boolean> bList = new List<Boolean>();
 		for(Double b : values){
 			if(b == 0.0){
 				bList.add(false);
 			}else{
 				bList.add(true);

 			}
 		}
 			
 		Boolean result =operator.compute(bList, v); 
 		if(result == false)
 			return 0.0;
 		
 		return 1.0;
    }
 	
 }
}
