package DiversityBenchmark.parts;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
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
//import org.jzy3d.bridge.awt.Bridge;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.CameraMouseController;
//import org.jzy3d.chart.controllers.mouse.camera.CameraMouseController;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.View;

import DiversityBenchmark.models.AlgorithmModel;
import DiversityBenchmark.utils.Constant;
import DiversityBenchmark.utils.Constant.FACTOR;
import DiversityBenchmark.utils.EventConstants;

import com.benchmark.data.DataCluster;
import com.benchmark.data.DataObject;
import com.benchmark.data.DataSet;
import com.benchmark.exp.ExpNumSubtopic;

/**
 * 
 * @author Diversity
 * UI for the 3D chart
 *
 */
public class Chart3DPart {

	@Inject
	IEclipseContext context;
	private Composite parent;
	private String xAxisTitle;
	private String yAxisTitle;
	private String chartTitle;
	private String partID;

	private String curAlgorithm;
	private String curFactor;
	private String curFactorValue;
	private Scatter scatter;
	private Chart chart;

	@PostConstruct
	public void createComposite(Composite parent) {
		this.parent = parent;
		initChartInfo();
		try {
			chart = createChart();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bridge.adapt(parent, (java.awt.Component) chart.getCanvas());
		chart.addController(new CameraMouseController());
	}

	private Chart createChart() throws IOException {
		Map<String, ExpNumSubtopic> expRes = (Map<String, ExpNumSubtopic>) context
				.get(Constant.EXP_RES);
		DataSet ds = expRes.get(curFactorValue).algor2ds.get(curAlgorithm);
		scatter = generateScatter(ds);

		Chart chart = new Chart(Quality.Advanced, "awt");
		View view = chart.getView();
		IAxe axe = view.getAxe();
		IAxeLayout layout = axe.getLayout();
		layout.setMainColor(Color.WHITE);
		layout.setZAxeLabel("Relevance value");
		
		chart.getView().setBackgroundColor(Color.BLACK);

		chart.getScene().add(scatter);
		return chart;

	}

	public Scatter generateScatter(DataSet ds) {
		DataObject[] candidates = ds.getDataObjects();
		Comparator<DataObject> cmp = new Comparator<DataObject>() {
			@Override
			public int compare(DataObject o1, DataObject o2) {
				if (o2.getRelevant() > o1.getRelevant()) {
					return 1;
				}
				if (o2.getRelevant() < o1.getRelevant()) {
					return -1;
				}
				return 0;
			}
		};
		Queue<DataObject> heap = new PriorityQueue<DataObject>(
				candidates.length, cmp);
		for (int i = 0; i < candidates.length; ++i) {
			heap.add(candidates[i]);
		}

		Coord3d[] points = new Coord3d[ds.getSize()];
		Color[] colors = new Color[ds.getSize()];
		int count = 0;
		for (int i = 0; i < ds.getNumberOfClusters(); i++) {
			DataCluster cluster = ds.getClusters()[i];
			int size = cluster.getSize();
			for (int j = 0; j < size; ++j) {
				DataObject o = cluster.getElement(j);
				if (o.isSelected()) {
					colors[count] = Color.RED;
				} else {
					colors[count] = Color.GREEN;
				}
				points[count++] = new Coord3d(o.getCoordinates().get(0), o
						.getCoordinates().get(1), o.getRelevant());
			}
		}

		Scatter scatter = new Scatter(points, colors);
		scatter.setWidth(3);
		return scatter;
	}

	private Scatter testScatter() {
		int size = 500000;
		float x;
		float y;
		float z;
		float a;

		Coord3d[] points = new Coord3d[size];
		Color[] colors = new Color[size];

		for (int i = 0; i < size; i++) {
			x = (float) Math.random() - 0.5f;
			y = (float) Math.random() - 0.5f;
			z = (float) Math.random() - 0.5f;
			points[i] = new Coord3d(x, y, z);
			a = 0.25f;
			colors[i] = new Color(x, y, z, a);
		}

		Scatter scatter = new Scatter(points, colors);
		scatter.setWidth((float) 5.0);
		return scatter;

	}

	private void initChartInfo() {
		Object algorithms = context.get(Constant.SELECTED_ALGORITHM);
		if (algorithms != null && algorithms instanceof AlgorithmModel) {
			AlgorithmModel tmp = (AlgorithmModel) algorithms;

			assert tmp.getAlgorithms().size() != 1;
			yAxisTitle = tmp.toString();
			curAlgorithm = tmp.toString();
		}

		Object factor = context.get(Constant.FACTOR);
		if (factor != null & factor instanceof FACTOR) {
			curFactor = ((FACTOR) factor).toString();
			Object factorValue = context.get(Constant.FACTOR_VALUE);
			if (factorValue != null & factorValue instanceof String) {
				curFactorValue = (String) factorValue;

			}
			curFactor += "_" + curFactorValue;
			xAxisTitle = curFactor;
		}

		partID = Chart3DConfigPart.genPartID(curAlgorithm, curFactor);
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
			@UIEventTopic(EventConstants.RESULT_UPDATE_UPDATED) Scatter scatter1) {
		Object tmp = context.get(Constant.NEW3DPART);
		assert tmp != null;
		if (tmp instanceof Set<?>) {
			Set<String> newparts = (Set<String>) tmp;
			if (newparts.contains(partID)) {
				chart.getScene().getGraph().getAll().clear();
				chart.getScene().getGraph().add(scatter1);
				chart.render();
			}
		}
	}

	@PreDestroy
	private void dispose() {

	}

}
