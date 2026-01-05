package org.firstinspires.ftc.teamcode.mechanisms;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class LauncherPID implements Subsystem {
    public static final LauncherPID INSTANCE = new LauncherPID();
    private LauncherPID() { }

    double targetRPM = -2000.0;
    double encoderTPR = 28.0;
    double targetTPS = targetRPM * encoderTPR / 60.0; //Target Ticks per Second

    double launcherPower = 0.7;


//    MotorGroup launcherGroup = new MotorGroup(
//            new MotorEx("top_launcher").reversed().floatMode(),
//            new MotorEx("bottom_launcher").reversed().floatMode()
//    );
    private final MotorEx launcherMotor = new MotorEx("bottom_launcher").reversed().floatMode();

    private ControlSystem controller;

    //public Command startLauncher = new RunToVelocity(controlSystem, targetTPS);
    public Command startLauncher = new SetPower(launcherMotor, 0.6);
    public Command stopLauncher = new SetPower(launcherMotor, 0.0);

    public Command farLauncher = new SetPower(launcherMotor, 0.8);

    public Command setLauncherPower(double power) {
        return new SetPower(launcherMotor, power);
    }

    public Command setLauncherRPM(double RPM) { return new InstantCommand(() -> {
        double velGoal = RPM * encoderTPR / 60.0;
        ActiveOpMode.telemetry().addData("Launcher Goal:", velGoal);
        ActiveOpMode.telemetry().update();
        controller.setGoal(new KineticState(0.0, velGoal));});}

    public double getLauncherRPM() {
        return launcherMotor.getVelocity() * 60.0 / encoderTPR;
    }


    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .velPid(0.002,0,0)
                .build();

        controller.setGoal(new KineticState(0.0));

        launcherMotor.setPower(controller.calculate(launcherMotor.getState()));
        ActiveOpMode.telemetry().addData("Launcher Speed:",launcherMotor.getState());
        ActiveOpMode.telemetry().update();
    }

//    public Command increaseLauncherPower = new LambdaCommand()
//            .setStart(())

    @Override
    public void periodic() {
        launcherMotor.setPower(controller.calculate(launcherMotor.getState()));
        ActiveOpMode.telemetry().addData("Launcher Speed:",launcherMotor.getState());
        ActiveOpMode.telemetry().update();
    }

//public LauncherPID(HardwareMap hwMap, Telemetry telemetry) {
//        this.launcherMotor = hwMap.get(DcMotor.class, "flywheel");
//        this.telemetry = telemetry;
//}

}
