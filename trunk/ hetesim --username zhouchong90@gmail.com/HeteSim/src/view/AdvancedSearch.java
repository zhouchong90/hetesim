package view;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;

public class AdvancedSearch extends Dialog
{

	protected Object	result;
	protected Shell		shell;
	private Text HeteSimPath;
	private String heteSimPath;
	private int	x;
	private int	y;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @param btnNewButton_1 
	 */
	public AdvancedSearch(Shell parent, int style, int x, int y)
	{
		super(parent, style);
		this.x = x;
		this.y = y;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String open()
	{
		createContents();
		shell.open();		
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		return heteSimPath;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents()
	{
		shell = new Shell(getParent(), SWT.BORDER | SWT.RESIZE);
		shell.setSize(387, 62);
		shell.setLocation(x, y);	
		shell.setText(getText());
		shell.setLayout(new GridLayout(3, false));
		
		Label lblHetesimPath = new Label(shell, SWT.NONE);
		lblHetesimPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHetesimPath.setText("Semantic Path");
		
		HeteSimPath = new Text(shell, SWT.BORDER);
		HeteSimPath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {			
				heteSimPath = HeteSimPath.getText().trim();
				shell.close();
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		btnNewButton.setText("ok");

	}

}
