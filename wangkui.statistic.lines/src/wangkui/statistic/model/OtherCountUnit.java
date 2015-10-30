// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OtherCountUnit.java

package wangkui.statistic.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;

// Referenced classes of package wangkui.statistic.model:
//            CountUnit

public class OtherCountUnit extends CountUnit
{

    public OtherCountUnit(IFile file)
    {
        super(file);
    }

    protected void count()
    {
        java.io.InputStream is = null;
        try
        {
            is = ((File)file).getContents();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            for(String line = reader.readLine(); line != null; line = reader.readLine())
                parseLine(line.trim());

            reader.close();
        }
        catch(Exception exception) { }
        numTL = numTN + numTB;
    }

    private void parseLine(String string)
    {
        if(isBlank(string))
            numTB++;
        else
            numTN++;
    }
}
