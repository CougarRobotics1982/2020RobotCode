package frc.robot.subsystems;

// import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Intake implements Subsystem{
    public WPI_VictorSPX chuteMotor,intakeMotor,intakeRaiser;
    public enum IntakePosition{Down,Up};
    private IntakePosition intakePos = IntakePosition.Up;
    private boolean intakeRaiserInUse = false;

    private static final double intakeRaiserHoldUpPower  = 0; //TODO These Values
    private static final double intakeRaiserHoldDownPower= 0;

    public Intake(){
        chuteMotor = new WPI_VictorSPX(9);
        intakeMotor = new WPI_VictorSPX(10);
        intakeRaiser = new WPI_VictorSPX(11);

        CommandScheduler.getInstance().registerSubsystem(this);
    }

    public void chuteIntake(){  chuteMotor.set(1);  }
    public void chuteStop(){    chuteMotor.set(0);  }
    public void chuteReverse(){ chuteMotor.set(-1); }

    public void intakeStop(){   intakeMotor.set(0); }
    public void intakeSpin(){
        if(intakePos==IntakePosition.Down)
            intakeMotor.set(1); //TODO Direction
        else
            intakeStop();
    }
    public void intakeReverse(){
        if(intakePos==IntakePosition.Down)
            intakeMotor.set(-1); //TODO Direction
        else
            intakeStop();
    }

    public void periodic(){
        if(!intakeRaiserInUse){
            switch(intakePos){
                case Up:
                    intakeRaiser.set(intakeRaiserHoldUpPower);
                    break;
                case Down:
                    intakeRaiser.set(intakeRaiserHoldDownPower);
                    break;
            }
        }
    }

    public void lowerIntake(){
        intakeRaiserInUse=true;
        intakeRaiser.set(-0.4); //TODO Direction
        intakePos = IntakePosition.Down;
    }

    public void raiseIntake(){
        intakeRaiserInUse=true;
        intakeRaiser.set(0.4); //TODO Direction
        intakePos = IntakePosition.Up;
    }

    public void stopIntakeRaise(){
        intakeRaiserInUse=false;
        intakeRaiser.set(0);
    }

    public boolean shouldBeUp(){
        return intakePos==IntakePosition.Up;
    }
    public boolean shouldBeDown(){
        return intakePos==IntakePosition.Down;
    }
}