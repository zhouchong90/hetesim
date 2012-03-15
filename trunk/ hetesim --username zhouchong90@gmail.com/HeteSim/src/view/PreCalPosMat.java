package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.Data;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import calHeteSim.QuickHeteSim;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import toolKit.PreCalculate;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

public class PreCalPosMat extends Dialog
{

	protected Object		result;
	protected Shell			shlPrecalculationOfPaths;
	private QuickHeteSim	qhs;
	private Text			filePath;
	private Text			hetePath;
	private Data			data;
	private Text			low;
	private Text			high;
	private PreCalculate	preCal;
	private Text			start;
	private Text			end;
	private Text			top;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PreCalPosMat(Shell parent, int style, Data data, PreCalculate preCal)
	{
		super(parent, style);
		setText("SWT Dialog");
		this.data = data;
		this.preCal = preCal;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public QuickHeteSim open()
	{
		createContents();
		shlPrecalculationOfPaths.open();
		shlPrecalculationOfPaths.layout();
		Display display = getParent().getDisplay();
		while (!shlPrecalculationOfPaths.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		return qhs;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents()
	{
		shlPrecalculationOfPaths = new Shell(getParent(), getStyle());
		shlPrecalculationOfPaths.setSize(549, 254);
		shlPrecalculationOfPaths.setText("PreCalculation of Paths");
		shlPrecalculationOfPaths.setLayout(new GridLayout(3, false));

		Label lblNewLabel = new Label(shlPrecalculationOfPaths, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel.setText("Temp File Directory:");

		filePath = new Text(shlPrecalculationOfPaths, SWT.BORDER);
		filePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Button btnNewButton = new Button(shlPrecalculationOfPaths, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				filePath.setEditable(false);
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnNewButton.setText("Ok");

		Label lblHetesimpath = new Label(shlPrecalculationOfPaths, SWT.NONE);
		lblHetesimpath.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1));
		lblHetesimpath.setText("HeteSimPath:");

		hetePath = new Text(shlPrecalculationOfPaths, SWT.BORDER);
		hetePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Button btnNewButton_1 = new Button(shlPrecalculationOfPaths, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{

				try
				{
					if (filePath.getText().isEmpty())
					{
						MessageDialog.openError(shlPrecalculationOfPaths,
								"ERROR", "Invalid directory!");
					}
					else if (data == null)
					{
						MessageDialog.openError(shlPrecalculationOfPaths,
								"ERROR", "Please import source data first!");
					}
					else
					{
						File dir = new File(filePath.getText());
						if (!dir.isDirectory())
						{
							MessageDialog.openError(shlPrecalculationOfPaths,
									"ERROR", "Invalid directory!");
						}
						else
						{
							QuickHeteSim qhs = new QuickHeteSim();
							qhs.preCalPosMat(data, filePath.getText(),
									hetePath.getText());
							hetePath.setText("");

							MessageDialog.openInformation(
									shlPrecalculationOfPaths, "Finished...",
									"path " + hetePath.getText()
											+ " has finished.");
						}
					}
				} catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}

			}
		});
		GridData gd_btnNewButton_1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_btnNewButton_1.widthHint = 30;
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		btnNewButton_1.setText("Add");

		Label lblPreCallAll = new Label(shlPrecalculationOfPaths, SWT.NONE);
		GridData gd_lblPreCallAll = new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1);
		gd_lblPreCallAll.heightHint = 25;
		lblPreCallAll.setLayoutData(gd_lblPreCallAll);
		lblPreCallAll.setText("Pre Call All Paths:");

		Composite composite = new Composite(shlPrecalculationOfPaths,
				SWT.BORDER);
		composite.setLayout(new GridLayout(4, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1);
		gd_composite.heightHint = 102;
		composite.setLayoutData(gd_composite);

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel_1.setText("Start:");

		start = new Text(composite, SWT.BORDER);
		start.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel_2.setText("End:");

		end = new Text(composite, SWT.BORDER);
		end.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblLowBound = new Label(composite, SWT.NONE);
		lblLowBound.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblLowBound.setText("Low:");

		low = new Text(composite, SWT.BORDER);
		low.addVerifyListener(new VerifyListener()
		{
			public void verifyText(VerifyEvent e)
			{
				boolean b = ("0123456789".indexOf(e.text) >= 0);
				e.doit = b;
			}
		});
		low.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblHigh = new Label(composite, SWT.NONE);
		lblHigh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblHigh.setText("High:");

		high = new Text(composite, SWT.BORDER);
		high.addVerifyListener(new VerifyListener()
		{
			public void verifyText(VerifyEvent e)
			{
				boolean b = ("0123456789".indexOf(e.text) >= 0);
				e.doit = b;
			}
		});
		high.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblSaveTop = new Label(composite, SWT.NONE);
		lblSaveTop.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblSaveTop.setText("Save Top:");

		top = new Text(composite, SWT.BORDER);
		top.addVerifyListener(new VerifyListener()
		{
			public void verifyText(VerifyEvent e)
			{
				boolean b = ("0123456789".indexOf(e.text) >= 0);
				e.doit = b;
			}
		});
		top.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Button btnCal = new Button(shlPrecalculationOfPaths, SWT.NONE);
		GridData gd_btnCal = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1);
		gd_btnCal.widthHint = 69;
		btnCal.setLayoutData(gd_btnCal);
		btnCal.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// ‘§À„Ãÿ∂®
				if (filePath.getText().isEmpty())
				{
					MessageDialog.openError(shlPrecalculationOfPaths, "ERROR",
							"Invalid directory!");
				}
				else
				{
					File dir = new File(filePath.getText());
					if (!dir.isDirectory())
					{
						MessageDialog.openError(shlPrecalculationOfPaths,
								"ERROR", "Invalid directory!");
					}
					else
					{
						try
						{
							int lowBound = 0, highBound = 7;

							lowBound = Integer.parseInt(low.getText().trim());
							highBound = Integer.parseInt(high.getText().trim());

							if (!start.getText().isEmpty()
									&& !end.getText().isEmpty())
							{
								if (!top.getText().isEmpty())
								{
									preCal.calAllPaths(lowBound, highBound,
											start.getText().trim(), end
													.getText().trim(), filePath
													.getText().trim(), Integer
													.parseInt(top.getText()));
								}
								else
								{
									preCal.calAllPaths(lowBound, highBound,
											start.getText().trim(), end
													.getText().trim(), filePath
													.getText().trim());
								}

								MessageDialog
										.openInformation(
												shlPrecalculationOfPaths,
												"Finished...",
												"All HeteSim Matrix has been calculated.");
								shlPrecalculationOfPaths.close();
							}
							else if (start.getText().isEmpty()
									&& end.getText().isEmpty())
							{
								if (!top.getText().isEmpty())
								{

									preCal.calAllPaths(lowBound, highBound,
											filePath.getText());
								}
								else
								{
									preCal.calAllPaths(lowBound, highBound,
											filePath.getText(),
											Integer.parseInt(top.getText()));
								}
								MessageDialog
										.openInformation(
												shlPrecalculationOfPaths,
												"Finished...",
												"All HeteSim Matrix has been calculated.");
								shlPrecalculationOfPaths.close();
							}
							else
							{
								MessageDialog
										.openError(shlPrecalculationOfPaths,
												"ERROR",
												"You should specify start and end simutaneously.");
							}

						} catch (FileNotFoundException e1)
						{
							e1.printStackTrace();
						} catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
				}

			}
		});
		btnCal.setText("Calculate");
		new Label(shlPrecalculationOfPaths, SWT.NONE);
		new Label(shlPrecalculationOfPaths, SWT.NONE);

		Button btnLoad = new Button(shlPrecalculationOfPaths, SWT.NONE);
		GridData gd_btnLoad = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_btnLoad.widthHint = 30;
		btnLoad.setLayoutData(gd_btnLoad);
		btnLoad.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{

				try
				{
					if (filePath.getText().isEmpty())
					{
						MessageDialog.openError(shlPrecalculationOfPaths,
								"ERROR", "Invalid directory!");
					}
					else
					{
						File dir = new File(filePath.getText());
						if (!dir.isDirectory())
						{
							MessageDialog.openError(shlPrecalculationOfPaths,
									"ERROR", "Invalid directory!");
						}
						else
						{
							qhs = QuickHeteSim.loadPosMats(filePath.getText());

							MessageDialog.openInformation(
									shlPrecalculationOfPaths, "Finished...",
									"PreCaled HeteSim Matrix has loaded.");
							shlPrecalculationOfPaths.close();
						}
					}
				} catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				} catch (ClassNotFoundException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		btnLoad.setText("Load");
	}
}
