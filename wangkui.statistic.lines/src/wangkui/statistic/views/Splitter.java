// FrontEnd Plus GUI for JAD
// DeCompiled : Splitter.class

package wangkui.statistic.views;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.*;

public class Splitter extends SashForm
{

    private static final String VISIBILITY = "org.holon.statistic.visibility";

    public Splitter(Composite parent, int style)
    {
        super(parent, style);
    }

    public void setVisible(Control child, boolean visible)
    {
        boolean wasEmpty = isEmpty();
        child.setVisible(visible);
        child.setData("org.holon.statistic.visibility", new Boolean(visible));
        if(wasEmpty != isEmpty())
        {
            Composite parent = getParent();
            if(parent instanceof Splitter)
            {
                Splitter sp = (Splitter)parent;
                sp.setVisible(((Control) (this)), visible);
                sp.layout();
            }
        } else
        {
            layout();
        }
    }

    public void setMaximizedControl(Control control)
    {
        if(control == null || control == getMaximizedControl())
            super.setMaximizedControl(null);
        else
            super.setMaximizedControl(control);
        Composite parent = getParent();
        if(parent instanceof Splitter)
            ((Splitter)parent).setMaximizedControl(((Control) (this)));
        else
            layout(true);
    }

    private boolean isEmpty()
    {
        Control controls[] = getChildren();
        for(int i = 0; i < controls.length; i++)
            if(isVisible(controls[i]))
                return false;

        return true;
    }

    private boolean isVisible(Control child)
    {
        if(child instanceof Sash)
            return false;
        Object data = child.getData("org.holon.statistic.visibility");
        if(data instanceof Boolean)
            return ((Boolean)data).booleanValue();
        else
            return true;
    }
}
