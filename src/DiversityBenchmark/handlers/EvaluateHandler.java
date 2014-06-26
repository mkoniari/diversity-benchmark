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

		eval_start.send(EventConstants.FUNCTION_SIMULATING_START, "start");

		String chart3DPartURI = "diversitybenchmark.part.chart3dcontrol";
		String part3DStackURI = "diversitybenchmark.partstack.chart3Dcontrol";
		String chart3DConfigPartClass = "bundleclass://DiversityBenchmark/DiversityBenchmark.parts.Chart3DConfigPart";
		List<MPartStack> stacks = modelService.findElements(application,
				part3DStackURI, MPartStack.class, null);


		MPart part = MBasicFactory.INSTANCE.createPart();
		part.setElementId(chart3DPartURI);
		part.setContributionURI(chart3DConfigPartClass);
		part.setContext(context);
		part.setLabel("Chart 3D Config");
		stacks.get(0).getChildren().clear();
		stacks.get(0).getChildren().add(part);
		partService.showPart(part, PartState.ACTIVATE);
	}

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

}