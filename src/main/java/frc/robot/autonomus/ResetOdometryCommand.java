package frc.robot.autonomus;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class ResetOdometryCommand implements Command{
    public ResetOdometryCommand(){}

    public void initialize(){
        Robot.driveBase.resetOdometry();
    }

    public boolean isFinished(){
        return true;
    }

    public Set<Subsystem> getRequirements() {
        return new HashSet<Subsystem>();
    }
}