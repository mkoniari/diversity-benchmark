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
	public static final String EXISTINGPART = "existingPart";
	public static final String NEWPART = "newPart";
	public static final String FACTOR = "observer";
	public static final String ADVANCED_PARA = "advancedPara";

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
		NumOfSubtopics, Relevance_Difference, NumOfResults, Subtopic_Dissimilarity
	};

	public static enum CENTGEN {
		Line, Circle

	};

	public static enum CHART_XY {
		AnswerPerQuestion_Accurary, AnswerPerQuestion_CompletionTime, DEFAULT
	}

}
