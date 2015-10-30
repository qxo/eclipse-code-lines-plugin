// FrontEnd Plus GUI for JAD
// DeCompiled : ViewerPane.class

package wangkui.statistic.views;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import wangkui.statistic.StatisticPlugin;

// Referenced classes of package wangkui.statistic.views:
//            Splitter, LinesView

public class ViewerPane extends ViewForm
{

    private ToolBarManager fToolBarManager;
    private int identify;

    public ViewerPane(Composite parent, int style)
    {
        super(parent, style);
        marginWidth = 0;
        marginHeight = 0;
        CLabel label = new CLabel(this, 0) {

            public Point computeSize(int wHint, int hHint, boolean changed)
            {
                return super.computeSize(wHint, Math.max(24, hHint), changed);
            }

        }
;
        setTopLeft(label);
        MouseAdapter ml = new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e)
            {
                Control content = getContent();
                if(content != null && content.getBounds().contains(e.x, e.y))
                    return;
                Control parent = getParent();
                if(parent instanceof Splitter)
                {
                    LinesView view = null;
                    try
                    {
                        view = (LinesView)StatisticPlugin.getPlugin().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("wangkui.statistic.views.Lines");
                    }
                    catch(PartInitException partinitexception) { }
                    if(view == null)
                        return;
                    if(((Splitter)parent).getMaximizedControl() != null)
                    {
                        view.setModeButtons(3);
                        view.setMode(3);
                    } else
                    if(getIdentify() == 1)
                    {
                        view.setModeButtons(1);
                        view.setMode(1);
                    } else
                    {
                        view.setModeButtons(2);
                        view.setMode(2);
                    }
                    ((Splitter)parent).setMaximizedControl(ViewerPane.this);
                }
            }

        }
;
        addMouseListener(ml);
        label.addMouseListener(ml);
    }

    public void setText(String label)
    {
        CLabel cl = (CLabel)getTopLeft();
        cl.setText(label);
    }

    public String getText()
    {
        CLabel cl = (CLabel)getTopLeft();
        return cl.getText();
    }

    public void setImage(Image image)
    {
        CLabel cl = (CLabel)getTopLeft();
        cl.setImage(image);
    }

    public static ToolBarManager getToolBarManager(Composite parent)
    {
        if(parent instanceof ViewerPane)
        {
            ViewerPane pane = (ViewerPane)parent;
            return pane.getToolBarManager();
        } else
        {
            return null;
        }
    }

    public static void clearToolBar(Composite parent)
    {
        ToolBarManager tbm = getToolBarManager(parent);
        if(tbm != null)
        {
            tbm.removeAll();
            tbm.update(true);
        }
    }

    public ToolBarManager getToolBarManager()
    {
        if(fToolBarManager == null)
        {
            ToolBar tb = new ToolBar(this, 0x800000);
            setTopCenter(tb);
            fToolBarManager = new ToolBarManager(tb);
        }
        return fToolBarManager;
    }

    public int getIdentify()
    {
        return identify;
    }

    public void setIdentify(int identify)
    {
        this.identify = identify;
    }
}
