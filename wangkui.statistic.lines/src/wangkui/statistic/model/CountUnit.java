// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CountUnit.java

package wangkui.statistic.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public abstract class CountUnit
{

    public CountUnit(IFile file)
    {
        numJD = 0;
        numMM = 0;
        numSM = 0;
        numTM = 0;
        numTC = 0;
        numTN = 0;
        numTB = 0;
        numTL = 0;
        this.file = file;
        count();
    }

    protected abstract void count();

    public IFile getFile()
    {
        return file;
    }

    public String getName()
    {
        return file.getName();
    }

    public String getExtension()
    {
        String name = file.getName();
        String extension = name.substring(name.lastIndexOf(".") + 1);
        return extension;
    }

    public String getPath()
    {
        String str1 = file.getFullPath().toString();
        String str2 = str1.substring(1, str1.lastIndexOf("/"));
        return str2;
    }

    boolean isBlank(String string)
    {
        return string.equals("");
    }

    public int getJD()
    {
        return numJD;
    }

    public int getMM()
    {
        return numMM;
    }

    public int getSM()
    {
        return numSM;
    }

    public int getTM()
    {
        return numTM;
    }

    public int getTC()
    {
        return numTC;
    }

    public int getTN()
    {
        return numTN;
    }

    public int getTB()
    {
        return numTB;
    }

    public int getTL()
    {
        return numTL;
    }

    public static final int NULL = 0;
    IFile file;
    int numJD;
    int numMM;
    int numSM;
    int numTM;
    int numTC;
    int numTN;
    int numTB;
    int numTL;
}
