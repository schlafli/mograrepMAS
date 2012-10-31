package org.mograrep.kbrep;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class AgentRef implements Comparable<AgentRef> {
	private String name;
	private OWLNamedIndividual agentRef;
	

	
	public AgentRef(String name){
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public OWLNamedIndividual getAgentRef() {
		return agentRef;
	}

	public void setAgentRef(OWLNamedIndividual agentRef) {
		this.agentRef = agentRef;
	}
	
	public String toString(){
		return name;
	}
	
	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Object o){
		//same ref
		if(this == o){ 
			return true;
		}
		
		if(o instanceof AgentRef){
			return this.hashCode()==o.hashCode();
		}
		return false;
	}

	public int compareTo(AgentRef o) {
		return name.compareTo(o.getName());
	}
	
	
}
