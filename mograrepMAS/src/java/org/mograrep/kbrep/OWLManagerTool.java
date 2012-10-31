package org.mograrep.kbrep;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OWLManagerTool {
	private OWLOntologyManager manager = null;
	
	
	public OWLManagerTool(OWLOntologyManager manager){
		this.manager = manager;
	}
	
	public OWLManagerTool(){
		manager = OWLManager.createOWLOntologyManager();
	}
	
	
	
}
