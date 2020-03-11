package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Hanger implements Subsystem{
    public WPI_TalonSRX motor;
    public Servo servo;

    public Hanger(){
        motor = new WPI_TalonSRX(7);
        servo = new Servo(0);
        CommandScheduler.getInstance().registerSubsystem(this);
    }

    public void extend(){
        motor.set(-1);
    }

    public void stopMotor(){
        motor.set(0);
    }

    public void climb(){
        motor.set(1);
    }

    public void lock(){
        servo.setPosition(1);
    }

    public void unlock(){
        servo.setPosition(0);
    }
}