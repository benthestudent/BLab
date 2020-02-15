package soundboard;

import java.io.File;
import java.net.URL;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Babyc
 */
public class Cue {
    private String name;
    private double len = 0;
    private String filePath;
    private File soundFile;
    private int volume;
    private MediaPlayer audio;
    private int hotKey;
    private final JFXPanel fxPanel = new JFXPanel();
    public Cue(String cueName, File cueFile, int key)
    {
        name = cueName;
        soundFile = cueFile;
        filePath = soundFile.getPath();
        volume = 100;
        Media cueMedia = new Media(cueFile.toURI().toString());
        audio = new MediaPlayer(cueMedia);
        len = cueMedia.getDuration().toSeconds();
        hotKey = key;
        //add code to get length 
        
    }
    
    public Cue(String cueName, File cueFile, int key, int newVolume)
    {
        name = cueName;
        soundFile = cueFile;
        filePath = soundFile.getPath();
        volume = newVolume;
        Media cueMedia = new Media(cueFile.toURI().toString());
        audio = new MediaPlayer(cueMedia);
        len = cueMedia.getDuration().toSeconds();
        hotKey = key;
        //add code to get length 
    }
    
    public void play()
    {
        audio.setVolume(volume * .01);
        audio.play();
    }
    public void pause()
    {
        audio.pause();
    }
    public void stop()
    {
        audio.stop();
    }
    public String getName()
    {
          return name;
    }
    public void setName(String newName)
    {
          name = newName;
    }
    
    public String getFilePath()
    {
        return filePath;
    }
 
    public double getLength()
    {
        return len;
    }
    public int getHotKey()
    {
        return hotKey;
    }
    public void setHotKey(int newKey)
    {
        hotKey = newKey;
    }
    
    public File getFile()
    {
        return soundFile;
    }
    
    public void setVolume(int newVolume)
    {
        volume = newVolume;
        audio.setVolume(volume * 0.01);
    }
    
    public int getVolume()
    {
        return volume;
    }
}
