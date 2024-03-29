package DiversityBenchmark.models;

import java.beans.PropertyChangeSupport;
import java.util.Map;

/**
 * 
 * @author Diversity
 * Contains the parameter for the simulation
 *
 */
public class SimulationParameter extends AbstractModel {

	private Integer numOfClusters;
	private Integer sizeOfClusters;
	private Integer dimensionality;
	private Double agWeight;
	private Double grassHopperDamping;
	private Double motleyTheta;
	private Double swapUpperBound;
	private Double msdLambda;
	private Double mmrLambda;
	private Integer index;
	private String distribution;
	private Double minObserverValue;
	private Double maxObserverValue;
	private Double stepObserverValue;
	private Map<String, String> list;

	public SimulationParameter() {
		// init();
		// initMap();
	}

	private void init() {
		this.numOfClusters = new Integer(5);
		this.sizeOfClusters = new Integer(500);
		this.grassHopperDamping = new Double(0.85);
		this.agWeight = new Double(0.8);
		this.motleyTheta = new Double(0.001);
		this.swapUpperBound = new Double(0.001);
		this.msdLambda = new Double(0.4);
		this.mmrLambda = new Double(0.4);
		this.dimensionality = new Integer(2);
	}

	private void initMap() {

	}

	public Integer getNumOfClusters() {
		return numOfClusters;
	}

	public void setNumOfClusters(Integer numOfClusters) {
		propertyChangeSupport.firePropertyChange("numOfClusters",
				this.numOfClusters, this.numOfClusters = numOfClusters);
	}

	public Integer getSizeOfClusters() {
		return sizeOfClusters;
	}

	public void setSizeOfClusters(Integer sizeOfClusters) {
		propertyChangeSupport.firePropertyChange("sizeOfClusters",
				this.sizeOfClusters, this.sizeOfClusters = sizeOfClusters);
	}

	public Double getAgWeight() {
		return agWeight;
	}

	public void setAgWeight(Double agWeight) {
		propertyChangeSupport.firePropertyChange("agWeight", this.agWeight,
				this.agWeight = agWeight);
	}

	public Double getGrassHopperDamping() {
		return grassHopperDamping;
	}

	public void setGrassHopperDamping(Double grassHopperDamping) {
		propertyChangeSupport.firePropertyChange("grassHopperDamping",
				this.grassHopperDamping,
				this.grassHopperDamping = grassHopperDamping);
	}

	public Double getMotleyTheta() {
		return motleyTheta;
	}

	public void setMotleyTheta(Double motleyTheta) {
		propertyChangeSupport.firePropertyChange("motleyTheta",
				this.motleyTheta, this.motleyTheta = motleyTheta);
	}

	public Double getSwapUpperBound() {
		return swapUpperBound;
	}

	public void setSwapUpperBound(Double swapUpperBound) {
		propertyChangeSupport.firePropertyChange("swapUpperBound",
				this.swapUpperBound, this.swapUpperBound = swapUpperBound);
	}

	public Double getMsdLambda() {
		return msdLambda;
	}

	public void setMsdLambda(Double msdLambda) {
		propertyChangeSupport.firePropertyChange("msdLambda", this.msdLambda,
				this.msdLambda = msdLambda);
	}

	public Double getMmrLambda() {
		return mmrLambda;
	}

	public void setMmrLambda(Double mmrLambda) {
		propertyChangeSupport.firePropertyChange("mmrLambda", this.mmrLambda,
				this.mmrLambda = mmrLambda);
	}

	public Map<String, String> getList() {
		return list;
	}

	public void setList(Map<String, String> list) {
		this.list = list;
	}

	public void setMinObserverValue(Double minObserverValue) {
		propertyChangeSupport
				.firePropertyChange("minObserverValue", this.minObserverValue,
						this.minObserverValue = minObserverValue);
	}

	public void setMaxObserverValue(Double maxObserverValue) {
		propertyChangeSupport
				.firePropertyChange("maxObserverValue", this.maxObserverValue,
						this.maxObserverValue = maxObserverValue);
	}

	public void setStepObserverValue(Double stepObserverValue) {
		propertyChangeSupport.firePropertyChange("stepObserverValue",
				this.stepObserverValue,
				this.stepObserverValue = stepObserverValue);
	}

	public Double getMinObserverValue() {
		return minObserverValue;
	}

	public Double getMaxObserverValue() {
		return maxObserverValue;
	}

	public Double getStepObserverValue() {
		return stepObserverValue;
	}

	public PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getDimensionality() {
		return dimensionality;
	}

	public void setDimensionality(Integer dimensionality) {
		propertyChangeSupport.firePropertyChange("dimensionality",
				this.dimensionality, this.dimensionality = dimensionality);
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

}
