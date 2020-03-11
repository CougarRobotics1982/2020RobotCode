package frc.robot.autonomus;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class TurnAngle implements Command {
    private static final double tolerance = 1;
    private static final double kP = 0.015;
    private double startAngle,angle;
    private boolean finished = false;
    private long momentFinished;


    public TurnAngle(double degrees){ //clockwise
        angle=degrees;
    }

    public void initialize(){
        startAngle=Robot.driveBase.gyro.getAngle();
        momentFinished=Long.MAX_VALUE;
        finished=false;
    }

    public void execute(){
        double diff = angle - (Robot.driveBase.gyro.getAngle()-startAngle);
        System.out.println(diff);
        if(Math.abs(diff)<tolerance){
            if(!finished)
                momentFinished=System.currentTimeMillis();
            finished=true;
        }
        Robot.driveBase.diffDrive.arcadeDrive(0,kP*diff,false);
    }

    public void end(){
        Robot.driveBase.diffDrive.arcadeDrive(0, 0);
    }

    public boolean isFinished(){
        return finished&&System.currentTimeMillis()-momentFinished>100;
    }

    public Set<Subsystem> getRequirements() {
        Set<Subsystem> r = new HashSet<Subsystem>();
        r.add(Robot.driveBase);
        return r;
    }
}