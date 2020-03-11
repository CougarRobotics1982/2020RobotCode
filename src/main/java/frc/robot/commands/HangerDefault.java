package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class HangerDefault implements Command{
    public HangerDefault(){
    }

    public void execute(){
        Robot.hanger.lock();
        Robot.hanger.stopMotor();
    }

    public boolean isFinished() {
        return false;
    }

    public Set<Subsystem> getRequirements() {
        Set<Subsystem> r = new HashSet<Subsystem>();
        r.add(Robot.hanger);
        return r;
    }
}