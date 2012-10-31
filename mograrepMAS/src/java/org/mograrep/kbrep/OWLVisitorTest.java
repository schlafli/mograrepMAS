package org.mograrep.kbrep;

import java.util.HashSet;
import java.util.Set;

import org.mograrep.model.ContextData;
import org.mograrep.model.ContextDataValue;
import org.mograrep.model.ContextInformation;
import org.mograrep.model.DoubleContextDataValue;
import org.semanticweb.owlapi.model.DataRangeType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.vocab.OWLFacet;


public class OWLVisitorTest implements OWLClassExpressionVisitor {

	public int individuals=0;

	ContextInformation head;
	ContextInformation current;
	boolean charObjProp = false;
	boolean hasQObjProp = false;

	boolean print = true;


	int tabs = 0;
	String tab = "    ";

	public void visit(OWLClass arg0) {
		// TODO Auto-generated method stub

	}

	private void indent(){
		tabs++;
	}

	private void unindent(){
		tabs--;
		if(tabs<0){
			tabs = 0;
		}
		charObjProp=false;
		hasQObjProp=false;
	}

	private void printlnIndented(String s){
		if(print){
			printTheIndent();
			System.out.println(s);
			lned = true;
		}
	}


	private void printTheIndent(){
		if(print){
			if(lned){
				for(int i=0;i<tabs;i++){
					System.out.print(tab);
				}
			}
			lned = false;
		}
	}

	private void printIndented(String s){
		if(print){
			printTheIndent();
			System.out.print(s);
		}
	}

	private boolean lned = true;



	public void visit(OWLObjectIntersectionOf arg0) {
		//printlnIndented("Intersection");
		indent();
		Set<OWLClassExpression> tmp = arg0.asConjunctSet();
		Set<OWLClassExpression> others = new HashSet<OWLClassExpression>();

		boolean jumpup=false;
		for(OWLClassExpression ce:tmp){
			if(ce.isClassExpressionLiteral()){
				ContextInformation tmpCI = new ContextInformation(ce.asOWLClass().getIRI(), ce.asOWLClass().getIRI());
				if(head==null){
					tmpCI.checkParents();
					head = tmpCI;
					current=head;
				}
				if(charObjProp||hasQObjProp){
					if(charObjProp){
						if(head==current){
							head.setAction(true);
						}
					}
					current.addContextInformation(tmpCI);
					current = tmpCI;
					jumpup = true;
				}
				printIndented(ce.asOWLClass().getIRI().getFragment());
				individuals++;

				printlnIndented(" and");
			}else{
				others.add(ce);
			}
		}

		for(OWLClassExpression ce:others){
			ce.accept(this);

			//			indent();
			//			printlnIndented("ret");
			//			unindent();
		}
		if(jumpup){
			current = current.getParents().get(current.getParents().size()-1);
		}
		unindent();
	}

	public void visit(OWLObjectUnionOf arg0) {
		printlnIndented("Union");
		indent();
		Set<OWLClassExpression> tmp = arg0.asConjunctSet();
		for(OWLClassExpression ce:tmp){
			if(ce.isClassExpressionLiteral()){
				printlnIndented(ce.asOWLClass().getIRI().getFragment());
			}else{
				//ce.accept(this);
			}
		}
		unindent();
	}

	public void visit(OWLObjectComplementOf arg0) {
		// TODO Auto-generated method stub
		printlnIndented("1");
	}

	public void visit(OWLObjectSomeValuesFrom arg0) {
		// TODO Auto-generated method stub
		indent();
		OWLObjectPropertyExpression ope = arg0.getProperty();
		String propName = ope.asOWLObjectProperty().getNamedProperty().getIRI().getFragment();
		
		boolean charObjectProp = propName.equals("characterizedBy");
		boolean hasQObjectProp = propName.equals("hasQuality");

		printIndented(propName);
		printlnIndented(" some");

		OWLClassExpression filler = arg0.getFiller();
		if(filler.isClassExpressionLiteral()){
			printlnIndented(tab + filler.asOWLClass().getIRI().getFragment());
			System.err.println("Should do something \"OWLObjectSomeValuesFrom\"");
		}else{
			charObjProp=charObjectProp;
			hasQObjProp = hasQObjectProp;
			arg0.getFiller().accept(this);
		}


		unindent();

	}

	public void visit(OWLObjectAllValuesFrom arg0) {
		// TODO Auto-generated method stub
		printlnIndented("1");
	}

	public void visit(OWLObjectHasValue arg0) {
		// TODO Auto-generated method stub
		printlnIndented("2");
	}

	public void visit(OWLObjectMinCardinality arg0) {
		// TODO Auto-generated method stub
		printlnIndented("3");
	}

	public void visit(OWLObjectExactCardinality arg0) {
		// TODO Auto-generated method stub
		printlnIndented("4");
	}

	public void visit(OWLObjectMaxCardinality arg0) {
		// TODO Auto-generated method stub
		printlnIndented("5");
	}

	public void visit(OWLObjectHasSelf arg0) {
		// TODO Auto-generated method stub
		printlnIndented("6");
	}

	public void visit(OWLObjectOneOf arg0) {
		// TODO Auto-generated method stub
		printlnIndented("7");
	}

	public void visit(OWLDataSomeValuesFrom arg0) {
		// TODO Auto-generated method stub
		//printlnIndented("8");
		indent();
		OWLDataPropertyExpression dpe = arg0.getProperty();

		String propName = dpe.asOWLDataProperty().getIRI().getFragment();
		ContextData cd = new ContextData(dpe.asOWLDataProperty().getIRI());
		printIndented(propName);
		printlnIndented(" someVal");
		indent();

		OWLDataRange range = arg0.getFiller();
		if(range.getDataRangeType() == DataRangeType.DATATYPE_RESTRICTION){
			OWLDatatypeRestriction restriction = (OWLDatatypeRestriction) range;

			cd.setMax(getMax(restriction));
			cd.setMin(getMin(restriction));
			current.addValue(cd);
			Set<OWLFacetRestriction> rset = restriction.getFacetRestrictions();
			for(OWLFacetRestriction ofr: rset){
				printIndented(ofr.getFacet().getSymbolicForm()+" ");
				printIndented(ofr.getFacetValue().getLiteral() + " ");
			}
		}else{
			System.err.println("not implemented: DataRangetype!=DataRangeType.DATATYPE_RESTRICTION");
		}
		
		printlnIndented("");


		unindent();
		unindent();
	}

	public void visit(OWLDataAllValuesFrom arg0) {
		// TODO Auto-generated method stub
		printlnIndented("9");
	}

	public void visit(OWLDataHasValue arg0) {
		// TODO Auto-generated method stub

		indent();
		OWLDataPropertyExpression dpe = arg0.getProperty();

		String propName = dpe.asOWLDataProperty().getIRI().getFragment();
		ContextData cd = new ContextData(dpe.asOWLDataProperty().getIRI());
		printIndented(propName);
		printlnIndented(" hasVal");
		indent();

		OWLLiteral value = arg0.getValue();
		printlnIndented(value.toString());
		if(value.isDouble()){
			cd.setValue(new DoubleContextDataValue(value.parseDouble()));
		}
		current.addValue(cd);
		unindent();
		unindent();
		printlnIndented("10");
	}


	private ContextDataValue getMin(OWLDatatypeRestriction res){
		if(res.getDatatype().isDouble()){
			Set<OWLFacetRestriction> rset = res.getFacetRestrictions();
			for(OWLFacetRestriction ofr: rset){
					if(ofr.getFacet()==OWLFacet.MIN_INCLUSIVE){
						return new DoubleContextDataValue(ofr.getFacetValue().parseDouble());
					}else if(ofr.getFacet()==OWLFacet.MIN_EXCLUSIVE){
						System.err.println("MinExclusive not implemented properly");
						return new DoubleContextDataValue(ofr.getFacetValue().parseDouble()+0.0001);
					}
				
			}
			return new DoubleContextDataValue(Double.NEGATIVE_INFINITY);
			
		}else{
			System.err.println("datatype:"+res.getDatatype().toString()+" not implemented");
		}
		
		return null;
	}

	private ContextDataValue getMax(OWLDatatypeRestriction res){
		if(res.getDatatype().isDouble()){
			Set<OWLFacetRestriction> rset = res.getFacetRestrictions();
			for(OWLFacetRestriction ofr: rset){
					if(ofr.getFacet()==OWLFacet.MAX_INCLUSIVE){
						return new DoubleContextDataValue(ofr.getFacetValue().parseDouble());
					}else if(ofr.getFacet()==OWLFacet.MAX_EXCLUSIVE){
						System.err.println("MaxExclusive not implemented properly");
						return new DoubleContextDataValue(ofr.getFacetValue().parseDouble()-0.0001);
					}
				
			}
			return new DoubleContextDataValue(Double.POSITIVE_INFINITY);
			
		}else{
			System.err.println("datatype:"+res.getDatatype().toString()+" not implemented");
		}
		
		return null;
	}





	public void visit(OWLDataMinCardinality arg0) {
		// TODO Auto-generated method stub
		printlnIndented("11");
	}

	public void visit(OWLDataExactCardinality arg0) {
		// TODO Auto-generated method stub
		printlnIndented("12");
	}

	public void visit(OWLDataMaxCardinality arg0) {
		// TODO Auto-generated method stub
		printlnIndented("13");
	}

}
