package at.fhooe.mc.ois;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by laureenschausberger on 04.05.17.
 */
public class GPSSimulator extends BufferedReader{

    /**
     * filename and filter of Data
     */
    String mFileName, mFilter;

    /**
     * mSleep that thread should sleep before returning readline
     */
    int mSleep;


    /**
     * constructor
     * @param _filename
     * @param _sleep
     * @param _filter
     * @throws FileNotFoundException
     */
    public GPSSimulator(String _filename, int _sleep, String _filter) throws FileNotFoundException {
        super(new FileReader(_filename));
        mFileName   = _filename;
        mSleep      = _sleep;
        mFilter     = _filter;
    }

    /**
     * reads line and lets thread "sleep" if no error occurs
     * @return
     */
    public String readLine() {
        String s = "";
        try{
            s = super.readLine();
            if(s == null){
                return null;
            }
            else if(s.startsWith(mFilter)){
                Thread.sleep(mSleep);
            }
        } catch (Exception _e){
            _e.printStackTrace();
        }
        return s;
    }

    public static void main(String[] args) {
        NMEAParser parser = new NMEAParser("at/fhooe/mc/ois/GPS-Log-I.log");
        parser.start();
    }

}
