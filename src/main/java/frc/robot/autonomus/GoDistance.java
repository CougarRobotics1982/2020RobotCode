package frc.robot.autonomus;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class GoDistance implements Command {
    private static final double tolerance = 0.1;
    private static final double kP = 0.5;
    // private static final double kP = 0.25;

    private double startDistance,distance;
    private boolean finished = false;
    private long momentFinished;
    private int sequentialCyclesStopped;

    public GoDistance(double meters){
        distance=meters;
    }

    public void initialize(){
        finished = false;
        momentFinished=Long.MAX_VALUE;
        sequentialCyclesStopped=0;
        startDistance=Robot.driveBase.robotDistance();
    }

    public void execute(){
        double diff = distance+startDistance-Robot.driveBase.robotDistance();
        if(Math.abs(Robot.driveBase.robotSpeed())<0.05)
            sequentialCyclesStopped++;
        else
            sequentialCyclesStopped=0;
        if(Math.abs(diff)<tolerance || sequentialCyclesStopped>=75){//1.5 seconds
            if(!finished)
                momentFinished = System.currentTimeMillis();
            finished=true;
        }
        Robot.driveBase.diffDrive.arcadeDrive(kP*diff, 0,false);
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