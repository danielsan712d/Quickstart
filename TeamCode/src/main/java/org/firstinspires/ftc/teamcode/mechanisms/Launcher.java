package org.firstinspires.ftc.teamcode.mechanisms;

import java.util.ResourceBundle;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Launcher implements Subsystem {
    public static final Launcher INSTANCE = new Launcher();
    private Launcher() { }

    double targetRPM = -2000.0;
    double encoderTPR = 28.0;
    double targetTPS = targetRPM * encoderTPR / 60.0; //Target Ticks per Second

    double launcherPower = 0.7;


//    MotorGroup launcherGroup = new MotorGroup(
//            new MotorEx("top_launcher").reversed().floatMode(),
//            new MotorEx("bottom_launcher").reversed().floatMode()
//    );
    private final MotorEx launcherMotor = new MotorEx("bottom_launcher").reversed().floatMode();

//    private final ControlSystem controlSystem = ControlSystem.builder()
//            .velPid(0.005,0,0)
//            .build();

    //public Command startLauncher = new RunToVelocity(controlSystem, targetTPS);
    public Command startLauncher = new SetPower(launcherMotor, 0.6);
    public Command stopLauncher = new SetPower(launcherMotor, 0.0);

    public Command farLauncher = new SetPower(launcherMotor, 0.8);

    public Command setLauncherPower(double power) {
        return new SetPower(launcherMotor, power);
    }

//    public Command increaseLauncherPower = new LambdaCommand()
//            .setStart(())

//    @Override
//    public void periodic() {
//        launcherGroup.setPower(controlSystem.calculate(launcherGroup.getState()));
//    }


}
