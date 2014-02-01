package DiversityBenchmark.models;

public class AdvanceDatasetParameter extends AbstractModel {
	private Double maxClusterDistance;
	private Double minClusterDistance;
	private Double maxRadius;
	private Double minRadius;

	private Double maxCosine;
	private Double minCosine;
	private Double meanNormal;
	private Double stdNormal;
	private Double shapeTail;
	private Double maxTail;
	private Double minTail;
	private Integer centgenIndex;
	private Double centgenDistance;

	public AdvanceDatasetParameter() {
		// TODO init default value
		maxClusterDistance = new Double(1.0);
		minClusterDistance = new Double(0.1);
		maxRadius = new Double(0.25);
		minRadius = new Double(0.25);

		maxCosine = new Double(0);
		minCosine = new Double(0);
		meanNormal = new Double(0);
		stdNormal = new Double(0);
		shapeTail = new Double(0);
		maxTail = new Double(0);
		minTail = new Double(0);
		centgenDistance = new Double(0.5);
	}

	public Double getMaxClusterDistance() {
		return maxClusterDistance;
	}

	public void setMaxClusterDistance(Double maxClusterDistance) {
		propertyChangeSupport.firePropertyChange("maxClusterDistance",
				this.maxClusterDistance,
				this.maxClusterDistance = maxClusterDistance);
	}

	public Double getMinClusterDistance() {
		return minClusterDistance;
	}

	public void setMinClusterDistance(Double minClusterDistance) {
		propertyChangeSupport.firePropertyChange("minClusterDistance",
				this.minClusterDistance,
				this.minClusterDistance = minClusterDistance);
	}

	public Double getMaxRadius() {
		return maxRadius;
	}

	public void setMaxRadius(Double maxRadius) {
		propertyChangeSupport.firePropertyChange("maxRadius", this.maxRadius,
				this.maxRadius = maxRadius);
	}

	public Double getMinRadius() {
		return minRadius;
	}

	public void setMinRadius(Double minRadius) {
		propertyChangeSupport.firePropertyChange("minRadius", this.minRadius,
				this.minRadius = minRadius);
	}

	public Double getMaxCosine() {
		return maxCosine;
	}

	public void setMaxCosine(Double maxCosine) {
		propertyChangeSupport.firePropertyChange("maxCosine", this.maxCosine,
				this.maxCosine = maxCosine);
	}

	public Double getMinCosine() {
		return minCosine;
	}

	public void setMinCosine(Double minCosine) {
		propertyChangeSupport.firePropertyChange("minCosine", this.minCosine,
				this.minCosine = minCosine);
	}

	public Double getMeanNormal() {
		return meanNormal;
	}

	public void setMeanNormal(Double meanNormal) {
		propertyChangeSupport.firePropertyChange("meanNormal", this.meanNormal,
				this.meanNormal = meanNormal);
	}

	public Double getStdNormal() {
		return stdNormal;
	}

	public void setStdNormal(Double stdNormal) {
		propertyChangeSupport.firePropertyChange("stdNormal", this.stdNormal,
				this.stdNormal = stdNormal);
	}

	public Double getShapeTail() {
		return shapeTail;
	}

	public void setShapeTail(Double shapeTail) {
		propertyChangeSupport.firePropertyChange("shapeTail", this.shapeTail,
				this.shapeTail = shapeTail);
	}

	public Double getMaxTail() {
		return maxTail;
	}

	public void setMaxTail(Double maxTail) {
		propertyChangeSupport.firePropertyChange("maxTail", this.maxTail,
				this.maxTail = maxTail);
	}

	public Double getMinTail() {
		return minTail;
	}

	public void setMinTail(Double minTail) {
		propertyChangeSupport.firePropertyChange("minTail", this.minTail,
				this.minTail = minTail);
	}

	public Integer getCentgenIndex() {
		return centgenIndex;
	}

	public void setCentgenIndex(Integer centgenIndex) {
		propertyChangeSupport.firePropertyChange("centgenIndex",
				this.centgenIndex, this.centgenIndex = centgenIndex);
	}

	public Double getCentgenDistance() {
		return centgenDistance;
	}

	public void setCentgenDistance(Double centgenDistance) {
		propertyChangeSupport.firePropertyChange("centgenDistance",
				this.centgenDistance, this.centgenDistance = centgenDistance);
	}

}
