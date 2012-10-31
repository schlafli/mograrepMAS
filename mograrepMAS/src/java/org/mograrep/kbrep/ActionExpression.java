package org.mograrep.kbrep;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class ActionExpression {

	private OWLClassExpression actionExpression;
	private OWLClass actionClass;
	private String actionName;
	
	
	public ActionExpression(String name){
		this.actionName = name;
	}
	
	public String getName(){
		return actionName;
	}

	public OWLClass getOWLClass() {
		return actionClass;
	}

	public void setOWLClass(OWLClass actionClass) {
		this.actionClass = actionClass;
	}

	public void setOWLClassExpression(OWLClassExpression exp){
		this.actionExpression = exp;
	}

	public OWLClassExpression getOWLClassExpression(){
		return actionExpression;
	}
	
	
	public void setName(String name){
		this.actionName = name;
	}

}
