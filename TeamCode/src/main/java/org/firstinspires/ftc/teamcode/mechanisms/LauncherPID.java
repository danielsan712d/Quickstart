package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.robotcore.internal.hardware.android.GpioPin;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class LauncherPID implements Subsystem {
    public static final LauncherPID INSTANCE = new LauncherPID();
    private LauncherPID() { }

//    double targetRPM = -2000.0;
    public static double kP = 0.1;
    public static double kI = 0.0;
    public static double kD = 0.00;
    boolean isStopped = true;
//    double farVel = -1540.0;
//    double closeVel = -1270.0;
//    double encoderTPR = 28.0;
    //double targetTPS = targetRPM * encoderTPR / 60.0; //Target Ticks per Second

//    MotorGroup launcherGroup = new MotorGroup(
//            new MotorEx("launcher").reversed().floatMode(),
//            new MotorEx("launcher").reversed().floatMode()
//    );
    private final MotorEx launcherMotor = new MotorEx("launcher").floatMode();

    private ControlSystem controller;

    //public Command startLauncher = new RunToVelocity(controlSystem, targetTPS);
    public Command startLauncher = new SetPower(launcherMotor, 0.6);
    public Command stopLauncher = new InstantCommand(() -> { isStopped = true; });

//    public Command farLauncher = new RunToVelocity(controller, -1540.0);
//    public Command closeLauncher = new RunToVelocity(controller, -1270.0);

    public Command setLauncherPower(double power) {
        return new SetPower(launcherMotor, power);
    }

//    public Command setLauncherRPM(double RPM) { return new InstantCommand(() -> {
//        double velGoal = RPM * encoderTPR / 60.0;
//        ActiveOpMode.telemetry().addData("Launcher Goal:", velGoal);
//        ActiveOpMode.telemetry().addData("Launcher Goal RPM:", RPM);
//        ActiveOpMode.telemetry().update();*/
//        controller.setGoal(new KineticState(0.0, velGoal));});}

    public Command setLauncherVel(double vel) {
        isStopped = false;
        return new RunToVelocity(controller, vel);
    }

//    public double getLauncherRPM() {
//        return launcherMotor.getVelocity() * 60.0 / encoderTPR;
//    }


    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .velPid(kP,kI,kD)
                .build();

        controller.setGoal(new KineticState(0.0));

        launcherMotor.setPower(controller.calculate(launcherMotor.getState()));
//        ActiveOpMode.telemetry().addData("Launcher Speed:",launcherMotor.getState());
//        ActiveOpMode.telemetry().update();
    }

    @Override
    public void periodic() {
        if ( isStopped) {
            launcherMotor.setPower(0.0);
        } else {
            launcherMotor.setPower(controller.calculate(launcherMotor.getState()));
        }
        ActiveOpMode.telemetry().addData("Launcher Vel:",launcherMotor.getVelocity());
//        ActiveOpMode.telemetry().update();
    }

}
