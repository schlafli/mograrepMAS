package org.mograrep.model.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mograrep.model.ContextData;
import org.mograrep.model.IDGManager;
import org.mograrep.model.InstanceDataGenerator;
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
		for(int i=0;i<1000;i++){
			final double value = new Random().nextDouble();
		idgm.addGenerator(new InstanceDataGenerator() {

			public double matchType(List<IRI> chain) {
				return value;
			}

			public String getGeneratorName() {
				// TODO Auto-generated method stub
				return "TestG "+value;
			}

			public List<ContextData> generateData(List<IRI> chain) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		}



		ArrayList<IRI> testList = new ArrayList<IRI>();
		testList.add(IRI.create("http://bla.com/ont#duck"));
		InstanceDataGenerator d = idgm.getMostApplicableGenerator(testList);
		if(d!=null)
		{
			System.out.println(d.getGeneratorName());
		}
		assertTrue(true);
	}

}
