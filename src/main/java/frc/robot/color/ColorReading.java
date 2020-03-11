package frc.robot.color;

public class ColorReading{
    private static final int tolerance = 20;
    private static final int expiry = 500; //ms

    private int uncertainty;
    private Color color;
    private long creationTime;

    public ColorReading(Color color, int uncertainty){
        this.color=color;
        this.uncertainty=uncertainty;
        creationTime=System.currentTimeMillis();
    }

    public int getUncertainty(){return uncertainty;}
    public Color getClosestColor(){ return color; }
    public boolean isConfident(){ return uncertainty<tolerance;}
    
    public boolean isKnown(){
        return isConfident() && color!=Color.Unknown;
    }

    public Color getResult(){
        if(isConfident())
            return getClosestColor();
        else
            return Color.Unknown;
    }

    public boolean isExpired(){
        return age()>expiry;
    }
    public long age(){
        return System.currentTimeMillis()-creationTime;
    }
}
