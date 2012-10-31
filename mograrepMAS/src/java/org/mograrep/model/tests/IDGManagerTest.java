package org.mograrep.model.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mograrep.model.IDGManager;
import org.semanticweb.owlapi.model.IRI;

public class IDGManagerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetMostApplicableGenerator() {
		IDGManager idgm = new IDGManager();
		ArrayList<IRI> testList = new ArrayList<IRI>();
		testList.add(IRI.create("http://bla.com/ont#duck"));
		idgm.getMostApplicableGenerator(testList);
		
		assertTrue(true);
	}

}
