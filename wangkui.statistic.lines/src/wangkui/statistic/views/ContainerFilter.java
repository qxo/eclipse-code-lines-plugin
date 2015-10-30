// FrontEnd Plus GUI for JAD
// DeCompiled : ContainerFilter.class

package wangkui.statistic.views;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ContainerFilter extends ViewerFilter
{

    private boolean fFilterContainers;
    public static boolean FILTER_CONTAINERS = true;
    public static boolean FILTER_NON_CONTAINERS = false;

    public ContainerFilter(boolean filterContainers)
    {
        fFilterContainers = filterContainers;
    }

    public boolean select(Viewer viewer, Object parent, Object element)
    {
        boolean isContainer = element instanceof IContainer;
        if(!isContainer && (element instanceof IJavaElement))
        {
            int type = ((IJavaElement)element).getElementType();
            isContainer = type == 1 || type == 2 || type == 4 || type == 3;
        }
        return fFilterContainers && !isContainer || !fFilterContainers && isContainer;
    }

}
