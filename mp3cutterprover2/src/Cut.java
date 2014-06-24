import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class Cut
{
  static   byte[] arrayOfByte= null;
    
  public static void copyfile(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramString1.equals(paramString2))
      return;
    FileConnection localFileConnection1 = null;
    FileConnection localFileConnection2 = null;
    InputStream localInputStream = null;
    OutputStream localOutputStream = null;
    try
    {
      localFileConnection1 = (FileConnection)Connector.open("file://" + paramString1, 1);
      localInputStream = localFileConnection1.openInputStream();
      localFileConnection2 = (FileConnection)Connector.open("file://" + paramString2, 3);
      if (localFileConnection2.exists())
        localFileConnection2.delete();
      localFileConnection2.create();
      localOutputStream = localFileConnection2.openOutputStream();
      int i = 0;
      int j = paramInt1;
      do
      {
        System.out.println("xoffset " + Integer.toString(j));
        if (j + paramInt3 <= paramInt1 + paramInt2)
        {
          arrayOfByte = new byte[paramInt3];
        }
        else
        {
          arrayOfByte = new byte[Math.abs(paramInt1 + paramInt2 - j)];
          System.out.println("xoffset " + Integer.toString(j));
        }
        localInputStream.close();
        localInputStream = localFileConnection1.openInputStream();
        localInputStream.skip(j);
        localInputStream.read(arrayOfByte);
        localOutputStream.write(arrayOfByte);
        i += arrayOfByte.length;
        System.out.println("Total " + Integer.toString(i));
        byte[] arrayOfByte = null;
        j += paramInt3;
      }
      while (i != paramInt2);
      localInputStream.close();
      localInputStream = null;
      localFileConnection1.close();
      localFileConnection1 = null;
      localOutputStream.close();
      localOutputStream = null;
      localFileConnection2.close();
      localFileConnection2 = null;
    }
    catch (IOException localIOException)
    {
    }
  }
}
