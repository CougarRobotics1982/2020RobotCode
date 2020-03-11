package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class IntakeLower implements Command{
    private static final long duration = 1250;
    private long startTime;

    public IntakeLower(){}

    public void initialize(){
        // if(Robot.intake.shouldBeUp())
            startTime=System.currentTimeMillis();
    }

    public void execute(){
        if(isFinished())
            Robot.intake.stopIntakeRaise();
        else
            Robot.intake.lowerIntake();
    }
    
    public void end(){
        Robot.intake.stopIntakeRaise();
    }
    
    public boolean isFinished() {
        return System.currentTimeMillis()>startTime+duration;
    }

    public Set<Subsystem> getRequirements() {
        Set<Subsystem> r = new HashSet<Subsystem>();
        r.add(Robot.intake);
        return r;
    }
}