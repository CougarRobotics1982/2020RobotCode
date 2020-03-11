package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;
import frc.robot.subsystems.Dump;
import frc.robot.subsystems.Dump.DumpPosition;

public class DumperControl implements Command{
    private DumpPosition position;

    public DumperControl(Dump.DumpPosition position){
        this.position=position;
    }

    public DumpPosition getTargetPosition(){
        return position;
    }

    public void execute(){
        Robot.dump.gotoPos(position);
    }

    public boolean isFinished() {
        return false;
    }

    public Set<Subsystem> getRequirements() {
        Set<Subsystem> r = new HashSet<Subsystem>();
        r.add(Robot.dump);
        return r;
    }

    public String toString(){
        return "Dumper Control ("+position+") @"+Integer.toHexString(hashCode());
    }
}