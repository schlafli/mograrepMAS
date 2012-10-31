package org.mograrep.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.IRI;


public class ContextInformation {

	public IRI getName() {
		return name;
	}

	public IRI getType() {
		return type;
	}

	private IRI name;
	private IRI type;

	protected boolean isAction;
	
	protected List<ContextInformation> hasContext;

	
	//ContextInformation hasMinimum;

	protected List<ContextInformation> parents;


	protected TreeSet<ContextData> hasValues;


	public ContextInformation(IRI name, IRI type){
		this.name = name;
		this.type = type;
	}

	public boolean sameType(ContextInformation comp){
		return name.equals(comp.name) && type.equals(comp.type);
	}

	public boolean isAction(){
		return isAction;
	}
	
	public void setAction(boolean isAction){
		this.isAction = isAction;
	}
	
	private void checkHasValues(){
		if(hasValues == null){
			hasValues = new TreeSet<ContextData>(new Comparator<ContextData>() {
				public int compare(ContextData o1, ContextData o2) {
					return o1.getDataProperty().compareTo(o2.getDataProperty());

				}
			});
		}
	}

	public boolean addValue(ContextData value){
		checkHasValues();
		return hasValues.add(value);
	}

	public TreeSet<ContextData> getValues(){
		return hasValues;
	}

	public boolean hasValues(){
		return hasValues!=null;
	}

	public boolean hasContext(){
		return hasContext!=null;
	}

	public List<ContextInformation> getContextInformationList(){
		return hasContext;
	}

	public void addParents(List<ContextInformation> parents){
		checkParents();
		this.parents.addAll(parents);
	}

	public void addParent(ContextInformation parent){
		checkParents();
		this.parents.add(parent);
	}

	public void checkParents(){
		if(this.parents==null){
			this.parents = new ArrayList<ContextInformation>();
		}
	}

	public List<ContextInformation> getParents(){
		return parents;
	}

	public void addContextInformation(ContextInformation q){
		if(hasContext==null){
			hasContext = new ArrayList<ContextInformation>();
		}
		q.addParents(parents);
		q.addParent(this);

		hasContext.add(q);
	}

	public void addContextInformation(List<ContextInformation> q){
		if(hasContext==null){
			hasContext = new ArrayList<ContextInformation>();
		}

		for(ContextInformation i:q){
			addContextInformation(i);
		}
	}


	private String getStringTabs(int tabs, String tab){
		String ret = "";
		for(int i=0;i<tabs;i++){
			ret+=tab;
		}
		return ret;
	}
	
	public List<IRI> getInverseTypeChain(){
		ArrayList<IRI> tc = new ArrayList<IRI>();
		tc.add(getType());
		for(int i=parents.size()-1;i>=0;i--){
			tc.add(parents.get(i).getType());
		}
		return tc;
	}
	
	public List<IRI> getTypeChain(){
		ArrayList<IRI> tc = new ArrayList<IRI>();
		for(ContextInformation parent: parents){
			tc.add(parent.getType());
		}
		tc.add(getType());
		return tc;
	}

	public String getFormattedView(int tabs, String tab){
		String indent = getStringTabs(tabs, tab);
		String ret=indent;
		ret += name.getFragment()+"("+type.getFragment()+")"+((this.isAction)?"_action":"") + " \n";
		indent = getStringTabs(tabs+1, tab);
		if(hasContext()){
			for(ContextInformation ci: hasContext){
				//ret += indent +"hasQuality some\n";
				ret += ci.getFormattedView(tabs+2, tab);	
			}
		}
		if(hasValues()){
			for(ContextData cd: hasValues){
				ret += indent + cd.toString()+"\n";
			}
		}
		return ret;
	}

}
