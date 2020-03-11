package frc.robot.autonomus;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Robot;
import frc.robot.subsystems.DriveBase;

public class PathWeaverCommandGenerator {
    private static final String[] pathNames = {"CurveLeftToGoal","CurveRightToGoal","StraightToGoal","Unnamed"};
    private static final HashMap<String,Command> autoPaths = new HashMap<String,Command>();
   
    public static void generateAllPaths(){
        System.out.println("Generating Auto Paths");
        long startTime = System.currentTimeMillis();
        for(String name: pathNames){
            try {
                Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve("/home/lvuser/deploy/output/"+name+".wpilib.json");
                Trajectory trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
                System.out.println("Made path '"+name+"'");
                autoPaths.put(name, generateRamsete(trajectory,true));
            } catch (IOException ex) {
                DriverStation.reportError("Unable to open trajectory: " + name, ex.getStackTrace());
            }
        }
        long finishTime = System.currentTimeMillis()-startTime;
        System.out.println("Done making paths, time taken: "+finishTime+" ms\n");
    }

    public static Command getPath(String name){
        return autoPaths.get(name);
    }

    private static Command generateRamsete(Trajectory trajectory, boolean reverse){
        if(reverse){
            return generateBackwardsRamsete(trajectory);
        }else{
            return generateRamsete(trajectory);
        }
    }

    private static Command generateRamsete(Trajectory trajectory){
        RamseteCommand ramseteCommand = new RamseteCommand(
            trajectory,
            Robot.driveBase.odometry::getPoseMeters,
            new RamseteController(DriveBase.ramseteB, DriveBase.ramseteZeta),
            new SimpleMotorFeedforward(DriveBase.kS,DriveBase.kV,DriveBase.kA),
            Robot.driveBase.kinematics,
            Robot.driveBase::getWheelSpeeds,
            new PIDController(DriveBase.ramsetePIDkP, DriveBase.ramsetePIDkI, DriveBase.ramsetePIDkD), //left side
            new PIDController(DriveBase.ramsetePIDkP, DriveBase.ramsetePIDkI, DriveBase.ramsetePIDkD), //right side
            // RamseteCommand passes volts to the callback 
            Robot.driveBase::tankDriveVolts, //method that makes wheels go
            Robot.driveBase //requirements
        );

        // Run path following command, then stop at the end.
        return ramseteCommand.andThen(() -> Robot.driveBase.tankDriveVolts(0, 0));
    }

    private static Command generateBackwardsRamsete(Trajectory trajectory){
        RamseteCommand ramseteCommand = new RamseteCommand(
            trajectory,
            Robot.driveBase::backwardsPose2d,
            // Robot.driveBase.odometry::getPoseMeters,
            new RamseteController(DriveBase.ramseteB, DriveBase.ramseteZeta),
            new SimpleMotorFeedforward(DriveBase.kS,DriveBase.kV,DriveBase.kA),
            Robot.driveBase.backwardsKinematics,
            Robot.driveBase::getBackwardsWheelSpeeds,
            new PIDController(DriveBase.ramsetePIDkP, DriveBase.ramsetePIDkI, DriveBase.ramsetePIDkD), //left side
            new PIDController(DriveBase.ramsetePIDkP, DriveBase.ramsetePIDkI, DriveBase.ramsetePIDkD), //right side
            // RamseteCommand passes volts to the callback 
            Robot.driveBase::backwardsTankDriveVolts, //method that makes wheels go
            Robot.driveBase //requirements
        );

        // Run path following command, then stop at the end.
        return ramseteCommand.andThen(() -> Robot.driveBase.backwardsTankDriveVolts(0, 0));
    }
}