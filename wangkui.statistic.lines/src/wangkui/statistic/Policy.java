// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Policy.java

package wangkui.statistic;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Policy
{

    public Policy()
    {
    }

    public static void localize(String bundleName)
    {
        bundle = ResourceBundle.getBundle(bundleName);
    }

    public static String bind(String id, String binding)
    {
        return bind(id, ((Object []) (new String[] {
            binding
        })));
    }

    public static String bind(String id, String binding1, String binding2)
    {
        return bind(id, ((Object []) (new String[] {
            binding1, binding2
        })));
    }

    public static String bind(String key)
    {
        try
        {
            return bundle.getString(key);
        }
        catch(MissingResourceException e)
        {
            return key;
        }
        catch(NullPointerException e)
        {
            return "!" + key + "!";
        }
    }

    public static String bind(String key, Object args[])
    {
        try
        {
            return MessageFormat.format(bind(key), args);
        }
        catch(MissingResourceException e)
        {
            return key;
        }
        catch(NullPointerException e)
        {
            return "!" + key + "!";
        }
    }

    protected static ResourceBundle bundle = null;

}
