package org.mograrep.kbrep.tests;

import static org.junit.Assert.*;
import static org.mograrep.kbrep.OWLTools.addIRIMapping;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class MatchMakerTester {

	static OWLOntologyManager manager;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		manager = OWLManager.createOWLOntologyManager();
		
		addIRIMapping("http://www.semanticweb.org/ontologies/2012/8/domain1", 
				"/home/schlafli/Ontologies/domain1.owl", manager);

		addIRIMapping("http://www.csd.abdn.ac.uk/~schlafli/TREvCOnt", 
				"/home/schlafli/Ontologies/TREvCOnt.owl", manager);

		addIRIMapping("http://www.semanticweb.org/ontologies/2012/7/CoOL", 
				"/home/schlafli/Ontologies/CoOL.owl", manager);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		manager = null;
	}


	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	

}
