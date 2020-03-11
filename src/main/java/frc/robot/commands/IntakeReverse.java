package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class IntakeReverse implements Command{
    public IntakeReverse(){}

    public void execute(){
        Robot.intake.chuteReverse();
        Robot.intake.intakeReverse();
    }
    
    public void end(){
        Robot.intake.chuteStop();
        Robot.intake.intakeStop();
    }
    
    public boolean isFinished() {
        return false;
    }

    public Set<Subsystem> getRequirements() {
        Set<Subsystem> r = new HashSet<Subsystem>();
        r.add(Robot.intake);
        return r;
    }
}