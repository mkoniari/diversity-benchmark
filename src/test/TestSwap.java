package test;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


import javax.swing.JFrame;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;

import com.benchmark.algorithm.separate.Swap;
import com.benchmark.data.DataCluster;
import com.benchmark.data.DataObject;
import com.benchmark.data.DataSet;
import com.benchmark.data.loader.CameraLoader;
import com.benchmark.evaluator.NormalizedRelevanceEvaluator;
import com.benchmark.evaluator.SRecallEvaluator;
import com.benchmark.generator.configuration.ConfigurationLoader;
import com.benchmark.generator.configuration.DatasetConfigLoader;
import com.benchmark.metrics.Distance;
import com.benchmark.utility.Constants;

public class TestSwap {
	private static DataSet ds;
	private static int distanceType;

	public static void main(String[] args) {
		int k = 10;
		// testSwap(k, 0.1);
		testSwap(k, 0.4);

		// Print dissimilarity matrix
		// System.out.println(Utilities.printMatrix(ds, Distance.EUCLIDE,
		// false));
		// System.out.println(Utilities.printMatrix(ds, Distance.EUCLIDE,
		// true));

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

		List<DataObject> list = new ArrayList<DataObject>();
		for (int i = 0; i < k; i++) {
			list.add(heap.poll());
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
					// System.out.println(o.getId());
					if (distanceType == Distance.HAMMING) {
						System.out.println(o.getLabel());
					}
					colors[count] = Color.RED;
					for (int ik = 0; ik < k; ik++) {
						if (o.getId() == list.get(ik).getId()) {
							colors[count] = Color.YELLOW;
							break;
						}
					}
				} else {
					colors[count] = Color.GREEN;
					// colors[count] = Utilities.getColor(i);
					for (int ik = 0; ik < k; ik++) {
						if (o.getId() == list.get(ik).getId()) {
							colors[count] = Color.BLUE;
							break;
						}
					}
				}
				points[count++] = new Coord3d(o.getCoordinates().get(0), o
						.getCoordinates().get(1), o.getRelevant());
			}
		}
		if (distanceType != Distance.HAMMING) {
			Scatter scatter = new Scatter(points, colors);
			scatter.setWidth(3);
			Chart chart = new Chart();
			chart.getAxeLayout().setMainColor(Color.WHITE);
			chart.getView().setBackgroundColor(Color.BLACK);
			chart.getScene().add(scatter);

			chart.getAxeLayout().setZAxeLabel("Relevance value");
			// ChartLauncher.openChart(chart);
			ChartLauncher.configureControllers(chart, "Jzy3d", true, false);
			chart.render();
			Component sth = (java.awt.Component) chart.getCanvas();

			JFrame frame = new JFrame();
			frame.add((java.awt.Component) chart.getCanvas());
			frame.setSize(600, 600);
			frame.setLocationRelativeTo(null);
			frame.setTitle("Swap");
			frame.setVisible(true);
		}
	}

	protected static DataSet dataSet1() {
		// ConfigurationLoader loader = new
		// DatasetConfigLoader("data/relevance/prop.xml", false,
		// "data/relevance/data");
		// ConfigurationLoader loader = new
		// DatasetConfigLoader("data/morecluster/c1/prop.xml", false,
		// "data/morecluster/c1/data");
		Constants.SEED = System.currentTimeMillis();
		// Constants.SEED = Constants.SEEDS[1];
		// ConfigurationLoader loader = new
		// DatasetConfigLoader("data/morecluster/c1/prop.xml", true,
		// "data/morecluster/c1/data2");
		// ConfigurationLoader loader = new
		// DatasetConfigLoader("data/overlap/prop.xml", true,
		// "data/overlap/data");
		// ConfigurationLoader loader = new DatasetConfigLoader(
		// "data/distance/d0.02/prop.xml", true,
		// "data/distance/d0.02/data2");
//		ConfigurationLoader loader = new DatasetConfigLoader(
//				"../code/data/numtopic/c6/prop.xml", true,
//				"../code/data/numtopic/c6/data");
		ConfigurationLoader loader = new DatasetConfigLoader(
				"prop_1.0.xml", true,
				"data");
		// ConfigurationLoader loader = new DatasetConfigLoader(
		// "data/mindist2/d20/prop.xml", true, "data/distance/d20/data");
		// ConfigurationLoader loader = new DatasetConfigLoader(
		// "data/numret/prop.xml", true, "data/numret/data");
		// String str = "0.251";
		// ConfigurationLoader loader = new DatasetConfigLoader(
		// "data/mindist3/d" + str +"/prop.xml", true, "data/mindist3/d"+ str
		// +"/data");
		// ConfigurationLoader loader = new
		// DatasetConfigLoader("data/powertail/d0.2/prop.xml", true,
		// "data/powertail/d0.2/data2");
		ds = loader.load(false);
		// ds.saveDataFile("data/overlap/data");
		// try {
		// // ds = new DataSet("data/1",false);
		// // ds = new DataSet("data/multicluster",true);
		// ds = new DataSet("data/test",true);
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		distanceType = Distance.EUCLIDE;
		return ds;
	}

	protected static DataSet realDS() {
		CameraLoader loader = new CameraLoader(true, false);
		// CameraLoader loader = new CameraLoader(false, true);
		ds = loader.load("data/real/cam/camsold.csv");
		distanceType = Distance.HAMMING;
		return ds;
	}

	private static void testSwap(int rsize, double ub) {
		ds = dataSet1();
		// ds = realDS();
		Swap swap = new Swap(rsize, ds, ub, distanceType);
		long s = System.currentTimeMillis();
		swap.run();
		long e = System.currentTimeMillis();
		SRecallEvaluator eval = new SRecallEvaluator(ds);
		eval.setK(rsize);
		System.out.println("Srecall " + eval.evaluate());
		NormalizedRelevanceEvaluator neval = new NormalizedRelevanceEvaluator(
				ds);
		System.out.println("Norm rel " + neval.evaluate());
		System.out.println(e - s);
		System.out.println("Diversification done.");
	}
}
