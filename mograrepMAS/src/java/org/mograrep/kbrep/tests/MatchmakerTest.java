package org.mograrep.kbrep.tests;




import org.mograrep.kbrep.ActionExpression;
import org.mograrep.kbrep.AgentRef;
import org.mograrep.kbrep.MatchMaker;
import org.mograrep.kbrep.OWLInstantGenerator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.vocab.OWLFacet;

import static org.mograrep.kbrep.OWLTools.addIRIMapping;

public class MatchmakerTest {
	
	OWLOntologyManager manager;
	MatchMaker match;
	
	public MatchmakerTest(){
		manager = OWLManager.createOWLOntologyManager();
		
	}
	

	public void setUpIRIMapping(){
		addIRIMapping("http://www.semanticweb.org/ontologies/2012/8/domain1", 
				"file://localhost/home/schlafli/Ontologies/domain1.owl", manager);

		addIRIMapping("http://www.csd.abdn.ac.uk/~schlafli/TREvCOnt", 
				"file://localhost/home/schlafli/Ontologies/TREvCOnt.owl", manager);

		addIRIMapping("http://www.semanticweb.org/ontologies/2012/7/CoOL", 
				"file://localhost/home/schlafli/Ontologies/CoOL.owl", manager);

	}
	
	public void createMatchMaker(){
		match = new MatchMaker(manager);
	
	}
	
	public void loadOntology(){
		match.loadOntology(IRI.create("http://www.semanticweb.org/ontologies/2012/8/domain1"));
	}
	
	
	public boolean setAgentIRI(){
		return match.setAgentClassIRI(IRI.create("http://www.csd.abdn.ac.uk/~schlafli/TREvCOnt#Agent"));
	}
	
	public boolean addAgent(String name){
		AgentRef a = new AgentRef(name);
		return match.registerAgent(a);
	}
	
	public boolean check(OWLEntity e){
		
		boolean result = match.getOnt().containsEntityInSignature(e, true);
		if(!result){
			System.err.println("Not in ont:"+e.getIRI().toString());
		}
		return result;
	}
	
	public boolean addAction(){
		OWLDataFactory fac = manager.getOWLDataFactory();
		ActionExpression ae = new ActionExpression("Action_A");
		
		OWLClass action = fac.getOWLClass(IRI.create("http://www.csd.abdn.ac.uk/~schlafli/TREvCOnt#Action"));
		OWLObjectProperty charachterizedBy = fac.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/ontologies/2012/7/CoOL#characterizedBy"));
		OWLClass deliveryLocation = fac.getOWLClass(IRI.create("http://www.semanticweb.org/ontologies/2012/8/domain1#DeliveryLocation"));
		OWLClass pickupLocation = fac.getOWLClass(IRI.create("http://www.semanticweb.org/ontologies/2012/8/domain1#PickupLocation"));
		OWLObjectProperty hasQuality = fac.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/ontologies/2012/7/CoOL#hasQuality"));
		OWLClass location = fac.getOWLClass(IRI.create("http://www.semanticweb.org/ontologies/2012/8/domain1#Location"));
		OWLDataProperty coordinateX = fac.getOWLDataProperty(IRI.create("http://www.semanticweb.org/ontologies/2012/8/domain1#coordinateX"));
		OWLDataProperty coordinateY = fac.getOWLDataProperty(IRI.create("http://www.semanticweb.org/ontologies/2012/8/domain1#coordinateY"));
		
		
		//OWLDataSomeValuesFrom valInRange = fac.getOWLDataSomeValuesFrom(coordinateX, fac.getOWLDatatypeMinMaxExclusiveRestriction(0.1, 2.0));
		OWLDatatypeRestriction valRange = fac.getOWLDatatypeRestriction(fac.getDoubleOWLDatatype(), fac.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, 0.1), fac.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, 2.0));
		
		
		check(action);
		check(charachterizedBy);
		check(deliveryLocation);
		check(hasQuality);
		check(location);
		
		OWLClassExpression locationAndCoordinate = fac.getOWLObjectIntersectionOf(location, fac.getOWLDataSomeValuesFrom(coordinateX, valRange), fac.getOWLDataSomeValuesFrom(coordinateY, valRange));
		OWLClassExpression hasQualitySomeLocation = fac.getOWLObjectSomeValuesFrom(hasQuality, locationAndCoordinate);
		OWLClassExpression deliveryLocationAndHasQualitySomeLocation  = fac.getOWLObjectIntersectionOf(deliveryLocation, hasQualitySomeLocation);
		OWLClassExpression charBySome = fac.getOWLObjectSomeValuesFrom(charachterizedBy, deliveryLocationAndHasQualitySomeLocation);
		
		OWLClassExpression pickupLAHQSL = fac.getOWLObjectIntersectionOf(pickupLocation, hasQualitySomeLocation);
		OWLClassExpression charBySome2 = fac.getOWLObjectSomeValuesFrom(charachterizedBy, pickupLAHQSL);
		
		
		OWLClassExpression all = fac.getOWLObjectIntersectionOf(action, charBySome, charBySome2);
		
		
		//Set<OWLClassExpression> aset = all.getNestedClassExpressions();
		
//		for(OWLClassExpression e: aset){
//			System.out.println(e.toString());
//		}
		
		ae.setOWLClassExpression(all);

		
		System.out.println("Visitor:");
		OWLInstantGenerator.generateIndividualFromClassExpression(all);
		
//		AgentRef a = new AgentRef("AgentBob");
//		System.out.println("registerAction: " +match.registerAction(a, ae));
		
		//don't return yourself
//		match.listActions(ae);
		
		
		return false;
	}
	
	
	
	
	public void printa(){
		//System.out.println(match.getOntologyIRI());
		match.listAgents();
	}
	
	public static void main(String args[]){
		
		MatchmakerTest mm = new MatchmakerTest();
		
		System.out.print("Setting up IRI mapper:  ");
		mm.setUpIRIMapping();
		System.out.println("done");
		
		
		System.out.print("Creating MML ");
		mm.createMatchMaker();
		System.out.println("done");
		
		
		System.out.print("Loading ontologyL ");
		mm.loadOntology();
		System.out.println("done");
		
		
		System.out.print("Setting Agent IRI def: ");
		System.out.print(mm.setAgentIRI());
		System.out.println(" :done");
		
		
		
		System.out.println(mm.addAgent("AgentBob"));
//		System.out.println(mm.addAgent("AgentBob"));
//		
//		System.out.println(mm.addAgent("AgentCharlie"));
//		System.out.println(mm.addAgent("AgentDan"));
//		System.out.println(mm.addAgent("AgentEarl"));
//		System.out.println(mm.addAgent("AgentFiona"));
//		System.out.println(mm.addAgent("AgentGertrude"));
//		System.out.println(mm.addAgent("AgentHellary"));
//		System.out.println(mm.addAgent("AgentIan"));
//		
//		mm.printa();
		
		mm.addAction();
		
	}
}
