package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;
import frc.robot.subsystems.*;
import frc.robot.Oi;

public class DriveEnable implements Command {
    private Oi oi = Robot.oi;
    private DriveBase driveBase = Robot.driveBase;

    public DriveEnable(){
    }

    public void execute() {
        double speedMultiplier=1;
        double turnMultiplier =0.6;
        if(slowMode()){
            double slowFactor = 0.6;
            speedMultiplier*=slowFactor;
            turnMultiplier*=slowFactor;
        }
        driveBase.diffDrive.arcadeDrive(oi.getStickY()*speedMultiplier, oi.getStickTwist()*turnMultiplier);
    }

    private boolean slowMode(){
        return oi.getController().getRawButton(4);
    }

    public boolean isFinished() {
        return false;
    }

    public Set<Subsystem> getRequirements() {
        Set<Subsystem> r = new HashSet<Subsystem>();
        r.add(Robot.driveBase);
        return r;
    }
}