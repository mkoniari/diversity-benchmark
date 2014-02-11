package DiversityBenchmark.models;

public class Data extends AbstractModel {
	private String evalID;
	private String numberOfSubtopics;
	private String relevanceDifference;
	private String numOfResults;
	private String subtopicDissimilarity;
	private String algorithm;
	private Long time;
	private Double srecall;
	private Double normalizedRelevance;

	private void Data() {
		// TODO Auto-generated method stub

	}

	public Data(String evalID, String algorithm, Double normalizedRelevance,
			String numberOfSubtopics, String numOfResults,
			String subtopicDissimilarity, Long time, Double srecall,
			String relevanceDifference) {
		this.evalID = evalID;
		this.algorithm = algorithm;
		this.normalizedRelevance = normalizedRelevance;
		this.numberOfSubtopics = numberOfSubtopics;
		this.numOfResults = numOfResults;
		this.subtopicDissimilarity = subtopicDissimilarity;
		this.time = time;
		this.srecall = srecall;
		this.relevanceDifference = relevanceDifference;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		propertyChangeSupport.firePropertyChange("algorithm", this.algorithm,
				this.algorithm = algorithm);
	}

	public String getEvalID() {
		return evalID;
	}

	public void setEvalID(String evalID) {
		propertyChangeSupport.firePropertyChange("evalID", this.evalID,
				this.evalID = evalID);
	}

	public String getNumberOfSubtopics() {
		return numberOfSubtopics;
	}

	public void setNumberOfSubtopics(String numberOfSubtopics) {
		propertyChangeSupport.firePropertyChange("numberOfSubtopics",
				this.numberOfSubtopics,
				this.numberOfSubtopics = numberOfSubtopics);
	}

	public String getRelevanceDifference() {
		return relevanceDifference;
	}

	public void setRelevanceDifference(String relevanceDifference) {
		propertyChangeSupport.firePropertyChange("relevanceDifference",
				this.relevanceDifference,
				this.relevanceDifference = relevanceDifference);
	}

	public String getNumOfResults() {
		return numOfResults;
	}

	public void setNumOfResults(String numOfResults) {
		propertyChangeSupport.firePropertyChange("numOfResults",
				this.numOfResults, this.numOfResults = numOfResults);
	}

	public String getSubtopicDissimilarity() {
		return subtopicDissimilarity;
	}

	public void setSubtopicDissimilarity(String subtopicDissimilarity) {
		propertyChangeSupport.firePropertyChange("subtopicDissimilarity",
				this.subtopicDissimilarity,
				this.subtopicDissimilarity = subtopicDissimilarity);
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		propertyChangeSupport.firePropertyChange("time", this.time,
				this.time = time);
	}

	public Double getSrecall() {
		return srecall;
	}

	public void setSrecall(Double srecall) {
		propertyChangeSupport.firePropertyChange("srecall", this.srecall,
				this.srecall = srecall);
	}

	public Double getNormalizedRelevance() {
		return normalizedRelevance;
	}

	public void setNormalizedRelevance(Double normalizedRelevance) {
		propertyChangeSupport.firePropertyChange("normalizedRelevance",
				this.normalizedRelevance,
				this.normalizedRelevance = normalizedRelevance);
	}

	// @Override
	// public String toString() {
	// return evalID + "\t" + answerPerQuestion + "\t" + algorithm + "\t"
	// + workerEstimation + "\t" + accuracy + "\t" + duration + "\t"
	// + observer;
	// }

}
