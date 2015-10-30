// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JavaCountUnit.java

package wangkui.statistic.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;

// Referenced classes of package wangkui.statistic.model:
//            CountUnit

public class JavaCountUnit extends CountUnit
{

    public JavaCountUnit(IFile file)
    {
        super(file);
        status = 0;
        style = 4;
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
                style = 4;
                parseLine(line.trim(), true);
                increase();
            }

            reader.close();
        }
        catch(Exception exception) { }
        numTM = numJD + numMM + numSM;
        numTN = numTM + numTC;
        numTL = numTN + numTB;
    }

    private void increase()
    {
        switch(style)
        {
        case 0: // '\0'
            numTC++;
            break;

        case 1: // '\001'
            numJD++;
            break;

        case 2: // '\002'
            numMM++;
            break;

        case 3: // '\003'
            numSM++;
            break;

        case 4: // '\004'
            numTB++;
            break;
        }
    }

    private void parseLine(String string, boolean isNew)
    {
        switch(status)
        {
        default:
            break;

        case 0: // '\0'
            if(isBlank(string))
            {
                style = style >= 4 ? 4 : style;
                status = 0;
                break;
            }
            if(isSingleComment(string))
            {
                style = style >= 3 ? 3 : style;
                status = 0;
                break;
            }
            if(isStartOfJavaDoc(string))
            {
                style = style >= 1 ? 1 : style;
                status = 1;
                parseLine(string.substring(3).trim(), false);
                break;
            }
            if(isStartOfMultiComment(string))
            {
                style = style >= 2 ? 2 : style;
                status = 2;
                parseLine(string.substring(2).trim(), false);
                break;
            }
            if(isStartOfSingleQuote(string))
            {
                style = style >= 0 ? 0 : style;
                status = 0;
                string = string.substring(1).trim();
                if(findEndOfSingleQuote(string))
                    parseLine(string.substring(getEndOfSingleQuote(string)).trim(), false);
                break;
            }
            if(isStartOfDoubleQuote(string))
            {
                style = style >= 0 ? 0 : style;
                status = 0;
                string = string.substring(1).trim();
                if(findEndOfDoubleQuote(string))
                    parseLine(string.substring(getEndOfDoubleQuote(string)).trim(), false);
            } else
            {
                style = 0;
                parseLine(string.substring(getNextStart(string)).trim(), false);
            }
            break;

        case 1: // '\001'
            if(isBlank(string))
            {
                if(isNew)
                    style = 4;
                break;
            }
            if(findEndOfMultiComment(string))
            {
                style = style >= 1 ? 1 : style;
                status = 0;
                parseLine(string.substring(getEndOfMultiComment(string)).trim(), false);
            } else
            {
                style = style >= 1 ? 1 : style;
                status = 1;
            }
            break;

        case 2: // '\002'
            if(isBlank(string))
            {
                if(isNew)
                    style = 4;
                break;
            }
            if(findEndOfMultiComment(string))
            {
                style = style >= 2 ? 2 : style;
                status = 0;
                parseLine(string.substring(getEndOfMultiComment(string)).trim(), false);
            } else
            {
                style = style >= 2 ? 2 : style;
                status = 2;
            }
            break;
        }
    }

    private int getNextStart(String string)
    {
        int length = string.length();
        for(int i = 0; i < length; i++)
        {
            char cc = string.charAt(i);
            if(cc == '\'' || cc == '"')
                return i;
            if(cc == '/' && i != length)
            {
                char c2 = string.charAt(i + 1);
                if(c2 == '/' || c2 == '*')
                    return i;
            }
        }

        return length;
    }

    private boolean isSingleComment(String string)
    {
        return string.startsWith("//");
    }

    private boolean isStartOfJavaDoc(String string)
    {
        return string.startsWith("/**");
    }

    private boolean isStartOfMultiComment(String string)
    {
        return string.startsWith("/*");
    }

    private boolean isStartOfSingleQuote(String string)
    {
        return string.startsWith("'");
    }

    private boolean isStartOfDoubleQuote(String string)
    {
        return string.startsWith("\"");
    }

    private boolean findEndOfMultiComment(String string)
    {
        return string.indexOf("*/") >= 0;
    }

    private int getEndOfMultiComment(String string)
    {
        return string.indexOf("*/") + 2;
    }

    private boolean findEndOfSingleQuote(String string)
    {
        int index = string.indexOf("'");
        int start = 0;
        for(; index >= 0; index = string.indexOf("'", start))
        {
            if(index == start)
                return true;
            if(string.charAt(index - 1) != '\\')
                return true;
            start = index + 1;
        }

        return false;
    }

    private boolean findEndOfDoubleQuote(String string)
    {
        int index = string.indexOf("\"");
        int start = 0;
        for(; index >= 0; index = string.indexOf("\"", start))
        {
            if(index == start)
                return true;
            if(string.charAt(index - 1) != '\\')
                return true;
            start = index + 1;
        }

        return false;
    }

    private int getEndOfSingleQuote(String string)
    {
        int index = string.indexOf("'");
        int start = 0;
        for(; index >= 0; index = string.indexOf("'", start))
        {
            if(index == start || string.charAt(index - 1) != '\\')
                break;
            start = index + 1;
        }

        return index + 1;
    }

    private int getEndOfDoubleQuote(String string)
    {
        int index = string.indexOf("\"");
        int start = 0;
        for(; index >= 0; index = string.indexOf("\"", start))
        {
            if(index == start || string.charAt(index - 1) != '\\')
                break;
            start = index + 1;
        }

        return index + 1;
    }

    public static final int STATUS_CODE = 0;
    public static final int STATUS_JAVA_DOC = 1;
    public static final int STATUS_MULTI_LINE_COMMENT = 2;
    public static final int STYLE_CODE = 0;
    public static final int STYLE_JAVA_DOC = 1;
    public static final int STYLE_MULTI_LINE_COMMENT = 2;
    public static final int STYLE_SINGLE_LINE_COMMENT = 3;
    public static final int STYLE_BLANK = 4;
    private int status;
    private int style;
}
