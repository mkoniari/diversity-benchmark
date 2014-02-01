package DiversityBenchmark.parts;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import DiversityBenchmark.models.AdvanceDatasetParameter;
import DiversityBenchmark.utils.Constant;
import DiversityBenchmark.utils.ContextUtil;
import DiversityBenchmark.utils.NumericValidator;

public class AdvanceConfigPart extends TitleAreaDialog {
	private AdvanceDatasetParameter advanceConfig;

	private FormToolkit toolkit;
	private Form form;
	final String[] observerValues = new String[] { "Circle", "Line" };

	private Section sctnDataset;
	private Composite datasetComposite;

	private Section sctnCentGen;
	private Composite centGenComposite;

	private Label lblCosineMin;

	private Label lblCosineMax;
	private Text textCosineMax;
	private Text textCosineMin;
	private Label lblMean;
	private Label lblStd;
	private Label lblNormal;
	private Text textMeanNormal;
	private Text textStdNormal;
	private Label lblShape;
	private Label lblMax;
	private Label lblMin;
	private Label lblPowerTail;
	private Text textShapeTail;
	private Text textMaxTail;
	private Text textMinTail;
	private Label lblMaxClusterDistance;
	private Text textMaxClusterDistance;
	private Label lblMinClusterDistance;
	private Text textMinClusterDistance;
	private Label lblMaxRadius;
	private Text textMaxRadius;
	private Label lblMinRadius;
	private Text textMinRadius;
	private Label lblShape_1;
	private Label lblDistance;
	private Text textCentGenDistance;
	private Combo combo;
	private ComboViewer comboViewer;
	private AdvanceDatasetParameter simuPara;

	private DataBindingContext ctx;
	private NumericValidator validator = null;
	Map<Text, String> txtBindding = new HashMap<Text, String>();

	@Inject
	IEclipseContext context;

	public AdvanceConfigPart(Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(false);
	}

	@Override
	public void create() {
		super.create();
		// Set the title
		setTitle("Advance Configuration\r\n");
		// Set the message
		// setMessage("This is a TitleAreaDialog",
		// IMessageProvider.INFORMATION);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		simuPara = new AdvanceDatasetParameter();
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		// layout.horizontalAlignment = GridData.FILL;
		parent.setLayout(layout);

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		initValue();
		addParameterFormPart(parent);

		generateSimuParamBinding();
		validator = new NumericValidator();
		bindSimuParamValues(validator);

		return parent;
	}

	private void initValue() {
		advanceConfig = new AdvanceDatasetParameter();
	}

	private void addParameterFormPart(final Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.horizontalSpacing = 0;
		form.getBody().setLayout(layout);
		layout.numColumns = 2;
		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		addClusterSection(parent);
		addDatasetSection(parent);
		addCentGenSection(parent);
	}

	private void addClusterSection(Composite parent) {
		Section observerSection = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR);
		GridData gd_observerSection = new GridData(SWT.FILL, SWT.TOP, true,
				true, 1, 2);
		gd_observerSection.widthHint = 360;
		observerSection.setLayoutData(gd_observerSection);
		toolkit.paintBordersFor(observerSection);
		observerSection.setText("Distribution");

		Composite observerSectionBody = new Composite(observerSection, SWT.NONE);
		observerSection.setClient(observerSectionBody);
		toolkit.adapt(observerSectionBody);
		toolkit.paintBordersFor(observerSectionBody);
		observerSectionBody.setLayout(new GridLayout(4, false));

		new Label(observerSectionBody, SWT.NONE);

		lblCosineMax = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblCosineMax, true, true);
		lblCosineMax.setText("Max");

		lblCosineMin = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblCosineMin, true, true);
		lblCosineMin.setText("Min");
		new Label(observerSectionBody, SWT.NONE);

		Label lblCosine = new Label(observerSectionBody, SWT.NONE);
		lblCosine.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		toolkit.adapt(lblCosine, true, true);
		lblCosine.setText("Cosine");

		textCosineMax = new Text(observerSectionBody, SWT.BORDER);
		textCosineMax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(textCosineMax, true, true);

		textCosineMin = new Text(observerSectionBody, SWT.BORDER);
		textCosineMin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(textCosineMin, true, true);
		new Label(observerSectionBody, SWT.NONE);
		new Label(observerSectionBody, SWT.NONE);

		lblMean = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblMean, true, true);
		lblMean.setText("Mean");

		lblStd = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblStd, true, true);
		lblStd.setText("Std");
		new Label(observerSectionBody, SWT.NONE);

		lblNormal = new Label(observerSectionBody, SWT.NONE);
		lblNormal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		toolkit.adapt(lblNormal, true, true);
		lblNormal.setText("Normal");

		textMeanNormal = new Text(observerSectionBody, SWT.BORDER);
		textMeanNormal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(textMeanNormal, true, true);

		textStdNormal = new Text(observerSectionBody, SWT.BORDER);
		textStdNormal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(textStdNormal, true, true);
		new Label(observerSectionBody, SWT.NONE);
		new Label(observerSectionBody, SWT.NONE);

		lblShape = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblShape, true, true);
		lblShape.setText("Shape");

		lblMax = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblMax, true, true);
		lblMax.setText("Max");

		lblMin = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblMin, true, true);
		lblMin.setText("Min");

		lblPowerTail = new Label(observerSectionBody, SWT.NONE);
		lblPowerTail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		toolkit.adapt(lblPowerTail, true, true);
		lblPowerTail.setText("Power tail");

		textShapeTail = new Text(observerSectionBody, SWT.BORDER);
		textShapeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(textShapeTail, true, true);

		textMaxTail = new Text(observerSectionBody, SWT.BORDER);
		textMaxTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(textMaxTail, true, true);

		textMinTail = new Text(observerSectionBody, SWT.BORDER);
		textMinTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(textMinTail, true, true);
	}

	private void addDatasetSection(Composite parent) {
		sctnDataset = toolkit.createSection(form.getBody(),
				Section.CLIENT_INDENT | Section.TITLE_BAR);
		// gd_workerSection.widthHint = 335;
		sctnDataset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 2));

		sctnDataset.setText("Dataset");

		datasetComposite = new Composite(sctnDataset, SWT.NONE);
		toolkit.adapt(datasetComposite);
		toolkit.paintBordersFor(datasetComposite);
		sctnDataset.setClient(datasetComposite);
		GridLayout gl_datasetComposite = new GridLayout(2, false);
		gl_datasetComposite.horizontalSpacing = 2;
		datasetComposite.setLayout(gl_datasetComposite);

		lblMaxClusterDistance = new Label(datasetComposite, SWT.NONE);
		toolkit.adapt(lblMaxClusterDistance, true, true);
		lblMaxClusterDistance.setText("Max cluster distance");

		textMaxClusterDistance = new Text(datasetComposite, SWT.BORDER);
		textMaxClusterDistance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		toolkit.adapt(textMaxClusterDistance, true, true);

		lblMinClusterDistance = new Label(datasetComposite, SWT.NONE);
		toolkit.adapt(lblMinClusterDistance, true, true);
		lblMinClusterDistance.setText("Min cluster distance");

		textMinClusterDistance = new Text(datasetComposite, SWT.BORDER);
		textMinClusterDistance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		toolkit.adapt(textMinClusterDistance, true, true);

		lblMaxRadius = new Label(datasetComposite, SWT.NONE);
		toolkit.adapt(lblMaxRadius, true, true);
		lblMaxRadius.setText("Max radius");

		textMaxRadius = new Text(datasetComposite, SWT.BORDER);
		textMaxRadius.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(textMaxRadius, true, true);

		lblMinRadius = new Label(datasetComposite, SWT.NONE);
		toolkit.adapt(lblMinRadius, true, true);
		lblMinRadius.setText("Min radius");

		textMinRadius = new Text(datasetComposite, SWT.BORDER);
		textMinRadius.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(textMinRadius, true, true);

	}

	private void addCentGenSection(Composite parent) {
		sctnCentGen = toolkit.createSection(form.getBody(),
				Section.CLIENT_INDENT | Section.TITLE_BAR);
		// gd_workerSection.widthHint = 335;
		GridData gd_sctnCentGen = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_sctnCentGen.widthHint = 293;
		sctnCentGen.setLayoutData(gd_sctnCentGen);

		sctnCentGen.setText("Cluster Generator");

		centGenComposite = new Composite(sctnCentGen, SWT.NONE);
		toolkit.adapt(centGenComposite);
		toolkit.paintBordersFor(centGenComposite);
		sctnCentGen.setClient(centGenComposite);
		GridLayout gl_centGenComposite = new GridLayout(3, false);
		gl_centGenComposite.horizontalSpacing = 3;
		centGenComposite.setLayout(gl_centGenComposite);
		new Label(centGenComposite, SWT.NONE);

		lblShape_1 = new Label(centGenComposite, SWT.NONE);
		toolkit.adapt(lblShape_1, true, true);
		lblShape_1.setText("Shape");

		lblDistance = new Label(centGenComposite, SWT.NONE);
		toolkit.adapt(lblDistance, true, true);
		lblDistance.setText("Distance");
		new Label(centGenComposite, SWT.NONE);

		comboViewer = new ComboViewer(centGenComposite, SWT.READ_ONLY);
		combo = comboViewer.getCombo();
		toolkit.paintBordersFor(combo);

		comboViewer.setContentProvider(new ArrayContentProvider()); // org.eclipse.jface.viewers.ArrayContentProvider()
		comboViewer.setLabelProvider(new LabelProvider()); // org.eclipse.jface.viewers.LabelProvider()

		combo.setItems(observerValues);
		combo.setText(observerValues[0]);
		combo.setVisibleItemCount(observerValues.length);
		combo.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				advanceConfig.setCentgenName(combo.getText());
				// System.out.println(filterByText[index]);

			}
		});

		textCentGenDistance = new Text(centGenComposite, SWT.BORDER);
		textCentGenDistance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		toolkit.adapt(textCentGenDistance, true, true);
		new Label(form.getBody(), SWT.NONE);

	}

	private void bindSimuParamValues(IValidator validator) {
		if (ctx != null) {
			ctx.dispose();
		}
		ctx = new DataBindingContext();

		if (simuPara.getCentgenName() != null) {
			// int index = simuPara.getCentgenIndex();
			// combo.select(index);
			combo.setText(simuPara.getCentgenName());
		} else {
			simuPara.setCentgenName(combo.getText());
		}

		for (Text key : txtBindding.keySet()) {
			IObservableValue widgetValue = WidgetProperties.text(SWT.Modify)
					.observe(key);
			IObservableValue modelValue = BeanProperties.value(
					AdvanceDatasetParameter.class, txtBindding.get(key))
					.observe(simuPara);
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

	public void createFeedbacksForm() {
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, "Apply", true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		boolean valid = true;

		return valid;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// Coyy textFields because the UI gets disposed
	// and the Text Fields are not accessible any more.
	private void saveInput() {
		// saveWorker();
		// saveAlgos();
		// advanceConfig.writeAdvanceConfigFile(Constant.ADVANCE_CONFIG_FILE);
		ContextUtil.updateContext(context, Constant.ADVANCED_PARA,
				advanceConfig);
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	private void generateSimuParamBinding() {
		txtBindding.put(textCentGenDistance, "centgenDistance");
		txtBindding.put(textCosineMax, "maxCosine");
		txtBindding.put(textCosineMin, "minCosine");
		txtBindding.put(textMaxClusterDistance, "maxClusterDistance");
		txtBindding.put(textMinClusterDistance, "minClusterDistance");
		txtBindding.put(textMaxRadius, "maxRadius");
		txtBindding.put(textMinRadius, "minRadius");
		txtBindding.put(textMeanNormal, "meanNormal");
		txtBindding.put(textStdNormal, "stdNormal");
		txtBindding.put(textShapeTail, "shapeTail");
		txtBindding.put(textMaxTail, "maxTail");
		txtBindding.put(textMinTail, "minTail");
	}
}