
import com.telecom.FileSystemAccessor;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.midlet.MIDlet;

public class MP3Cutter extends MIDlet
        implements CommandListener {

    int sound = 0;
    Player mp3 = null;
    private Display mydisplay;
    private List myList;
    private Command exitMidlet = new Command("Exit", Command.EXIT, 2);
    private Command back = new Command("Back", Command.BACK, 1);
    private Command index = new Command("Back?", Command.BACK, 1);
    private Command okey = new Command("OK", Command.OK, 2);
    private Command cutscr = new Command("Select", Command.OK, 2);
    private Command cut = new Command("OK", Command.OK, 2);
    private Command aboutHelp = new Command("About & Help", Command.HELP, 2);
    private Command cmdReplay = new Command("Replay", Command.HELP, 2);
    private Command cmdGetStart = new Command("Get start time", Command.HELP, 2);
    private Command cmdGetEnd = new Command("Get end time", Command.HELP, 2);
    private Image anyfile;
    private Image audio;
    private Image disk;
    private Image folder;
    Image[] icons = null;
    private TextField tBitrate;
    private TextField tBegin;
    private TextField tEnd;
    private TextField tNameFile;
    String[] files;
    String nameT = "";
    private Gauge gaProgress;
    private Timer tm;               // The Timer
    private DownloadTimer tt;       // The task to run

    public MP3Cutter() {
        // Create a timer that fires off every 1000 milliseconds    

        try {
            gaProgress = new Gauge("Play Progress", false, 100, 1);
            this.anyfile = Image.createImage("/icons/anyfile.png");
            this.audio = Image.createImage("/icons/audio.png");
            this.disk = Image.createImage("/icons/disk.png");
            this.folder = Image.createImage("/icons/folder.png");
        } catch (IOException localIOException) {
            System.out.println("ex = " + localIOException);
        }
        this.mydisplay = Display.getDisplay(this);
        tm = new Timer();
        tt = new DownloadTimer();
        tm.scheduleAtFixedRate(tt, 0, 1000);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean paramBoolean) {
    }

    public void startApp() {
        //System.out.println("startApp");
        FileSystemAccessor localFileSystemAccessor = new FileSystemAccessor(this.nameT);
        String str = this.nameT;
        int i;
        if (this.nameT.equals("")) {
            str = "MP3Cutter";
            this.files = FileSystemAccessor.listRoots();
            this.icons = new Image[this.files.length];
            for (i = 0; i < this.files.length; i++) {
                this.icons[i] = this.disk;
            }
        } else {
            try {
                this.files = localFileSystemAccessor.list();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.icons = new Image[this.files.length];
            for (i = 0; i < this.files.length; i++) {
                this.files[i] = this.files[i].substring(this.nameT.length());
                this.icons[i] = this.anyfile;
                if (this.files[i].substring(this.files[i].length() - 1, this.files[i].length()).equals("/")) {
                    this.icons[i] = this.folder;
                } else if (this.files[i].endsWith(".mp3") || this.files[i].endsWith(".MP3") || this.files[i].endsWith(".wav") || this.files[i].endsWith(".arm")) {
                    this.icons[i] = this.audio;
                }
            }
        }
        this.myList = new List(str, List.IMPLICIT, this.files, this.icons);

        if (this.nameT.equals("")) {
            this.myList.addCommand(this.exitMidlet);
        } else {
            this.myList.addCommand(this.back);
        }
        this.myList.addCommand(this.aboutHelp);

        this.myList.addCommand(this.okey);
        //this.myList.addCommand(this.cutscr);
        this.myList.addCommand(this.exitMidlet);
        this.myList.setSelectCommand(this.okey);
        this.myList.setCommandListener(this);
        this.mydisplay.setCurrent(this.myList);
    }

    public void commandAction(Command paramCommand, Displayable paramDisplayable) {
        String str2;
        Object localObject;

        if (paramCommand == this.aboutHelp) {
            Display.getDisplay(this).setCurrent(new Alert("About & Help", "MP3 Cutter Pro \n  ver 2.1 deverloper name: vu van tuong\n\n You can select music files mp3 and music selection cut desired time!  ", null, AlertType.INFO));

        } else if (paramCommand == this.exitMidlet) {
            destroyApp(false);
            notifyDestroyed();
        } else if (paramCommand == this.okey) {
            System.out.println(" click folder");
            String str1 = this.files[this.myList.getSelectedIndex()];
            if (str1.endsWith("/")) {
                this.nameT += this.files[this.myList.getSelectedIndex()];
                System.out.println("OK: nameT = " + this.nameT);
                startApp();
            } else if (str1.endsWith(".mp3") || str1.endsWith(".MP3") || str1.endsWith(".arm") || str1.endsWith(".wav")) {

                localObject = new Form("MP3 Cutter");
                gaProgress = new Gauge("Play Progress", false, 100, 1);
                ((Form) localObject).append(gaProgress);
                setMp3();
                this.tNameFile = new TextField("files: ", this.nameT + this.files[this.myList.getSelectedIndex()], 255, 0);
                this.tBitrate = new TextField("rate quality (kb/s)", "128", 3, 2);
                this.tBegin = new TextField("start time (s)", "0", 255, 2);
                this.tEnd = new TextField("end time (s)", "10", 255, 2);
                ((Form) localObject).append(this.tNameFile);
                ((Form) localObject).append(this.tBitrate);
                ((Form) localObject).append(this.tBegin);
                ((Form) localObject).append(this.tEnd);
                ((Form) localObject).addCommand(this.index);
                ((Form) localObject).addCommand(this.cut);
                ((Form) localObject).addCommand(this.cmdReplay);
                ((Form) localObject).addCommand(this.cmdGetStart);
                ((Form) localObject).addCommand(this.cmdGetEnd);


                ((Form) localObject).setCommandListener(this);
                this.mydisplay.setCurrent((Displayable) localObject);
                System.gc();
            }

        } else if (paramCommand == this.back) {
            sound = 1;
            if (this.nameT.length() < 5) {
                this.nameT = "";
            } else {
                for (int i = 2; i < this.nameT.length(); i++) {
                    str2 = this.nameT.substring(this.nameT.length() - i, this.nameT.length() - i + 1);
                    if (str2.equals("/")) {
                        this.nameT = this.nameT.substring(0, this.nameT.length() - i + 1);
                    }
                }
            }
            System.out.println("BACK: nameT = " + this.nameT);
            startApp();
        } else if (paramCommand == this.cutscr) {
        } else if (paramCommand == this.index) {
            sound = 1;
            startApp();
        } else if (paramCommand == this.cmdGetStart) {
            tBegin.setString(gaProgress.getValue() + "");
        } else if (paramCommand == this.cmdGetEnd) {
            tEnd.setString(gaProgress.getValue() + "");
        } else if (paramCommand == this.cmdReplay) {
            setMp3();

        } else if (paramCommand == this.cut) {

            try {
                sound = 1;
                str2 = this.nameT + this.files[this.myList.getSelectedIndex()];
                localObject = str2.substring(0, str2.length() - 4) + "_cut.mp3";
                System.out.println((String) localObject);
                int j = Integer.parseInt(this.tBitrate.getString());
                int k = j / 8 * 1024 * Integer.parseInt(this.tBegin.getString());
                int m = j / 8 * 1024 * Integer.parseInt(this.tEnd.getString());
                Cut localCut = new Cut();
                Cut.copyfile(str2, (String) localObject, k, m - k, 10000);
                Display.getDisplay(this).setCurrent(new Alert("messenger", "Successfull! ", null, AlertType.INFO));

                startApp();
            } catch (Exception e) {
                Display.getDisplay(this).setCurrent(new Alert("messenger", "Error! " + e.getMessage(), null, AlertType.INFO));

            }
        }

    }

    private void setMp3() {
        try {
            sound = 0;
            mp3 = Manager.createPlayer("file://" + this.nameT + this.files[this.myList.getSelectedIndex()]);
            mp3.prefetch();
            System.out.println("total time --- " + mp3.getDuration() / 1000000 + " s");
            gaProgress.setMaxValue((int) (mp3.getDuration() / 1000000));
            gaProgress.setValue(1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class DownloadTimer extends TimerTask {

        public final void run() {
            if (mp3 != null) {
                try {
                    if (sound == 0) {
                        mp3.realize();
                        mp3.prefetch();
                        mp3.start();
                        mp3.setLoopCount(-1);
                    } else {
                        mp3.close();
                        // mp3=null;
                        System.gc();
                    }
                } catch (Exception e) {
                }
            }

            if (gaProgress.getValue() + 1 < gaProgress.getMaxValue()) {

                try {
                    gaProgress.setLabel("Playing...     " + (int) (mp3.getMediaTime() / 1000000) + " s/ " + mp3.getDuration() / 1000000 + " (s)");

                    System.out.println("------: " + (int) mp3.getMediaTime());
                    gaProgress.setValue((int) (mp3.getMediaTime() / 1000000));
                } catch (Exception ex) {
                }


                //  System.out.println(" van chay "+gaProgress.getValue());
            } else {
                gaProgress.setValue(gaProgress.getMaxValue());
                sound = 1;
                System.out.println(" bi het");
                gaProgress.setLabel("Play end!");
            }
        }
    }
}
