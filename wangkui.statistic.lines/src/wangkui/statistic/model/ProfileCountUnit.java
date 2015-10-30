// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProfileCountUnit.java

package wangkui.statistic.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;

// Referenced classes of package wangkui.statistic.model:
//            CountUnit

public class ProfileCountUnit extends CountUnit
{

    public ProfileCountUnit(IFile file)
    {
        super(file);
        style = 2;
    }

    protected void count()
    {
        java.io.InputStream is = null;
        try
        {
            is = ((File)file).getContents();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            for(String line = reader.readLine(); line != null; line = reader.readLine())
            {
                style = 2;
                parseLine(line.trim(), true);
                increase();
            }

            reader.close();
        }
        catch(Exception exception) { }
        numTN = numTM + numTC;
        numTL = numTM + numTC + numTB;
    }

    private void increase()
    {
        switch(style)
        {
        case 0: // '\0'
            numTC++;
            break;

        case 1: // '\001'
            numTM++;
            break;

        case 2: // '\002'
            numTB++;
            break;
        }
    }

    private void parseLine(String string, boolean isNew)
    {
        if(isBlank(string))
            style = 2;
        else
        if(isSingleComment(string))
            style = 1;
        else
            style = 0;
    }

    private boolean isSingleComment(String string)
    {
        return string.startsWith("#") || string.startsWith("!");
    }

    public static final int STYLE_CODE = 0;
    public static final int STYLE_COMMENT = 1;
    public static final int STYLE_BLANK = 2;
    private int style;
}
