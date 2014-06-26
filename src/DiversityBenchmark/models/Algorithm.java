package DiversityBenchmark.models;

/**
 * 
 * @author Diversity
 * Contains the info about the algorithm
 *
 */
public class Algorithm extends AbstractModel {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		propertyChangeSupport.firePropertyChange("name", this.name,
				this.name = name);
	}

	@Override
	public String toString() {
		return name;
	}

}
