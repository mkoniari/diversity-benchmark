package DiversityBenchmark.parts;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.widgets.Composite;
import org.jzy3d.bridge.swt.Bridge;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.controllers.mouse.camera.CameraMouseController;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.Scatter;

import DiversityBenchmark.handlers.EvaluateHandler;
import DiversityBenchmark.models.MetricModel;
import DiversityBenchmark.utils.Constant;
import DiversityBenchmark.utils.Constant.FACTOR;
import DiversityBenchmark.utils.EventConstants;

public class Chart3DPart {

	@Inject
	IEclipseContext context;
	private Composite parent;
	private String xAxisTitle;
	private String yAxisTitle;
	private String chartTitle;
	private String partID;

	private String curAlgorithm;
	private String curFactorValue;
	private Scatter scatter;
	private Chart chart;

	@PostConstruct
	public void createComposite(Composite parent) {
		this.parent = parent;
		initChartInfo();
		// createTestData();
		chart = createChart();
		Bridge.adapt(parent, (java.awt.Component) chart.getCanvas());
		chart.addController(new CameraMouseController());

		// Bridge.adapt(parent, (Component) createChart().getCanvas());

	}

	private Chart createChart() {
		// TODO Auto-generated method stub
		Chart chart = new Chart();
		// Chart chart = new Chart(Quality.Advanced, "newt");
		// Chart chart = AWTChartComponentFactory.chart(Quality.Advanced,
		// "awt");
		chart.getAxeLayout().setMainColor(Color.WHITE);
		chart.getView().setBackgroundColor(Color.BLACK);
		// chart.getScene().add(scatter);

		chart.getAxeLayout().setZAxeLabel("Relevance value");
		// ChartLauncher.openChart(chart);
		ChartLauncher.configureControllers(chart, "Jzy3d", true, false);
		chart.render();

		// Create a chart

		return chart;

	}

	private void initChartInfo() {
		Object metrics = context.get(Constant.METRIC);
		if (metrics != null && metrics instanceof MetricModel) {
			MetricModel tmp = (MetricModel) metrics;

			assert tmp.getMetrics().size() != 1;
			yAxisTitle = tmp.toString();
			// curAlgorithm = METRIC.valueOf(yAxisTitle);
			curAlgorithm = tmp.toString();
		}

		Object factor = context.get(Constant.FACTOR);
		if (factor != null & factor instanceof FACTOR) {
			// curFactorValue = (FACTOR) factor;
			curFactorValue = ((FACTOR) factor).toString();
			xAxisTitle = curFactorValue;
		}

		partID = EvaluateHandler.genPartID(curAlgorithm,
				curFactorValue.toString());
		// partID = EvaluateHandler.genPartID(curMetric,
		// curFactorValue.toString());

		// dataset = new DefaultCategoryDataset();
		scatter = new Scatter();
	}

	private void createTestData() {

	}

	@Focus
	public void setFocus() {
	}

	@SuppressWarnings("restriction")
	@Inject
	@Optional
	void updateHandler(
			@UIEventTopic(EventConstants.RESULT_UPDATE_UPDATED) Scatter scatter) {
		Object tmp = context.get(Constant.NEW3DPART);
		assert tmp != null;
		if (tmp instanceof Set<?>) {
			Set<String> newparts = (Set<String>) tmp;
			if (newparts.contains(partID)) {
				this.scatter = scatter;
				chart.getScene().add(scatter);
				// this.data = data;
				// createDataSet(data);
			}
		}
	}

	@PreDestroy
	private void dispose() {

	}

}
