package DiversityBenchmark.models;

import java.util.HashSet;
import java.util.Set;

import DiversityBenchmark.utils.Constant.ALGORITHM;

/**
 * 
 * @author Diversity
 * Contains information about the algorithms
 *
 */
public class AlgorithmModel extends AbstractModel {

	public AlgorithmModel() {
		ALGORITHM[] algoNames = ALGORITHM.values();
		for (int i = 0; i < algoNames.length; i++) {
			Algorithm algo = new Algorithm();
			algo.setName(String.valueOf(algoNames[i]));
			algorithms.add(algo);
		}
	}

	private Set<Algorithm> algorithms = new HashSet<Algorithm>();

	public Set<Algorithm> getAlgorithms() {
		return algorithms;
	}

	public void setAlgorithms(Set<Algorithm> algorithms) {
		propertyChangeSupport.firePropertyChange("algorithms", this.algorithms,
				this.algorithms = new HashSet(algorithms));
	}

	@Override
	public String toString() {
		String tmp = "";
		for (Algorithm o : algorithms) {
			tmp += ", " + o.getName();
		}
		if (!tmp.isEmpty()) {
			tmp = tmp.substring(1).trim();

		}
		return tmp;
	}
}
