package DiversityBenchmark.parts;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import DiversityBenchmark.models.Algorithm;
import DiversityBenchmark.models.AlgorithmModel;
import DiversityBenchmark.utils.Constant;
import DiversityBenchmark.utils.Constant.FACTOR;
import DiversityBenchmark.utils.ContextUtil;

/**
 * 
 * @author Diversity
 * UI for the 3D Chart config
 *
 */
public class Chart3DConfigPart extends AbstractPart {

	private FormToolkit toolkit;
	private Form form;
	private Composite char3DConfigSectionBody;
	private ComboViewer comboViewer;
	private Combo combo;
	private Label lblDataFactorValue;
	private Section sctnAlgorithm;
	private Composite algorithmComposite;
	private CheckboxTableViewer algorithmTableViewer;
	private AlgorithmModel selectedAlgorithm;
	private Button btnVisualize;

	@Inject
	EPartService partService;

	@Inject
	MApplication application;

	@Inject
	EModelService modelService;

	@Inject
	IEclipseContext context;

	@Inject
	IEventBroker visual_start;

	private Set<String> existing3DPart = new HashSet<String>();
	private Set<String> new3DPart = new HashSet<String>();

	@PostConstruct
	public void createComposite(Composite parent) {
		selectedAlgorithm = new AlgorithmModel();
		toolkit = new FormToolkit(parent.getDisplay());

		parent.setLayout(new GridLayout(1, false));
		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		addParameterFormPart(parent);

		// Start binding
		bindAlgoParamValues();

		algorithmTableViewer.setAllChecked(true);

		btnVisualize = new Button(form.getBody(), SWT.NONE);
		btnVisualize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				visualize();
			}
		});
		toolkit.adapt(btnVisualize, true, true);
		btnVisualize.setText("Visualize");
		new Label(form.getBody(), SWT.NONE);

	}

	protected void visualize() {
		String part3DStackURI = "diversitybenchmark.partstack.chart3D";
		String chart3DPartURI = "diversitybenchmark.part.chart3Dview.";
		String chart3DPartClass = "bundleclass://DiversityBenchmark/DiversityBenchmark.parts.Chart3DPart";
		genChart3DPart(part3DStackURI, chart3DPartURI, chart3DPartClass);

	}

	@SuppressWarnings("restriction")
	private void genChart3DPart(String part3dStackURI, String chart3dPartURI,
			String chart3dPartClass) {

		// visual_start.send(EventConstants.METRIC_OBSERVER_UPDATE_UPDATED,
		// "start");

		List<MPartStack> stacks = modelService.findElements(application,
				part3dStackURI, MPartStack.class, null);

		Object o = context.get(Constant.ALGORITHM_NAME);
		assert o != null;

		Object o1 = context.get(Constant.FACTOR);
		assert o1 != null;
		FACTOR factor = null;
		if (o1 instanceof FACTOR) {
			factor = (FACTOR) o1;
		}

		assert factor != null;
		String curFactorValue = combo.getText();
		String curFactor = factor.toString() + "_" + curFactorValue;

		if (o instanceof AlgorithmModel) {
			new3DPart.clear();
			AlgorithmModel algorithms = (AlgorithmModel) o;
			// System.out.println(metrics.toString());
			for (Algorithm algorithm : algorithms.getAlgorithms()) {

				String partID = genPartID(algorithm.getName(), curFactor);
				if (!existing3DPart.contains(partID)) {
					AlgorithmModel selectedAlgorithm = new AlgorithmModel();
					selectedAlgorithm.getAlgorithms().clear();
					selectedAlgorithm.getAlgorithms().add(algorithm);
					ContextUtil.updateContext(context,
							Constant.SELECTED_ALGORITHM, selectedAlgorithm);
					ContextUtil.updateContext(context, Constant.FACTOR_VALUE,
							combo.getText());

					MPart part = MBasicFactory.INSTANCE.createPart();
					part.setElementId(chart3dPartURI + partID);
					part.setContributionURI(chart3dPartClass);
					part.setContext(context);
					part.setLabel(algorithm.getName() + " vs. " + factor);
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

	private void bindAlgoParamValues() {
		DataBindingContext ctx = new DataBindingContext();
		IObservableSet modelSet = BeansObservables.observeSet(
				Realm.getDefault(), selectedAlgorithm, "algorithms");

		IObservableSet widgetSet = ViewersObservables.observeCheckedElements(
				algorithmTableViewer, Algorithm.class);

		ctx.bindSet(widgetSet, modelSet);

	}

	private void addParameterFormPart(Composite parent) {
		form = toolkit.createForm(parent);
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 2;
		form.getBody().setLayout(layout);
		layout.numColumns = 2;
		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		Section chart3DConfigSection = toolkit.createSection(form.getBody(),
				Section.CLIENT_INDENT | Section.TITLE_BAR);
		chart3DConfigSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		FACTOR factor = (FACTOR) context.get(Constant.FACTOR);
		chart3DConfigSection.setText(factor.toString());
		customizeSection(chart3DConfigSection);

		char3DConfigSectionBody = new Composite(chart3DConfigSection, SWT.NONE);
		toolkit.adapt(char3DConfigSectionBody);
		toolkit.paintBordersFor(char3DConfigSectionBody);
		chart3DConfigSection.setClient(char3DConfigSectionBody);
		char3DConfigSectionBody.setLayout(new GridLayout(2, false));
		createCharr3DForm(char3DConfigSectionBody);
		new Label(char3DConfigSectionBody, SWT.NONE);
		new Label(char3DConfigSectionBody, SWT.NONE);

		addAlgorithmSection(parent);

	}

	private void addAlgorithmSection(Composite parent) {
		sctnAlgorithm = toolkit.createSection(form.getBody(), Section.COMPACT
				| Section.TITLE_BAR);
		sctnAlgorithm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		toolkit.adapt(sctnAlgorithm);
		toolkit.paintBordersFor(sctnAlgorithm);
		sctnAlgorithm.setText("Selected Algorithms");
		customizeSection(sctnAlgorithm);

		algorithmComposite = new Composite(sctnAlgorithm, SWT.NONE);
		toolkit.adapt(algorithmComposite);
		toolkit.paintBordersFor(algorithmComposite);
		sctnAlgorithm.setClient(algorithmComposite);

		algorithmTableViewer = addCheckedTableViewer(toolkit,
				algorithmComposite);

		Algorithm selectAll = new Algorithm();
		selectAll.setName(Constant.SELECTALL);
		algorithmTableViewer.add(selectAll);
		selectedAlgorithm = (AlgorithmModel) context
				.get(Constant.ALGORITHM_NAME);
		for (Algorithm algo : selectedAlgorithm.getAlgorithms()) {
			algorithmTableViewer.add(algo);
		}

	}

	private void createCharr3DForm(Composite char3dConfigSectionBody2) {

		lblDataFactorValue = new Label(char3DConfigSectionBody, SWT.NONE);
		lblDataFactorValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		toolkit.adapt(lblDataFactorValue, true, true);
		lblDataFactorValue.setText("Data Factor Value");
		comboViewer = new ComboViewer(char3dConfigSectionBody2, SWT.READ_ONLY);
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		toolkit.paintBordersFor(combo);

		comboViewer.setContentProvider(new ArrayContentProvider()); // org.eclipse.jface.viewers.ArrayContentProvider()
		comboViewer.setLabelProvider(new LabelProvider()); // org.eclipse.jface.viewers.LabelProvider()

		List<String> factorValues = (List<String>) context
				.get(Constant.FACTOR_VALUES);
		if (factorValues != null) {
			String[] observerValues = factorValues.toArray(new String[0]);

			combo.setItems(observerValues);
			combo.setText(observerValues[0]);
			combo.setVisibleItemCount(observerValues.length);
			combo.addListener(SWT.Modify, new Listener() {

				private int index;

				@Override
				public void handleEvent(Event event) {
					index = combo.getSelectionIndex();
				}
			});
		}
	}

	private CheckboxTableViewer addCheckedTableViewer(FormToolkit toolkit,
			Composite composite) {
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);

		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer
				.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION
						| SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		Table table = checkboxTableViewer.getTable();
		toolkit.paintBordersFor(table);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);
		checkboxTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				boolean checked = event.getChecked();
				Object ele = event.getElement();
				if (Constant.SELECTALL.equalsIgnoreCase(ele.toString())) {
					if (checked) {
						checkboxTableViewer.setAllChecked(true);
					} else {
						checkboxTableViewer.setAllChecked(false);
					}

				}
			}
		});
		return checkboxTableViewer;
	}

	public static String genPartID(String curAlgorithm, String curFactor) {
		return curAlgorithm.toLowerCase() + "_" + curFactor.toLowerCase();
	}

}
