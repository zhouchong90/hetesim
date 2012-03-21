package view;

import io.LoadTxtFromFile;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import model.Data;
import model.Entity;
import model.TransNode;
import model.TransitiveMatrix;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

import calHeteSim.CalHeteSim;
import calHeteSim.QuickHeteSim;
import toolKit.PreCalculate;
import toolKit.WeightedTransMats;
import org.eclipse.swt.custom.SashForm;

public class MainWindow
{
	private static Shell shlHetesimdemo;
	private static String srcFilePath;
	private static Composite dataView;
	private static List entityList, relationList;
	private static Data data;
	private static PreCalculate preCal;
	private static TabFolder tabFolder;
	private static Text proName;
	private static StyledText profileText;
	private static Text heteSimPath;
	private static Text keyWord;
	private static Text k;
	private static TabItem tbtmGenerateProfile;
	private static TabItem tbtmRelevenceSearch;
	private static TabItem tbtmRecommandationSearch;
	private static List resultList, resultList2;
	private static Combo combo;
	private static Text status;
	private static ProfileTemplate pt;
	private static Button btnUseTemplate;
	private static Text RStext;
	private static QuickHeteSim qhs = null;
	private static WeightedTransMats wtm;
	private static SashForm sashForm;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		Display display = Display.getDefault();
		shlHetesimdemo = new Shell();
		shlHetesimdemo.addHelpListener(new HelpListener()
		{
			public void helpRequested(HelpEvent e)
			{
				Help helpWindow = new Help(shlHetesimdemo, SWT.DIALOG_TRIM);
				helpWindow.open();
			}
		});
		shlHetesimdemo.setSize(970, 600);
		shlHetesimdemo.setText("HeteSim");
		centerShell(shlHetesimdemo);

		constructLoadedFiles();
		constructFunctionsFolder();
		constructMenu();

		shlHetesimdemo.open();
		shlHetesimdemo.layout();
		while (!shlHetesimdemo.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	private static void constructLoadedFiles()
	{
		shlHetesimdemo.setLayout(new GridLayout(2, false));
		dataView = new Composite(shlHetesimdemo, SWT.BORDER);
		GridData gd_dataView = new GridData(SWT.LEFT, SWT.FILL, false, true, 1,
				1);
		gd_dataView.widthHint = 173;
		dataView.setLayoutData(gd_dataView);
		dataView.setLayout(new GridLayout(1, false));
		// dataView.setVisible(false);

		Label lblNewLabel = new Label(dataView, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true,
				false, 1, 1));
		lblNewLabel.setText("Loaded File");

		Label label = new Label(dataView, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		Label lblEntities = new Label(dataView, SWT.NONE);
		lblEntities.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		lblEntities.setText("entities");

		ScrolledComposite scrolledComposite = new ScrolledComposite(dataView,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		gd_scrolledComposite.heightHint = 140;
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		entityList = new List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);

		scrolledComposite.setContent(entityList);
		scrolledComposite.setMinSize(entityList.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		Label lblRelations = new Label(dataView, SWT.NONE);
		lblRelations.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		lblRelations.setText("relations");

		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(dataView,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite_1 = new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1);
		gd_scrolledComposite_1.heightHint = 140;
		scrolledComposite_1.setLayoutData(gd_scrolledComposite_1);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);

		relationList = new List(scrolledComposite_1, SWT.BORDER | SWT.V_SCROLL);

		scrolledComposite_1.setContent(relationList);
		scrolledComposite_1.setMinSize(relationList.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
	}

	private static void constructFunctionsFolder()
	{
		tabFolder = new TabFolder(shlHetesimdemo, SWT.NONE);
		tabFolder.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tabFolder.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		generateProfiles();
		relevenceSearch();
		recommandationSearch();
	}

	private static void constructMenu()
	{
		Menu menu = new Menu(shlHetesimdemo, SWT.BAR);
		shlHetesimdemo.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmNew = new MenuItem(menu_1, SWT.NONE);
		mntmNew.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				New newWindow = new New(shlHetesimdemo, SWT.DIALOG_TRIM
						| SWT.SYSTEM_MODAL);
				srcFilePath = newWindow.open();
				if (!srcFilePath.isEmpty())
				{
					try
					{
						loadData();

					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});
		mntmNew.setText("New");

		// the open button
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog open = new FileDialog(shlHetesimdemo, SWT.OPEN);
				String[] extentions =
				{ "*.hsd", "*.*" };
				String[] extentionNames =
				{ "HeteSimDemo文件类型(*.hsd)", "所有文件类型(*.*)" };
				open.setFilterExtensions(extentions);
				open.setFilterNames(extentionNames);
				String openedPath = open.open();// 打开的文件路径
				if (openedPath != null)
				{
					try
					{
						FileReader fr = new FileReader(openedPath);
						BufferedReader br = new BufferedReader(fr);
						br.readLine();
						String dir = br.readLine();
						String type = br.readLine();
						if (!containsLegalFile(dir, type))// 打开失败
						{
							MessageDialog
									.openError(
											shlHetesimdemo,
											"ERROR",
											"the source file directory "
													+ dir
													+ " isn't existed or there isn't any (."
													+ type + ") file in it");
						} else
						// 打开成功
						{
							srcFilePath = dir;
							if (!srcFilePath.isEmpty())
								loadData();
						}

					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});
		mntmOpen.setText("Open...");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmSaveAs = new MenuItem(menu_1, SWT.NONE);
		mntmSaveAs.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// 保存modelPath

				if (data == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please import source data first!");
				} else
				{
					FileDialog saveFile = new FileDialog(shlHetesimdemo,
							SWT.SAVE);
					saveFile.setFilterExtensions(new String[]
					{ "*.dat" });
					String savePath = saveFile.open();
					if (savePath == null)
						return;
					while (new File(savePath).exists())
					{
						if (MessageDialog.openQuestion(shlHetesimdemo,
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
						data.outAsStream(savePath);
						MessageDialog.openInformation(shlHetesimdemo,
								"Finished", "Data has been saved.");
					} catch (FileNotFoundException e1)
					{
						e1.printStackTrace();
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}

				}
			}
		});
		mntmSaveAs.setText("save as...");

		MenuItem mntmOpenFromExisted = new MenuItem(menu_1, SWT.NONE);
		mntmOpenFromExisted.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog open = new FileDialog(shlHetesimdemo, SWT.OPEN);
				String[] extentions =
				{ "*.dat", "*.*" };
				String[] extentionNames =
				{ "HeteSimDemo文件类型(*.dat)", "所有文件类型(*.*)" };
				open.setFilterExtensions(extentions);
				open.setFilterNames(extentionNames);
				String openedPath = open.open();// 打开的文件路径
				if (openedPath != null)
				{
					// 打开成功
					status.setText("Loading........");
					MessageDialog
							.openInformation(shlHetesimdemo,
									"Ready to Load...",
									"Click ok and wait...\nTry not to touch your keyboard and mouse.");

					try
					{

						data = Data.loadData(openedPath);

						entityList.removeAll();
						relationList.removeAll();

						for (String entity : data.getEntities().keySet())
						{
							entityList.add(entity + ":"
									+ data.getEntities().get(entity).size());
						}

						for (String relation : data.getTransMats().keySet())
						{
							relationList.add(relation
									+ ":"
									+ data.getTransMat(relation).getRows()
											.size()
									+ "*"
									+ data.getTransMat(relation).getCols()
											.size());
						}

						preCal = new PreCalculate(data);

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
					status.setText("");

				}
			}
		});
		mntmOpenFromExisted.setText("Open from existed data...");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmOptions = new MenuItem(menu_1, SWT.NONE);
		mntmOptions.setText("Options");

		// the close button
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				shlHetesimdemo.close();
			}
		});
		mntmExit.setText("Exit");

		// saved for the preprocess button
		MenuItem mntmPreprocess = new MenuItem(menu, SWT.CASCADE);
		mntmPreprocess.setText("Preprocess");

		Menu menu_4 = new Menu(mntmPreprocess);
		mntmPreprocess.setMenu(menu_4);

		MenuItem mntmPrecalposmat = new MenuItem(menu_4, SWT.NONE);
		mntmPrecalposmat.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (data == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please import source data first!");
				} else
				{
					PreCalPosMat window = new PreCalPosMat(shlHetesimdemo,
							SWT.DIALOG_TRIM, data, preCal);
					qhs = window.open();
				}
			}
		});
		mntmPrecalposmat.setText("PreCalculation of Paths");

		MenuItem mntmConstructWeightedMatrix = new MenuItem(menu_4, SWT.NONE);
		mntmConstructWeightedMatrix.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (qhs == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please load preCaled Mats first!");
				}
				// TODO 等数据齐全之后完善判断逻辑！
				else
				{
					// TODO demo，现在只以C开头,且非自动，等xml

//					wtm = new WeightedTransMats(data);
//					PreCalculate pc = new PreCalculate(data);
//
//					ArrayList<String> paths = pc.calPosiPath("C", "A", 1, 4);
//					TransitiveMatrix tmpMat = wtm.calWeightedMat(paths, qhs);
//					tmpMat.setMatrixName("C-A");
//					wtm.weightedMats.put("C-A", tmpMat);
//
//					paths = pc.calPosiPath("C", "T", 1, 4);
//					tmpMat = wtm.calWeightedMat(paths, qhs);
//					tmpMat.setMatrixName("C-T");
//					wtm.weightedMats.put("C-T", tmpMat);
//
//					paths = pc.calPosiPath("C", "L", 1, 4);
//					tmpMat = wtm.calWeightedMat(paths, qhs);
//					tmpMat.setMatrixName("C-L");
//					wtm.weightedMats.put("C-L", tmpMat);
//
//					paths = pc.calPosiPath("C", "C", 1, 4);
//					tmpMat = wtm.calWeightedMat(paths, qhs);
//					tmpMat.setMatrixName("C-C");
//					wtm.weightedMats.put("C-C", tmpMat);
//
//					try
//					{
//						wtm.outAsStream("C:/HeteSim/weightedMats/C.wtm");
//					} catch (IOException e1)
//					{
//						e1.printStackTrace();
//					}

					try
					{
						wtm = WeightedTransMats
								.loadWeightedTransMats("C:/HeteSim/weightedMats/C.wtm");
					} catch (ClassNotFoundException | IOException e1)
					{
						e1.printStackTrace();
					}

					MessageDialog.openInformation(shlHetesimdemo,
							"Loading Finished",
							"Weighted Matrices has been constructed.");
				}

			}
		});
		mntmConstructWeightedMatrix.setText("Construct Weighted Matrix");

		MenuItem mntmFunction = new MenuItem(menu, SWT.CASCADE);
		mntmFunction.setText("Functions");

		Menu menu_3 = new Menu(mntmFunction);
		mntmFunction.setMenu(menu_3);

		MenuItem mntmGenerateProfile = new MenuItem(menu_3, SWT.NONE);
		mntmGenerateProfile.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				tabFolder.setSelection(tbtmGenerateProfile);
			}
		});
		mntmGenerateProfile.setText("Generate Profile");

		MenuItem mntmRelevenceSearch = new MenuItem(menu_3, SWT.NONE);
		mntmRelevenceSearch.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				tabFolder.setSelection(tbtmRelevenceSearch);
			}
		});
		mntmRelevenceSearch.setText("Relevence Search");

		MenuItem mntmRecommandationSearch = new MenuItem(menu_3, SWT.NONE);
		mntmRecommandationSearch.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				tabFolder.setSelection(tbtmRecommandationSearch);
			}
		});
		mntmRecommandationSearch.setText("Recommandation Search");

		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");

		Menu menu_2 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_2);

		// the help button
		MenuItem mntmHelp_1 = new MenuItem(menu_2, SWT.NONE);
		mntmHelp_1.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Help helpWindow = new Help(shlHetesimdemo, SWT.DIALOG_TRIM);
				helpWindow.open();
			}
		});
		mntmHelp_1.setText("Help");

		// the about button
		MenuItem mntmAbout = new MenuItem(menu_2, SWT.NONE);
		mntmAbout.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				About aboutWindow = new About(shlHetesimdemo, SWT.DIALOG_TRIM);
				aboutWindow.open();
			}
		});
		mntmAbout.setText("About");
		new Label(shlHetesimdemo, SWT.NONE);

		status = new Text(shlHetesimdemo, SWT.READ_ONLY | SWT.RIGHT);
		status.setEditable(false);
		status.setEnabled(false);
		status.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	}

	private static void loadData() throws IOException// 添加list.
	{
		status.setText("Loading........");
		MessageDialog
				.openInformation(shlHetesimdemo, "Ready to Load...",
						"Click ok and wait...\nTry not to touch your keyboard and mouse.");

		entityList.removeAll();
		relationList.removeAll();
		LoadTxtFromFile txt = new LoadTxtFromFile(srcFilePath);// 从文件中读取出数据,
												// 并且构造data对象并赋值
		data = txt.run();
		for (String entity : data.getEntities().keySet())
		{
			entityList
					.add(entity + ":" + data.getEntities().get(entity).size());
		}

		for (String relation : data.getTransMats().keySet())
		{
			relationList.add(relation + ":"
					+ data.getTransMat(relation).getRows().size() + "*"
					+ data.getTransMat(relation).getCols().size());
		}

		preCal = new PreCalculate(data);

		status.setText("");
	}

	private static boolean containsLegalFile(String dirPath, String type)
	{
		File fileDir = new File(dirPath);
		if (!fileDir.isDirectory())
			return false;
		String[] filelist = fileDir.list();
		for (String file : filelist)
		{
			File singlefile = new File(dirPath + "/" + file);
			if (!singlefile.isDirectory())
			{
				if (file.substring(file.lastIndexOf(".") + 1).equals(type))
					return true;
			}
		}
		return false;
	}

	private static void generateProfiles()
	{
		tbtmGenerateProfile = new TabItem(tabFolder, SWT.NONE);
		tbtmGenerateProfile.setText("Generate Profile");

		Composite composite = new Composite(tabFolder, SWT.BORDER);
		composite.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbtmGenerateProfile.setControl(composite);
		composite.setLayout(new GridLayout(2, false));

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, true,
				1, 1);
		gd_composite_1.widthHint = 196;
		composite_1.setLayoutData(gd_composite_1);

		Composite composite_6 = new Composite(composite_1, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		Group grpName = new Group(composite_1, SWT.NONE);
		grpName.setLayout(new GridLayout(1, false));
		GridData gd_grpName = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpName.heightHint = 50;
		gd_grpName.widthHint = 143;
		grpName.setLayoutData(gd_grpName);
		grpName.setText("Name");

		proName = new Text(grpName, SWT.BORDER);
		proName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_composite_2.heightHint = 39;
		composite_2.setLayoutData(gd_composite_2);

		Group grpType = new Group(composite_1, SWT.NONE);
		grpType.setLayout(new GridLayout(1, false));
		GridData gd_grpType = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpType.heightHint = 50;
		grpType.setLayoutData(gd_grpType);
		grpType.setText("Type");

		combo = new Combo(grpType, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		combo.add("Author");
		combo.add("Conference");
		combo.setText("Author");

		Composite composite_4 = new Composite(composite_1, SWT.NONE);
		composite_4.setLayout(new GridLayout(2, false));
		GridData gd_composite_4 = new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1);
		gd_composite_4.heightHint = 39;
		composite_4.setLayoutData(gd_composite_4);

		btnUseTemplate = new Button(composite_4, SWT.NONE);
		btnUseTemplate.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// use template
				FileDialog open = new FileDialog(shlHetesimdemo, SWT.OPEN);
				String[] extentions =
				{ "*.prf", "*.*" };
				String[] extentionNames =
				{ "Profile Template文件类型(*.prf)", "所有文件类型(*.*)" };
				open.setFilterExtensions(extentions);
				open.setFilterNames(extentionNames);
				String openedPath = open.open();// 打开的文件路径
				if (openedPath != null)
				{
					try
					{
						pt = ProfileTemplate.InAsStream(openedPath);
						combo.setText(pt.type);
						btnUseTemplate.setText("loaded");
						btnUseTemplate.setEnabled(false);
					} catch (IOException e1)
					{
						e1.printStackTrace();
					} catch (ClassNotFoundException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});
		btnUseTemplate.setSize(107, 30);
		btnUseTemplate.setText("use template");

		Button btnCancl = new Button(composite_4, SWT.NONE);
		btnCancl.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// cancel
				pt = null;
				btnUseTemplate.setText("use template");
				btnUseTemplate.setEnabled(true);
			}
		});
		btnCancl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnCancl.setText("cancel");

		Button btnGenerate = new Button(composite_1, SWT.NONE);
		btnGenerate.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// generate
				if (data == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please import source data first!");
				} else if (pt == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please use a profile template first");
				} else if (proName.getText().isEmpty()
						|| data.getInstanceIndex(combo.getText()
								.substring(0, 1), proName.getText()) == -1)
				{
					MessageDialog
							.openError(shlHetesimdemo, "ERROR",
									"Name is empty or does not existed in the source file");
				} else
				{
					status.setText("Calculating........");
					MessageDialog
							.openInformation(shlHetesimdemo, "Calculating...",
									"Click ok and wait...\nTry not to touch your keyboard and mouse.");

					ArrayList<String> name = new ArrayList<String>();
					name.add(proName.getText());
					StringBuffer sb = new StringBuffer();
					sb.append(getProfileItem(pt.type, name));

					// Research Interest
					for (int i = 0; i < pt.ProfileList.size(); i++)
					{
						String[] tmp = pt.ProfileList.get(i).split("@", -1);
						CalHeteSim calHete = new CalHeteSim(data, tmp[1]);
						TransitiveMatrix posMat = calHete.getHeteSim(proName
								.getText());
						int tmpk;
						if (tmp[2].isEmpty())
							tmpk = 5;
						else
							tmpk = Integer.parseInt(tmp[2]);

						sb.append(getProfileItem(
								tmp[0],
								outputFirstKResult(posMat, proName.getText(),
										tmpk)));
					}

					profileText.setText(sb.toString());
					status.setText("");
				}
			}
		});
		GridData gd_btnGenerate = new GridData(SWT.CENTER, SWT.FILL, false,
				false, 1, 1);
		gd_btnGenerate.widthHint = 130;
		btnGenerate.setLayoutData(gd_btnGenerate);
		btnGenerate.setText("generate");

		Button btnOptions = new Button(composite_1, SWT.NONE);
		btnOptions.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// options
				if (data == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please import source data first!");
				} else
				{
					ProfileOptions opt = new ProfileOptions(shlHetesimdemo,
							SWT.DIALOG_TRIM, preCal);
					opt.open();
				}
			}
		});
		GridData gd_btnOptions = new GridData(SWT.CENTER, SWT.FILL, false,
				false, 1, 1);
		gd_btnOptions.widthHint = 130;
		btnOptions.setLayoutData(gd_btnOptions);
		btnOptions.setText("create template");

		Button btnSaveAs = new Button(composite_1, SWT.NONE);
		btnSaveAs.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// save as
				if (data == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please import source data first!");
				} else if (profileText.getText().isEmpty())
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please generate profile first!");
				} else
				{
					FileDialog saveFile = new FileDialog(shlHetesimdemo,
							SWT.SAVE);
					saveFile.setFilterExtensions(new String[]
					{ "*.txt" });
					saveFile.setFileName(keyWord.getText());
					String savePath = saveFile.open();
					if (savePath == null)
						return;
					while (new File(savePath).exists())
					{
						if (MessageDialog.openQuestion(shlHetesimdemo,
								"WARNING", "File already exists, overwrite?"))
						{
							break;
						}
						savePath = saveFile.open();
						if (savePath == null)
							return;
					}

					try
					// 把新建信息写进文件里.按顺序一个一行
					{
						FileWriter fw = new FileWriter(savePath);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(profileText.getText());
						bw.close();
						fw.close();
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});
		GridData gd_btnSaveAs = new GridData(SWT.CENTER, SWT.FILL, false,
				false, 1, 1);
		gd_btnSaveAs.widthHint = 130;
		btnSaveAs.setLayoutData(gd_btnSaveAs);
		btnSaveAs.setText("save as...");

		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		Group grpProfile = new Group(composite, SWT.NONE);
		grpProfile.setLayout(new GridLayout(1, false));
		grpProfile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		grpProfile.setText("Profile");

		profileText = new StyledText(grpProfile, SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL | SWT.MULTI);
		profileText.setDoubleClickEnabled(false);
		profileText.setEditable(false);
		profileText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		profileText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		tabFolder.setSelection(tbtmGenerateProfile);
	}

	private static void relevenceSearch()
	{
		tbtmRelevenceSearch = new TabItem(tabFolder, SWT.NONE);
		tbtmRelevenceSearch.setText("Relevence Search");

		Composite composite = new Composite(tabFolder, SWT.BORDER);
		composite.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbtmRelevenceSearch.setControl(composite);
		composite.setLayout(new GridLayout(2, false));

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.FILL, false, true,
				1, 1);
		gd_composite_1.widthHint = 196;
		composite_1.setLayoutData(gd_composite_1);

		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		GridData gd_composite_3 = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_composite_3.heightHint = 50;
		composite_3.setLayoutData(gd_composite_3);

		Group grpHetesimPath = new Group(composite_1, SWT.NONE);
		grpHetesimPath.setLayout(null);
		GridData gd_grpHetesimPath = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		gd_grpHetesimPath.heightHint = 40;
		grpHetesimPath.setLayoutData(gd_grpHetesimPath);
		grpHetesimPath.setText("HeteSim Path");

		Label lblExampleApvc = new Label(grpHetesimPath, SWT.NONE);
		lblExampleApvc.setBounds(8, 25, 170, 20);
		lblExampleApvc.setText("Example: A,P,V,C");

		heteSimPath = new Text(grpHetesimPath, SWT.BORDER);
		heteSimPath.setBounds(8, 49, 170, 26);

		Group grpKeyWord = new Group(composite_1, SWT.NONE);
		grpKeyWord.setLayout(null);
		GridData gd_grpKeyWord = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_grpKeyWord.heightHint = 20;
		grpKeyWord.setLayoutData(gd_grpKeyWord);
		grpKeyWord.setText("Key word");

		keyWord = new Text(grpKeyWord, SWT.BORDER);
		keyWord.setBounds(10, 28, 170, 26);

		Group grpMaxResultNumber = new Group(composite_1, SWT.NONE);
		grpMaxResultNumber.setLayout(null);
		GridData gd_grpMaxResultNumber = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		gd_grpMaxResultNumber.heightHint = 20;
		grpMaxResultNumber.setLayoutData(gd_grpMaxResultNumber);
		grpMaxResultNumber.setText("Max result number");

		k = new Text(grpMaxResultNumber, SWT.BORDER);
		k.setBounds(8, 25, 170, 26);

		Button btnSearchButton = new Button(composite_1, SWT.NONE);
		btnSearchButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			// search
			public void widgetSelected(SelectionEvent e)
			{
				status.setText("Calculating..........");
				if (data == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please import source data first!");
				} else if (heteSimPath.getText().split(",").length < 2
						|| !preCal.isPathRight(heteSimPath.getText()))
				{
					MessageDialog
							.openError(
									shlHetesimdemo,
									"ERROR",
									"HeteSim Path illegal. And it must be seperated by \",\" and at least length two");
				} else if (keyWord.getText().isEmpty()
						|| data.getInstanceIndex(
								heteSimPath.getText().split(",")[0],
								keyWord.getText()) == -1)
				{
					MessageDialog
							.openError(shlHetesimdemo, "ERROR",
									"key work is empty or does not existed in the source file");
				} else if (!k.getText().matches("[0-9]*"))
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Max result number should be a number!");
				} else
				// 所有输入正确
				{
					resultList2.setItems(resultList.getItems());

					resultList.removeAll();
					CalHeteSim calHete = new CalHeteSim(data, heteSimPath
							.getText().trim());
					TransitiveMatrix posMat = calHete.getHeteSim(keyWord
							.getText());

					resultList.add(heteSimPath.getText().trim());

					int maxNum = Integer.MAX_VALUE;
					if (!k.getText().isEmpty())
					{
						maxNum = Integer.parseInt(k.getText());
					}

					ArrayList<String> results = outputFirstKResultWithValue(
							posMat, keyWord.getText(), maxNum);
					int i = 1;
					for (String result : results)
					{
						resultList.add(String.valueOf(i) + ": " + result);
						i++;
					}
				}
				status.setText("");
			}
		});
		btnSearchButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, true, 1, 1));
		btnSearchButton.setSize(59, 30);
		btnSearchButton.setText("search");

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_composite_2.heightHint = 48;
		composite_2.setLayoutData(gd_composite_2);

		Composite grpResult = new Composite(composite, SWT.NONE);
		grpResult.setLayout(new GridLayout(2, false));
		grpResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		Label lblNewLabel_1 = new Label(grpResult, SWT.NONE);
		lblNewLabel_1.setText("Result Result");

		Label lblNewLabel_2 = new Label(grpResult, SWT.NONE);
		lblNewLabel_2.setText("Last Search");

		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(
				grpResult, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);

		resultList = new List(scrolledComposite_1, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL);
		scrolledComposite_1.setContent(resultList);
		scrolledComposite_1.setMinSize(resultList.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		ScrolledComposite scrolledComposite = new ScrolledComposite(grpResult,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		resultList2 = new List(scrolledComposite, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL);
		scrolledComposite.setContent(resultList2);
		scrolledComposite.setMinSize(resultList2.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
	}

	private static void recommandationSearch()
	{
		tbtmRecommandationSearch = new TabItem(tabFolder, SWT.NONE);
		tbtmRecommandationSearch.setText("Recommandation Search");

		final Composite composite = new Composite(tabFolder, SWT.BORDER);
		composite.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbtmRecommandationSearch.setControl(composite);
		composite.setLayout(new GridLayout(1, false));

		Composite searchComposite = new Composite(composite, SWT.NONE);
		searchComposite.setLayout(new GridLayout(3, false));
		GridData gd_searchComposite = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_searchComposite.heightHint = 52;
		searchComposite.setLayoutData(gd_searchComposite);

		sashForm = new SashForm(composite, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		RStext = new Text(searchComposite, SWT.BORDER);
		RStext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1,
				1));
		RStext.setText("");

		Button btnNewButton = new Button(searchComposite, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				true, 1, 1));
		btnNewButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// search
				status.setText("Calculating..........");
				if (data == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please import source data first!");
				} else if (RStext.getText().isEmpty())
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Key word is empty!");
				} else
				{
					// construct five composites
					sashForm.dispose();

					sashForm = new SashForm(composite, SWT.NONE);
					sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
							true, true, 1, 1));

					SashForm sashForm_1 = new SashForm(sashForm, SWT.VERTICAL);

					Group grpLable1 = new Group(sashForm_1, SWT.NONE);
					grpLable1.setText("lable1");
					grpLable1.setLayout(new GridLayout(1, false));

					ScrolledComposite scrolledComposite = new ScrolledComposite(
							grpLable1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite.setLayoutData(new GridData(SWT.FILL,
							SWT.FILL, true, true, 1, 1));
					scrolledComposite.setExpandHorizontal(true);
					scrolledComposite.setExpandVertical(true);

					List list1 = new List(scrolledComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite.setContent(list1);
					scrolledComposite.setMinSize(list1.computeSize(SWT.DEFAULT,
							SWT.DEFAULT));

					Group grpLable2 = new Group(sashForm_1, SWT.NONE);
					grpLable2.setText("lable2");
					grpLable2.setLayout(new GridLayout(1, false));

					ScrolledComposite scrolledComposite_2 = new ScrolledComposite(
							grpLable2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite_2.setLayoutData(new GridData(SWT.FILL,
							SWT.FILL, true, true, 1, 1));
					scrolledComposite_2.setExpandHorizontal(true);
					scrolledComposite_2.setExpandVertical(true);

					List list2 = new List(scrolledComposite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite_2.setContent(list2);
					scrolledComposite_2.setMinSize(list2.computeSize(
							SWT.DEFAULT, SWT.DEFAULT));
					sashForm_1.setWeights(new int[]
					{ 223, 139 });

					SashForm sashForm_2 = new SashForm(sashForm, SWT.VERTICAL);

					Group grpLable3 = new Group(sashForm_2, SWT.NONE);
					grpLable3.setText("lable3");
					grpLable3.setLayout(new GridLayout(1, false));

					ScrolledComposite scrolledComposite_1 = new ScrolledComposite(
							grpLable3, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite_1.setLayoutData(new GridData(SWT.FILL,
							SWT.FILL, true, true, 1, 1));
					scrolledComposite_1.setExpandHorizontal(true);
					scrolledComposite_1.setExpandVertical(true);

					List list3 = new List(scrolledComposite_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite_1.setContent(list3);
					scrolledComposite_1.setMinSize(list3.computeSize(
							SWT.DEFAULT, SWT.DEFAULT));

					Group grpLable4 = new Group(sashForm_2, SWT.NONE);
					grpLable4.setText("lable4");
					grpLable4.setLayout(new GridLayout(1, false));

					ScrolledComposite scrolledComposite_3 = new ScrolledComposite(
							grpLable4, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite_3.setLayoutData(new GridData(SWT.FILL,
							SWT.FILL, true, true, 1, 1));
					scrolledComposite_3.setExpandHorizontal(true);
					scrolledComposite_3.setExpandVertical(true);

					List list4 = new List(scrolledComposite_3, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite_3.setContent(list4);
					scrolledComposite_3.setMinSize(list4.computeSize(
							SWT.DEFAULT, SWT.DEFAULT));

					Group grpLable5 = new Group(sashForm_2, SWT.NONE);
					grpLable5.setText("lable5");
					grpLable5.setLayout(new GridLayout(1, false));

					ScrolledComposite scrolledComposite_4 = new ScrolledComposite(
							grpLable5, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite_4.setLayoutData(new GridData(SWT.FILL,
							SWT.FILL, true, true, 1, 1));
					scrolledComposite_4.setExpandHorizontal(true);
					scrolledComposite_4.setExpandVertical(true);

					List list5 = new List(scrolledComposite_4, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					scrolledComposite_4.setContent(list5);
					scrolledComposite_4.setMinSize(list5.computeSize(
							SWT.DEFAULT, SWT.DEFAULT));
					sashForm_2.setWeights(new int[]
					{ 1, 1, 1 });
					sashForm.setWeights(new int[]
					{ 563, 172 });

					composite.layout();

					// search: fill the lists.
//有问题				for(String eName : data.getEntities().keySet())
//					{
//						if(data.getEntities().get(eName).containsName(RStext.getText()))
//						{
//							for(String otherName : data.getEntities().keySet())
//							{
//								if(eName!=otherName)
//							{
//									
//								}
//							}
//							
//							grpLable1.setText("Authors");
//							fillTheList(list1, "", RStext.getText(),
//									wtm.getWeightedMat("C-A"));
//							
//							grpLable2.setText("Conferences");
//							fillTheList(list2, "", RStext.getText(),
//									wtm.getWeightedMat("C-C"));
//							
//							grpLable3.setText("Terms");
//							fillTheList(list3, "", RStext.getText(),
//									wtm.getWeightedMat("C-T"));
//							
//							grpLable4.setText("Tags");
//							fillTheList(list4, "", RStext.getText(),
//									wtm.getWeightedMat("C-L"));
//							
//							break;
//						}
//					}
					sashForm.layout();
				}
				status.setText("");
			}
		});
		btnNewButton.setText("Search");

		final Button btnNewButton_1 = new Button(searchComposite, SWT.NONE);
		btnNewButton_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				true, 1, 1));
		btnNewButton_1.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// advanced search

				status.setText("Calculating..........");

				if (data == null)
				{
					MessageDialog.openError(shlHetesimdemo, "ERROR",
							"Please import source data first!");
				} else
				{
					int x = btnNewButton_1.getLocation().x
							+ shlHetesimdemo.getLocation().x;
					int y = btnNewButton_1.getLocation().y
							+ shlHetesimdemo.getLocation().y + 140;
					AdvancedSearch as = new AdvancedSearch(shlHetesimdemo,
							SWT.BORDER | SWT.RESIZE, x, y);
					String hetePath = as.open();

					if (hetePath.isEmpty() || !preCal.isPathRight(hetePath))
					{
						MessageDialog.openError(shlHetesimdemo, "ERROR",
								"Please input correct HeteSim Path!");
					} else if (RStext.getText().isEmpty()
							|| data.getInstanceIndex(hetePath.split(",")[0],
									RStext.getText()) == -1)
					{
						MessageDialog.openError(shlHetesimdemo, "ERROR",
								"Key word is empty or have no result!");
					} else
					{
						// 没有初级错误

						sashForm.dispose();

						sashForm = new SashForm(composite, SWT.NONE);
						sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
								true, true, 1, 1));

						SashForm sashForm_1 = new SashForm(sashForm,
								SWT.VERTICAL);

						Group grpLable1 = new Group(sashForm_1, SWT.NONE);
						grpLable1.setText("lable1");
						grpLable1.setLayout(new GridLayout(1, false));

						ScrolledComposite scrolledComposite = new ScrolledComposite(
								grpLable1, SWT.BORDER | SWT.H_SCROLL
										| SWT.V_SCROLL);
						scrolledComposite.setLayoutData(new GridData(SWT.FILL,
								SWT.FILL, true, true, 1, 1));
						scrolledComposite.setExpandHorizontal(true);
						scrolledComposite.setExpandVertical(true);

						List list1 = new List(scrolledComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
						scrolledComposite.setContent(list1);
						scrolledComposite.setMinSize(list1.computeSize(
								SWT.DEFAULT, SWT.DEFAULT));

						composite.layout();
						// fill the list
						TransitiveMatrix posMat = null;
						if (qhs != null)
						{
							if (qhs.containsTransitiveMatrix(hetePath))
							{
								posMat = qhs.getTransitiveMatrix(hetePath);
							} else
							{
								CalHeteSim calHete = new CalHeteSim(data,
										hetePath);
								posMat = calHete.getHeteSim(RStext.getText());
							}
						} else
						{
							CalHeteSim calHete = new CalHeteSim(data, hetePath);
							posMat = calHete.getHeteSim(RStext.getText());
						}
						grpLable1.setText(hetePath);
						fillTheList(list1, "", RStext.getText(), posMat);

						sashForm.layout();
					}
				}

				status.setText("");
			}
		});
		btnNewButton_1.setText("Semantic Search");
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<String> outputFirstKResult(
			TransitiveMatrix posMat, String keyword, int k)
	{
		// 排序, 输出前k个(注意不足k个的情况)
		@SuppressWarnings("rawtypes")
		Comparator c = new Comparator()
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				TransNode n1 = (TransNode) o1;
				TransNode n2 = (TransNode) o2;
				if (n1.getData() > n2.getData())
					return -1;
				else if (n1.getData() < n2.getData())
					return 1;
				else
					return 0;
			}
		};

		ArrayList<String> result = new ArrayList<String>();
		if (posMat == null)
			System.out.println("posMat = null");
		else
			System.out.println(posMat.getMatrixName());

		String tmp[] = posMat.getMatrixName().split("-");
		String resultType = tmp[1];
		String srcType = tmp[0];
		int srcIndex = data.getEntities().get(srcType).getIndex(keyword);

		if (!posMat.getRows().containsKey(srcIndex))// bug proof
		{
			return result;
		}

		LinkedList<TransNode> row = posMat.getRows().get(srcIndex);
		Collections.sort(row, c);
		int maxIndex = Math.min(row.size(), k);
		for (int i = 0; i < maxIndex; i++)
		{
			result.add(data.getEntities().get(resultType)
					.getName(row.get(i).getColIndex()));
		}
		return result;
	}

	private static ArrayList<String> outputFirstKResultWithValue(
			TransitiveMatrix posMat, String keyword, int k)
	{
		// 排序, 输出前k个(注意不足k个的情况)
		Comparator<TransNode> c = new Comparator<TransNode>()
		{
			@Override
			public int compare(TransNode o1, TransNode o2)
			{
				TransNode n1 = (TransNode) o1;
				TransNode n2 = (TransNode) o2;
				if (n1.getData() > n2.getData())
					return -1;
				else if (n1.getData() < n2.getData())
					return 1;
				else
					return 0;
			}
		};

		ArrayList<String> result = new ArrayList<String>();
		String tmp[] = posMat.getMatrixName().split("-");
		String resultType = tmp[1];
		String srcType = tmp[0];
		int srcIndex = data.getEntities().get(srcType).getIndex(keyword);

		if (!posMat.getRows().containsKey(srcIndex))// bug proof
		{
			return result;
		}

		LinkedList<TransNode> row = posMat.getRows().get(srcIndex);

		Collections.sort(row, c);
		int maxIndex = Math.min(row.size(), k);
		for (int i = 0; i < maxIndex; i++)
		{
			result.add(data.getEntities().get(resultType)
					.getName(row.get(i).getColIndex())
					+ "@"
					+ Math.floor(row.get(i).getData() * 10000 + .5)
					/ 10000);
		}
		return result;
	}

	private static void fillTheList(List list, String listName, String keyWord,
			TransitiveMatrix posMat)
	{
		list.removeAll();

		if(!listName.isEmpty())
			list.add(listName);

		int maxNum = Integer.MAX_VALUE;

		ArrayList<String> results = outputFirstKResultWithValue(posMat, keyWord, maxNum);
		int i = 1;
		for (String result : results)
		{
			list.add(String.valueOf(i) + ": " + result);
			i++;
		}
	}

	private static StringBuffer getProfileItem(String itemName,
			ArrayList<String> items)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(itemName);
		sb.append(":\r\n");

		StringBuffer item = new StringBuffer();
		for (String tmp : items)
		{
			item.append("\t");
			item.append(tmp);
			item.append("\r\n");
		}
		item.setLength(item.length() - 2);

		sb.append(item);

		if (items.size() > 1)
			sb.append("...\r\n\r\n");
		else
			sb.append("\r\n\r\n");

		return sb;
	}

	/**
	 * 把窗口放中间显示
	 * 
	 * @param shell
	 */
	private static void centerShell(Shell shell)
	{
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int shellHeight = shell.getBounds().height;
		int shellWidth = shell.getBounds().width;
		if (shellHeight > screenHeight)
			shellHeight = screenHeight;
		if (shellWidth > screenWidth)
			shellWidth = screenWidth;
		shell.setLocation(((screenWidth - shellWidth) / 2),
				((screenHeight - shellHeight) / 2));
	}
}
