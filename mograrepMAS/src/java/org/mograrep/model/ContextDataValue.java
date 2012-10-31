package org.mograrep.model;

public abstract class ContextDataValue {
	public static final int DOUBLE = 1;
	
	
	protected int dataValueID;
	
	public abstract Object getValue();
	
	public int getDataValueID(){
		return dataValueID;
	}
	
	public abstract String toString();
}
