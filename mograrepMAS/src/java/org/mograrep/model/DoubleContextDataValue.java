package org.mograrep.model;

public class DoubleContextDataValue extends ContextDataValue {
	
	private Double value;
	
	public DoubleContextDataValue(double value){
		this.value = value;
		this.dataValueID = ContextDataValue.DOUBLE;
	}


	public Double getValue() {
		return value;
	}


	@Override
	public String toString() {
		return ""+value;
	}
	

}
