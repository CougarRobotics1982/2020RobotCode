package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.*;
import frc.robot.subsystems.Dump.DumpPosition;;

public class Oi{
    public static final int controllerReverseButton = 1;

    private Joystick controller;
    private Joystick opBox;

    public Oi(){
        initController();
        if(Constants.operatorBox)
            initOpBox();
    }

    class CommandButton extends Trigger{
        private Joystick controller;
        private int button;
        public CommandButton(Joystick controller, int button){
            this.controller=controller;
            this.button=button;
        }
        public CommandButton(Joystick controller, int button, Command command){
            this(controller,button);
            whileActiveOnce(command);
        }

        public boolean get(){
            return controller.getRawButton(button);
        }
    }

    private void initController(){
        controller = new Joystick(0);
        new CommandButton(controller,5,new IntakeSlurp());

        new CommandButton(controller,6,new DumperControl(DumpPosition.Top));
        new CommandButton(controller,10,new DumperControl(DumpPosition.Middle));
    }

    private void initOpBox(){
        opBox = new Joystick(1);
        new CommandButton(opBox,5,new HangerExtend());//RB
        new CommandButton(opBox,7,new HangerClimb());//RT

        new CommandButton(opBox,8,new IntakeLower());//RT
        new CommandButton(opBox,6,new IntakeRaise());//RB

        new CommandButton(opBox,2,new IntakeSlurp());//A
        new CommandButton(opBox,3,new IntakeReverse());//B

        new CommandButton(opBox,1,new DumperControl(DumpPosition.Top));//X
        new CommandButton(opBox,4,new DumperControl(DumpPosition.Middle));//Y
    }

    public double getStickY(){
        if(controller.getRawButton(controllerReverseButton)){ //if the fourth button is pressed
            return -controller.getY(); //backwards
        }else{
            return controller.getY(); //forwards
        }
    }

    public double getStickTwist(){
        return controller.getTwist();
    }

    public double getStickX(){
        return controller.getX();
    }

    public Joystick getController(){
        return controller;
    }

    public Joystick getOpBox(){
        return opBox;
    }
}