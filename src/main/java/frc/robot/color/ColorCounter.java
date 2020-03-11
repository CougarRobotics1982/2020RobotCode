package frc.robot.color;

import java.util.HashMap;

public class ColorCounter {
    private static final HashMap<Color,Integer> colorMap = new HashMap<Color,Integer>();
    static{
        colorMap.put(Color.Green, 1);
        colorMap.put(Color.Blue, 2);
        colorMap.put(Color.Yellow, 3);
        colorMap.put(Color.Red, 4);
    }

    private ColorReader reader;
    private ColorReading lastReading = new ColorReading(Color.Unknown,100000);
    private int counter = 0;

    public ColorCounter(ColorReader reader){
        this.reader=reader;
    }

    private static int compare(ColorReading oldReading,ColorReading newestReading){
        if((!oldReading.isKnown()) || (!newestReading.isKnown()))
            return 0;
        int old = colorMap.get(oldReading.getClosestColor());
        int newest = colorMap.get(newestReading.getClosestColor());
        int change = (old-newest+4)%4;
        if(change==3)
            return -1;
        if(change==1)
            return 1;
        return 0;
    }

    public int getCounter(){
        return counter;
    }

    public double getRotations(){
        return (double)counter/8;
    }

    public void scan(){
        ColorReading newest = reader.read();
        if(newest.isKnown()){
            if(!lastReading.isExpired())
                counter+=compare(lastReading,newest);
            lastReading=newest;
        }else{
            if(lastReading.age()>10000)//10 seconds
                reset();
        }
    }

    public void reset(){
        counter = 0;
    }
}