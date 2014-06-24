package com.telecom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

public class FileSystemAccessor
{
  private FileConnection connection;
  private String location;

  public FileSystemAccessor(String paramString)
  {
    this.location = paramString;
  }

  private void close()
  {
    try
    {
      this.connection.close();
      this.connection = null;
    }
    catch (Exception localException)
    {
      System.out.println("Close connection failed: " + localException.getMessage());
    }
  }

  private void open(int paramInt)
  {
    try
    {
      this.connection = ((FileConnection)Connector.open("file://" + this.location, paramInt));
    }
    catch (Exception localException)
    {
      this.connection = null;
      System.out.println("Open connection '" + this.location + "' failed: " + localException.getMessage());
    }
  }

  private void open()
  {
    open(1);
  }

  public long availableSize()
  {
    long l = -1L;
    open();
    if (this.connection != null)
    {
      l = this.connection.availableSize();
      close();
    }
    return l;
  }

  public boolean canRead()
  {
    boolean bool = false;
    open();
    if (this.connection != null)
    {
      bool = this.connection.canRead();
      close();
    }
    return bool;
  }

  public boolean canWrite()
  {
    boolean bool = false;
    open();
    if (this.connection != null)
    {
      bool = this.connection.canWrite();
      close();
    }
    return bool;
  }

  public boolean create() throws IOException
  {
    open(3);
    if (this.connection != null)
    {
      if (!this.connection.exists())
        this.connection.create();
      close();
    }
    return true;
  }

  public boolean delete() throws IOException
  {
    open(3);
    if (this.connection != null)
    {
      if (this.connection.exists())
        this.connection.delete();
      close();
    }
    return true;
  }

  public long directorySize(boolean paramBoolean) throws IOException
  {
    long l = -1L;
    open();
    if (this.connection != null)
    {
      l = this.connection.directorySize(paramBoolean);
      close();
    }
    return l;
  }

  public boolean exists()
  {
    boolean bool = false;
    open();
    if (this.connection != null)
    {
      bool = this.connection.exists();
      close();
    }
    return bool;
  }

  public long fileSize() throws IOException
  {
    long l = -1L;
    open();
    if (this.connection != null)
    {
      l = this.connection.fileSize();
      close();
    }
    return l;
  }

  public boolean isDirectory()
  {
    boolean bool = false;
    open();
    if (this.connection != null)
    {
      bool = this.connection.isDirectory();
      close();
    }
    return bool;
  }

  public boolean isHidden()
  {
    boolean bool = false;
    open();
    if (this.connection != null)
    {
      bool = this.connection.isHidden();
      close();
    }
    return bool;
  }

  public long lastModified()
  {
    long l = -1L;
    open();
    if (this.connection != null)
    {
      l = this.connection.lastModified();
      close();
    }
    return l;
  }

  public String[] list() throws IOException
  {
    String[] arrayOfString = null;
    open();
    if (this.connection != null)
    {
      Vector localVector = new Vector();
      Enumeration localEnumeration = this.connection.list("*", true);
      while (localEnumeration.hasMoreElements())
        localVector.addElement(localEnumeration.nextElement());
      close();
      arrayOfString = new String[localVector.size()];
      for (int i = 0; i < localVector.size(); i++)
        arrayOfString[i] = (this.location + localVector.elementAt(i));
    }
    return arrayOfString;
  }

  public static String[] listRoots()
  {
    String[] arrayOfString = null;
    Vector localVector = new Vector();
    Enumeration localEnumeration = FileSystemRegistry.listRoots();
    while (localEnumeration.hasMoreElements())
      localVector.addElement(localEnumeration.nextElement());
    arrayOfString = new String[localVector.size()];
    for (int i = 0; i < localVector.size(); i++)
      arrayOfString[i] = ("/" + localVector.elementAt(i));
    return arrayOfString;
  }

  public boolean mkdir() throws IOException
  {
    open(3);
    if (this.connection != null)
    {
      if (!this.connection.exists())
        this.connection.mkdir();
      close();
    }
    return true;
  }

  public byte[] read()
  {
    byte[] arrayOfByte = null;
    try
    {
      open();
      long l = this.connection.fileSize();
      arrayOfByte = new byte[(int)l];
      DataInputStream localDataInputStream = this.connection.openDataInputStream();
      if (l != localDataInputStream.read(arrayOfByte))
        arrayOfByte = null;
      localDataInputStream.close();
      localDataInputStream = null;
      close();
    }
    catch (Exception localException)
    {
      arrayOfByte = null;
      System.out.println("Read from '" + this.location + "' failed: " + localException.getMessage());
    }
    return arrayOfByte;
  }

  public boolean rename(String paramString) throws IOException
  {
    open(3);
    if (this.connection != null)
    {
      int i = paramString.lastIndexOf('/');
      if (i != -1)
        paramString = paramString.substring(i + 1);
      this.connection.rename(paramString);
      close();
    }
    return true;
  }

  public boolean write(byte[] paramArrayOfByte)
  {
    boolean bool = false;
    try
    {
      open(3);
      if (this.connection.exists())
        this.connection.delete();
      this.connection.create();
      DataOutputStream localDataOutputStream = this.connection.openDataOutputStream();
      localDataOutputStream.write(paramArrayOfByte, 0, paramArrayOfByte.length);
      localDataOutputStream.flush();
      localDataOutputStream.close();
      close();
      bool = true;
    }
    catch (Exception localException)
    {
      System.out.println("Write to '" + this.location + "' failed: " + localException.getMessage());
    }
    return bool;
  }

  public void setHidden(boolean paramBoolean) throws IOException
  {
    open();
    if (this.connection != null)
    {
      this.connection.setHidden(paramBoolean);
      close();
    }
  }

  public void setReadable(boolean paramBoolean) throws IOException
  {
    open();
    if (this.connection != null)
    {
      this.connection.setReadable(paramBoolean);
      close();
    }
  }

  public void setWriteable(boolean paramBoolean) throws IOException
  {
    open();
    if (this.connection != null)
    {
      this.connection.setWritable(paramBoolean);
      close();
    }
  }

  public long totalSize()
  {
    long l = -1L;
    open();
    if (this.connection != null)
    {
      l = this.connection.totalSize();
      close();
    }
    return l;
  }

  public long usedSize()
  {
    long l = -1L;
    open();
    if (this.connection != null)
    {
      l = this.connection.usedSize();
      close();
    }
    return l;
  }

  public InputStream openInputStream()
  {
    InputStream localInputStream;
    try
    {
      open();
      localInputStream = this.connection.openInputStream();
    }
    catch (Exception localException)
    {
      localInputStream = null;
      System.out.println("openInputStream from '" + this.location + "' failed: " + localException.getMessage());
    }
    return localInputStream;
  }

  public OutputStream openOutputStream()
  {
    OutputStream localOutputStream;
    try
    {
      open(3);
      if (this.connection.exists())
        this.connection.delete();
      this.connection.create();
      localOutputStream = this.connection.openOutputStream();
    }
    catch (Exception localException)
    {
      localOutputStream = null;
      System.out.println("openOutputStream on '" + this.location + "' failed: " + localException.getMessage());
    }
    return localOutputStream;
  }

  public void closeInputStream(InputStream paramInputStream)
  {
    try
    {
      paramInputStream.close();
    }
    catch (IOException localIOException)
    {
      System.out.println("closeInputStream from '" + this.location + "' failed: " + localIOException.getMessage());
    }
    close();
  }

  public void closeOutputStream(OutputStream paramOutputStream)
  {
    try
    {
      paramOutputStream.flush();
      paramOutputStream.close();
    }
    catch (IOException localIOException)
    {
      System.out.println("closeOutputStream on '" + this.location + "' failed: " + localIOException.getMessage());
    }
    close();
  }
}
