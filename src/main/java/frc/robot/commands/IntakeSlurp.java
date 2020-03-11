package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class IntakeSlurp implements Command{
    public IntakeSlurp(){}
    
    public void execute(){
        Robot.intake.chuteIntake();
        Robot.intake.intakeSpin();
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