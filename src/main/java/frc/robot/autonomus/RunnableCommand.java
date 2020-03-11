package frc.robot.autonomus;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class RunnableCommand implements Command{
    private Runnable runnable;
    public RunnableCommand(Runnable r){
        runnable=r;
    }

    public void initialize(){
        runnable.run();
    }

    public boolean isFinished(){
        return true;
    }

    public Set<Subsystem> getRequirements() {
        return new HashSet<Subsystem>();
    }
}