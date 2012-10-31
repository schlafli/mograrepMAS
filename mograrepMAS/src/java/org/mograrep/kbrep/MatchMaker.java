package org.mograrep.kbrep;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;



public class MatchMaker {
	private IRI agentIRI = null;
	private IRI ontologyIRI = null;

	private OWLClassExpression agentOWLClass = null;
	private OWLOntology ont = null;

	private OWLOntologyManager manager;
	private PelletReasoner reasoner=null;

	private Set<AgentRef> agents = null;
	private Set<String> actions = null;

	private BiMap<String, String> actionToIRIMapping;

	//private Map<OWLClassExpression, OWLClass> expressionToClass; //ONLY contains classes that are in the ontology, if they are here it is safe to use the owl classes
	private Set<String> actionIRIs;


	private SetMultimap<String, AgentRef> canBePerformedBy;
	private SetMultimap<AgentRef, String> canPerform;


	public MatchMaker(OWLOntologyManager manager){
		agents = new HashSet<AgentRef>();
		actions = new HashSet<String>();
		actionIRIs = new HashSet<String>();
		actionToIRIMapping = HashBiMap.create();
		//expressionToClass = new HashMap<OWLClassExpression, OWLClass>();

		//actionNames = new HashSet<String>();
		canBePerformedBy = HashMultimap.create();
		canPerform = HashMultimap.create();
		this.manager = manager;

	}

	public void listAgents(){
		for(AgentRef a: agents){
			System.out.println(a.toString());
		}
	}

	public boolean loadOntology(IRI localOntologyIRI){
		boolean success = false;
		try {
			ont = manager.loadOntology(localOntologyIRI);
			reasoner = PelletReasonerFactory.getInstance().createReasoner(ont);
			manager.addOntologyChangeListener(reasoner);
			if(!reasoner.isConsistent()){
				System.err.println("Warning: Matchmaker ontology is inconsistant!");
			}


			ontologyIRI = ont.getOntologyID().getOntologyIRI();
			success = true;

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	public IRI getOntologyIRI(){
		return ontologyIRI;
	}

	public boolean setAgentClassExpression(OWLClassExpression agentClass){

		if(reasoner == null){
			System.err.println("Reasoner not initialised. Cannot set agent class expression!");
			return false;
		}
		agentOWLClass = agentClass;

		if(!reasoner.isSatisfiable(agentOWLClass)){
			System.err.println("Agent class expression is not satisfiable!");
			System.out.println(agentOWLClass.toString());
			agentOWLClass = null;
			return false;
		}

		agentIRI = agentClass.asOWLClass().getIRI(); //TODO: this may need fixing for anonymous classes
		//System.out.println(agentIRI);

		return true;
	}

	public void printOntology(){
		Set<OWLEntity> sig = ont.getSignature(true);
		for(OWLEntity e : sig){
			System.out.println(e.getIRI().toString());
		}
	}
	
	public boolean setAgentClassIRI(IRI agentClassIRI){
		if(ont == null){
			System.err.println("Reasoner not initialised. Cannot set agent IRI!");

			return false;
		}

		if(!ont.containsClassInSignature(agentClassIRI, true)){
			System.err.println("AgentClassIRI: \"" + agentClassIRI.toString()+"\" does not appear in the signature");
			printOntology();
			return false;
		}

		if(!OWLTools.isEntityIRI(agentClassIRI)){
			System.err.println("Age");
			return false;
		}
		if(!agentClassIRI.isAbsolute()){
			System.err.println("Age");
			return false;
		}
		OWLDataFactory fac= manager.getOWLDataFactory();

		OWLClassExpression agentClassFromIRI = fac.getOWLClass(agentClassIRI);

		return setAgentClassExpression(agentClassFromIRI);
	}

	//TODO: 
	public boolean registerAgent(AgentRef a){
		if(agents.contains(a)){
			return false;
		}


		agents.add(a);
		OWLDataFactory fac= manager.getOWLDataFactory();

		OWLNamedIndividual ind = fac.getOWLNamedIndividual(IRI.create(ontologyIRI.toString()+"#"+a.getName()));
		OWLAxiom addAxiom = fac.getOWLClassAssertionAxiom(agentOWLClass, ind);

		manager.addAxiom(ont, addAxiom);

		a.setAgentRef(ind);

		return true;
	}

	//	public OWLClass getClassOfAction(ActionExpression ae){
	//		
	//	}
//TODO: temp, remove
	public OWLOntology getOnt(){
		
		return ont;
	}
	
	
	public boolean registerAction(ActionExpression ae){
		OWLClass actionClass = null;
		boolean checkAdd = true;

		if(ae.getOWLClass()!=null){
			String owlClass = ae.getOWLClass().getIRI().toString();
			if(actionIRIs.contains(owlClass)){
				actionClass = ae.getOWLClass();
				if(!actions.contains(ae.getName())){
					System.err.println("The ActionExpression :\""+ae.getName()+"\" points to the same class as a different one, rename action");
					return false;
				}
				checkAdd = false;
				//could return true here
			}
		}


		if(actionClass==null){ //If this happens we have a "new" action (I.e., not in ont/unknown equivalence)
			if(!reasoner.isSatisfiable(ae.getOWLClassExpression())){
				System.err.println("ActionClassExpression is unsatisfiable! : " +ae.getOWLClassExpression().toString());
				return false;
			}

			Node<OWLClass> results = reasoner.getEquivalentClasses(ae.getOWLClassExpression());

			if(results.getSize()>=1){

				for(OWLClass oc: results){	
					if(oc.getIRI().getFragment().equals(ae.getName())){
						actionClass = oc; //this is the same as comparing class IRIs
					}
				}

				if(actionClass == null){
					actionClass = results.getRepresentativeElement();
					String newAEName = actionClass.getIRI().getFragment();
					System.out.println("Found equivalent action with different name, renaming ae:\""+ae.getName()+"\" to:\"" + newAEName+"\"");
					ae.setName(newAEName);

				}
				ae.setOWLClass(actionClass);

			}else{

				String tmpName = ae.getName();
				int i=1;
				String newActionClassName = tmpName;
				while(actions.contains(newActionClassName)){

					newActionClassName = tmpName+i;

					i++;
				}
				if(tmpName != newActionClassName){ //this is only safe since we assigned one to the other before the loop
					System.err.println("Action name: \""+ tmpName+"\" already exists, adding as: \""+newActionClassName+"\"");
					ae.setName(newActionClassName);
				}


				OWLDataFactory fac = manager.getOWLDataFactory();
				OWLClass newActionClass = fac.getOWLClass(OWLTools.createLocalEntityIRI(ont, newActionClassName));


				OWLDeclarationAxiom decAxiom = fac.getOWLDeclarationAxiom(newActionClass);
				OWLEquivalentClassesAxiom eqAxiom = fac.getOWLEquivalentClassesAxiom(newActionClass, ae.getOWLClassExpression());

				
				manager.addAxiom(ont, decAxiom);
				manager.addAxiom(ont, eqAxiom);

				ae.setOWLClass(newActionClass);


				//OWLClass nc = ae.getOWLClassExpression().


			}
		}

		if(checkAdd){
			String checkIRI = ae.getOWLClass().getIRI().toString();
			String checkName = ae.getName();

			if(actions.contains(checkName)){
				if(actionIRIs.contains(checkIRI)){
					//action and IRI are already stored


				}else{
					//actionName is saved but IRI isnt, this may happen if we are adding an action that is a copy of an aciton that is in the ontology.
					actionIRIs.add(checkIRI);
				}
			}else{
				if(actionIRIs.contains(checkIRI)){
					//no name but have IRI
					actions.add(checkName);
				}else{
					//neither name nor IRI
					actions.add(checkName);
					actionIRIs.add(checkIRI);
				}	
			}
			actionToIRIMapping.put(checkName, checkIRI);
		}

		return true;

	}


	public boolean registerAction(AgentRef ar, ActionExpression ae){
		if(!registerAction(ae)){
			return false;
		}
		if(!agents.contains(ar)){
			agents.add(ar);
		}

		canPerform.put(ar, ae.getName());
		canBePerformedBy.put(ae.getName(), ar);

		return true;
	}


	public void syncReasoner(){
		if(reasoner!=null)
			reasoner.flush();
	}
	
	
	public void listActions(ActionExpression ae){
		syncReasoner();
		OWLClassExpression ce = ae.getOWLClass();
		
		NodeSet<OWLNamedIndividual> results = reasoner.getInstances(ce, false);
		Set<OWLNamedIndividual> res = results.getFlattened();
		System.out.println("Actions for "+ae.getName()+":");
		for(OWLNamedIndividual i: res){
			System.out.println(i.getIRI().getFragment());

		}
	}
	
	public Set<AgentRef> getAgentsThatCanPerform(OWLIndividual action){
		OWLDataFactory fac = manager.getOWLDataFactory();
		//TODO: get parents of action
		
		
		return null;
	}

	public void listAgentsFromOntologyDef(){
		syncReasoner();
		NodeSet<OWLNamedIndividual> results = reasoner.getInstances(agentOWLClass, false);
		Set<OWLNamedIndividual> res = results.getFlattened();
		System.out.println("Agents:");
		for(OWLNamedIndividual i: res){
			System.out.println(i.getIRI().getFragment());

		}
	}

}
