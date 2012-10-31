package org.mograrep.kbrep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.util.SimpleIRIMapper;



public class OWLTools {
	public static OWLOntologyManager manager = null;

	public static OWLOntologyManager createOntologyManager() {
		if(manager==null){
			manager = OWLManager.createOWLOntologyManager();
		}
		return manager;
	}

	public static OWLDataFactory getOWLDataFactory(){
		createOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		return factory;
	}


	public static boolean isEntityIRI(IRI iri){
		boolean result = false;
		String ns = iri.toString();
		if(ns.indexOf("#")!=-1){
			result = true;
		}
		
		return result;
	}
	
	public static String getEntityIRI(OWLEntity oc){
		String uri = "";
		String ns = oc.toString();
		if(ns.indexOf("#")!=-1){
			uri = ns.substring(0,ns.indexOf("#"));
		}
		return uri;
	}
	
	
	public static void addIRIMapping(String ontologyIRI, String documentIRI, OWLOntologyManager man){
		IRI ontIRI = IRI.create(ontologyIRI);
		IRI docIRI = IRI.create(documentIRI);
		SimpleIRIMapper mapper = new SimpleIRIMapper(ontIRI, docIRI);
		man.addIRIMapper(mapper);
	}

	/*
 public static String getOntologyURI(OWLOntology onto){
         String ns = "";         manager.g
         URI uri = onto.;
         if(uri==null)
                 ns = "";
         else if(ns.indexOf("#")==-1)
                 ns = ns + "#";
         return ns;
 }*/

	//This method has the same functionality as getEntityURI (qiji)
	public static String getNamespace(OWLEntity ent){
		String ns = null;
		if(ent!=null){
			String ts = ent.toString();
			int index = ts.lastIndexOf("#");
			if(index==-1){
				index = ts.lastIndexOf("/");
				if(index==-1){
					ns = ts;
				} else {
					ns = ts.substring(0,index);
				}
			} else {
				ns = ts.substring(0,index);
			}
		}

		return ns;
	}

	// public static HashSet<OWLEntity> getEntities(OWLAxiom a){
	//         HashSet<OWLEntity> ents = new HashSet<OWLEntity>(
	//                         a.getReferencedEntities());
	//         return ents;
	// }
	//
	// public static HashSet<OWLEntity> getEntities(HashSet<OWLAxiom> axioms){
	//         HashSet<OWLEntity> ents = new HashSet<OWLEntity>();
	//         for(OWLAxiom a : axioms){
	//                 ents.addAll(getEntities(a));
	//         }
	//
	//         return ents;
	// }
	//
	// public static HashMap<OWLAxiom, HashSet<OWLEntity>> getAxiomEntityMap(Set<OWLAxiom> axioms){
	//         HashMap<OWLAxiom, HashSet<OWLEntity>> map = new HashMap<OWLAxiom, HashSet<OWLEntity>>();
	//         for(OWLAxiom a : axioms){
	//                 HashSet<OWLEntity> ents = getEntities(a);
	//                 if(ents!=null){
	//                         map.put(a, ents);
	//                 }
	//         }
	//         return map;
	// }



	public static String getLocalName(OWLOntology o){
		String name = null;
		createOntologyManager();
		IRI iri = manager.getOntologyDocumentIRI(o);
		if(iri!=null){
			name = getLocalName(iri.toString());
		}
		return name;
	}


	public static String getLocalName(OWLEntity ent){
		String name = null;
		if(ent!=null){
			String ts = ent.toString();
			int ind1 = ts.lastIndexOf("#");
			if(ind1==-1){
				ind1 = ts.lastIndexOf("/");
				if(ind1==-1){
					name = ts;
				} else {
					name = ts.substring(ind1+1);
				}
			} else {
				name = ts.substring(ind1+1);
			}
		}

		return name;
	}

	public static String getLocalName(String ts){
		String name = null;
		int ind1 = ts.lastIndexOf("/");
		if(ind1==-1){
			ind1 = ts.lastIndexOf(":");
			if(ind1==-1){
				name = ts;
			} else {
				name = ts.substring(ind1+1);
			}
		} else {
			int ind2 = ts.lastIndexOf('.');
			if(ind2>ind1){
				name = ts.substring(ind1+1, ind2);
			}
		}
		return name;
	}

	public static String hasRelation(OWLClass A, OWLClass B, OWLOntology onto, boolean useReasoner){
		String relation = "norelation";
		if(!useReasoner){
			Set<OWLClassExpression> sets = A.getSuperClasses(onto);
			if(sets.contains(B)){
				relation = "subclassof";
			}
			sets = A.getSubClasses(onto);
			if(sets.contains(B)){
				relation = "superclassof";
			}
		} else {

		}
		return relation;
	}

	public static OWLOntology openOntology(String ontoPath) throws Exception {
		createOntologyManager();
		String path = checkOntoPath(ontoPath);

		IRI physicalIRI = IRI.create(path);
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(physicalIRI);
		return ontology;
	}


	public static OWLOntology openOntology(String ontoPath,
			OWLOntologyManager conn) throws Exception {
		String path = checkOntoPath(ontoPath);
		IRI physicalIRI = IRI.create(path);
		OWLOntology ontology = conn.loadOntologyFromOntologyDocument(physicalIRI);
		return ontology;
	}


	public static String checkOntoPath(String ontoPath) {
		String path = ontoPath;
		if (!ontoPath.startsWith("http:"))
			if (!ontoPath.startsWith("https:"))
				if (!ontoPath.startsWith("ftp:"))
					if (!ontoPath.startsWith("file:"))
						path =  "file:" + ontoPath;
		return path;
	}

	public static void clearOntology(OWLOntology o,
			OWLOntologyManager manager) throws Exception {
		List<OWLOntologyChange> list = new ArrayList<OWLOntologyChange>();
		for(OWLAxiom a : o.getAxioms()){
			RemoveAxiom removeAxiom = new RemoveAxiom(o, a);
			list.add(removeAxiom);
		}
		if(list.size()>0){
			manager.applyChanges(list);
		}
	}

	public static void addAxioms(
			OWLOntology o,
			OWLOntologyManager conn,
			HashSet<OWLAxiom> axioms) throws Exception {

		List<OWLOntologyChange> list = new ArrayList<OWLOntologyChange>();
		for (OWLAxiom axiom : axioms) {
			if(axiom==null){
				continue;
			}
			AddAxiom addAxiom = new AddAxiom(o, axiom);
			list.add(addAxiom);
		}
		if(list.size()>0){
			conn.applyChanges(list);
		}
	}

	public static void addAxioms(
			OWLOntology onto,
			OWLOntology newOnto,
			OWLOntologyManager manager) throws Exception {

		HashSet<OWLAxiom> axioms = new HashSet<OWLAxiom>(getAxioms(newOnto, null));
		addAxioms(onto,manager, axioms);
	}

	public static void addAxiom(
			OWLOntology onto,
			OWLAxiom a) throws Exception{

		createOntologyManager();
		AddAxiom axiom = new AddAxiom(onto, a);
		manager.applyChange(axiom);
	}

	public static void removeAxioms(
			OWLOntology onto,
			HashSet<OWLAxiom> axioms) throws Exception {

		createOntologyManager();
		List<OWLOntologyChange> list = new ArrayList<OWLOntologyChange>();
		for (OWLAxiom a : axioms) {
			RemoveAxiom axiom = new RemoveAxiom(onto, a);
			list.add(axiom);
		}
		if(list.size()>0){
			manager.applyChanges(list);
		}
	}

	public static void removeAxiom(
			OWLOntology onto,
			OWLAxiom a) throws Exception {

		createOntologyManager();
		RemoveAxiom axiom = new RemoveAxiom(onto, a);
		manager.applyChange(axiom);
	}

	//*********************************************
	//Create an ontology with or without some axioms
	//*********************************************

	public static OWLOntology createOntology() throws Exception {
		return createOntology(null, new HashSet<OWLAxiom>(), null, null);
	}

	public static OWLOntology createOntology(String logicalUrl) throws Exception {
		return createOntology(null, new HashSet<OWLAxiom>(), logicalUrl, null);
	}

	public static OWLOntology createOntology(String logicalUrl,
			String physicalUrl) throws Exception {
		return createOntology(null, new HashSet<OWLAxiom>(), logicalUrl, physicalUrl);
	}

	public static OWLOntology createOntology(
			HashSet<OWLAxiom> axioms) throws Exception  {
		return createOntology(null, axioms, null, null);
	}

	public static OWLOntology createOntology(
			HashSet<OWLAxiom> axioms,
			String physicalPath,
			String logicalPath) throws Exception  {
		return createOntology(null, axioms, logicalPath, physicalPath);
	}

	public static OWLOntology createOntology(
			HashSet<OWLAxiom> axioms,
			String logicalUrl) throws Exception {
		return createOntology(null, axioms, logicalUrl, null);
	}

	public static OWLOntology createOntology(
			OWLOntologyManager conn,
			HashSet<OWLAxiom> axioms) throws Exception{
		return createOntology(conn, axioms, null, null);
	}

	public static OWLOntology createOntology(
			OWLOntologyManager conn_p,
			HashSet<OWLAxiom> axioms_p,
			String logicalUrl_p,
			String physicalUrl_p) throws Exception{
		String logicalUrl = logicalUrl_p;
		String physicalUrl = physicalUrl_p;
		long id = System.currentTimeMillis();
		if(logicalUrl==null){
			logicalUrl = "http://radon.ontoware.org/example"+id;
		}
		if(physicalUrl==null){
			physicalUrl = "file:newOnto-"+ id;
		} else {
			checkOntoPath(physicalUrl);
		}

		IRI ontologyIRI = IRI.create(logicalUrl);
		IRI physicalIRI = IRI.create(physicalUrl);
		SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, physicalIRI);
		OWLOntology o = null;
		if(conn_p==null){
			manager = OWLManager.createOWLOntologyManager();
			//createOntologyManager();
			manager.addIRIMapper(mapper);
			o = manager.createOntology(ontologyIRI);
			addAxioms(o, manager, axioms_p);
		} else {
			conn_p.addIRIMapper(mapper);
			o = conn_p.createOntology(ontologyIRI);
			addAxioms(o, conn_p, axioms_p);
		}
		return o;
	}

	@SuppressWarnings("rawtypes")
	public static HashSet<OWLAxiom> getAxioms(OWLOntology o, AxiomType type) throws Exception {
		return getAxioms(o,type,false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashSet<OWLAxiom> getAxioms(
			OWLOntology o,
			AxiomType type,
			boolean withAnnotationAxioms) throws Exception {
		HashSet<OWLAxiom> all = new HashSet<OWLAxiom>();
		if(type==null){
			if(!withAnnotationAxioms){
				all.addAll(o.getLogicalAxioms());
			} else {
				all.addAll(o.getAxioms());
			}
		} else {
			all.addAll(o.getAxioms(type));
		}
		return (HashSet<OWLAxiom>)all.clone();
	}

	public static IRI createLocalEntityIRI(OWLOntology ont, String localName){
		IRI baseIRI = ont.getOntologyID().getOntologyIRI();
		return IRI.create(baseIRI.toString() +"#"+localName);
	}
	
	public static OWLClass createOWLClass(
			OWLOntology onto,
			String entityIRI) throws Exception{

		createOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLClass ent = factory.getOWLClass(IRI.create(entityIRI));

		return ent;
	}

	public static OWLEntity getEntity(
			OWLOntology onto,
			String entityURI) throws Exception{
		OWLEntity ent = null;
		IRI iri = IRI.create(entityURI);
		createOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();


		if(onto.containsClassInSignature(iri)){
			ent = factory.getOWLClass(iri);
		} else if(onto.containsObjectPropertyInSignature(iri)){
			ent = factory.getOWLObjectProperty(iri);
		} else if(onto.containsDataPropertyInSignature(iri)){
			ent = factory.getOWLDataProperty(iri);
		} else if(onto.containsIndividualInSignature(iri)){
			ent = factory.getOWLNamedIndividual(iri);
		} else if(onto.containsDatatypeInSignature(iri)){
			ent = factory.getOWLDatatype(iri);
		}
		return ent;
	}

	public static OWLClass getOWLClass(String entityIRI){

		createOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLClass ent = factory.getOWLClass(IRI.create(entityIRI));
		return ent;
	}

	public static OWLIndividual getOWLNamedIndividual(
			String entityIRI) throws Exception{

		createOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLIndividual ent = factory.getOWLNamedIndividual(IRI.create(entityIRI));
		return ent;
	}

	public static OWLEntity getEntityWithLocalName(
			OWLOntology onto,
			String localName) throws Exception{
		OWLEntity ent = null;
		for(OWLEntity entity : onto.getSignature(true)){
			String name = getLocalName(entity);
			if(name!=null && name.equals(localName)){
				ent = (OWLEntity)entity;
				break;
			}
		}
		return ent;
	}

	public static Set<OWLObject> getFunctionalProperties(
			OWLOntology o) throws Exception {
		Set<OWLObject> allFunc = new HashSet<OWLObject>();
		Set<OWLObjectProperty> all = o.getObjectPropertiesInSignature(true);
		for(OWLObjectProperty op : all){
			if(op.isFunctional(o)){
				allFunc.add(op);
			}
		}
		Set<OWLDataProperty> alldp = o.getDataPropertiesInSignature(true);
		for(OWLDataProperty op : alldp){
			if(op.isFunctional(o)){
				allFunc.add(op);
			}
		}
		return allFunc;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashSet getIntersect( HashSet A_in, HashSet B_in ){
		if(A_in == null || B_in == null){
			System.out.println("Can't obtain Intersection of  null Sets");
		}
		HashSet A = (HashSet)A_in.clone();
		A.retainAll(B_in);
		return A;
	}


	public static HashSet<OWLAxiom> getTBox(OWLOntology ontology) throws Exception{
		HashSet<OWLAxiom> allAxioms = getAxioms(ontology,null,false);
		HashSet<OWLAxiom> aBox = getABox(ontology);
		allAxioms.removeAll(aBox);
		return allAxioms;
	}

	@SuppressWarnings("unchecked")
	public static HashSet<OWLAxiom> getRBox(OWLOntology ontology) throws Exception{
		Set<OWLAxiom> tempAxioms = new HashSet<OWLAxiom>();

		//Obtain all the axioms except annotation axioms
		HashSet<OWLAxiom> axioms = getAxioms(ontology,null,false);

		//Remove abox
		tempAxioms = getABox(ontology);
		axioms.removeAll(tempAxioms);

		//Remove pure TBox
		tempAxioms = getPureTBox(ontology);
		axioms.removeAll(tempAxioms);

		return (HashSet<OWLAxiom>) axioms.clone();
	}


	public static HashSet<OWLAxiom> getPureTBox(OWLOntology ontology) throws Exception{
		HashSet<OWLAxiom> axioms = getAxioms(ontology,AxiomType.SUBCLASS_OF);
		axioms.addAll(getAxioms(ontology,AxiomType.EQUIVALENT_CLASSES));
		axioms.addAll(getAxioms(ontology,AxiomType.DISJOINT_CLASSES));
		return axioms;
	}

	public static HashSet<OWLAxiom> getABox(OWLOntology ontology)  throws Exception{
		HashSet<OWLAxiom> axioms = getAxioms(ontology,AxiomType.CLASS_ASSERTION);
		axioms.addAll(getAxioms(ontology,AxiomType.OBJECT_PROPERTY_ASSERTION));
		axioms.addAll(getAxioms(ontology,AxiomType.DATA_PROPERTY_ASSERTION));
		axioms.addAll(getAxioms(ontology,AxiomType.DIFFERENT_INDIVIDUALS));
		axioms.addAll(getAxioms(ontology,AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION));
		axioms.addAll(getAxioms(ontology,AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION));
		axioms.addAll(getAxioms(ontology,AxiomType.SAME_INDIVIDUAL));
		return axioms;
	}

	public static void saveOntology(OWLOntologyManager manager,
			OWLOntology onto, OWLOntologyFormat format, String uriStr)
					throws Exception{
		manager.saveOntology(onto, format, IRI.create(uriStr));
	}

	public static void saveOntology(OWLOntology onto)
			throws Exception{
		manager.saveOntology(onto);
	}

	public static void saveOntology(HashSet<OWLAxiom> set, String physicalPath)
			throws Exception{
		OWLOntology onto = createOntology(set, physicalPath, null);
		manager.saveOntology(onto, new RDFXMLOntologyFormat(), IRI.create(physicalPath));
		//saveOntology(onto);
	}

	public static void saveOntology(HashSet<OWLAxiom> set, String formatStr, String physicalPath)
			throws Exception{
		if(physicalPath.contains(" ")){
			physicalPath = physicalPath.replace(" ", "%20");
		}
		OWLOntology onto = createOntology(set, physicalPath, null);
		if(formatStr.contains("Manchester OWL Syntax")){
			manager.saveOntology(onto, new ManchesterOWLSyntaxOntologyFormat(), IRI.create(physicalPath));
		} else if(formatStr.contains("OWL Functional Syntax")){
			manager.saveOntology(onto, new OWLFunctionalSyntaxOntologyFormat(), IRI.create(physicalPath));
		} else if(formatStr.contains("OWL/XML-Format")){
			manager.saveOntology(onto, new OWLXMLOntologyFormat(), IRI.create(physicalPath));
		} else if(formatStr.contains("RDF/XML-Format")){
			manager.saveOntology(onto, new RDFXMLOntologyFormat(), IRI.create(physicalPath));
		}
	}

	public static ArrayList<String> getSaveFormats(){
		ArrayList<String> ret = new ArrayList<String>(4);
		ret.add("Manchester OWL Syntax");
		ret.add("OWL Functional Syntax");
		ret.add("OWL/XML-Format");
		ret.add("RDF/XML-Format");
		return ret;
	}
}
