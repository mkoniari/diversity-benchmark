package DiversityBenchmark.parts;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import DiversityBenchmark.handlers.EvaluateHandler;
import DiversityBenchmark.models.Data;
import DiversityBenchmark.models.MetricModel;
import DiversityBenchmark.utils.Constant;
import DiversityBenchmark.utils.Constant.FACTOR;
import DiversityBenchmark.utils.Constant.METRIC;
import DiversityBenchmark.utils.EventConstants;

public class ChartPart {

	private List<Data> data;
	private Map<String, List<Data>> map;
	// CHART_XY chartType = CHART_XY.AnswerPerQuestion_Accurary;
	// Map<CHART_XY, XYSeriesCollection> chartInfo;

	@Inject
	IEclipseContext context;
	private JFreeChart chart;
	private DefaultCategoryDataset dataset;
	private Composite parent;
	private String xAxisTitle;
	private String yAxisTitle;
	private String chartTitle;
	private String partID;

	private METRIC curMetric;
	private FACTOR curFactor;

	@PostConstruct
	public void createComposite(Composite parent) {
		this.parent = parent;
		initChartInfo();
		createTestData();

		chart = createChart(createDataSet(data));

		ChartComposite chartComposite = new ChartComposite(parent, SWT.NONE,
				chart, true);
		chartComposite.setDisplayToolTips(true);
		chartComposite.setHorizontalAxisTrace(false);
		chartComposite.setVerticalAxisTrace(false);

	}

	private void initChartInfo() {
		Object metrics = context.get(Constant.SELECTED_METRIC);
		if (metrics != null && metrics instanceof MetricModel) {
			MetricModel tmp = (MetricModel) metrics;

			assert tmp.getMetrics().size() != 1;
			yAxisTitle = tmp.toString();
			curMetric = METRIC.valueOf(yAxisTitle);

			if (curMetric == METRIC.ComputationTime) {
				yAxisTitle = yAxisTitle + " (ms)";
			}
		}

		Object factor = context.get(Constant.FACTOR);
		if (factor != null & factor instanceof FACTOR) {
			curFactor = (FACTOR) factor;
			xAxisTitle = curFactor.toString();
		}

		chartTitle = "The effect of " + curFactor + " on "
				+ String.valueOf(curMetric);
		partID = EvaluateHandler.genPartID(curMetric, curFactor.toString());

		dataset = new DefaultCategoryDataset();
	}

	private void createTestData() {
		// CSVReader reader = new CSVReader();
		// reader.readfile(Constant.RESULT_TEST_FILE);
		// this.data = reader.getContent();

		// dataset.setValue(6, "Profit1", "Jane");
		// dataset.setValue(3, "Profit2", "Jane");
		// dataset.setValue(7, "Profit1", "Tom");
		// dataset.setValue(10, "Profit2", "Tom");
		// dataset.setValue(8, "Profit1", "Jill");
		// dataset.setValue(8, "Profit2", "Jill");
		// dataset.setValue(5, "Profit1", "John");
		// dataset.setValue(6, "Profit2", "John");
		// dataset.setValue(12, "Profit1", "Fred");
		// dataset.setValue(5, "Profit2", "Fred");

	}

	// private JFreeChart createChart(List<Data> data, CHART_XY chartType) {
	// // if (data == null) {
	// // return createChart(createDataSet(data, CHART_XY.DEFAULT),
	// // CHART_XY.DEFAULT);
	// // }
	// // return createChart(createDataSet(data, chartType), chartType);
	// }

	private JFreeChart createChart(CategoryDataset dataset) {

		// switch (chartType) {
		// case AnswerPerQuestion_Accurary:
		// xAxisTitle = Constant.ANSWER_PER_QUESTION;
		// yAxisTitle = Constant.ACCURACY;
		// chartTitle = Constant.ACCURACY_VS_ANSWER_PER_QUESTION;
		// break;
		//
		// case AnswerPerQuestion_CompletionTime:
		// xAxisTitle = Constant.ANSWER_PER_QUESTION;
		// yAxisTitle = Constant.COMPLETETION_TIME + " (ms)";
		// chartTitle = Constant.COMPLETETION_VS_ANSWER_PER_QUESTION;
		// break;
		// default:
		// break;
		// }
		JFreeChart chart = ChartFactory.createBarChart(chartTitle, xAxisTitle, // x
																				// axis
																				// label
				yAxisTitle, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);
		chart.setBackgroundPaint(Color.white);

		chart.getTitle().setPaint(Color.blue); // Adjust the colour of the title
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray); // Modify the plot background
		// plot.setDomainGridlinePaint(Color.white);
		// plot.setRangeGridlinePaint(Color.white);
		// plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		// plot.setDomainCrosshairVisible(true);
		// plot.setRangeCrosshairVisible(true);

		// XYItemRenderer r = plot.getRenderer();
		// if (r instanceof XYLineAndShapeRenderer) {
		// XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
		// renderer.setBaseShapesVisible(true);
		// renderer.setBaseShapesFilled(true);
		// }

		// change the auto tick unit selection to integer units only...
		// NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}

	private CategoryDataset createDataSet(List<Data> data) {
		if (data == null) {
			return dataset;
		} else {
			dataset.clear();
		}
		map = new HashMap<String, List<Data>>();
		for (Data d : data) {
			// if (!map.containsKey(d.getAlgorithm())) {
			// List<Data> tmp = new ArrayList<Data>();
			// tmp.add(d);
			// map.put(d.getAlgorithm(), tmp);
			// } else {
			// map.get(d.getAlgorithm()).add(d);
			// }
		}

		// switch (chartType) {
		// case AnswerPerQuestion_CompletionTime:
		// for (String algo : map.keySet()) {
		// List<Data> value = map.get(algo);
		// XYSeries series = new XYSeries(algo);
		// for (Data d : value) {
		// series.add(Double.valueOf(d.getObserver()),
		// Double.valueOf(d.getCompletionTime()));
		// }
		// dataset.addSeries(series);
		// }
		// break;

		// case AnswerPerQuestion_Accurary:
		// for (String algo : map.keySet()) {
		// List<Data> value = map.get(algo);
		// XYSeries series = new XYSeries(algo);
		// for (Data d : value) {
		// series.add(Double.valueOf(d.getObserver()),
		// Double.valueOf(d.getAccuracy()));
		// }
		// dataset.addSeries(series);
		// }
		// break;
		// }
		switch (curMetric) {
		case NormalizedRelevance:
			for (Data d : data) {
				switch (curFactor) {
				case NumOfResults:
					dataset.setValue(d.getNormalizedRelevance(),
							d.getAlgorithm(), d.getNumOfResults());
					break;
				case NumOfSubtopics:
					dataset.setValue(d.getNormalizedRelevance(),
							d.getAlgorithm(), d.getNumberOfSubtopics());
					break;
				case Relevance_Difference:
					dataset.setValue(d.getNormalizedRelevance(),
							d.getAlgorithm(), d.getRelevanceDifference());
					break;
				case Subtopic_Dissimilarity:
					dataset.setValue(d.getNormalizedRelevance(),
							d.getAlgorithm(), d.getSubtopicDissimilarity());
					break;
				default:
					break;
				}
			}
			// for (String algo : map.keySet()) {
			// List<Data> value = map.get(algo);
			// XYSeries series = new XYSeries(algo);
			// for (Data d : value) {
			// series.add(Double.valueOf(d.getObserver()),
			// Double.valueOf(d.getWorkerEstimation()));
			// }
			// dataset.addSeries(series);
			// }
			break;
		case SRecall:
			for (Data d : data) {
				switch (curFactor) {
				case NumOfResults:
					dataset.setValue(d.getSrecall(), d.getAlgorithm(),
							d.getNumOfResults());
					break;
				case NumOfSubtopics:
					dataset.setValue(d.getSrecall(), d.getAlgorithm(),
							d.getNumberOfSubtopics());
					break;
				case Relevance_Difference:
					dataset.setValue(d.getSrecall(), d.getAlgorithm(),
							d.getRelevanceDifference());
					break;
				case Subtopic_Dissimilarity:
					dataset.setValue(d.getSrecall(), d.getAlgorithm(),
							d.getSubtopicDissimilarity());
					break;
				default:
					break;
				}
			}
			break;
		case ComputationTime:
			for (Data d : data) {
				switch (curFactor) {
				case NumOfResults:
					dataset.setValue(d.getTime(), d.getAlgorithm(),
							d.getNumOfResults());
					break;
				case NumOfSubtopics:
					dataset.setValue(d.getTime(), d.getAlgorithm(),
							d.getNumberOfSubtopics());
					break;
				case Relevance_Difference:
					dataset.setValue(d.getTime(), d.getAlgorithm(),
							d.getRelevanceDifference());
					break;
				case Subtopic_Dissimilarity:
					dataset.setValue(d.getTime(), d.getAlgorithm(),
							d.getSubtopicDissimilarity());
					break;
				default:
					break;
				}
			}
			break;
		default:
			break;
		}

		return dataset;
	}

	@Focus
	public void setFocus() {
	}

	@SuppressWarnings("restriction")
	@Inject
	@Optional
	void updateHandler(
			@UIEventTopic(EventConstants.RESULT_UPDATE_UPDATED) List<Data> data) {
		Object tmp = context.get(Constant.NEWPART);
		assert tmp != null;
		if (tmp instanceof Set<?>) {
			Set<String> newparts = (Set<String>) tmp;
			if (newparts.contains(partID)) {
				this.data = data;
				createDataSet(data);
			}
		}
	}

	//
	// @Inject
	// @Optional
	// void updateHandler(
	// @UIEventTopic(EventConstants.OBSERVER_UPDATE_UPDATED) String observer) {
	// Object metrics = context.get(Constant.METRIC);
	// if (metrics != null && metrics instanceof MetricModel) {
	//
	// }
	// }

	@PreDestroy
	private void dispose() {

	}

}
