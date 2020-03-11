package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.autonomus.AutoCommands;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Dump.DumpPosition;

public class Robot extends TimedRobot {
  private static final int cameraResX = 320;
  private static final int cameraResY = 240;

  public static Hanger hanger;
  public static DriveBase driveBase;
  public static Dump dump;
  public static Intake intake;
  public static Oi oi;
  public static ColorController colorController;

  private long enabledTime;
  private UsbCamera cam1,cam2;
  // private NetworkTableEntry cameraSelection;
  private VideoSink camServer;
  private boolean lastJoystickTrigger;

  // private Command autonomousCommand;
  
  public void robotInit() {
    oi = new Oi();
    
    dump = new Dump();
    intake = new Intake();
    driveBase = new DriveBase();
    hanger = new Hanger();

    if(Constants.colorSensor)
      colorController = new ColorController();

    CommandScheduler.getInstance().setDefaultCommand(dump, new DumperControl(DumpPosition.Bottom));
    CommandScheduler.getInstance().setDefaultCommand(intake,new IntakeStop());
    CommandScheduler.getInstance().setDefaultCommand(driveBase,new DriveEnable());
    CommandScheduler.getInstance().setDefaultCommand(hanger,new HangerDefault());
    
    AutoCommands.generateCommands();

    if(Constants.doPrintScheduledCommands)
      CommandScheduler.getInstance().onCommandInitialize((command)->{
        System.out.println("Command started at t="+secondsSinceEnabled()+" : " + command);
      });

    cam1 = CameraServer.getInstance().startAutomaticCapture(0);
    cam2 = CameraServer.getInstance().startAutomaticCapture(1);

    // cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
    camServer=CameraServer.getInstance().getServer();

    // cam1.setFPS(24);
    // cam2.setFPS(24);
    // cam1.setResolution(cameraRes, cameraRes);
    // cam2.setResolution(cameraRes, cameraRes);

    cam1.setVideoMode(PixelFormat.kMJPEG, cameraResX, cameraResY, 24);
    cam2.setVideoMode(PixelFormat.kMJPEG, cameraResX, cameraResY, 24);    
  }

  public void robotPeriodic(){
    boolean joystickTrigger = oi.getController().getRawButton(1);
    if(joystickTrigger!=lastJoystickTrigger){
      if(joystickTrigger){
        // cameraSelection.setString(cam1.getName());
        camServer.setSource(cam1);
      }else{
        // cameraSelection.setString(cam2.getName());
        camServer.setSource(cam2);
      }
    }
    lastJoystickTrigger=joystickTrigger;
  }

  public void teleopInit(){
    enabledTime = System.currentTimeMillis();
    CommandScheduler.getInstance().cancelAll();
  }

  public void teleopPeriodic() {
    printStuff();
    CommandScheduler.getInstance().run();
  }


  public void autonomousInit(){
    enabledTime=System.currentTimeMillis();
    CommandScheduler.getInstance().schedule(AutoCommands.getAutoCommand());
  }
  public void autonomousPeriodic(){
    CommandScheduler.getInstance().run();
  }

  public void disabledInit(){
    CommandScheduler.getInstance().cancelAll();
    // intake.stopIntakeRaise();
    driveBase.diffDrive.arcadeDrive(0, 0);
  }

  public double secondsSinceEnabled(){
    return (double)(System.currentTimeMillis()-enabledTime)/1000;
  }

  private void printStuff(){
    if(Constants.doInfoPrints){
      System.out.println("Color Targert: "+ColorController.getPositionTarget());

      if(Constants.colorSensor){
        System.out.println("\tColor reading: "+colorController.reader.read().getResult());
        System.out.println("\tColor count: "+colorController.counter.getCounter());
        System.out.println("\tColor count: "+colorController.counter.getRotations());
      }
    
      System.out.println("Dump Position: "+dump.getEncoderPos());
      
      System.out.println("Left encoder Distance: "+driveBase.leftEncoderDistance());
      System.out.println("Right encoder Distance: "+driveBase.rightEncoderDistance());


      System.out.println("\n");
    }
  }
}