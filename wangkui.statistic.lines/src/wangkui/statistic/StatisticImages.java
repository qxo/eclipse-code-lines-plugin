// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatisticImages.java

package wangkui.statistic;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

// Referenced classes of package wangkui.statistic:
//            StatisticPlugin

public class StatisticImages
{

    public StatisticImages()
    {
    }

    protected static void createImageDescriptor(String id, URL baseURL)
    {
        URL url = null;
        try
        {
            url = new URL(baseURL, "icons/full/" + id);
        }
        catch(MalformedURLException malformedurlexception) { }
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        imageRegistry.put(id, desc);
    }

    public static ImageDescriptor getImageDescriptor(String id)
    {
        return imageRegistry.getDescriptor(id);
    }

    protected static void createImage(String id, URL baseURL)
    {
        URL url = null;
        try
        {
            url = new URL(baseURL, "icons/full/" + id);
        }
        catch(MalformedURLException malformedurlexception) { }
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        Image image = desc.createImage();
        imageRegistry.put(id, image);
    }

    public static Image getImage(String key)
    {
        return imageRegistry.get(key);
    }

    private static void initializeImages()
    {
        URL baseURL = StatisticPlugin.getPlugin().getDescriptor().getInstallURL();
        createImageDescriptor("elcl16/mode1.gif", baseURL);
        createImageDescriptor("elcl16/mode2.gif", baseURL);
        createImageDescriptor("elcl16/mode3.gif", baseURL);
        createImageDescriptor("elcl16/count.gif", baseURL);
        createImageDescriptor("elcl16/collapseall.gif", baseURL);
        createImageDescriptor("elcl16/refresh.gif", baseURL);
        createImageDescriptor("elcl16/export.gif", baseURL);
        createImageDescriptor("dlcl16/export.gif", baseURL);
        createImageDescriptor("elcl16/clear.gif", baseURL);
        createImageDescriptor("dlcl16/clear.gif", baseURL);
        
        createImageDescriptor("elcl16/help.gif", baseURL);
    }

    private static ImageRegistry imageRegistry = new ImageRegistry();

    static 
    {
        initializeImages();
    }
}
