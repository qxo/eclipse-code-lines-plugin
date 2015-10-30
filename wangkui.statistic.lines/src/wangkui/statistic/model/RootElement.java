// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RootElement.java

package wangkui.statistic.model;

import java.util.LinkedList;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

// Referenced classes of package wangkui.statistic.model:
//            CountUnit, CountUnitElement

public class RootElement
    implements IWorkbenchAdapter, IAdaptable
{

    public RootElement(LinkedList list)
    {
        this.list = list;
    }

    public Object[] getChildren(Object o)
    {
        CountUnit links[] = new CountUnit[list.size()];
        list.toArray(links);
        CountUnitElement elements[] = new CountUnitElement[links.length];
        for(int i = 0; i < links.length; i++)
            elements[i] = new CountUnitElement(links[i]);

        return elements;
    }

    public String getLabel(Object o)
    {
        return "";
    }

    public Object getParent(Object o)
    {
        return ((Object) (new Object[0]));
    }

    public Object getAdapter(Class adapter)
    {
        if(adapter == org.eclipse.ui.model.IWorkbenchAdapter.class)
            return this;
        else
            return null;
    }

    public ImageDescriptor getImageDescriptor(Object object)
    {
        return null;
    }

    private LinkedList list;
}
