package org.mograrep.model;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.mograrep.utils.MapFunction;
import org.mograrep.utils.ReduceFunction;
import org.mograrep.utils.SimpleMapReduce;
import org.semanticweb.owlapi.model.IRI;


public class IDGManager {

	public static final int PRIORITY_DEFAULT = 0;

	private TreeSet<Pair<InstanceDataGenerator, Integer>> generators;


	public IDGManager(){
		generators = new TreeSet<Pair<InstanceDataGenerator,Integer>>(new Comparator<Pair<InstanceDataGenerator,Integer>>() {

			public int compare(Pair<InstanceDataGenerator, Integer> o1,
					Pair<InstanceDataGenerator, Integer> o2) {

				int p1 = o1.getValue1();
				int p2 = o2.getValue1();

				String n1 = o1.getValue0().getGeneratorName();
				String n2 = o2.getValue0().getGeneratorName();


				int cmpVal = n1.compareTo(n2);

				if(cmpVal==0){
					return p1 - p2;
				}else{
					return cmpVal;
				}
			}

		});
	}



	public boolean addGenerator(InstanceDataGenerator g, int priority){
		return generators.add(new Pair<InstanceDataGenerator, Integer>(g, priority));
	}

	public boolean removeGenerator(InstanceDataGenerator g, int priority){
		return generators.remove(new Pair<InstanceDataGenerator, Integer>(g, priority));
	}

	public boolean addGenerator(InstanceDataGenerator g){
		return addGenerator(g, IDGManager.PRIORITY_DEFAULT);
	}

	public InstanceDataGenerator getMostApplicableGenerator(final List<IRI> chain){
		SimpleMapReduce<Pair<InstanceDataGenerator, Integer>, Triplet<InstanceDataGenerator, Double, Integer>, InstanceDataGenerator> smr = new SimpleMapReduce<Pair<InstanceDataGenerator,Integer>, Triplet<InstanceDataGenerator, Double, Integer>, InstanceDataGenerator>();
		MapFunction<Pair<InstanceDataGenerator, Integer>, Triplet<InstanceDataGenerator, Double, Integer>> mapF = new MapFunction<Pair<InstanceDataGenerator,Integer>, Triplet<InstanceDataGenerator, Double, Integer>>() {

			public Triplet<InstanceDataGenerator, Double, Integer> f(
					Pair<InstanceDataGenerator, Integer> p) {
				Triplet<InstanceDataGenerator, Double, Integer> rValue = new Triplet<InstanceDataGenerator, Double, Integer>(p.getValue0(), p.getValue0().matchType(chain), p.getValue1());
				return rValue;
			}
		};
		ReduceFunction<Triplet<InstanceDataGenerator, Double, Integer>, InstanceDataGenerator> reduceF = new ReduceFunction<Triplet<InstanceDataGenerator, Double, Integer>, InstanceDataGenerator>() {

			public InstanceDataGenerator reduce(
					List<Triplet<InstanceDataGenerator, Double, Integer>> in) {
				if(in.size()==0){
					return null;
				}
				double tolerance = 0.001f;
				InstanceDataGenerator idg = in.get(0).getValue0();
				double maxF = in.get(0).getValue1();
				int priorityMax = in.get(0).getValue2();

				for(int i=1;i<in.size();i++){
					boolean swap=false;
					if((in.get(i).getValue1() < (maxF+tolerance)) && ((in.get(i).getValue1() > (maxF-tolerance)))){
						if(in.get(i).getValue2()>priorityMax){
							//same value and higher priority
							swap=true;
						}
					}else if(in.get(i).getValue1()> maxF){
						//higher value
						swap=true;
					}
					
					if(swap){
						idg = in.get(i).getValue0();
						maxF = in.get(i).getValue1();
						priorityMax = in.get(i).getValue2();
					}
				}
				return idg;
			}
		};
		
		return smr.mapReduce(mapF, reduceF, generators.iterator());
	}


}
