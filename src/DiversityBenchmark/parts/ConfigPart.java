package DiversityBenchmark.parts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import DiversityBenchmark.models.AdvanceDatasetParameter;
import DiversityBenchmark.models.Algorithm;
import DiversityBenchmark.models.AlgorithmModel;
import DiversityBenchmark.models.Metric;
import DiversityBenchmark.models.MetricModel;
import DiversityBenchmark.models.SimulationParameter;
import DiversityBenchmark.utils.Constant;
import DiversityBenchmark.utils.Constant.DISTRIBUTION;
import DiversityBenchmark.utils.Constant.FACTOR;
import DiversityBenchmark.utils.ContextUtil;
import DiversityBenchmark.utils.EventConstants;
import DiversityBenchmark.utils.NumericValidator;

public class ConfigPart extends AbstractPart {

	private FormToolkit toolkit;
	private Form form;

	Composite clusterSectionBody;

	private Link link;
	private Text txtMinvalue;
	private Text txtMaxvalue;
	private Text txtStep;
	private Label lblMin;
	private Label lblMax;
	private Label lblStep;
	private Combo distributionCombo;
	private ComboViewer distributionComboViewer;
	private Text txtNumOfClusters;
	private Text txtSizeOfClusters;
	private AdvanceDatasetParameter advanceConfigPara;

	final String[] observerValues = new String[] {
			FACTOR.NumOfSubtopics.toString(),
			FACTOR.Relevance_Difference.toString(),
			FACTOR.NumOfResults.toString(),
			FACTOR.Subtopic_Dissimilarity.toString() };

	final String[] distributionValues = new String[] {
			DISTRIBUTION.Normal.toString(), DISTRIBUTION.Cosine.toString(),
			DISTRIBUTION.Powertail.toString() };
	private Text txtAGWeight;
	private Text txtGrassHopperDamping;
	private Text txtMotleyTheta;
	private Text txtSwapUpperBound;
	private Text txtMSDLambda;
	private Text txtMMRLambda;
	private ComboViewer comboViewer;
	private Combo combo;
	protected int index;
	private Section sctnAlgorithm;
	private Composite algorithmComposite;
	private CheckboxTableViewer algorithmTableViewer;
	private AlgorithmModel selectedAlgorithm;
	private Section sctnMetric;
	private Composite metricComposite;
	private CheckboxTableViewer metricTableViewer;
	private MetricModel selectedMetric;
	private SimulationParameter simuPara;

	private DataBindingContext ctx;
	Map<Text, String> txtBindding = new HashMap<Text, String>();
	Map<Button, String> btnBindding = new HashMap<Button, String>();
	Map<String, Text> strBindding = new HashMap<String, Text>();
	Map<TableItem, String> tblBindding = new HashMap<TableItem, String>();

	private NumericValidator validator = null;
	private Composite composite_1;

	private String propxml = "prop.xml";
	private String algorxml = "algor.xml";

	@Inject
	IEventBroker chart;

	@Inject
	IEclipseContext context;

	@Inject
	IEventBroker eval_start;

	@Inject
	IEventBroker broker;
	private Label lblDimentionality;
	private Text textDimensionality;

	/**
	 * * This is a callback that will allow us to create the viewer and
	 * initialize * it.
	 */
	@PostConstruct
	public void createComposite(Composite parent) {
		simuPara = new SimulationParameter();
		advanceConfigPara = new AdvanceDatasetParameter();
		selectedAlgorithm = new AlgorithmModel();
		selectedMetric = new MetricModel();
		toolkit = new FormToolkit(parent.getDisplay());

		parent.setLayout(new GridLayout(1, false));
		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		addParameterFormPart(parent);

		// Start binding
		generateSimuParamBinding();
		validator = new NumericValidator();
		bindSimuParamValues(validator);

		bindAlgoParamValues();
		bindMetricParamValues();

		algorithmTableViewer.setAllChecked(true);
		metricTableViewer.setAllChecked(true);

		// addValidator();
		// updateWorkerChart();

	}

	private void bindAlgoParamValues() {
		DataBindingContext ctx = new DataBindingContext();
		IObservableSet modelSet = BeansObservables.observeSet(
				Realm.getDefault(), selectedAlgorithm, "algorithms");
		// ViewerSupport.bind(checkboxTableViewer, modelSet,
		// BeanProperties.value(Algorithm.class, "name"));

		IObservableSet widgetSet = ViewersObservables.observeCheckedElements(
				algorithmTableViewer, Algorithm.class);
		// modelSet = BeansObservables.obser(Realm.getDefault(),
		// selectedAlgorithm, "algorithms");

		ctx.bindSet(widgetSet, modelSet);

		// .observe(key);
		// ctx.bindSet(widgetSet, modelSet);
		// IObservableValue modelValue = BeanProperties.value(
		// AlgorithmParameter.class, btnBindding.get(key)).observe(
		// algoPara);

	}

	private void bindMetricParamValues() {
		DataBindingContext ctx = new DataBindingContext();
		IObservableSet modelSet = BeansObservables.observeSet(
				Realm.getDefault(), selectedMetric, "metrics");
		IObservableSet widgetSet = ViewersObservables.observeCheckedElements(
				metricTableViewer, Metric.class);

		ctx.bindSet(widgetSet, modelSet);
	}

	private void generateSimuParamBinding() {
		txtBindding.put(txtNumOfClusters, "numOfClusters");
		txtBindding.put(txtSizeOfClusters, "sizeOfClusters");
		txtBindding.put(textDimensionality, "dimensionality");
		txtBindding.put(txtAGWeight, "agWeight");
		txtBindding.put(txtGrassHopperDamping, "grassHopperDamping");
		txtBindding.put(txtMotleyTheta, "motleyTheta");
		txtBindding.put(txtSwapUpperBound, "swapUpperBound");
		txtBindding.put(txtMSDLambda, "msdLambda");
		txtBindding.put(txtMMRLambda, "mmrLambda");
		txtBindding.put(txtMaxvalue, "maxObserverValue");
		txtBindding.put(txtMinvalue, "minObserverValue");
		txtBindding.put(txtStep, "stepObserverValue");
	}

	private void bindSimuParamValues(IValidator validator) {
		if (ctx != null) {
			ctx.dispose();
		}
		ctx = new DataBindingContext();

		if (simuPara.getIndex() != null) {
			int index = simuPara.getIndex();
			combo.select(index);
		}

		if (simuPara.getDistribution() != null) {
			distributionCombo.setText(simuPara.getDistribution());
		} else {
			simuPara.setDistribution(distributionCombo.getText());
		}

		for (Text key : txtBindding.keySet()) {
			IObservableValue widgetValue = WidgetProperties.text(SWT.Modify)
					.observe(key);
			IObservableValue modelValue = BeanProperties.value(
					SimulationParameter.class, txtBindding.get(key)).observe(
					simuPara);
			if (validator == null) {
				ctx.bindValue(widgetValue, modelValue);
			} else {
				UpdateValueStrategy strategy = new UpdateValueStrategy();
				strategy.setAfterGetValidator(validator);
				// strategy.setBeforeSetValidator(validator);
				// strategy.setAfterConvertValidator(validator);
				// strategy.setConverter(new Converter(String.class,
				// Integer.class) {
				//
				// @Override
				// public Object convert(Object o) {
				// if (o == null) {
				// return 0;
				// }
				// if (o instanceof String) {
				// System.out.println("Convert");
				// String str = (String) o;
				// if (str.trim().isEmpty()) {
				// return 0;
				// }
				// return Integer.valueOf(str);
				// }
				// return o;
				// }
				//
				// });
				Binding bindValue = ctx.bindValue(widgetValue, modelValue,
						strategy, null);
				// Add some decorations
				ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.RIGHT);

			}
		}

	}

	private void addParameterFormPart(final Composite parent) {
		form = toolkit.createForm(parent);
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 2;
		form.getBody().setLayout(layout);
		layout.numColumns = 2;
		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		Section clusterSection = toolkit.createSection(form.getBody(),
				Section.CLIENT_INDENT | Section.TITLE_BAR);
		// gd_workerSection.widthHint = 335;
		clusterSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		clusterSection.setText("Dataset");
		customizeSection(clusterSection);

		clusterSectionBody = new Composite(clusterSection, SWT.NONE);
		toolkit.adapt(clusterSectionBody);
		toolkit.paintBordersFor(clusterSectionBody);
		clusterSection.setClient(clusterSectionBody);
		// new Label(form.getBody(), SWT.NONE);
		createClusterForm(clusterSectionBody);

		// Create algorithm section
		Section algorithmSection = toolkit.createSection(form.getBody(),
				Section.CLIENT_INDENT | Section.TITLE_BAR);
		algorithmSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		algorithmSection.setText("Algorithms");
		customizeSection(algorithmSection);

		Composite algorithmSectionBody = new Composite(algorithmSection,
				SWT.NONE);
		toolkit.adapt(algorithmSectionBody);
		toolkit.paintBordersFor(algorithmSectionBody);
		algorithmSection.setClient(algorithmSectionBody);
		createAlgorithmForm(algorithmSectionBody);
		new Label(form.getBody(), SWT.NONE);

		Section observerSection = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR);
		observerSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));
		toolkit.paintBordersFor(observerSection);
		observerSection.setText("Factor");
		customizeSection(observerSection);

		Composite observerSectionBody = new Composite(observerSection, SWT.NONE);
		observerSection.setClient(observerSectionBody);
		toolkit.adapt(observerSectionBody);
		toolkit.paintBordersFor(observerSectionBody);
		observerSectionBody.setLayout(new GridLayout(5, false));

		new Label(observerSectionBody, SWT.NONE);

		lblMin = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblMin, true, true);
		lblMin.setText("Min");

		lblMax = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblMax, true, true);
		lblMax.setText("Max");

		lblStep = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblStep, true, true);
		lblStep.setText("Step");
		new Label(observerSectionBody, SWT.NONE);

		comboViewer = new ComboViewer(observerSectionBody, SWT.READ_ONLY);
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		toolkit.paintBordersFor(combo);

		comboViewer.setContentProvider(new ArrayContentProvider()); // org.eclipse.jface.viewers.ArrayContentProvider()
		comboViewer.setLabelProvider(new LabelProvider()); // org.eclipse.jface.viewers.LabelProvider()

		combo.setItems(observerValues);
		combo.setText(observerValues[0]);
		combo.setVisibleItemCount(observerValues.length);
		combo.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				index = combo.getSelectionIndex();
				// System.out.println(filterByText[index]);

			}
		});

		txtMinvalue = new Text(observerSectionBody, SWT.BORDER);
		txtMinvalue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(txtMinvalue, true, true);

		txtMaxvalue = new Text(observerSectionBody, SWT.BORDER);
		txtMaxvalue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(txtMaxvalue, true, true);

		txtStep = new Text(observerSectionBody, SWT.BORDER);
		txtStep.setText("1");
		txtStep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		toolkit.adapt(txtStep, true, true);
		new Label(observerSectionBody, SWT.NONE);

		addAlgorithmSection(parent);

		addMetricSection(parent);

		composite_1 = new Composite(form.getBody(), SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		toolkit.adapt(composite_1);
		toolkit.paintBordersFor(composite_1);

		final Button saveButton = new Button(composite_1, SWT.PUSH);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Save current config");
				FileDialog dlg = new FileDialog(saveButton.getShell(), SWT.SAVE);
				String[] filterExt = { "*.xml" };
				dlg.setFilterExtensions(filterExt);
				dlg.setText("Save");
				String path = dlg.open();
				if (path == null)
					return;
				saveConfig(path);
				// System.out.println("End simulating");
			}
		});
		saveButton.setText("Save Config");

		final Button loadConfig = new Button(composite_1, SWT.PUSH);
		loadConfig.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Load config");
				FileDialog dlg = new FileDialog(loadConfig.getShell(), SWT.OPEN);
				String[] filterExt = { "*.xml" };
				dlg.setFilterExtensions(filterExt);
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null)
					return;
				loadConfig(path);
			}
		});
		loadConfig.setText("Load Config");

		link = new Link(form.getBody(), SWT.NONE);
		link.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1,
				1));
		toolkit.adapt(link, true, true);
		link.setText("<a>Advanced</a>");

		link.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				AdvanceConfigPart dialog = new AdvanceConfigPart(parent
						.getShell());
				dialog.create();
				if (dialog.open() == Window.OK) {
					System.out.println("Open advanced config");
				}
			}
		});

	}

	protected void saveConfig(String path) {
		// TODO Auto-generated method stub

	}

	protected void loadConfig(String path) {
		// TODO Auto-generated method stub

	}

	private void addMetricSection(Composite parent) {
		sctnMetric = toolkit.createSection(form.getBody(), Section.COMPACT
				| Section.TITLE_BAR);
		sctnMetric.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		toolkit.adapt(sctnMetric);
		toolkit.paintBordersFor(sctnMetric);
		sctnMetric.setText("Metrics");
		customizeSection(sctnMetric);

		metricComposite = new Composite(sctnMetric, SWT.NONE);
		toolkit.adapt(metricComposite);
		toolkit.paintBordersFor(metricComposite);
		sctnMetric.setClient(metricComposite);

		metricTableViewer = addCheckedTableViewer(toolkit, metricComposite);
		Metric selectAll = new Metric();
		selectAll.setName(Constant.SELECTALL);
		metricTableViewer.add(selectAll);
		for (Metric metric : selectedMetric.getMetrics()) {
			metricTableViewer.add(metric);
		}
		// Table table = metricTableViewer.getTable();
		// addSelectAllListener(table);
		// table.setSize(100, 100);
		// table.pack();
	}

	private void addAlgorithmSection(Composite parent) {
		sctnAlgorithm = toolkit.createSection(form.getBody(), Section.COMPACT
				| Section.TITLE_BAR);
		sctnAlgorithm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		toolkit.adapt(sctnAlgorithm);
		toolkit.paintBordersFor(sctnAlgorithm);
		sctnAlgorithm.setText("Algorithms");
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
		for (Algorithm algo : selectedAlgorithm.getAlgorithms()) {
			algorithmTableViewer.add(algo);
		}
		// Table table = algorithmTableViewer.getTable();
		// addSelectAllListener(table);
		// table.setSize(100, 100);
		// table.pack();
		// table.deselectAll();
	}

	private void createAlgorithmForm(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Label lblAG = new Label(parent, SWT.NONE);
		lblAG.setText("AG Weight");

		txtAGWeight = new Text(parent, SWT.BORDER);
		txtAGWeight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblGrassHopper = new Label(parent, SWT.NONE);
		lblGrassHopper.setText("GrassHopper Damping");

		txtGrassHopperDamping = new Text(parent, SWT.BORDER);
		txtGrassHopperDamping.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label lblMotley = new Label(parent, SWT.NONE);
		lblMotley.setText("Motley Theta");

		txtMotleyTheta = new Text(parent, SWT.BORDER);
		txtMotleyTheta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblSwap = new Label(parent, SWT.NONE);
		lblSwap.setText("Swap Upper Bound");

		txtSwapUpperBound = new Text(parent, SWT.BORDER);
		txtSwapUpperBound.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label lblMSD = new Label(parent, SWT.NONE);
		lblMSD.setText("MSD Lambda");

		txtMSDLambda = new Text(parent, SWT.BORDER);
		txtMSDLambda.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblMMR = new Label(parent, SWT.NONE);
		lblMMR.setText("MMR Lambda");

		txtMMRLambda = new Text(parent, SWT.BORDER);
		txtMMRLambda.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
	}

	private void createClusterForm(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Label lblNumOfClusters = new Label(parent, SWT.NONE);
		lblNumOfClusters.setText("No. Clusters");

		txtNumOfClusters = new Text(parent, SWT.BORDER);
		txtNumOfClusters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblSizeOfClusters = new Label(parent, SWT.NONE);
		lblSizeOfClusters.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblSizeOfClusters.setText("Size");

		txtSizeOfClusters = new Text(parent, SWT.BORDER);
		txtSizeOfClusters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		lblDimentionality = new Label(clusterSectionBody, SWT.NONE);
		lblDimentionality.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		toolkit.adapt(lblDimentionality, true, true);
		lblDimentionality.setText("Dimentionality");

		textDimensionality = new Text(clusterSectionBody, SWT.BORDER);
		textDimensionality.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		toolkit.adapt(textDimensionality, true, true);

		Label lblDistribution = new Label(parent, SWT.NONE);
		lblDistribution.setText("Distribution");

		distributionComboViewer = new ComboViewer(parent, SWT.READ_ONLY);
		distributionCombo = distributionComboViewer.getCombo();
		distributionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		toolkit.paintBordersFor(distributionCombo);

		distributionComboViewer.setContentProvider(new ArrayContentProvider()); // org.eclipse.jface.viewers.ArrayContentProvider()
		distributionComboViewer.setLabelProvider(new LabelProvider()); // org.eclipse.jface.viewers.LabelProvider()

		distributionCombo.setItems(distributionValues);
		distributionCombo.setText(distributionValues[0]);
		distributionCombo.setVisibleItemCount(distributionValues.length);
		distributionCombo.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				simuPara.setDistribution(distributionCombo.getText());
			}
		});

	}

	private CheckboxTableViewer addCheckedTableViewer(FormToolkit toolkit,
			Composite composite) {
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);

		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer
				.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION
						| SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		Table table = checkboxTableViewer.getTable();
		// table.setHeaderVisible(true);
		toolkit.paintBordersFor(table);
		// TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		// tableColumn.setText("Select All");
		// tableColumn.setImage(new Image(Display.getCurrent(),
		// "icons/checkbox.gif"));
		//
		// tableColumnLayout.setColumnData(tableColumn, new ColumnWeightData(20,
		// 150, true));
		// tableColumn.setWidth(150);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);
		// addSelectAllListener(table, tableItem);
		checkboxTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				boolean checked = event.getChecked();
				Object ele = event.getElement();
				if (Constant.SELECTALL.equalsIgnoreCase(ele.toString())) {
					if (checked) {
						checkboxTableViewer.setAllChecked(true);
					} else
						checkboxTableViewer.setAllChecked(false);
				}
			}
		});
		return checkboxTableViewer;

		// friendsViewer = CheckboxTableViewer.newCheckList(friendsComposite,
		// SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		//
		// Table friendsTable = friendsViewer.getTable();
		// friendsTable.setHeaderVisible(true);
		// friendsTable.setLinesVisible(true);
		// TableColumn friendNameColumn = new TableColumn(friendsTable,
		// SWT.NONE);
		// friendNameColumn.setText("Name");
		// friendsColumnLayout.setColumnData(friendNameColumn,
		// new ColumnWeightData(1));
		//
		// GridDataFactory.fillDefaults().grab(true, true)
		// .applyTo(friendsViewer.getTable());

	}

	@Inject
	@Optional
	void updateMetricHandler(
			@UIEventTopic(EventConstants.METRIC_OBSERVER_UPDATE_UPDATED) String s) {
		// start simulate
		System.out.println("Update metrics to context");
		ContextUtil.updateContext(context, Constant.METRIC, selectedMetric);

		System.out.println("Update observer to context");
		ContextUtil.updateContext(context, Constant.FACTOR,
				FACTOR.valueOf(observerValues[index]));

	}

	@Inject
	@Optional
	void evaluateHandler(
			@UIEventTopic(EventConstants.FUNCTION_SIMULATING_START) String s) {
		System.out.println("Start Evaluating...");
		startEvaluating();
		// System.out.println(selectedAlgorithm.toString());
		System.out.println("End Evaluating");
	}

	private void startEvaluating() {

		// TODO: generate algor.xml and prop.xml

		// sent signal to simulate model
		eval_start.send(EventConstants.DATA_SIMULATING_START, "start");

		// run algorithms;
		/*
		 * if (model != null) { Experiment_Evaluate eval = new
		 * Experiment_Evaluate(); eval.SetFileConfig(Constant.ALGO_CONFIG_FILE);
		 * eval.setModel(model); eval.run(); } else {
		 * System.out.println("No input data"); }
		 * importResult(Constant.RESULT_FILE);
		 */

	}

	@Inject
	@Optional
	void updateHandler(
			@UIEventTopic(EventConstants.DATA_SIMULATING_START) String s) {
		// start simulate
		System.out.println("Simulating data...");
		startSimulate();
		System.out.println("Data Generated");
		// broker.send(EventConstants.DATA_UPDATE_CLEAR, 0);

		Double minvalue = simuPara.getMinObserverValue();
		Double maxValue = simuPara.getMaxObserverValue();
		Double step = simuPara.getStepObserverValue();
		if (minvalue == null || maxValue == null) {
			// throw new IllegalArgumentException();
			return;
		}
		double interval = maxValue - minvalue;
		String factor = observerValues[index];

		double start = minvalue;
		int evalID = 0;
		System.out.println("Start Evaluating...");
		while (start <= maxValue) {

			double value = start;
			// System.out.println("value: " + value + ", key: " + attribute);
			// test(initHashMap(value, attribute));

			// TODO calling experiment logic to generate the data

			start += step;
			evalID++;
		}
		System.out.println("End Evaluting...");
	}

	private void startSimulate() {
		if (context.containsKey(Constant.ADVANCED_PARA)) {
			advanceConfigPara = (AdvanceDatasetParameter) context
					.get(Constant.ADVANCED_PARA);
		}

		try {
			createAlgorXML();
			createPropXML();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createPropXML() throws IOException {
		// Creating document
		Document document = DocumentHelper.createDocument();
		Element config = document.addElement("configuration");

		config.addElement("NumCluster").setText(
				String.valueOf(simuPara.getNumOfClusters()));
		config.addElement("size").setText(
				String.valueOf(simuPara.getSizeOfClusters()));
		config.addElement("dimensionality").setText(
				String.valueOf(simuPara.getDimensionality()));
		config.addElement("maxClusterDistance").setText(
				String.valueOf(advanceConfigPara.getMaxClusterDistance()));
		config.addElement("minClusterDistance").setText(
				String.valueOf(advanceConfigPara.getMinClusterDistance()));
		config.addElement("maxRadius").setText(
				String.valueOf(advanceConfigPara.getMaxRadius()));
		config.addElement("minRadius").setText(
				String.valueOf(advanceConfigPara.getMinRadius()));

		Element centgen = config.addElement("centgen");
		centgen.addAttribute("name", advanceConfigPara.getCentgenName());
		centgen.addElement("distance").setText(
				String.valueOf(advanceConfigPara.getCentgenDistance()));

		Element clusters = config.addElement("clusters");
		int n = simuPara.getNumOfClusters();
		int sizeCluster = simuPara.getSizeOfClusters() / n;
		for (int i = 0; i < n; i++) {
			Element cluster = clusters.addElement("cluster");
			cluster.addAttribute("dimensionality",
					String.valueOf(simuPara.getDimensionality()));
			cluster.addAttribute("size", String.valueOf(sizeCluster));
			Element d = cluster.addElement("distribution");
			String distributionName = simuPara.getDistribution();
			d.addAttribute("name", simuPara.getDistribution());
			switch (DISTRIBUTION.valueOf(distributionName)) {
			case Cosine:
				d.addElement("max").setText(
						String.valueOf(advanceConfigPara.getMaxCosine()));
				d.addElement("min").setText(
						String.valueOf(advanceConfigPara.getMinCosine()));
				break;
			case Normal:
				d.addElement("mean").setText(
						String.valueOf(advanceConfigPara.getMeanNormal()));
				d.addElement("std").setText(
						String.valueOf(advanceConfigPara.getStdNormal()));
				break;
			case Powertail:
				d.addElement("shape").setText(
						String.valueOf(advanceConfigPara.getShapeTail()));
				d.addElement("max").setText(
						String.valueOf(advanceConfigPara.getMaxTail()));
				d.addElement("min").setText(
						String.valueOf(advanceConfigPara.getMinTail()));
				break;
			default:
				break;
			}

			Element cor = cluster.addElement("coordinate_generator");
			cor.addAttribute("name", "clustered");
			cor.addElement("radius");
			cor.addElement("centroid");

		}
		// Writing document contents to xml file
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter output = new XMLWriter(new FileWriter(new File(propxml)),
				format);
		output.write(document);
		output.close();
		System.out.println("Completed generate prop.xml");

	}

	private void createAlgorXML() throws IOException {
		// Creating document
		Document document = DocumentHelper.createDocument();
		Element config = document.addElement("configuration");

		config.addElement("resultSize").setText("100");

		Element algorithms = config.addElement("algorithms");
		Element ag = algorithms.addElement("algorithm");
		ag.addAttribute("name", "AG");
		ag.addElement("lambda").setText("0.4");
		ag.addElement("damping").setText("0.85");
		ag.addElement("weightThreshold").setText(
				String.valueOf(simuPara.getAgWeight()));

		Element mmr = algorithms.addElement("algorithm");
		mmr.addAttribute("name", "MMR");
		mmr.addElement("lambda").setText(
				String.valueOf(simuPara.getMmrLambda()));

		Element mmd = algorithms.addElement("algorithm");
		mmd.addAttribute("name", "MSD");
		mmd.addElement("lambda").setText(
				String.valueOf(simuPara.getMsdLambda()));

		Element motley = algorithms.addElement("algorithm");
		motley.addAttribute("name", "Motley");
		motley.addElement("theta").setText(
				String.valueOf(simuPara.getMotleyTheta()));

		Element swap = algorithms.addElement("algorithm");
		swap.addAttribute("name", "Swap");
		swap.addElement("upperBound").setText(
				String.valueOf(simuPara.getSwapUpperBound()));

		Element grasshopper = algorithms.addElement("algorithm");
		grasshopper.addAttribute("name", "GrassHopper");
		grasshopper.addElement("damping").setText(
				String.valueOf(simuPara.getGrassHopperDamping()));

		// Writing document contents to xml file
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter output = new XMLWriter(new FileWriter(new File(algorxml)),
				format);
		output.write(document);
		output.close();
		System.out.println("Completed generate algro.xml");

	}
}
