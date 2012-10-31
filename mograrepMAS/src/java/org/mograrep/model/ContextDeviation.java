package org.mograrep.model;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.semanticweb.owlapi.model.IRI;


public class ContextDeviation {

	private IRI name;
	private IRI type;

	private List<ContextInformation> parents;

	private ContextData aData;
	private ContextData bData;
	
	
	private ContextDeviation(){

	}

	public static Triplet<List<Pair<ContextInformation, ContextInformation>>,List<ContextInformation>, List<ContextInformation>> getSameFeatures(List<ContextInformation> a, List<ContextInformation> b){
		List<Pair<ContextInformation, ContextInformation>> result = new ArrayList<Pair<ContextInformation,ContextInformation>>();

		LinkedList<ContextInformation> unmatchedA = new LinkedList<ContextInformation>(a);
		LinkedList<ContextInformation> unmatchedB = new LinkedList<ContextInformation>(b);


		for(int i=0;i<unmatchedA.size();i++){
			ContextInformation elementA = unmatchedA.get(i);
			for(int j=0;j<unmatchedB.size();j++){
				ContextInformation elementB = unmatchedB.get(i);
				if(elementA.sameType(elementB)){
					result.add(new Pair<ContextInformation, ContextInformation>(elementA, elementB));
					unmatchedA.remove(i);
					unmatchedB.remove(j);
					i--; 
					break;
				}
			}
		}

		List<ContextInformation> restA = new ArrayList<ContextInformation>(unmatchedA);
		List<ContextInformation> restB = new ArrayList<ContextInformation>(unmatchedB);

		return new Triplet<List<Pair<ContextInformation,ContextInformation>>, List<ContextInformation>, List<ContextInformation>>(result,  restA,  restB);
	}

	public static List<ContextDeviation> getDeviations(Action agreed, Action performed){

		List<ContextInformation> agreedCI = agreed.getContextInformationList();
		List<ContextInformation> performedCI = performed.getContextInformationList();


		return getMatchedDeviations(agreedCI, performedCI);
	}

	public static List<ContextDeviation> getMatchedDeviations( List<ContextInformation> a, List<ContextInformation> b){

		List<ContextDeviation> result = new ArrayList<ContextDeviation>();

		Triplet<List<Pair<ContextInformation, ContextInformation>>,List<ContextInformation>, List<ContextInformation>> splits = getSameFeatures(a, b);
		List<Pair<ContextInformation, ContextInformation>> matching = splits.getValue0();

		//List<ContextInformation> unmatchedAgreed = splits.getValue1();
		//List<ContextInformation> unmatchedPerformed = splits.getValue2();

	
		
		for(Pair<ContextInformation, ContextInformation> pair: matching){
			ContextInformation ag = pair.getValue0();
			ContextInformation pe = pair.getValue1();
			result.addAll(getDeviations(ag, pe));
		}

		return result;

	}

	private static List<ContextDeviation> getDeviations(ContextInformation a, ContextInformation b){
		List<ContextDeviation> results=null;

		if(a.hasValues()){
			if(!b.hasValues()){
				System.err.println("One has values, the other doesnt... Hmm...");
				return new ArrayList<ContextDeviation>();
			}
			
			TreeSet<ContextData> aValues = a.getValues();
			TreeSet<ContextData> bValues = b.getValues();
			results = new ArrayList<ContextDeviation>();
			
			for(ContextData da:aValues){
				ContextDeviation cd = new ContextDeviation();
				cd.setName((a.getName().equals(b.getName())?a.getName():IRI.create(a.getName()+"_"+b.getName().getFragment()))); //check
				cd.setType((a.getType().equals(b.getType())?a.getType():IRI.create(a.getType()+"_"+b.getType().getFragment()))); //check
				
				cd.setParents(a.getParents());
				
				ContextData db = bValues.ceiling(da);
				if(db!=null){
					cd.aData = da;
					cd.bData = db;
					
					results.add(cd);	
				}else{
					System.err.println("CI has no same CD");
				}
				
			}	
		}
		
		
			if(a.getContextInformationList()!=null && b.getContextInformationList()!=null){
				
				List<ContextDeviation> resultsRec = getMatchedDeviations(a.getContextInformationList(), b.getContextInformationList());
				if(resultsRec.size()>0){
					if(results!=null){
						results.addAll(resultsRec);
					}else{
						results = resultsRec;
					}
				}else{
					if(results==null){
						results = resultsRec; 
					}
				}
			}

		return results;
	}

	public IRI getName() {
		return name;
	}

	public void setName(IRI name) {
		this.name = name;
	}

	public List<ContextInformation> getParents() {
		return parents;
	}

	public void setParents(List<ContextInformation> parents) {
		this.parents = parents;
	}
	
	
	public String toString(){
		String result="";

		if(parents==null){
			parents=new ArrayList<ContextInformation>();
		}

		for(ContextInformation parent:parents){
			result+=parent.getName()+"("+parent.getType()+")->";
		}

		result+=getName()+"("+getType()+"): A("+aData.toString()+"), B("+bData.toString()+")";
		return result;
	}

	public IRI getType() {
		return type;
	}

	public void setType(IRI type) {
		this.type = type;
	}

	public ContextData getaData() {
		return aData;
	}

	public ContextData getbData() {
		return bData;
	}
}
