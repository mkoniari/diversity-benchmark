package DiversityBenchmark.utils;

public class Constant {
	public static final String CONFIG_FOLDER = "./data";
	public static final String CONFIG_FILE = "./data/config.ini";
	public static final String ADVANCE_CONFIG_FILE = "./config.ini";
	public static final String SIMULATE_INIT_FILE = "./data/default_config.txt";
	public static final String ALGO_CONFIG_FILE = "./data/algo_config.ini";
	public static final String ANSWER_FILE = "data/answer";
	public static final String RESULT_FILE = "data/result.csv";
	public static final String RESULT_TEST_FILE = "data/result_test.csv";

	public static final String ANSWER_PER_QUESTION = "Answers per Question";
	public static final String ACCURACY = "Accuracy";
	public static final String COMPUTATION_TIME = "Computation Time";
	public static final String ACCURACY_VS_ANSWER_PER_QUESTION = "Accuracy vs. Answers per Question ";
	public static final String COMPLETETION_VS_ANSWER_PER_QUESTION = "Completion vs. Answers per Question ";

	public static final String METRIC = "metric";
	public static final String SELECTED_METRIC = "selected_metric";
	public static final String ALGORITHM_NAME = "algorithm";
	public static final String SELECTED_ALGORITHM = "selected_algorithm";
	public static final String EXISTINGPART = "existingPart";
	public static final String NEWPART = "newPart";
	public static final String EXISTING3DPART = "existing3DPart";
	public static final String NEW3DPART = "new3DPart";
	public static final String FACTOR = "observer";
	public static final String FACTOR_VALUE = "observer_value";
	public static final String FACTOR_VALUES = "observer_values";
	public static final String ADVANCED_PARA = "advancedPara";
	public static final String EXP_RES = "exp_res";

	public static final String SELECTALL = "Select All";

	public static enum ALGORITHM {
		AG, GrassHopper, Motley, Swap, MSD, MMR

	};

	public static enum DISTRIBUTION {
		Normal, Cosine, Powertail

	};

	public static enum METRIC {
		ComputationTime, SRecall, NormalizedRelevance
	};

	public static enum FACTOR {
		NumOfTopics, Relevance_Difference, NumOfResults, Topic_Dissimilarity;
	}

	// public static enum FACTOR {
	// NumOfTopics("No. Topics"), Relevance_Difference("Relevance difference"),
	// NumOfResults(
	// "No. Results"), Topic_Dissimilarity("Topic Dissimilarity");
	//
	// private String value;
	//
	// FACTOR(String value) {
	// this.value = value;
	// }
	//
	// public String getValue() {
	// return value;
	// }
	//
	// @Override
	// public String toString() {
	// return this.getValue();
	// }
	//
	// public static FACTOR getEnum(String value) {
	// if (value == null)
	// throw new IllegalArgumentException();
	// for (FACTOR v : values())
	// if (value.equalsIgnoreCase(v.getValue()))
	// return v;
	// throw new IllegalArgumentException();
	// }
	// };

	public static enum CENTGEN {
		Line, circle

	};

	public static enum CHART_XY {
		AnswerPerQuestion_Accurary, AnswerPerQuestion_CompletionTime, DEFAULT
	}

}
