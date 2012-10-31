package org.mograrep.kbrep;


import java.util.ArrayList;

import org.mograrep.model.ContextData;
import org.mograrep.model.ContextInformation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;


public class OWLInstantGenerator {
	
	//TODO: range?
	//TODO: DataGenerators
	//TODO: Do I care about properties specified in classes not explicitly stated in the owl class expression?
	public static void generateIndividualFromClassExpression(OWLClassExpression e){
		//The ASC properties are important here...
		OWLVisitorTest ovt = new OWLVisitorTest();
		e.accept(ovt);
		
		System.out.println(ovt.head.getFormattedView(0, "  "));
	}
	
	
	//TODO: deal with union of props
	public static void generateIndividualAxioms(OWLDataFactory fac, ContextInformation head, String name){
		ArrayList<OWLAxiom> axioms;
		//always remove the last one
		String indvName = head.getName().toString()+"_"+name;
				
		OWLNamedIndividual individual = fac.getOWLNamedIndividual(IRI.create(name));
		OWLClassExpression iClass = fac.getOWLClass(head.getType());
		
		
		OWLClassAssertionAxiom classAssertion = fac.getOWLClassAssertionAxiom(iClass, individual);
		
		
		
		
		if(head.hasContext()){
			
		}
		
		if(head.hasValues()){
			for(ContextData cd: head.getValues()){
				OWLDataPropertyExpression odp = fac.getOWLDataProperty(cd.getDataProperty());
				
			}
			
			
		}
		
		
	}
	
	
	
}
