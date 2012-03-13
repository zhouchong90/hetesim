package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class New extends Dialog
{

	protected String	result="";
	protected Shell		shlNewApplication;
	private Text name;
	private Text srcPath;
	private Text errorText;
	private Text fileType;

	public String getSrcDir()
	{
		return srcPath.getText();
	}

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public New(Shell parent, int style)
	{
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String open()
	{
		createContents();
		shlNewApplication.open();
		shlNewApplication.layout();
		Display display = getParent().getDisplay();
		while (!shlNewApplication.isDisposed())
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
		shlNewApplication = new Shell(getParent(), getStyle());
		shlNewApplication.setSize(500, 316);
		shlNewApplication.setText("New application");
		shlNewApplication.setLayout(null);
		
		Group grpName = new Group(shlNewApplication, SWT.NONE);
		grpName.setBounds(5, 67, 484, 64);
		grpName.setText("New Application Name");
		
		name = new Text(grpName, SWT.BORDER);
		name.setBounds(10, 26, 464, 28);
		
		Group grpSourceFilePath = new Group(shlNewApplication, SWT.NONE);
		grpSourceFilePath.setBounds(5, 137, 484, 64);
		grpSourceFilePath.setText("Source File Folder Path");
		
		srcPath = new Text(grpSourceFilePath, SWT.BORDER);
		srcPath.setBounds(10, 28, 389, 26);
		
		Button btnBrowse = new Button(grpSourceFilePath, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			//open
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog browse = new DirectoryDialog(shlNewApplication);
				browse.setMessage("open the directory that contains all source files for the application");
				String tmp = browse.open();
				if(tmp==null)
					srcPath.setText("");
				else
					srcPath.setText(tmp);
			}
		});
		btnBrowse.setBounds(404, 26, 70, 30);
		btnBrowse.setText("browse");
		
		Button btnClose = new Button(shlNewApplication, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			//close
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlNewApplication.close();
			}
		});
		btnClose.setBounds(410, 229, 70, 30);
		btnClose.setText("close");
		
		Button btnOk = new Button(shlNewApplication, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//ok
				if (isValid())
				{
					result = srcPath.getText();
					shlNewApplication.close();
				}
				else
				{
					new MessageDialog(shlNewApplication,"错误信息",null,errorText.getText(),MessageDialog.WARNING,new String[]{"ok"},0).open();
				}
			}
		});
		btnOk.setText("ok");
		btnOk.setBounds(334, 229, 70, 30);
		
		Button btnSaveAs = new Button(shlNewApplication, SWT.NONE);
		btnSaveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//save
				if (isValid())
				{
					FileDialog saveFile= new FileDialog(shlNewApplication,SWT.SAVE);
					saveFile.setFilterExtensions(new String[]{"*.hsd"});
					saveFile.setFileName(name.getText());
					String savePath = saveFile.open();
					if(savePath==null)
						return;
					while(new File(savePath).exists())
					{
						if(MessageDialog.openQuestion(shlNewApplication, "WARNING","File already exists, overwrite?"))
						{
							break;
						}
						savePath = saveFile.open();
						if(savePath==null)
							return;
					}
					
					try//把新建信息写进文件里.按顺序一个一行
					{
						FileWriter fw = new FileWriter(savePath);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(name.getText());
						bw.newLine();
						bw.write(srcPath.getText());
						bw.newLine();
						bw.write(fileType.getText());
						bw.newLine();
						bw.close();
						fw.close();		
					} catch (IOException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					MessageDialog.openWarning(shlNewApplication, "WARNING", errorText.getText());
				}	
			}
		});
		btnSaveAs.setText("save as...");
		btnSaveAs.setBounds(253, 229, 75, 30);
		
		errorText = new Text(shlNewApplication, SWT.NONE);
		errorText.setEnabled(false);
		errorText.setEditable(false);
		errorText.setBounds(44, 22, 404, 26);
		
		Group grpFileType = new Group(shlNewApplication, SWT.NONE);
		grpFileType.setText("Source File Type");
		grpFileType.setBounds(5, 207, 160, 64);
		
		fileType = new Text(grpFileType, SWT.BORDER);
		fileType.setBounds(10, 28, 106, 26);
	}
	
	private boolean isValid()
	{
		if(name.getText().isEmpty())
		{
			errorText.setText("Please input a new application name");
			return false;
		}
		
		if(srcPath.getText().isEmpty())
		{
			errorText.setText("Please select a source file folder path");
			return false;
		}
		
		if(!new File(srcPath.getText()).isDirectory())
		{
			errorText.setText("this is not a directory");
			return false;
		}
		
		if(fileType.getText().isEmpty())
		{
			errorText.setText("Please enter a source file type");
			return false;
		}
		
		if(name.getText().contains("\\")||name.getText().contains("/")||name.getText().contains(":")||
		   name.getText().contains("*")||name.getText().contains("\"")||name.getText().contains("?")||
		   name.getText().contains("<")||name.getText().contains(">")||name.getText().contains("|"))
		{
			errorText.setText("application name cannot contains \\,/,:,*,\",?,<,>, or |");
			return false;
		}
		
		if(!containsLegalFile())
		{
			errorText.setText("the file folder doesn't contains (."+ fileType.getText()+") file.");
			return false;
		}
		
		return true;
	}

	private String getTypeName(String file)
	{
		return file.substring(file.lastIndexOf(".")+1);
	}
	
	private boolean containsLegalFile()
	{
		File fileDir = new File(srcPath.getText());
		String[] filelist = fileDir.list();
		for (String file : filelist)
		{
			File singlefile = new File(srcPath.getText() + "/" + file);
			if (!singlefile.isDirectory())
			{
				if (getTypeName(file).equals(fileType.getText()))
					return true;
			}
		}
		return false;
	}
}
