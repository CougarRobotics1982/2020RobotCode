package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;

public class HangerExtend implements Command{
    private static final long delayUntilRaise = 300;
    private static final long delayUntilStop = 3500; //TODO timing

    long startTime;


    public HangerExtend(){
    }
    public void initialize(){
        startTime = System.currentTimeMillis();
    }
    public void execute(){
        Robot.hanger.unlock();
        if (System.currentTimeMillis() - startTime >= delayUntilRaise && System.currentTimeMillis()-startTime<=delayUntilStop){
            Robot.hanger.extend();
        }else{
            Robot.hanger.stopMotor();
        }
    }

    public void end(){
        Robot.hanger.stopMotor();
        Robot.hanger.lock();
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