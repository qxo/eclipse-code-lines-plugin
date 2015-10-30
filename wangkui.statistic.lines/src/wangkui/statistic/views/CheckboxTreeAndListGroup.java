// FrontEnd Plus GUI for JAD
// DeCompiled : CheckboxTreeAndListGroup.class

package wangkui.statistic.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

public class CheckboxTreeAndListGroup
    implements ICheckStateListener, ISelectionChangedListener, ITreeViewerListener
{

    private Object fRoot;
    private Object fCurrentTreeSelection;
    private List fExpandedTreeNodes;
    private Map fCheckedStateStore;
    private List fWhiteCheckedTreeItems;
    private List fListeners;
    private ITreeContentProvider fTreeContentProvider;
    private IStructuredContentProvider fListContentProvider;
    private ILabelProvider fTreeLabelProvider;
    private ILabelProvider fListLabelProvider;
    private Composite composite;
    private CheckboxTreeViewer fTreeViewer;
    private CheckboxTableViewer fListViewer;

    public CheckboxTreeAndListGroup(Composite parent, Object rootObject, ITreeContentProvider treeContentProvider, ILabelProvider treeLabelProvider, IStructuredContentProvider listContentProvider, ILabelProvider listLabelProvider, int style, 
            int width, int height)
    {
        fExpandedTreeNodes = new ArrayList();
        fCheckedStateStore = new HashMap(9);
        fWhiteCheckedTreeItems = new ArrayList();
        fListeners = new ArrayList();
        fRoot = rootObject;
        fTreeContentProvider = treeContentProvider;
        fListContentProvider = listContentProvider;
        fTreeLabelProvider = treeLabelProvider;
        fListLabelProvider = listLabelProvider;
        createContents(parent, width, height, style);
    }

    public void addCheckStateListener(ICheckStateListener listener)
    {
        fListeners.add(listener);
    }

    private void addToHierarchyToCheckedStore(Object treeElement)
    {
        if(!fCheckedStateStore.containsKey(treeElement))
            fCheckedStateStore.put(treeElement, new ArrayList());
        Object parent = fTreeContentProvider.getParent(treeElement);
        if(parent != null)
            addToHierarchyToCheckedStore(parent);
    }

    protected boolean areAllChildrenWhiteChecked(Object treeElement)
    {
        Object children[] = getTreeChildren(treeElement);
        for(int i = 0; i < children.length; i++)
            if(!fWhiteCheckedTreeItems.contains(children[i]))
                return false;

        return true;
    }

    protected boolean areAllElementsChecked(Object treeElement)
    {
        List checkedElements = (List)fCheckedStateStore.get(treeElement);
        if(checkedElements == null)
            return false;
        return getListItemsSize(treeElement) == checkedElements.size();
    }

    protected void checkNewTreeElements(Object elements[])
    {
        for(int i = 0; i < elements.length; i++)
        {
            Object currentElement = elements[i];
            boolean checked = fCheckedStateStore.containsKey(currentElement);
            fTreeViewer.setChecked(currentElement, checked);
            fTreeViewer.setGrayed(currentElement, checked && !fWhiteCheckedTreeItems.contains(currentElement));
        }

    }

    public void checkStateChanged(final CheckStateChangedEvent event)
    {
        BusyIndicator.showWhile(fTreeViewer.getControl().getDisplay(), new Runnable() {

            public void run()
            {
                if(event.getCheckable().equals(fTreeViewer))
                    treeItemChecked(event.getElement(), event.getChecked());
                else
                    listItemChecked(event.getElement(), event.getChecked(), true);
                notifyCheckStateChangeListeners(event);
            }

        }
);
    }

    protected void createContents(Composite parent, int width, int height, int style)
    {
        composite = new Composite(parent, style);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = true;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(1808));
        SashForm sash = new SashForm(composite, 256);
        GridData data = new GridData(1808);
        data.horizontalSpan = 2;
        sash.setLayoutData(data);
        createTreeViewer(sash, width / 2, height);
        createListViewer(sash, width / 2, height);
        initialize();
    }

    protected void createListViewer(Composite parent, int width, int height)
    {
        fListViewer = CheckboxTableViewer.newCheckList(parent, 0);
        fListViewer.setUseHashlookup(true);
        GridData data = new GridData(1808);
        data.widthHint = width;
        data.heightHint = height;
        fListViewer.getTable().setLayoutData(data);
        fListViewer.setContentProvider(fListContentProvider);
        fListViewer.setLabelProvider(fListLabelProvider);
        fListViewer.addCheckStateListener(this);
    }

    protected void createTreeViewer(Composite parent, int width, int height)
    {
        Tree tree = new Tree(parent, 32);
        GridData data = new GridData(1808);
        data.widthHint = width;
        data.heightHint = height;
        tree.setLayoutData(data);
        fTreeViewer = new CheckboxTreeViewer(tree);
        fTreeViewer.setUseHashlookup(true);
        fTreeViewer.setContentProvider(fTreeContentProvider);
        fTreeViewer.setLabelProvider(fTreeLabelProvider);
        fTreeViewer.addTreeListener(this);
        fTreeViewer.addCheckStateListener(this);
        fTreeViewer.addSelectionChangedListener(this);
    }

    protected boolean determineShouldBeAtLeastGrayChecked(Object treeElement)
    {
        List checked = (List)fCheckedStateStore.get(treeElement);
        if(checked != null && !checked.isEmpty())
            return true;
        Object children[] = getTreeChildren(treeElement);
        for(int i = 0; i < children.length; i++)
            if(fCheckedStateStore.containsKey(children[i]))
                return true;

        return false;
    }

    protected boolean determineShouldBeWhiteChecked(Object treeElement)
    {
        return areAllChildrenWhiteChecked(treeElement) && areAllElementsChecked(treeElement);
    }

    protected void determineWhiteCheckedDescendents(Object treeElement)
    {
        Object children[] = getTreeChildren(treeElement);
        for(int i = 0; i < children.length; i++)
            determineWhiteCheckedDescendents(children[i]);

        if(determineShouldBeWhiteChecked(treeElement))
            setWhiteChecked(treeElement, true);
    }

    public void expandAll()
    {
        fTreeViewer.expandAll();
    }

    public void collapseAll()
    {
        fTreeViewer.collapseAll();
    }

    public Iterator getAllCheckedListItems()
    {
        Set result = new HashSet();
        for(Iterator listCollectionsEnum = fCheckedStateStore.values().iterator(); listCollectionsEnum.hasNext(); result.addAll((List)listCollectionsEnum.next()));
        return result.iterator();
    }

    public Set getAllCheckedTreeItems()
    {
        return new HashSet(fCheckedStateStore.keySet());
    }

    public int getCheckedElementCount()
    {
        return fCheckedStateStore.size();
    }

    protected int getListItemsSize(Object treeElement)
    {
        Object elements[] = getListElements(treeElement);
        return elements.length;
    }

    public Table getTable()
    {
        return fListViewer.getTable();
    }

    public Tree getTree()
    {
        return fTreeViewer.getTree();
    }

    public CheckboxTableViewer getTableViewer()
    {
        return fListViewer;
    }

    public CheckboxTreeViewer getTreeViewer()
    {
        return fTreeViewer;
    }

    public void addTreeFilter(ViewerFilter filter)
    {
        fTreeViewer.addFilter(filter);
    }

    public void addListFilter(ViewerFilter filter)
    {
        fListViewer.addFilter(filter);
    }

    protected void grayCheckHierarchy(Object treeElement)
    {
        if(fCheckedStateStore.containsKey(treeElement))
            return;
        fCheckedStateStore.put(treeElement, new ArrayList());
        if(determineShouldBeWhiteChecked(treeElement))
            setWhiteChecked(treeElement, true);
        Object parent = fTreeContentProvider.getParent(treeElement);
        if(parent != null)
            grayCheckHierarchy(parent);
    }

    public void initialCheckListItem(Object element)
    {
        Object parent = fTreeContentProvider.getParent(element);
        fCurrentTreeSelection = parent;
        listItemChecked(element, true, false);
        updateHierarchy(parent);
    }

    public void initialCheckTreeItem(Object element)
    {
        treeItemChecked(element, true);
    }

    protected void initialize()
    {
        fTreeViewer.setInput(fRoot);
    }

    protected void listItemChecked(Object listElement, boolean state, boolean updatingFromSelection)
    {
        List checkedListItems = (List)fCheckedStateStore.get(fCurrentTreeSelection);
        if(state)
        {
            if(checkedListItems == null)
            {
                grayCheckHierarchy(fCurrentTreeSelection);
                checkedListItems = (List)fCheckedStateStore.get(fCurrentTreeSelection);
            }
            checkedListItems.add(listElement);
        } else
        {
            checkedListItems.remove(listElement);
            if(checkedListItems.isEmpty())
                ungrayCheckHierarchy(fCurrentTreeSelection);
        }
        if(updatingFromSelection)
            updateHierarchy(fCurrentTreeSelection);
    }

    protected void notifyCheckStateChangeListeners(CheckStateChangedEvent event)
    {
        for(Iterator listenersEnum = fListeners.iterator(); listenersEnum.hasNext(); ((ICheckStateListener)listenersEnum.next()).checkStateChanged(event));
    }

    protected void populateListViewer(Object treeElement)
    {
        if(treeElement == fCurrentTreeSelection)
            return;
        fCurrentTreeSelection = treeElement;
        fListViewer.setInput(treeElement);
        List listItemsToCheck = (List)fCheckedStateStore.get(treeElement);
        if(listItemsToCheck != null)
        {
            for(Iterator listItemsEnum = listItemsToCheck.iterator(); listItemsEnum.hasNext(); fListViewer.setChecked(listItemsEnum.next(), true));
        }
    }

    public void removeCheckStateListener(ICheckStateListener listener)
    {
        fListeners.remove(listener);
    }

    public void selectionChanged(final SelectionChangedEvent event)
    {
        BusyIndicator.showWhile(getTable().getShell().getDisplay(), new Runnable() {

            public void run()
            {
                IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                Object selectedElement = selection.getFirstElement();
                if(selectedElement == null)
                {
                    fCurrentTreeSelection = null;
                    fListViewer.setInput(fCurrentTreeSelection);
                    return;
                } else
                {
                    populateListViewer(selectedElement);
                    return;
                }
            }

        }
);
    }

    public void setAllSelections(final boolean selection)
    {
        BusyIndicator.showWhile(fTreeViewer.getControl().getDisplay(), new Runnable() {

            public void run()
            {
                setTreeChecked(fRoot, selection);
                fListViewer.setAllChecked(selection);
            }

        }
);
    }

    public void setListProviders(IStructuredContentProvider contentProvider, ILabelProvider labelProvider)
    {
        fListViewer.setContentProvider(contentProvider);
        fListViewer.setLabelProvider(labelProvider);
    }

    public void setListSorter(ViewerSorter sorter)
    {
        fListViewer.setSorter(sorter);
    }

    public void setRoot(Object newRoot)
    {
        fRoot = newRoot;
        initialize();
    }

    public Object getRoot()
    {
        return fRoot;
    }

    protected void setTreeChecked(Object treeElement, boolean state)
    {
        if(treeElement.equals(fCurrentTreeSelection))
            fListViewer.setAllChecked(state);
        if(state)
        {
            Object listItems[] = getListElements(treeElement);
            List listItemsChecked = new ArrayList();
            for(int i = 0; i < listItems.length; i++)
                listItemsChecked.add(listItems[i]);

            fCheckedStateStore.put(treeElement, listItemsChecked);
        } else
        {
            fCheckedStateStore.remove(treeElement);
        }
        setWhiteChecked(treeElement, state);
        fTreeViewer.setChecked(treeElement, state);
        fTreeViewer.setGrayed(treeElement, false);
        Object children[] = getTreeChildren(treeElement);
        for(int i = 0; i < children.length; i++)
            setTreeChecked(children[i], state);

    }

    public void setTreeProviders(ITreeContentProvider contentProvider, ILabelProvider labelProvider)
    {
        fTreeViewer.setContentProvider(contentProvider);
        fTreeViewer.setLabelProvider(labelProvider);
    }

    public void setTreeSorter(ViewerSorter sorter)
    {
        fTreeViewer.setSorter(sorter);
    }

    protected void setWhiteChecked(Object treeElement, boolean isWhiteChecked)
    {
        if(isWhiteChecked)
        {
            if(!fWhiteCheckedTreeItems.contains(treeElement))
                fWhiteCheckedTreeItems.add(treeElement);
        } else
        {
            fWhiteCheckedTreeItems.remove(treeElement);
        }
    }

    public void treeCollapsed(TreeExpansionEvent treeexpansionevent)
    {
    }

    public void treeExpanded(TreeExpansionEvent event)
    {
        Object item = event.getElement();
        if(!fExpandedTreeNodes.contains(item))
        {
            fExpandedTreeNodes.add(item);
            checkNewTreeElements(getTreeChildren(item));
        }
    }

    protected void treeItemChecked(Object treeElement, boolean state)
    {
        setTreeChecked(treeElement, state);
        Object parent = fTreeContentProvider.getParent(treeElement);
        if(parent == null)
            return;
        if(state)
            grayCheckHierarchy(parent);
        else
            ungrayCheckHierarchy(parent);
        updateHierarchy(treeElement);
    }

    protected void ungrayCheckHierarchy(Object treeElement)
    {
        if(!determineShouldBeAtLeastGrayChecked(treeElement))
            fCheckedStateStore.remove(treeElement);
        Object parent = fTreeContentProvider.getParent(treeElement);
        if(parent != null)
            ungrayCheckHierarchy(parent);
    }

    protected void updateHierarchy(Object treeElement)
    {
        boolean whiteChecked = determineShouldBeWhiteChecked(treeElement);
        boolean shouldBeAtLeastGray = determineShouldBeAtLeastGrayChecked(treeElement);
        fTreeViewer.setChecked(treeElement, shouldBeAtLeastGray);
        setWhiteChecked(treeElement, whiteChecked);
        if(whiteChecked)
            fTreeViewer.setGrayed(treeElement, false);
        else
            fTreeViewer.setGrayed(treeElement, shouldBeAtLeastGray);
        Object parent = fTreeContentProvider.getParent(treeElement);
        if(parent != null)
            updateHierarchy(parent);
    }

    public void updateSelections(final Map items)
    {
        BusyIndicator.showWhile(fTreeViewer.getControl().getDisplay(), new Runnable() {

            public void run()
            {
                handleUpdateSelection(items);
            }

        }
);
    }

    protected Object[] filter(ViewerFilter filters[], Object elements[])
    {
        if(filters != null)
        {
            ArrayList filtered = new ArrayList(elements.length);
            for(int i = 0; i < elements.length; i++)
            {
                boolean add = true;
                for(int j = 0; j < filters.length; j++)
                {
                    add = filters[j].select(null, null, elements[i]);
                    if(!add)
                        break;
                }

                if(add)
                    filtered.add(elements[i]);
            }

            return filtered.toArray();
        } else
        {
            return elements;
        }
    }

    private Object[] getTreeChildren(Object element)
    {
        return filter(fTreeViewer.getFilters(), fTreeContentProvider.getChildren(element));
    }

    private Object[] getListElements(Object element)
    {
        return filter(fListViewer.getFilters(), fListContentProvider.getElements(element));
    }

    public Set getWhiteCheckedTreeItems()
    {
        return new HashSet(fWhiteCheckedTreeItems);
    }

    private void handleUpdateSelection(Map items)
    {
        for(Iterator keyIterator = items.keySet().iterator(); keyIterator.hasNext();)
        {
            Object key = keyIterator.next();
            List selections = (List)items.get(key);
            if(selections.size() == 0)
            {
                fCheckedStateStore.remove(key);
            } else
            {
                fCheckedStateStore.put(key, selections);
                Object parent = fTreeContentProvider.getParent(key);
                if(parent != null)
                    addToHierarchyToCheckedStore(parent);
            }
        }

        for(Iterator keyIterator = items.keySet().iterator(); keyIterator.hasNext();)
        {
            Object key = keyIterator.next();
            updateHierarchy(key);
            if(fCurrentTreeSelection != null && fCurrentTreeSelection.equals(key))
            {
                fListViewer.setAllChecked(false);
                fListViewer.setCheckedElements(((List)items.get(key)).toArray());
            }
        }

    }

    public boolean isTreeItemGreyChecked(Object object)
    {
        return fTreeViewer.getGrayed(object);
    }

    public void expandTreeToLevel(Object object, int level)
    {
        fTreeViewer.expandToLevel(object, level);
    }

    public Composite getComposite()
    {
        return composite;
    }






}
