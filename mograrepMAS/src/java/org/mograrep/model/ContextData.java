package org.mograrep.model;

import org.semanticweb.owlapi.model.IRI;

/**
 * A representation of data for a context
 * 
 * @author schlafli
 *
 */
public class ContextData {

	private IRI dataProperty;
	
	private ContextDataValue min = null;
	private ContextDataValue max = null;
	
	private ContextDataValue value = null;
	
	public ContextData(IRI dataPropertyIRI){
		this.dataProperty = dataPropertyIRI;
	}
	
	public IRI getDataProperty() {
		return dataProperty;
	}
	public void setDataProperty(IRI dataProperty) {
		this.dataProperty = dataProperty;
	}
	public ContextDataValue getMin() {
		return min;
	}
	public void setMin(ContextDataValue min) {
		this.min = min;
	}
	public ContextDataValue getMax() {
		return max;
	}
	public void setMax(ContextDataValue max) {
		this.max = max;
	}
	public ContextDataValue getValue() {
		return value;
	}
	public void setValue(ContextDataValue value) {
		this.value = value;
	}
	
	
	public String toString(){
		String ret = dataProperty.getFragment() + " (";
		if(value!=null){
			ret += value.toString();
		}else{
			ret+= min.toString() +" to " +max.toString();
		}
		return ret+")";
	}
	
}
