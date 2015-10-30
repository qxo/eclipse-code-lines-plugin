// FrontEnd Plus GUI for JAD
// DeCompiled : LinesView.class

package wangkui.statistic.views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.filters.EmptyInnerPackageFilter;
import org.eclipse.jdt.internal.ui.viewsupport.LibraryFilter;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.JavaElementSorter;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

import wangkui.statistic.Policy;
import wangkui.statistic.StatisticImages;
import wangkui.statistic.StatisticPlugin;
import wangkui.statistic.model.CountUnit;
import wangkui.statistic.model.CountUnitElement;
import wangkui.statistic.model.JavaCountUnit;
import wangkui.statistic.model.OtherCountUnit;
import wangkui.statistic.model.ProfileCountUnit;
import wangkui.statistic.model.RootElement;

// Referenced classes of package wangkui.statistic.views:
//            Splitter, ViewerPane, CheckboxTreeAndListGroup, ContainerFilter

public class LinesView extends ViewPart
    implements IResourceChangeListener
{
    class TableViewerLabelProvider extends WorkbenchLabelProvider
        implements ITableLabelProvider
    {

        public Image getColumnImage(Object element, int columnIndex)
        {
            if(columnIndex == 0)
                return super.getImage(element);
            else
                return null;
        }

        public String getColumnText(Object element, int columnIndex)
        {
            switch(columnIndex)
            {
            case 0: // '\0'
                return super.getText(element);

            case 1: // '\001'
                String str0 = ((CountUnitElement)element).getCountUnit().getExtension();
                return str0;

            case 2: // '\002'
                String str1 = ((CountUnitElement)element).getCountUnit().getPath();
                return str1;

            case 3: // '\003'
                int numJD = ((CountUnitElement)element).getCountUnit().getJD();
                return (new Integer(numJD)).toString();

            case 4: // '\004'
                int numMM = ((CountUnitElement)element).getCountUnit().getMM();
                return (new Integer(numMM)).toString();

            case 5: // '\005'
                int numSM = ((CountUnitElement)element).getCountUnit().getSM();
                return (new Integer(numSM)).toString();

            case 6: // '\006'
                int numTM = ((CountUnitElement)element).getCountUnit().getTM();
                return (new Integer(numTM)).toString();

            case 7: // '\007'
                int numTC = ((CountUnitElement)element).getCountUnit().getTC();
                return (new Integer(numTC)).toString();

            case 8: // '\b'
                int numTN = ((CountUnitElement)element).getCountUnit().getTN();
                return (new Integer(numTN)).toString();

            case 9: // '\t'
                int numTB = ((CountUnitElement)element).getCountUnit().getTB();
                return (new Integer(numTB)).toString();

            case 10: // '\n'
                int numTL = ((CountUnitElement)element).getCountUnit().getTL();
                return (new Integer(numTL)).toString();
            }
            return "";
        }

        TableViewerLabelProvider()
        {
        }
    }

    class TableViewerSorter extends ViewerSorter
    {

        private boolean reversed;
        private int columnNumber;
        public static final int NAME = 0;
        public static final int EXTENSION = 1;
        public static final int FOLDER = 2;
        public static final int JD = 3;
        public static final int MM = 4;
        public static final int SM = 5;
        public static final int TM = 6;
        public static final int TC = 7;
        public static final int TN = 8;
        public static final int TB = 9;
        public static final int TL = 10;
        private int SORT_ORDERS_BY_COLUMN[][] = {
            {
                0, 2
            }, {
                1, 0, 2
            }, {
                2, 0
            }, {
                3, 0, 2
            }, {
                4, 0, 2
            }, {
                5, 0, 2
            }, {
                6, 0, 2
            }, {
                7, 0, 2
            }, {
                8, 0, 2
            }, {
                9, 0, 2
            }, {
                10, 0, 2
            }
        };

        public int compare(Viewer viewer, Object o1, Object o2)
        {
            CountUnit u1 = ((CountUnitElement)o1).getCountUnit();
            CountUnit u2 = ((CountUnitElement)o2).getCountUnit();
            int columnSortOrder[] = SORT_ORDERS_BY_COLUMN[columnNumber];
            int result = 0;
            for(int i = 0; i < columnSortOrder.length; i++)
            {
                result = compareColumnValue(columnSortOrder[i], u1, u2);
                if(result != 0)
                    break;
            }

            if(reversed)
                result = -result;
            return result;
        }

        int compareColumnValue(int columnSortOrder, CountUnit u1, CountUnit u2)
        {
            switch(columnSortOrder)
            {
            case 0: // '\0'
                return compareNames(u1, u2);

            case 1: // '\001'
                return compareExtensions(u1, u2);

            case 2: // '\002'
                return compareFolders(u1, u2);

            case 3: // '\003'
                return compareLines(u1.getJD(), u2.getJD());

            case 4: // '\004'
                return compareLines(u1.getMM(), u2.getMM());

            case 5: // '\005'
                return compareLines(u1.getSM(), u2.getSM());

            case 6: // '\006'
                return compareLines(u1.getTM(), u2.getTM());

            case 7: // '\007'
                return compareLines(u1.getTC(), u2.getTC());

            case 8: // '\b'
                return compareLines(u1.getTN(), u2.getTN());

            case 9: // '\t'
                return compareLines(u1.getTB(), u2.getTB());

            case 10: // '\n'
                return compareLines(u1.getTL(), u2.getTL());
            }
            return 0;
        }

        protected int compareNames(CountUnit u1, CountUnit u2)
        {
            return u1.getName().compareTo(u2.getName());
        }

        protected int compareFolders(CountUnit u1, CountUnit u2)
        {
            return u1.getPath().compareTo(u2.getPath());
        }

        protected int compareExtensions(CountUnit u1, CountUnit u2)
        {
            return u1.getExtension().compareTo(u2.getExtension());
        }

        protected int compareLines(int lines1, int lines2)
        {
            return (new Integer(lines1)).compareTo(new Integer(lines2));
        }

        public int getColumnNumber()
        {
            return columnNumber;
        }

        public boolean isReversed()
        {
            return reversed;
        }

        public void setReversed(boolean newReversed)
        {
            reversed = newReversed;
        }

        public TableViewerSorter(int columnNumber)
        {
            reversed = false;
            this.columnNumber = columnNumber;
        }
    }

    private class Mode1Action extends Action
    {

        public void run()
        {
            setModeButtons(1);
            if(getMode() == 1)
                return;
            Control parent = treePane.getParent();
            if((parent instanceof Splitter) && ((Splitter)parent).getMaximizedControl() != treePane)
                ((Splitter)parent).setMaximizedControl(treePane);
            setMode(1);
        }

        public Mode1Action()
        {
            setText(Policy.bind("Mode1Action.name"));
            setToolTipText(Policy.bind("Mode1Action.tooltip"));
            setImageDescriptor(StatisticImages.getImageDescriptor("elcl16/mode1.gif"));
            setHoverImageDescriptor(StatisticImages.getImageDescriptor("elcl16/mode1.gif"));
        }
    }

    private class Mode2Action extends Action
    {

        public void run()
        {
            setModeButtons(2);
            if(getMode() == 2)
                return;
            Control parent = treePane.getParent();
            if((parent instanceof Splitter) && ((Splitter)parent).getMaximizedControl() != tablePane)
                ((Splitter)parent).setMaximizedControl(tablePane);
            setMode(2);
        }

        public Mode2Action()
        {
            setText(Policy.bind("Mode2Action.name"));
            setToolTipText(Policy.bind("Mode2Action.tooltip"));
            setImageDescriptor(StatisticImages.getImageDescriptor("elcl16/mode2.gif"));
            setHoverImageDescriptor(StatisticImages.getImageDescriptor("elcl16/mode2.gif"));
        }
    }

    private class Mode3Action extends Action
    {

        public void run()
        {
            setModeButtons(3);
            if(getMode() == 3)
                return;
            Control parent = treePane.getParent();
            if((parent instanceof Splitter) && ((Splitter)parent).getMaximizedControl() != null)
            {
                if(((Splitter)parent).getMaximizedControl() == treePane)
                    ((Splitter)parent).setMaximizedControl(treePane);
                if(((Splitter)parent).getMaximizedControl() == tablePane)
                    ((Splitter)parent).setMaximizedControl(tablePane);
            }
            setMode(3);
        }

        public Mode3Action()
        {
            setText(Policy.bind("Mode3Action.name"));
            setToolTipText(Policy.bind("Mode3Action.tooltip"));
            setImageDescriptor(StatisticImages.getImageDescriptor("elcl16/mode3.gif"));
            setHoverImageDescriptor(StatisticImages.getImageDescriptor("elcl16/mode3.gif"));
        }
    }

    private class CountAction extends Action
    {

        public void run()
        {
            IRunnableWithProgress innerRunnable = new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor)
                    throws InvocationTargetException, InterruptedException
                {
                    if(monitor == null)
                        monitor = new NullProgressMonitor();
                    list.clear();
                    Object children[] = getNonContainers().toArray();
                    monitor.beginTask(null, children.length);
                    CountUnit units[] = new CountUnit[children.length];
                    int numFiles = 0;
                    int JD = 0;
                    int MM = 0;
                    int SM = 0;
                    int TM = 0;
                    int TC = 0;
                    int TN = 0;
                    int TB = 0;
                    int TL = 0;
                    for(int i = 0; i < children.length; i++)
                        if(children[i] instanceof IFile)
                        {
                            String name = ((IFile)children[i]).getName();
                            String extension = name.substring(name.lastIndexOf(".") + 1);
                            if(extension.equalsIgnoreCase("java"))
                                units[i] = new JavaCountUnit((IFile)children[i]);
                            else
                            if(extension.equalsIgnoreCase("properties"))
                                units[i] = new ProfileCountUnit((IFile)children[i]);
                            else
                                units[i] = new OtherCountUnit((IFile)children[i]);
                            list.add(units[i]);
                            numFiles++;
                            JD += units[i].getJD();
                            MM += units[i].getMM();
                            SM += units[i].getSM();
                            TM += units[i].getTM();
                            TC += units[i].getTC();
                            TN += units[i].getTN();
                            TB += units[i].getTB();
                            TL += units[i].getTL();
                            monitor.worked(1);
                        }

                    String strJD = (new Integer(JD)).toString();
                    String strMM = (new Integer(MM)).toString();
                    String strSM = (new Integer(SM)).toString();
                    String strTM = (new Integer(TM)).toString();
                    String strTC = (new Integer(TC)).toString();
                    String strTN = (new Integer(TN)).toString();
                    String strTB = (new Integer(TB)).toString();
                    String strTL = (new Integer(TL)).toString();
                    String strFiles = (new Integer(numFiles)).toString();
                    tablePane.setText(Policy.bind("TablePane.name2", strFiles));
                    viewer.refresh();
                    statusJD.setText(strJD);
                    statusMM.setText(strMM);
                    statusSM.setText(strSM);
                    statusTM.setText(strTM);
                    statusTC.setText(strTC);
                    statusTN.setText(strTN);
                    statusTB.setText(strTB);
                    statusTL.setText(strTL);
                    monitor.done();
                }

            }
;
            try
            {
                (new ProgressMonitorDialog(table.getShell())).run(false, true, innerRunnable);
            }
            catch(InvocationTargetException e)
            {
                MessageDialog.openError(table.getShell(), Policy.bind("Error.failureTitle", Policy.bind("DiffBackupAction.name")), e.toString());
                return;
            }
            catch(InterruptedException e)
            {
                MessageDialog.openError(table.getShell(), Policy.bind("Error.failureTitle", Policy.bind("DiffBackupAction.name")), e.toString());
                return;
            }
        }


        public CountAction()
        {
            setText(Policy.bind("CountAction.name"));
            setToolTipText(Policy.bind("CountAction.tooltip"));
            setImageDescriptor(StatisticImages.getImageDescriptor("elcl16/count.gif"));
            setHoverImageDescriptor(StatisticImages.getImageDescriptor("elcl16/count.gif"));
        }
    }

    private class CollapseAllAction extends Action
    {

        public void run()
        {
            fInputGroup.collapseAll();
        }

        public CollapseAllAction()
        {
            setText(Policy.bind("CollapseAllAction.name"));
            setToolTipText(Policy.bind("CollapseAllAction.tooltip"));
            setImageDescriptor(StatisticImages.getImageDescriptor("elcl16/collapseall.gif"));
            setHoverImageDescriptor(StatisticImages.getImageDescriptor("elcl16/collapseall.gif"));
        }
    }

    private class RefreshTreePaneAction extends Action
    {

        public void run()
        {
            refreshTreePane();
        }

        public RefreshTreePaneAction()
        {
            setText(Policy.bind("RefreshTreePaneAction.name"));
            setToolTipText(Policy.bind("RefreshTreePaneAction.tooltip"));
            setImageDescriptor(StatisticImages.getImageDescriptor("elcl16/refresh.gif"));
            setHoverImageDescriptor(StatisticImages.getImageDescriptor("elcl16/refresh.gif"));
        }
    }

    private class OpenEditorAction extends Action
    {

        public void run()
        {
            IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
            CountUnitElement elements[] = new CountUnitElement[selection.toList().size()];
            selection.toList().toArray(elements);
            IFile files[] = new IFile[elements.length];
            for(int i = 0; i < elements.length; i++)
            {
                files[i] = elements[i].getCountUnit().getFile();
                OpenEditor(files[i]);
            }

        }

        public OpenEditorAction()
        {
            setText(Policy.bind("OpenEditorAction.name"));
            setToolTipText(Policy.bind("OpenEditorAction.tooltip"));
            setEnabled(false);
        }
    }

    
    
    private class HelpAction extends Action
    {

        public void run()
        {

            try {
               // final URL url = new URL("http://www.cppblog.com/images/cppblog_com/bobquain/lines-2.PNG");
              //  PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url);
                PlatformUI.getWorkbench().getHelpSystem().displayHelpResource("/wangkui.statistic.lines/help/toc.html#sample");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

        public HelpAction()
        {
            setText(Policy.bind("HelpAction.name"));
            setToolTipText(Policy.bind("HelpAction.tooltip"));
            //setEnabled(false);
            final ImageDescriptor icon = StatisticImages.getImageDescriptor("elcl16/help.gif");
            setImageDescriptor(icon);
            setHoverImageDescriptor(icon);
            

            PlatformUI.getWorkbench().getHelpSystem().setHelp(this,"sample"); 
            
           // setDisabledImageDescriptor(icon);
        }
    }
    
    private class ExportAction extends Action
    {

        public void run()
        {
            handleExport();
        }

        public ExportAction()
        {
            setText(Policy.bind("ExportAction.name"));
            setToolTipText(Policy.bind("ExportAction.tooltip"));
            setImageDescriptor(StatisticImages.getImageDescriptor("elcl16/export.gif"));
            setHoverImageDescriptor(StatisticImages.getImageDescriptor("elcl16/export.gif"));
            setDisabledImageDescriptor(StatisticImages.getImageDescriptor("dlcl16/export.gif"));
        }
    }

    private class ClearAction extends Action
    {

        public void run()
        {
            list.clear();
            tablePane.setText(Policy.bind("TablePane.name2", "0"));
            clearStatus();
            viewer.refresh();
        }

        public ClearAction()
        {
            setText(Policy.bind("ClearAction.name"));
            setToolTipText(Policy.bind("ClearAction.tooltip"));
            setImageDescriptor(StatisticImages.getImageDescriptor("elcl16/clear.gif"));
            setHoverImageDescriptor(StatisticImages.getImageDescriptor("elcl16/clear.gif"));
            setDisabledImageDescriptor(StatisticImages.getImageDescriptor("dlcl16/clear.gif"));
        }
    }


    public static final String VIEW_ID = "wangkui.statistic.views.Lines";
    private Splitter splitter;
    private CheckboxTreeAndListGroup fInputGroup;
    private ViewerPane treePane;
    private ViewerPane tablePane;
    private TableViewer viewer;
    private Table table;
    private Label statusJD;
    private Label statusMM;
    private Label statusSM;
    private Label statusTM;
    private Label statusTC;
    private Label statusTN;
    private Label statusTB;
    private Label statusTL;
    private RootElement root;
    private int currentMode;
    private Action mode1Action;
    private Action mode2Action;
    private Action mode3Action;
    private Action countAction;
    private Action collapseAllAction;
    private Action refreshTreePaneAction;
    private Action openEditorAction;
    private Action exportAction;
    private Action clearAction;
    public static final int MODE1 = 1;
    public static final int MODE2 = 2;
    public static final int MODE3 = 3;
    public static final int SIZING_SELECTION_WIDGET_WIDTH = 480;
    public static final int SIZING_SELECTION_WIDGET_HEIGHT = 150;
    public static final String EXTENSION_JAVA = "java";
    public static final String EXTENSION_PROPERTIES = "properties";
    public static final String EXTENSION_XML = "xml";
    private String directory;
    private LinkedList list;

    private SelectionListener getColumnListener()
    {
        return new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e)
            {
                int column = viewer.getTable().indexOf((TableColumn)e.widget);
                TableViewerSorter oldSorter = (TableViewerSorter)viewer.getSorter();
                if(oldSorter != null && column == oldSorter.getColumnNumber())
                {
                    oldSorter.setReversed(!oldSorter.isReversed());
                    viewer.refresh();
                } else
                {
                    viewer.setSorter(new TableViewerSorter(column));
                }
            }

        }
;
    }

    public LinesView()
    {
    }

    public void createPartControl(Composite parent)
    {
        Composite p1 = new Composite(parent, 0);
        GridData data = new GridData(1808);
        p1.setLayoutData(data);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        p1.setLayout(gridLayout);
        Splitter h = new Splitter(p1, 512);
        data = new GridData(1808);
        h.setLayoutData(data);
        createTreePane(h);
        createTablePane(h);
        makeActions();
        hookContextMenu();
        contributeToActionBars();
        contributeToTreePane();
        contributeToTablePane();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        initializeValues();
    }

    private void createTablePane(Splitter h)
    {
        tablePane = new ViewerPane(h, 0x800800);
        tablePane.setText(Policy.bind("TablePane.name1"));
        tablePane.setIdentify(2);
        Composite p2 = new Composite(tablePane, 0x20000);
        GridData data = new GridData(1808);
        p2.setLayoutData(data);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 17;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.verticalSpacing = 1;
        gridLayout.horizontalSpacing = 0;
        p2.setLayout(gridLayout);
        createTable(p2);
        viewer = new TableViewer(table);
        viewer.setContentProvider(new WorkbenchContentProvider());
        viewer.setLabelProvider(new TableViewerLabelProvider());
        TableViewerSorter sorter = new TableViewerSorter(2);
        sorter.setReversed(false);
        viewer.setSorter(sorter);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event)
            {
                LinesView.this.selectionChanged(event);
            }

        }
);
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event)
            {
                handleMouseDoubleClick(event);
            }

        }
);
        createStatusLabel(p2);
        tablePane.setContent(p2);
    }

    private void createStatusLabel(Composite p2)
    {
        Label label1 = createLabel(p2, "JD", 1);
        statusJD = createWhiteLabel(p2, 1);
        Label label2 = createLabel(p2, "MM", 1);
        statusMM = createWhiteLabel(p2, 1);
        Label label3 = createLabel(p2, "SM", 1);
        statusSM = createWhiteLabel(p2, 1);
        Label label4 = createLabel(p2, "TM", 1);
        statusTM = createWhiteLabel(p2, 1);
        Label label5 = createLabel(p2, "TC", 1);
        statusTC = createWhiteLabel(p2, 1);
        Label label6 = createLabel(p2, "TN", 1);
        statusTN = createWhiteLabel(p2, 1);
        Label label7 = createLabel(p2, "TB", 1);
        statusTB = createWhiteLabel(p2, 1);
        Label label8 = createLabel(p2, "TL", 1);
        statusTL = createWhiteLabel(p2, 1);
        Label space = createGrayLabel(p2, 1);
    }

    private void createTable(Composite composite)
    {
        table = new Table(composite, 0x10302);
        GridData data = new GridData(1808);
        data.horizontalSpan = 17;
        table.setLayoutData(data);
        TableLayout layout = new TableLayout();
        table.setLayout(layout);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableColumn column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.name"));
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(30, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.extension"));
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(15, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.folder"));
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(45, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.JD"));
        column.setAlignment(0x20000);
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(10, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.MM"));
        column.setAlignment(0x20000);
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(10, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.SM"));
        column.setAlignment(0x20000);
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(10, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.TM"));
        column.setAlignment(0x20000);
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(10, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.TC"));
        column.setAlignment(0x20000);
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(10, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.TN"));
        column.setAlignment(0x20000);
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(10, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.TB"));
        column.setAlignment(0x20000);
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(10, true));
        column = new TableColumn(table, 0);
        column.setText(Policy.bind("TableColumn.TL"));
        column.setAlignment(0x20000);
        column.addSelectionListener(getColumnListener());
        layout.addColumnData(new ColumnWeightData(10, true));
    }

    private void createTreePane(Splitter h)
    {
        treePane = new ViewerPane(h, 0x800800);
        treePane.setText(Policy.bind("TreePane.name"));
        treePane.setIdentify(1);
        createInputGroup(treePane);
        treePane.setContent(fInputGroup.getComposite());
    }

    void selectionChanged(SelectionChangedEvent event)
    {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        updateStatusMessage(selection);
        if(selection.isEmpty())
            openEditorAction.setEnabled(false);
        else
            openEditorAction.setEnabled(true);
    }

    String getStatusMessage(IStructuredSelection selection)
    {
        if(selection != null && selection.size() == 1)
        {
            CountUnit unit = ((CountUnitElement)selection.getFirstElement()).getCountUnit();
            return unit.getName() + " - " + unit.getPath();
        }
        if(selection != null && selection.size() > 1)
            return Policy.bind("StatusMessage.items", (new Integer(selection.size())).toString());
        else
            return "";
    }

    private void updateStatusMessage(IStructuredSelection selection)
    {
        String message = getStatusMessage(selection);
        getViewSite().getActionBars().getStatusLineManager().setMessage(message);
    }

    private void handleMouseDoubleClick(DoubleClickEvent event)
    {
        if(event.getSelection() != null)
        {
            CountUnitElement element = (CountUnitElement)((IStructuredSelection)event.getSelection()).getFirstElement();
            IFile file = element.getCountUnit().getFile();
            OpenEditor(file);
        }
    }

    private void OpenEditor(IFile file)
    {
        if(file == null)
            return;
        try
        {
            IWorkbenchPage page = getWorkbenchPage();
            if(page != null)
                IDE.openEditor(page, file, true);
        }
        catch(CoreException e)
        {
            String title = Policy.bind("OpenEditorAction.errorTitle");
            String message = Policy.bind("OpenEditorAction.errorMessage");
            ErrorDialog.openError(getViewSite().getShell(), title, message, e.getStatus());
        }
      
    }

    private void initializeValues()
    {
        setModeButtons(3);
        list = new LinkedList();
        root = new RootElement(list);
        viewer.setInput(root);
        clearStatus();
    }

    private void clearStatus()
    {
        statusJD.setText("0");
        statusMM.setText("0");
        statusSM.setText("0");
        statusTM.setText("0");
        statusTC.setText("0");
        statusTN.setText("0");
        statusTB.setText("0");
        statusTL.setText("0");
    }

    protected Label createLabel(Composite parent, String text, int span)
    {
        Label label = new Label(parent, 0x1000800);
        label.setText(text);
        GridData data = new GridData();
        data.horizontalSpan = span;
        data.widthHint = 18;
        data.horizontalAlignment = 4;
        label.setLayoutData(data);
        Color colorB = new Color(label.getDisplay(), new RGB(63, 95, 191));
        Color colorF = new Color(label.getDisplay(), new RGB(255, 255, 255));
        label.setBackground(colorB);
        label.setForeground(colorF);
        return label;
    }

    protected Label createWhiteLabel(Composite parent, int span)
    {
        Label label = new Label(parent, 0x20800);
        GridData data = new GridData();
        data.horizontalSpan = span;
        data.widthHint = 48;
        data.horizontalAlignment = 4;
        label.setLayoutData(data);
        return label;
    }

    protected Label createGrayLabel(Composite parent, int span)
    {
        Label label = new Label(parent, 0x20800);
        GridData data = new GridData();
        data.horizontalSpan = span;
        data.horizontalAlignment = 4;
        label.setLayoutData(data);
        return label;
    }

    private Set getNonContainers()
    {
        Set nonContainers = new HashSet();
        for(Iterator iter = fInputGroup.getAllCheckedListItems(); iter.hasNext();)
        {
            Object element = iter.next();
            if(element instanceof IFile)
                nonContainers.add(element);
            else
            if(element instanceof IJavaElement)
                nonContainers.add(((IJavaElement)element).getResource());
        }

        return nonContainers;
    }

    private void createInputGroup(Composite parent)
    {
        int labelFlags = 272;
        org.eclipse.jface.viewers.ITreeContentProvider treeContentProvider = new StandardJavaElementContentProvider() {

            public boolean hasChildren(Object element)
            {
                return !(element instanceof IPackageFragment) && super.hasChildren(element);
            }

        }
;
        fInputGroup = new CheckboxTreeAndListGroup(parent, JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()), treeContentProvider, new JavaElementLabelProvider(labelFlags), new StandardJavaElementContentProvider(), new JavaElementLabelProvider(labelFlags), 0x800000, 480, 150);
        fInputGroup.addTreeFilter(new EmptyInnerPackageFilter());
        fInputGroup.setTreeSorter(new JavaElementSorter());
        fInputGroup.setListSorter(new JavaElementSorter());
        fInputGroup.addTreeFilter(new ContainerFilter(ContainerFilter.FILTER_NON_CONTAINERS));
        fInputGroup.addTreeFilter(new LibraryFilter());
        fInputGroup.addListFilter(new ContainerFilter(ContainerFilter.FILTER_CONTAINERS));
    }

    private void hookContextMenu()
    {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager)
            {
                fillContextMenu(manager);
            }

        }
);
        org.eclipse.swt.widgets.Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    private void contributeToActionBars()
    {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void contributeToTreePane()
    {
        ToolBarManager manager = treePane.getToolBarManager();
        manager.removeAll();
        manager.add(countAction);
        manager.add(new Separator("separator1"));
        manager.add(refreshTreePaneAction);
        manager.add(collapseAllAction);
        manager.update(true);
    }

    private void contributeToTablePane()
    {
        ToolBarManager manager = ViewerPane.getToolBarManager(tablePane);
        manager.removeAll();
        manager.add(exportAction);
        manager.add(new Separator("separator1"));
        manager.add(clearAction);
        manager.update(true);
    }

    private void fillLocalPullDown(IMenuManager imenumanager)
    {
    }

    private void fillContextMenu(IMenuManager manager)
    {
        manager.add(openEditorAction);
        manager.add(new Separator("separator1"));
        manager.add(exportAction);
        manager.add(clearAction);
        manager.add(new Separator("additions"));
    }

    private void fillLocalToolBar(IToolBarManager manager)
    {
        manager.add(mode1Action);
        manager.add(mode2Action);
        manager.add(mode3Action);
        manager.add(new HelpAction());
        
        
        
    }

    public void setModeButtons(int mode)
    {
        switch(mode)
        {
        case 1: // '\001'
            mode1Action.setChecked(true);
            mode2Action.setChecked(false);
            mode3Action.setChecked(false);
            break;

        case 2: // '\002'
            mode1Action.setChecked(false);
            mode2Action.setChecked(true);
            mode3Action.setChecked(false);
            break;

        case 3: // '\003'
            mode1Action.setChecked(false);
            mode2Action.setChecked(false);
            mode3Action.setChecked(true);
            break;
        }
    }

    public void setMode(int mode)
    {
        currentMode = mode;
    }

    public int getMode()
    {
        return currentMode;
    }

    private void makeActions()
    {
        mode1Action = new Mode1Action();
        mode2Action = new Mode2Action();
        mode3Action = new Mode3Action();
        countAction = new CountAction();
        collapseAllAction = new CollapseAllAction();
        refreshTreePaneAction = new RefreshTreePaneAction();
        openEditorAction = new OpenEditorAction();
        exportAction = new ExportAction();
        clearAction = new ClearAction();
    }

    public void refreshTreePane()
    {
        fInputGroup.setAllSelections(false);
        fInputGroup.getTreeViewer().refresh();
        fInputGroup.getTableViewer().refresh();
    }

    private void handleExport()
    {
        FileDialog dialog = new FileDialog(getViewSite().getShell(), 8192);
        dialog.setFilterExtensions(new String[] {
            "*.csv"
        });
        if(directory != null)
            dialog.setFilterPath(directory);
        String path = dialog.open();
        if(path != null)
        {
            if(!path.endsWith(".csv"))
                path = path + ".csv";
            File outputFile = (new Path(path)).toFile();
            directory = outputFile.getParent();
            if(outputFile.exists())
            {
                String message = Policy.bind("QuestionMessage.confirmOverwrite", outputFile.toString());
                if(!MessageDialog.openQuestion(getViewSite().getShell(), exportAction.getText(), message))
                    return;
            }
            write(outputFile);
        }
    }

    private void write(File file)
    {
        TableItem items[] = new TableItem[table.getItemCount()];
        items = table.getItems();
        int column = table.getColumnCount();
        TableColumn columns[] = new TableColumn[column];
        columns = table.getColumns();
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String title = tablePane.getText();
            writer.write(title);
            writer.newLine();
            String header = "";
            for(int i = 0; i < column; i++)
                header = header + "," + columns[i].getText();

            writer.write(header.substring(1));
            writer.newLine();
            for(int i = 0; i < items.length; i++)
            {
                String line = "";
                for(int j = 0; j < column; j++)
                    line = line + "," + items[i].getText(j);

                writer.write(line.substring(1));
                writer.newLine();
            }

            String total = "Total,,," + statusJD.getText() + "," + statusMM.getText() + "," + statusSM.getText() + "," + statusTM.getText() + "," + statusTC.getText() + "," + statusTN.getText() + "," + statusTB.getText() + "," + statusTL.getText();
            writer.write(total);
            writer.newLine();
            writer.close();
        }
        catch(IOException e)
        {
            MessageDialog.openError(getViewSite().getShell(), Policy.bind("Error.failureTitle", Policy.bind("TransmitAction.name")), e.toString());
            return;
        }
    }

    public void resourceChanged(IResourceChangeEvent iresourcechangeevent)
    {
    }

    public void setFocus()
    {
        fInputGroup.getTree().setFocus();
    }

    private StatisticPlugin getPlugin()
    {
        return StatisticPlugin.getPlugin();
    }

    private IWorkbenchPage getWorkbenchPage()
    {
        return StatisticPlugin.getPlugin().getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }




















}
