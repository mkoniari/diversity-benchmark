package DiversityBenchmark.models;

import java.beans.PropertyChangeSupport;
import java.util.Map;

public class SimulationParameter extends AbstractModel {

	private Integer numOfClusters;
	private Integer sizeOfClusters;
	private Double agWeight;
	private Double grassHopperDamping;
	private Double motleyTheta;
	private Double swapUpperBound;
	private Double msdLambda;
	private Double mmrLambda;
	private Integer index;
	private Double minObserverValue;
	private Double maxObserverValue;
	private Double stepObserverValue;
	private Map<String, String> list;

	public SimulationParameter() {
		init();
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
	}

	private void initMap() {
		// ConfigReader reader = new ConfigReader();
		// reader.readfile(Constant.SIMULATE_INIT_FILE);
		// list = reader.getConfig();
		// this.uniformSpammer = Integer.parseInt(list.get("uniformSpammer"));
		// this.randomSpammer = Integer.parseInt(list.get("randomSpammer"));
		// this.expert = Integer.parseInt(list.get("expert"));
		// this.normalWorker = Integer.parseInt(list.get("normalWorker"));
		// this.sloppyWorker = Integer.parseInt(list.get("sloppyWorker"));
		// this.question = Integer.parseInt(list.get("question"));
		// this.category = Integer.parseInt(list.get("category"));
		// this.trapQuestion = Integer.parseInt(list.get("trapQuestion"));
		// this.answerPerQuestion = Integer
		// .parseInt(list.get("answerPerQuestion"));
		// this.answerPerWorker = Integer.parseInt(list.get("answerPerWorker"));
		// this.minCommonQuestion = Integer
		// .parseInt(list.get("minCommonQuestion"));
		// this.index = Integer.parseInt(list.get("observer"));
		// this.minObserverValue =
		// Integer.parseInt(list.get("minObserverValue"));
		// this.maxObserverValue =
		// Integer.parseInt(list.get("maxObserverValue"));
		// this.stepObserverValue = Integer.parseInt(list.get("step"));

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

}
