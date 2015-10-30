// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CountUnitElement.java

package wangkui.statistic.model;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.*;
import org.eclipse.ui.model.IWorkbenchAdapter;

// Referenced classes of package wangkui.statistic.model:
//            CountUnit

public class CountUnitElement
    implements IWorkbenchAdapter, IAdaptable
{

    public CountUnitElement(CountUnit unit)
    {
        this.unit = unit;
    }

    public ImageDescriptor getImageDescriptor(Object object)
    {
        return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(unit.getName());
    }

    public Object[] getChildren(Object o)
    {
        return new Object[0];
    }

    public String getLabel(Object o)
    {
        return unit.getName();
    }

    public Object getParent(Object o)
    {
        return null;
    }

    public Object getAdapter(Class adapter)
    {
        if(adapter == org.eclipse.ui.model.IWorkbenchAdapter.class)
            return this;
        else
            return null;
    }

    public CountUnit getCountUnit()
    {
        return unit;
    }

    private CountUnit unit;
}
