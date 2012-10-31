package org.mograrep.model;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.IRI;


public class Action extends ContextInformation{
	
	
	public Action(IRI name, IRI type) {
		super(name, type);
		this.parents = new ArrayList<ContextInformation>();
		this.isAction = true;
	}
	
	
}
