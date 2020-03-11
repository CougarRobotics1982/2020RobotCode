package frc.robot.subsystems;

import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Dump implements Subsystem{
    private static int tolaerance = 1000;
    // private static final double maxSpeed = 0.16;
    private static final double maxSpeed = 0.32;

    public WPI_TalonSRX motor;
    public Encoder encoder;
    public enum DumpPosition{Bottom,Middle, Top};

    private DumpPosition latestTargetPos = DumpPosition.Bottom;

    private static final HashMap<DumpPosition,Integer> encoderPositions = new HashMap<DumpPosition,Integer>();
    static{
        encoderPositions.put(DumpPosition.Bottom,0);
        encoderPositions.put(DumpPosition.Middle,-100000);
        // encoderPositions.put(Position.Top,-165000);
        encoderPositions.put(DumpPosition.Top,-200000);

    }

    // private static final double kP = 0.00004;
    private static final double kP = 0.00002;



    public Dump(){
        motor = new WPI_TalonSRX(8);
        encoder = new Encoder(0,1);

        CommandScheduler.getInstance().registerSubsystem(this);
    }

    public void gotoPos(DumpPosition p){
        latestTargetPos = p;
        gotoPos(encoderPositions.get(p));
    }

    private void gotoPos(int setpoint){
        double pos = getEncoderPos();
        double diff = setpoint-pos;
        double speed = kP*diff;
        if(Math.abs(speed)>maxSpeed)
            speed = Math.copySign(maxSpeed, speed);

        if(Math.abs(diff)>tolaerance)
            motor.set(speed-0.05);
        else
            motor.set(0);
    }

    public double getEncoderPos(){
        return motor.getSelectedSensorPosition();
    }

    public void zeroEncoder(){
        encoder.reset();
    }

    public DumpPosition getTargetPosition(){
        return latestTargetPos;
    }
}