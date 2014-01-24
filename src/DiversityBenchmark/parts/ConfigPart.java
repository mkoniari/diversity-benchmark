package DiversityBenchmark.parts;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import DiversityBenchmark.models.Algorithm;
import DiversityBenchmark.models.AlgorithmModel;
import DiversityBenchmark.models.Metric;
import DiversityBenchmark.models.MetricModel;
import DiversityBenchmark.models.SimulationParameter;
import DiversityBenchmark.utils.Constant;
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

	final String[] observerValues = new String[] { "NumOfSubtopics",
			"Relavence Difference", "NumOfResult", "Subtopic Dissimilarity" };

	final String[] distributionValues = new String[] { "Normal", "Uniform" };
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

	/**
	 * * This is a callback that will allow us to create the viewer and
	 * initialize * it.
	 */
	@PostConstruct
	public void createComposite(Composite parent) {
		simuPara = new SimulationParameter();
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
		txtBindding.put(txtAGWeight, "agWeight");
		txtBindding.put(txtGrassHopperDamping, "grassHopperDamping");
		txtBindding.put(txtMotleyTheta, "motleyTheta");
		txtBindding.put(txtSwapUpperBound, "swapUpperBound");
		txtBindding.put(txtMSDLambda, "msdLambda");
		txtBindding.put(txtMMRLambda, "mmrLambda");
		txtBindding.put(txtMaxvalue, "maxObserverValue");
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

	private void addParameterFormPart(Composite parent) {
		form = toolkit.createForm(parent);
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout layout = new GridLayout();
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
		new Label(form.getBody(), SWT.NONE);
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
		// combo.setText(observerValues[0]);
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

}
