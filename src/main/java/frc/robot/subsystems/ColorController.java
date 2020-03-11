package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.color.*;

public class ColorController implements Subsystem{
    public ColorReader reader;
    public ColorCounter counter;

    public ColorController(){
        reader = new ColorReader();
        counter = new ColorCounter(reader);
        CommandScheduler.getInstance().registerSubsystem(this);
    }

    public static Color getPositionTarget(){
        String s = DriverStation.getInstance().getGameSpecificMessage();
        if(s.length()!=1)
            return Color.Unknown;

        char code = s.charAt(0);

        switch(code){
            case 'R':
                return Color.Red;
            case 'G':
                return Color.Green;
            case 'B':
                return Color.Blue;
            case 'Y':
                return Color.Yellow;
            default:
                return Color.Unknown;
        }
    }
}