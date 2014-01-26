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
import DiversityBenchmark.utils.Constant.METRIC;
import DiversityBenchmark.utils.ContextUtil;
import DiversityBenchmark.utils.EventConstants;

public class EvaluateHandler {

	@Inject
	IEventBroker eval_start;

	private Set<String> existingPart = new HashSet<String>();
	private Set<String> newPart = new HashSet<String>();

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

		// Request update metric to context
		eval_start.send(EventConstants.METRIC_OBSERVER_UPDATE_UPDATED, "start");

		List<MPartStack> stacks = modelService.findElements(application,
				"diversitybenchmark.partstack.chart", MPartStack.class, null);

		Object o = context.get(Constant.METRIC);
		assert o != null;

		Object o1 = context.get(Constant.OBSERVER);
		assert o1 != null;
		String observer = null;
		if (o1 instanceof String) {
			observer = (String) o1;
		}

		assert observer != null;

		if (o instanceof MetricModel) {
			newPart.clear();
			MetricModel metrics = (MetricModel) o;
			// System.out.println(metrics.toString());
			for (Metric metric : metrics.getMetrics()) {
				String partID = genPartID(METRIC.valueOf(metric.getName()),
						observer);
				if (!existingPart.contains(partID)) {
					MetricModel selectedMetric = new MetricModel();
					selectedMetric.getMetrics().clear();
					selectedMetric.getMetrics().add(metric);
					context.modify(Constant.METRIC, selectedMetric);

					MPart part = MBasicFactory.INSTANCE.createPart();
					part.setElementId("diversitybenchmark.part.chartview."
							+ partID);
					part.setContributionURI("bundleclass://DiversityBenchmark/DiversityBenchmark.parts.ChartPart");
					part.setContext(context);
					part.setLabel(metric.getName() + " vs. " + observer);
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

		// Add your Java objects to the context
		// context.set(MyDataObject.class.getName(), data);
		// context.set(MoreStuff.class, moreData);

		eval_start.send(EventConstants.FUNCTION_SIMULATING_START, "start");
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