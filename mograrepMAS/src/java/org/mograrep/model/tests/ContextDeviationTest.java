package org.mograrep.model.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;
import org.mograrep.model.Action;
import org.mograrep.model.ContextData;
import org.mograrep.model.ContextDeviation;
import org.mograrep.model.ContextInformation;
import org.mograrep.model.DoubleContextDataValue;
import org.semanticweb.owlapi.model.IRI;

public class ContextDeviationTest {

//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testGetDeviations() {
		Action aa1 = new Action(IRI.create("Action_1"), IRI.create("Type_1"));
		Action pa1 = new Action(IRI.create("Action_1"), IRI.create("Type_1"));
		
		ContextInformation aDelivery = new ContextInformation(IRI.create("Delivery"), IRI.create("Delivery"));
		ContextInformation aTime = new ContextInformation(IRI.create("DeliveryTime"), IRI.create("Temporal"));
		
		ContextData ahasTime = new ContextData(IRI.create("hasTime"));
		ahasTime.setMin(new DoubleContextDataValue(5.0));
		ahasTime.setMax(new DoubleContextDataValue(10.0));
		aTime.addValue(ahasTime);
		
		aa1.addContextInformation(aDelivery);
		aDelivery.addContextInformation(aTime);
		
		
		ContextInformation pDelivery = new ContextInformation(IRI.create("Delivery"), IRI.create("Delivery"));
		ContextInformation pTime = new ContextInformation(IRI.create("DeliveryTime"), IRI.create("Temporal"));
		
		ContextData phasTime = new ContextData(IRI.create("hasTime"));
		phasTime.setValue(new DoubleContextDataValue(6.0));
		pTime.addValue(phasTime);
		
		pa1.addContextInformation(pDelivery);
		pDelivery.addContextInformation(pTime);
		
		List<ContextDeviation> devs = ContextDeviation.getDeviations(aa1, pa1);
		for(ContextDeviation d: devs){
			System.out.println(d);
			
		}
		
		assertTrue(true);
		//fail("Not yet implemented");
		
	}

}
