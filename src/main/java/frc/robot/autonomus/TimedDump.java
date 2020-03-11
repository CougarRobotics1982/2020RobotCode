package frc.robot.autonomus;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;
import frc.robot.subsystems.Dump.DumpPosition;

public class TimedDump implements Command {
    private static final long delay = 1500;
    private long finishTime;

    public TimedDump(){}

    public void initialize(){
        finishTime = System.currentTimeMillis()+delay;
    }

    public void execute(){
        Robot.dump.gotoPos(DumpPosition.Top);
    }

    public boolean isFinished(){
        return System.currentTimeMillis()>finishTime;
    }

    public Set<Subsystem> getRequirements() {
        Set<Subsystem> r = new HashSet<Subsystem>();
        r.add(Robot.dump);
        return r;
    }
}