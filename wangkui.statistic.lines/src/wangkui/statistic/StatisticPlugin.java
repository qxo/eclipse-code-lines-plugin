// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatisticPlugin.java

package wangkui.statistic;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

// Referenced classes of package wangkui.statistic:
//            Policy

public class StatisticPlugin extends AbstractUIPlugin
{

    public StatisticPlugin()
    {
          plugin = this;
    }

    
    public void start(BundleContext context)
        throws Exception
    {
        super.start(context);
        Policy.localize("wangkui.statistic.messages");
    }

    public void stop(BundleContext context)
        throws Exception
    {
        super.stop(context);
    }

    public static StatisticPlugin getPlugin()
    {
        return plugin;
    }

    public static IWorkspace getWorkspace()
    {
        return ResourcesPlugin.getWorkspace();
    }

    private static StatisticPlugin plugin;
}
