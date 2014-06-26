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

/**
 * 
 * @author Diversity
 * UI for the 2D charts
 *
 */
public class ChartPart {

	private List<Data> data;
	private Map<String, List<Data>> map;

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

	}

	private JFreeChart createChart(CategoryDataset dataset) {
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
		}

		switch (curMetric) {
		case NormalizedRelevance:
			for (Data d : data) {
				switch (curFactor) {
				case NumOfResults:
					dataset.setValue(d.getNormalizedRelevance(),
							d.getAlgorithm(), d.getNumOfResults());
					break;
				case NumOfTopics:
					dataset.setValue(d.getNormalizedRelevance(),
							d.getAlgorithm(), d.getNumberOfSubtopics());
					break;
				case Relevance_Difference:
					dataset.setValue(d.getNormalizedRelevance(),
							d.getAlgorithm(), d.getRelevanceDifference());
					break;
				case Topic_Dissimilarity:
					dataset.setValue(d.getNormalizedRelevance(),
							d.getAlgorithm(), d.getSubtopicDissimilarity());
					break;
				default:
					break;
				}
			}
			break;
		case SRecall:
			for (Data d : data) {
				switch (curFactor) {
				case NumOfResults:
					dataset.setValue(d.getSrecall(), d.getAlgorithm(),
							d.getNumOfResults());
					break;
				case NumOfTopics:
					dataset.setValue(d.getSrecall(), d.getAlgorithm(),
							d.getNumberOfSubtopics());
					break;
				case Relevance_Difference:
					dataset.setValue(d.getSrecall(), d.getAlgorithm(),
							d.getRelevanceDifference());
					break;
				case Topic_Dissimilarity:
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
				case NumOfTopics:
					dataset.setValue(d.getTime(), d.getAlgorithm(),
							d.getNumberOfSubtopics());
					break;
				case Relevance_Difference:
					dataset.setValue(d.getTime(), d.getAlgorithm(),
							d.getRelevanceDifference());
					break;
				case Topic_Dissimilarity:
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

	@PreDestroy
	private void dispose() {

	}

}
