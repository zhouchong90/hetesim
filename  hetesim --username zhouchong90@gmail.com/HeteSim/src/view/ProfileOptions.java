package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.layout.FillLayout;

import toolKit.PreCalculate;

public class ProfileOptions extends Dialog
{

	protected Object			result;
	protected Shell				shlProfileOptions;
	ArrayList<Composite>		paths;
	private Text				text;
	private Text				text_1;
	private ProfileTemplate		pt;
	private Composite			compositeMain;
	private ScrolledComposite	scrolledComposite;
	private Text				text_2;
	private PreCalculate		preCal;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProfileOptions(Shell parent, int style, PreCalculate preCal)
	{
		super(parent, style);
		setText("SWT Dialog");
		pt = new ProfileTemplate();

		this.preCal = preCal;
	}

	/**
	 * Open the dialog.
	 * 
	 * @param type
	 * @return the result
	 */
	public Object open()
	{
		createContents();
		shlProfileOptions.open();
		shlProfileOptions.layout();
		Display display = getParent().getDisplay();
		while (!shlProfileOptions.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents()
	{
		shlProfileOptions = new Shell(getParent(), getStyle());
		shlProfileOptions.setSize(400, 470);
		shlProfileOptions.setText("Profile Options");
		shlProfileOptions.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(shlProfileOptions, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		composite.setLayout(new GridLayout(2, false));

		Label lblType = new Label(composite, SWT.NONE);
		lblType.setAlignment(SWT.CENTER);
		GridData gd_lblType = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_lblType.widthHint = 67;
		lblType.setLayoutData(gd_lblType);
		lblType.setText("Type:");

		final Combo combo = new Combo(composite, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		combo.add("Author");
		combo.add("Conference");
		combo.select(0);

		Label lblProfileComposites = new Label(shlProfileOptions, SWT.NONE);
		lblProfileComposites.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1));
		lblProfileComposites.setText("Profile Composites:");

		scrolledComposite = new ScrolledComposite(shlProfileOptions, SWT.BORDER
				| SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		compositeMain = new Composite(scrolledComposite, SWT.NONE);
		compositeMain.setTouchEnabled(true);
		compositeMain.setLayout(new GridLayout(1, false));

		Composite composite_0 = new Composite(compositeMain, SWT.BORDER);
		composite_0.setLayout(new GridLayout(5, false));
		GridData gd_composite_0 = new GridData(SWT.FILL, SWT.TOP, true, false,
				1, 1);
		gd_composite_0.widthHint = 232;
		gd_composite_0.heightHint = 27;
		composite_0.setLayoutData(gd_composite_0);

		Label lblName = new Label(composite_0, SWT.NONE);
		lblName.setAlignment(SWT.CENTER);
		GridData gd_lblName = new GridData(SWT.CENTER, SWT.CENTER, false, true,
				1, 1);
		gd_lblName.widthHint = 87;
		lblName.setLayoutData(gd_lblName);
		lblName.setText("Name");

		Label label = new Label(composite_0, SWT.SEPARATOR | SWT.VERTICAL);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		Label lblHetesimpath = new Label(composite_0, SWT.NONE);
		GridData gd_lblHetesimpath = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblHetesimpath.widthHint = 193;
		lblHetesimpath.setLayoutData(gd_lblHetesimpath);
		lblHetesimpath.setAlignment(SWT.CENTER);
		lblHetesimpath.setText("HeteSimPath");

		Label label_1 = new Label(composite_0, SWT.SEPARATOR | SWT.VERTICAL);
		label_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));

		Label lblK = new Label(composite_0, SWT.NONE);
		lblK.setText("K");

		createComposite();

		scrolledComposite.setContent(compositeMain);
		scrolledComposite.setMinSize(compositeMain.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		Button btnOk = new Button(shlProfileOptions, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				pt.type = combo.getText();
				FileDialog saveFile = new FileDialog(shlProfileOptions,
						SWT.SAVE);
				saveFile.setFilterExtensions(new String[] { "*.prf" });
				saveFile.setFileName(combo.getText());
				String savePath = saveFile.open();
				if (savePath == null)
					return;
				while (new File(savePath).exists())
				{
					if (MessageDialog.openQuestion(shlProfileOptions,
							"WARNING", "File already exists, overwrite?"))
					{
						break;
					}
					savePath = saveFile.open();
					if (savePath == null)
						return;
				}

				try
				{
					pt.OutAsStream(savePath);
				} catch (FileNotFoundException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				shlProfileOptions.dispose();
			}
		});
		GridData gd_btnOk = new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1);
		gd_btnOk.widthHint = 83;
		btnOk.setLayoutData(gd_btnOk);
		btnOk.setText("save");
	}

	public void createComposite()
	{
		final Composite composite_1 = new Composite(compositeMain, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		final Text name = new Text(composite_1, SWT.BORDER);
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_name.widthHint = 90;
		name.setLayoutData(gd_name);

		final Text path = new Text(composite_1, SWT.BORDER);
		path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		final Text k = new Text(composite_1, SWT.BORDER);
		GridData gd_textK = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_textK.widthHint = 20;
		k.setLayoutData(gd_textK);

		Button button = new Button(composite_1, SWT.NONE);
		GridData gd_button = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_button.widthHint = 20;
		button.setLayoutData(gd_button);
		button.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{

				if (name.getText().isEmpty())
				{
					MessageDialog.openError(shlProfileOptions, "ERROR",
							"Name is empty.");
				}
				else if (path.getText().split(",").length < 2
						|| !preCal.isPathRight(path.getText()))
				{
					MessageDialog
							.openError(
									shlProfileOptions,
									"ERROR",
									"HeteSim Path illegal. And it must be seperated by \",\" and at least length two");
				}
				else
				{
					final Composite NewComposite = new Composite(compositeMain,
							SWT.NONE);
					NewComposite.setLayout(new GridLayout(4, false));
					NewComposite.setLayoutData(new GridData(SWT.FILL,
							SWT.CENTER, true, false, 1, 1));

					final Text NewName = new Text(NewComposite, SWT.BORDER);
					GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER,
							false, false, 1, 1);
					gd_name.widthHint = 90;
					NewName.setLayoutData(gd_name);
					NewName.setText(name.getText());
					NewName.setEditable(false);

					final Text NewPath = new Text(NewComposite, SWT.BORDER);
					NewPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
							true, false, 1, 1));
					NewPath.setText(path.getText());
					NewPath.setEditable(false);

					final Text NewK = new Text(NewComposite, SWT.BORDER);
					GridData gd_textK = new GridData(SWT.LEFT, SWT.CENTER,
							false, false, 1, 1);
					gd_textK.widthHint = 20;
					NewK.setLayoutData(gd_textK);
					NewK.setText(k.getText());
					NewK.setEditable(false);
					pt.ProfileList.add(name.getText() + "@" + path.getText()
							+ "@" + k.getText());

					Button button = new Button(NewComposite, SWT.NONE);
					GridData gd_button = new GridData(SWT.LEFT, SWT.CENTER,
							false, false, 1, 1);
					gd_button.widthHint = 20;
					button.setLayoutData(gd_button);
					button.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(SelectionEvent e)
						{
							for (int i = 0; i < pt.ProfileList.size(); i++)
							{
								if (pt.ProfileList.get(i).split("@")[0]
										.equals(NewName))
								{
									pt.ProfileList.remove(i);
									break;
								}
							}

							NewComposite.dispose();
							scrolledComposite.setMinSize(compositeMain
									.computeSize(SWT.DEFAULT, SWT.DEFAULT));
							compositeMain.layout(true);
						}
					});
					button.setText("-");

					composite_1.dispose();

					createComposite();

					scrolledComposite.setMinSize(compositeMain.computeSize(
							SWT.DEFAULT, SWT.DEFAULT));
					compositeMain.layout(true);
				}
			}
		});
		button.setText("+");
	}
}
