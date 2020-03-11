package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class IntakeRaise implements Command{
    private static final long duration = 1250;
    private long startTime;

    public IntakeRaise(){}

    public void initialize(){
        // if(Robot.intake.shouldBeDown())
            startTime=System.currentTimeMillis();
    }

    public void execute(){
        if(isFinished())
            Robot.intake.stopIntakeRaise();
        else
            Robot.intake.raiseIntake();
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