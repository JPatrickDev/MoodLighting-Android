package me.jack.moodlighting;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jackp on 31/03/2017.
 */

public class FadePreset implements Serializable {

    public static final ArrayList<FadePreset> presets = new ArrayList<>();

    static{
        FadePreset green = new FadePreset("Greens",1500,500,"True");
        green.addColor(Color.rgb(0,255,0));
        green.addColor(Color.rgb(61,217,79));
        green.addColor(Color.rgb(5,133,20));
        green.addColor(Color.rgb(33,255,10));
        green.addColor(Color.rgb(0,255,120));

        FadePreset blue = new FadePreset("Blues",1500,500,"True");
        blue.addColor(Color.rgb(0,0,255));
        blue.addColor(Color.rgb(0,120,255));
        blue.addColor(Color.rgb(0,170,255));
        blue.addColor(Color.rgb(0,255,255));

        FadePreset greenBlue = new FadePreset("Green + Blue",1500,500,"True");
        greenBlue.addColor(Color.rgb(0,0,255));
        greenBlue.addColor(Color.rgb(0,120,255));
        greenBlue.addColor(Color.rgb(0,170,255));
        greenBlue.addColor(Color.rgb(0,255,255));
        greenBlue.addColor(Color.rgb(0,255,0));
        greenBlue.addColor(Color.rgb(61,217,79));
        greenBlue.addColor(Color.rgb(5,133,20));
        greenBlue.addColor(Color.rgb(33,255,10));
        greenBlue.addColor(Color.rgb(0,255,120));

        presets.add(green);
        presets.add(blue);
        presets.add(greenBlue);

    }
    private int fadeTime,pauseTime;
    private String rand;
    private ArrayList<Integer> colors = new ArrayList<>();
    private String name;
    public FadePreset(String name,int fadeTime, int pauseTime, String rand) {
        this.fadeTime = fadeTime;
        this.pauseTime = pauseTime;
        this.rand = rand;
        this.name = name;
    }

    public void addColor(int c){
        colors.add(c);
    }

    public int getFadeTime() {
        return fadeTime;
    }

    public void setFadeTime(int fadeTime) {
        this.fadeTime = fadeTime;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }

    public String getRand() {
        return rand;
    }

    public void setRand(String rand) {
        this.rand = rand;
    }

    public ArrayList<Integer> getColors() {
        return colors;
    }

    public void setColors(ArrayList<Integer> colors) {
        this.colors = colors;
    }

    @Override
    public String toString(){
        return name;
    }
}
