package DiversityBenchmark.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import DiversityBenchmark.models.Metric;
import DiversityBenchmark.models.MetricModel;
import DiversityBenchmark.utils.Constant;
import DiversityBenchmark.utils.Constant.FACTOR;
import DiversityBenchmark.utils.Constant.METRIC;
import DiversityBenchmark.utils.ContextUtil;
import DiversityBenchmark.utils.EventConstants;

public class EvaluateHandler {

	@Inject
	IEventBroker eval_start;

	private Set<String> existingPart = new HashSet<String>();
	private Set<String> newPart = new HashSet<String>();

	private Set<String> existing3DPart = new HashSet<String>();
	private Set<String> new3DPart = new HashSet<String>();
	EPartService partService;
	IEclipseContext context;

	// @Execute
	// public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
	// // sent signal to simulate model
	// eval_start.send(EventConstants.FUNCTION_SIMULATING_START, "start");
	//
	//
	// }
	@SuppressWarnings("restriction")
	@Execute
	public void execute(EPartService partService, MApplication application,
			EModelService modelService, IEclipseContext context) {
		this.partService = partService;
		this.context = context;
		// Request update metric to context
		eval_start.send(EventConstants.METRIC_OBSERVER_UPDATE_UPDATED, "start");
		String partStackURI = "diversitybenchmark.partstack.chart";
		String chartPartURI = "diversitybenchmark.part.chartview.";
		String chartPartClass = "bundleclass://DiversityBenchmark/DiversityBenchmark.parts.ChartPart";
		genChartPart(partService, application, modelService, context,
				partStackURI, chartPartURI, chartPartClass);

		// String part3DStackURI = "diversitybenchmark.partstack.chart3D";
		// String chart3DPartURI = "diversitybenchmark.part.chart3Dview.";
		// String chart3DPartClass =
		// "bundleclass://DiversityBenchmark/DiversityBenchmark.parts.Chart3DPart";
		// genChart3DPart(partService, application, modelService, context,
		// part3DStackURI, chart3DPartURI, chart3DPartClass);

		// Add your Java objects to the context
		// context.set(MyDataObject.class.getName(), data);
		// context.set(MoreStuff.class, moreData);

		eval_start.send(EventConstants.FUNCTION_SIMULATING_START, "start");

		// Object expRes = context.get(Constant.EXP_RES);

		MPart configchart3DPart = partService
				.findPart("diversitybenchmark.part.chart3dcontrol");

		// hide the part
		// partService.hidePart(configchart3DPart);

		// required if initial not visible
		configchart3DPart.setVisible(true);

		// set context to this part
		configchart3DPart.setContext(context);

		// show the part
		partService.showPart(configchart3DPart, PartState.VISIBLE);

	}

	// @SuppressWarnings("restriction")
	// @Inject
	// @Optional
	// void evaluateHandler(
	// @UIEventTopic(EventConstants.FUNCTION_SIMULATING_FINISHED) String s) {
	// Object expRes = context.get(Constant.EXP_RES);
	//
	// MPart configchart3DPart = partService
	// .findPart("diversitybenchmark.part.chart3dcontrol");
	//
	// // hide the part
	// // partService.hidePart(configchart3DPart);
	//
	// // required if initial not visible
	// configchart3DPart.setVisible(true);
	//
	// // set context to this part
	// configchart3DPart.setContext(context);
	//
	// // show the part
	// partService.showPart(configchart3DPart, PartState.VISIBLE);
	// }

	private void genChart3DPart(EPartService partService,
			MApplication application, EModelService modelService,
			IEclipseContext context, String partStackURI, String chartPartURI,
			String chartPartClass) {
		List<MPartStack> stacks = modelService.findElements(application,
				partStackURI, MPartStack.class, null);

		Object o = context.get(Constant.METRIC);
		assert o != null;

		Object o1 = context.get(Constant.FACTOR);
		assert o1 != null;
		FACTOR factor = null;
		if (o1 instanceof FACTOR) {
			factor = (FACTOR) o1;
		}

		assert factor != null;

		if (o instanceof MetricModel) {
			new3DPart.clear();
			MetricModel metrics = (MetricModel) o;
			// System.out.println(metrics.toString());
			for (Metric metric : metrics.getMetrics()) {
				String partID = genPartID(METRIC.valueOf(metric.getName()),
						factor.toString());
				if (!existing3DPart.contains(partID)) {
					MetricModel selectedMetric = new MetricModel();
					selectedMetric.getMetrics().clear();
					selectedMetric.getMetrics().add(metric);
					ContextUtil.updateContext(context,
							Constant.SELECTED_METRIC, selectedMetric);

					MPart part = MBasicFactory.INSTANCE.createPart();
					part.setElementId(chartPartURI + partID);
					part.setContributionURI(chartPartClass);
					part.setContext(context);
					part.setLabel(metric.getName() + " vs. " + factor);
					// part.setCloseable(true);
					stacks.get(0).getChildren().add(part);
					partService.showPart(part, PartState.ACTIVATE);
				}
				new3DPart.add(partID);
				existing3DPart.add(partID);
			}

		}

		ContextUtil.updateContext(context, Constant.EXISTING3DPART,
				existing3DPart);
		ContextUtil.updateContext(context, Constant.NEW3DPART, new3DPart);
	}

	private void genChartPart(EPartService partService,
			MApplication application, EModelService modelService,
			IEclipseContext context, String partStackURI, String chartPartURI,
			String chartPartClass) {
		List<MPartStack> stacks = modelService.findElements(application,
				partStackURI, MPartStack.class, null);

		Object o = context.get(Constant.METRIC);
		assert o != null;

		Object o1 = context.get(Constant.FACTOR);
		assert o1 != null;
		FACTOR factor = null;
		if (o1 instanceof FACTOR) {
			factor = (FACTOR) o1;
		}

		assert factor != null;

		if (o instanceof MetricModel) {
			newPart.clear();
			MetricModel metrics = (MetricModel) o;
			// System.out.println(metrics.toString());
			for (Metric metric : metrics.getMetrics()) {
				String partID = genPartID(METRIC.valueOf(metric.getName()),
						factor.toString());
				if (!existingPart.contains(partID)) {
					MetricModel selectedMetric = new MetricModel();
					selectedMetric.getMetrics().clear();
					selectedMetric.getMetrics().add(metric);
					ContextUtil.updateContext(context,
							Constant.SELECTED_METRIC, selectedMetric);

					MPart part = MBasicFactory.INSTANCE.createPart();
					part.setElementId(chartPartURI + partID);
					part.setContributionURI(chartPartClass);
					part.setContext(context);
					part.setLabel(metric.getName() + " vs. " + factor);
					// part.setCloseable(true);
					stacks.get(0).getChildren().add(part);
					partService.showPart(part, PartState.ACTIVATE);
				}
				newPart.add(partID);
				existingPart.add(partID);
			}

		}

		ContextUtil.updateContext(context, Constant.EXISTINGPART, existingPart);
		ContextUtil.updateContext(context, Constant.NEWPART, newPart);
	}

	public static String genPartID(METRIC metric, String observer) {
		switch (metric) {
		case NormalizedRelevance:
			return "relevance" + "_" + observer.toLowerCase();
		case SRecall:
			return "srecall" + "_" + observer.toLowerCase();
		case ComputationTime:
			return "time" + "_" + observer.toLowerCase();
		default:
			throw new IllegalArgumentException(String.valueOf(metric));
		}
	}

	// public static String genPartID(String curAlgorithm, String curFactor) {
	// return curAlgorithm.toLowerCase() + "_" + curFactor.toLowerCase();
	// }

}