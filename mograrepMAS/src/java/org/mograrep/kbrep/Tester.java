package org.mograrep.kbrep;

import org.semanticweb.owlapi.model.IRI;

public class Tester {
	public static void main(String args[]){
		IRI bla = IRI.create("http://www.csd.abdn.ac.uk/~schlafli/TREvCOnt.owl#Agent");
		System.out.println("Scheme: "+bla.getScheme());
		System.out.println("Fragment: "+bla.getFragment());
		System.out.println("Start: "+bla.getStart());
		System.out.println("Absolute: "+bla.isAbsolute());
		
	}
}
