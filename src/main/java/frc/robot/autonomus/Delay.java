package frc.robot.autonomus;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Delay implements Command{
    private long delay;
    private long finishTime;

    public Delay(long milliseconds){
        delay=milliseconds;
    }

    public void initialize(){
        finishTime=System.currentTimeMillis()+delay;
    }

    public boolean isFinished(){
        return System.currentTimeMillis()>finishTime;
    }

    public Set<Subsystem> getRequirements() {
        return new HashSet<Subsystem>();
    }
}