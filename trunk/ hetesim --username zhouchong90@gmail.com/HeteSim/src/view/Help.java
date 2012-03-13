package view;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Help extends Dialog
{

	protected Object	result;
	protected Shell		shlHelp;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Help(Shell parent, int style)
	{
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open()
	{
		createContents();
		shlHelp.open();
		shlHelp.layout();
		Display display = getParent().getDisplay();
		while (!shlHelp.isDisposed())
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
		shlHelp = new Shell(getParent(), getStyle());
		shlHelp.setSize(450, 300);
		shlHelp.setText("Help");

	}

}
