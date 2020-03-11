package frc.robot.autonomus;

import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;

public class AutoCommands{
    private static final HashMap<Command,Command> sequenceMap = new HashMap<Command,Command>();
    private static HashMap<String,Command> availableCommands = new HashMap<String,Command>();
    private static SendableChooser<Command> autoNameChooser = new SendableChooser<Command>();

    public static Command test,basicScoringAuto,lineCrossAuto,basicShiftLeftScore,basicShiftRightScore;

    public static void generateCommands(){
        if(Constants.pathweaver){
            PathWeaverCommandGenerator.generateAllPaths();
        }

        test = stitchSequential(
            new GoDistance(-0.3),
            new TimedDump()
        );
        availableCommands.put("Test", test);

        basicShiftLeftScore = stitchSequential(
            new GoDistance(-1),
            new TurnAngle(-45),
            new GoDistance(-Math.sqrt(2)),
            new TurnAngle(45),
            new GoDistance(-1.5),
            new TimedDump()
        );
        availableCommands.put("Shift Left And Score", basicShiftLeftScore);

        basicShiftRightScore = stitchSequential(
            new GoDistance(-1),
            new TurnAngle(45),
            new GoDistance(-Math.sqrt(2)),
            new TurnAngle(-45),
            new GoDistance(-1.5),
            new TimedDump()
        );
        availableCommands.put("Shift Right And Score", basicShiftRightScore);

        basicScoringAuto = stitchSequential(
            new GoDistance(-3.2),
            new TimedDump()
        );
        availableCommands.put("Straight Forward And Score", basicScoringAuto);

        lineCrossAuto = new GoDistance(-2);
        availableCommands.put("Line Cross", lineCrossAuto);

        if(Constants.useAutoSelector)
            sendToSmartDash();
    }

    public static void sendToSmartDash(){
        for(String autoName: availableCommands.keySet())
            autoNameChooser.addOption(autoName,availableCommands.get(autoName));
        SmartDashboard.putData("Auto commands",autoNameChooser);
        SmartDashboard.updateValues();
    }

    public static Command getAutoCommand(){
        // return pathAndDump("Unnamed");
        if(Constants.useAutoSelector){
            SmartDashboard.updateValues();
            return autoNameChooser.getSelected();
            // return SmartDashboard.
        }
        return basicShiftRightScore;
        // return basicScoringAuto;
        // return delay(basicScoringAuto, 1000);
    }

    public static Command delay(Command c, long milliseconds){
        return stitchSequential(
            new Delay(milliseconds),
            c
        );
    }

    public static Command pathAndDump(String name){
        return stitchSequential(new Command[]{
            new ResetOdometryCommand(),
            PathWeaverCommandGenerator.getPath(name),
            new TimedDump()
        });
    }


    
    public static Command stitchSequential(Command... commands){
        // Command firstCommand = commands[0].andThen(()->CommandScheduler.getInstance().schedule(commands[1]),(Subsystem[])commands[0].getRequirements().toArray());
    
        for(int i=0;i<commands.length-1;i++){
            if(i<commands.length+1)
                sequenceMap.put(commands[i],commands[i+1]);
        }
 
        
        CommandScheduler.getInstance().onCommandFinish(
            (command)->{
                if(sequenceMap.containsKey(command))
                CommandScheduler.getInstance().schedule(sequenceMap.get(command));
            }
        );
        
        return commands[0];
    }

}