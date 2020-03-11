package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

public class DriveBase implements Subsystem{
    public static final double encoderConversion = 1/11.25* Math.PI*0.1524; // gear ratio (RPM) * wheel circumference
    public static final double kS=0.118;
    public static final double kV=2.89;
    public static final double kA=0.401;
    public static final double kP=2.6;
    // public static final dobule kP=14.2;
    public static final double trackWidth = 0.7453172062355122;

    public static final double maxSpeed=0.02;
    public static final double maxAccel=0.03;
    public static final double voltageConstraint= 10;

    public static final double ramseteB=0.4; //B=2 and Zeta = 0.7 recommended by website, seem to be very high
    public static final double ramseteZeta=0.01;

    public static final double ramsetePIDkP=0.1;
    public static final double ramsetePIDkI=0;
    public static final double ramsetePIDkD=0;

    // public static final double ramsetePIDkP=14.2;
    // public static final double ramsetePIDkI=0;
    // public static final double ramsetePIDkD=0.1;

    // public static final double ramseteB=2; //B=2 and Zeta = 0.7 recommended by website, seem to be very high
    // public static final double ramseteZeta=0.7;
    

    public CANSparkMax left1,left2,left3,right4,right5,right6;
    private CANSparkMax[] motors;
    public SpeedControllerGroup left,right;
    public DifferentialDrive diffDrive;

    public double highestSpeedSoFar = 0;


    public DifferentialDriveKinematics kinematics,backwardsKinematics;
    public DifferentialDriveOdometry odometry;
    public ADXRS450_Gyro gyro;
    private double leftEncoderZeroing,rightEncoderZeroing;

    public DriveBase(){
        left1  = new CANSparkMax(1, MotorType.kBrushless);
        left2  = new CANSparkMax(2, MotorType.kBrushless);
        left3  = new CANSparkMax(3, MotorType.kBrushless);
        right4 = new CANSparkMax(4, MotorType.kBrushless);
        right5 = new CANSparkMax(5, MotorType.kBrushless);
        right6 = new CANSparkMax(6, MotorType.kBrushless);
        motors = new CANSparkMax[]{left1,left2,left3,right4,right5,right6};

        right4.setInverted(true);
        right5.setInverted(true);
        right6.setInverted(true);

        left = new SpeedControllerGroup(left1, left2, left3);
        right = new SpeedControllerGroup(right4, right5, right6);

        diffDrive = new DifferentialDrive(left, right);
        diffDrive.setRightSideInverted(false);

        for(CANSparkMax m: motors){
            m.setOpenLoopRampRate(0.3);
        }

        initAutoThings();

        CommandScheduler.getInstance().registerSubsystem(this);
    }

    private void initAutoThings(){
        kinematics = new DifferentialDriveKinematics(trackWidth);
        backwardsKinematics = new DifferentialDriveKinematics(trackWidth);
        gyro = new ADXRS450_Gyro();
        odometry = new DifferentialDriveOdometry(getGyroHeading());
        resetOdometry();
    }




    public void periodic(){ //called periodically
        // double speed = Math.abs(robotSpeed());
        // if(speed>highestSpeedSoFar)
            // highestSpeedSoFar = speed;
        // System.out.println("Highest speed so far: "+highestSpeedSoFar+" m/s");
        if(Constants.pathweaver)
            odometry.update(getGyroHeading(), leftEncoderDistance(),rightEncoderDistance());
    }

    public Pose2d backwardsPose2d(){
        Pose2d initial = odometry.getPoseMeters();
        return new Pose2d(-initial.getTranslation().getX(), -initial.getTranslation().getY(), new Rotation2d(initial.getRotation().getRadians()));
    }
    
    private Rotation2d getGyroHeading(){ //gets gryo angle but as a the object type the FRC classes want
        return Rotation2d.fromDegrees(gyro.getAngle());
    }
                    
    public void tankDriveVolts(double leftVolts, double rightVolts) { //sets motor controller voltages
        left.setVoltage(leftVolts);
        right.setVoltage(-rightVolts);
        diffDrive.feed(); //makes the diff drive not complain about not receiving inputs
    }

    public void backwardsTankDriveVolts(double leftVolts, double rightVolts) { //sets motor controller voltages
        left.setVoltage(rightVolts);
        right.setVoltage(-leftVolts);
        diffDrive.feed(); //makes the diff drive not complain about not receiving inputs
    }


    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(leftEncoderRate(), rightEncoderRate());
    }

    public DifferentialDriveWheelSpeeds getBackwardsWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(-rightEncoderRate(),-leftEncoderRate());
    }

    public double leftEncoderDistance(){ //distance in meters traveled by left side
        return encoderConversion*(mean(
            left1.getEncoder().getPosition(),
            left2.getEncoder().getPosition(),
            left3.getEncoder().getPosition()
        )-leftEncoderZeroing);
    }

    public double rightEncoderDistance(){ //like that other one but for the other side
        return encoderConversion*(mean(
            right4.getEncoder().getPosition(),
            right5.getEncoder().getPosition(),
            right6.getEncoder().getPosition()
        )-rightEncoderZeroing);
    }

    public double robotDistance(){ //distance in meters traveled by robot
        return mean(leftEncoderDistance(),rightEncoderDistance());
    }

    public double leftEncoderRate(){ //speed in meters per second of left side
        return encoderConversion*mean(
            left1.getEncoder().getVelocity(),
            left2.getEncoder().getVelocity(),
            left3.getEncoder().getVelocity()
        )/60;
    }

    public double rightEncoderRate(){ //you can probably guess this one
        return -encoderConversion*mean(
            right4.getEncoder().getVelocity(),
            right5.getEncoder().getVelocity(),
            right6.getEncoder().getVelocity()
        )/60;
    }

    public double robotSpeed(){ //speed in m/s of the robot
        return mean(leftEncoderRate(),rightEncoderRate());
    }

    public void resetEncoders() { // resets encoders
        leftEncoderZeroing = mean(left1.getEncoder().getPosition(),left2.getEncoder().getPosition(),left3.getEncoder().getPosition());
        rightEncoderZeroing = mean(right4.getEncoder().getPosition(),right5.getEncoder().getPosition(),right6.getEncoder().getPosition());
    }


    public void resetOdometry(){
        resetGyro();
        resetEncoders();
        odometry.resetPosition(new Pose2d(), new Rotation2d(gyro.getAngle())); 
        //IDK why I need to subtract 1 from the angle but without it it was roughly 1 radian off
    }

    public void resetGyro(){
        gyro.reset();
    }

    private double mean(double... values){ //returns the mean of values
        double sum = 0;
        for(int i=0; i<values.length;i++)
            sum+=values[i];
        return sum/values.length;
    }

    // public Command generateRamsete(Trajectory trajectory){
    //     // Create a voltage constraint to ensure we don't accelerate too fast
    //     // var autoVoltageConstraint =
    //     //     new DifferentialDriveVoltageConstraint(
    //     //         new SimpleMotorFeedforward(kS,kV,kA),
    //     //         kinematics,
    //     //         voltageConstraint );
    
    //     //  Create config for trajectory
    //     //    TrajectoryConfig config = new TrajectoryConfig(maxSpeed,maxAccel)
    //     //    // Add kinematics to ensure max speed is actually obeyed
    //     //    .setKinematics(kinematics)
    //     //    // Apply the voltage constraint
    //     //    .addConstraint(autoVoltageConstraint);

    //     // An example trajectory to follow.  All units in meters.
    //     // Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
    //     //     // Start at the origin facing the +X direction
    //     //     new Pose2d(0, 0, new Rotation2d(0)),
    //     //     // Pass through these two interior waypoints, making an 's' curve path
    //     //     List.of(
    //     //         new Translation2d(1, 1),
    //     //         new Translation2d(2, -1)
    //     //     ),
    //     //     // End 3 meters straight ahead of where we started, facing forward
    //     //     new Pose2d(3, 0, new Rotation2d(0)),
    //     //     // Pass config
    //     //     config
    //     // );
            
    //     RamseteCommand ramseteCommand = new RamseteCommand(
    //         trajectory,
    //         odometry::getPoseMeters,
    //         new RamseteController(ramseteB, ramseteZeta),
    //         new SimpleMotorFeedforward(kS,kV,kA),
    //         kinematics,
    //         this::getWheelSpeeds,
    //         new PIDController(ramsetePIDkP, ramsetePIDkI, ramsetePIDkD),
    //         new PIDController(ramsetePIDkP, ramsetePIDkI, ramsetePIDkD),
    //         // RamseteCommand passes volts to the callback 
    //         this::tankDriveVolts,
    //         this
    //     );

    //     // Run path following command, then stop at the end.
    //     return ramseteCommand.andThen(() -> this.tankDriveVolts(0, 0));
    // }
}