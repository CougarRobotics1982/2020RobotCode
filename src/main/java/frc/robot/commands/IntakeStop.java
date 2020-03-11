package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class IntakeStop implements Command{
    public IntakeStop(){
    }
    
    public void execute(){
        Robot.intake.chuteStop();
        Robot.intake.intakeStop();
        Robot.intake.stopIntakeRaise();
    }

    public Set<Subsystem> getRequirements() {
        Set<Subsystem> r = new HashSet<Subsystem>();
        r.add(Robot.intake);
        return r;
    }
}