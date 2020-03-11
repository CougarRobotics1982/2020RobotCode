package frc.robot.color;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;

public class ColorReader{
    private static final Color[] colors = new Color[] {Color.Blue, Color.Green, Color.Red, Color.Yellow};
    private static final int[][] colorValues = {
        {26,95,100},  //blue
        {28,100,42},  //green
        {100,67,23},  //red
        {56,100,20}   //yellow
    }; 

    private ColorSensorV3 sensor;
    private ColorReading lastReading;

    public ColorReader(){
        this(new ColorSensorV3(I2C.Port.kOnboard));
    }

    public ColorReader(ColorSensorV3 sensor){
        this.sensor=sensor;
    }

    public ColorReading lastReading(){return lastReading; }
    public ColorReading read(){return read(getProcessedRgb());}


    public int[] getRawRgb(){
        return new int[]{
            sensor.getRed(),
            sensor.getGreen(),
            sensor.getBlue()
        };
    }

    public int[] getProcessedRgb(){
        int[] raw = getRawRgb();
        int[] processed = new int[3];
        int highestRaw = 0;

        for(int i=0;i<raw.length;i++)
            if(raw[i]>highestRaw)
                highestRaw=raw[i];

        for(int i=0;i<processed.length;i++)
            processed[i] = raw[i]*100/highestRaw;
        
        return processed;
    }

    public ColorReading read(int[] rgb){
        int lowestDeviation = Integer.MAX_VALUE;
        Color bestColor = Color.Unknown;

        for(int i=0;i<colorValues.length;i++){
            int deviation = 0;
            for(int j=0;j<colorValues[i].length;j++)
                deviation+= Math.abs(colorValues[i][j]-rgb[j]);
            
            if(deviation<lowestDeviation){
                lowestDeviation = deviation;
                bestColor = colors[i];
            }
        }

        ColorReading ret = new ColorReading(bestColor,lowestDeviation);
        lastReading = ret;
        return ret;
    }
}