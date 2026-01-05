package org.firstinspires.ftc.teamcode.teleop;

import static dev.nextftc.bindings.Bindings.button;
import static dev.nextftc.bindings.Bindings.range;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherPID;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;


@TeleOp(name = "NextFTC Pedro Teleop")
public class PedroNextTeleopPID extends NextFTCOpMode {
    boolean intakeOn = false;

    double launcherRPMGoal = 750;
    double launcherRPM;

    public PedroNextTeleopPID(){
        addComponents(
                new SubsystemComponent(Feeder.INSTANCE, Intake.INSTANCE, LauncherPID.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }

    @Override
    public void onUpdate() {
        BindingManager.update();
        launcherRPM = LauncherPID.INSTANCE.getLauncherRPM();
        telemetry.addData("Launcher RPM", launcherRPM);
        telemetry.update();
    }

    @Override
    public void onStop() {
        BindingManager.reset();
    }

    @Override
    public void onInit() {
        Feeder.INSTANCE.down.schedule();
    }

    @Override
    public void onStartButtonPressed() {
        Range drivePower = range(() -> -0.8 * gamepad1.left_stick_y);
        Range strafePower = range(() -> -0.8 * gamepad1.left_stick_x);
        Range turnPower = range(() -> -0.6 * gamepad1.right_stick_x);

        DriverControlledCommand driverControlled = new PedroDriverControlled(
                drivePower,
                strafePower,
                turnPower
        );
        driverControlled.schedule();

        Gamepads.gamepad2().rightBumper()
                .whenBecomesTrue(Feeder.INSTANCE.up);
        Button intakeButton = button(() -> gamepad2.x)
                .toggleOnBecomesTrue()
                .whenBecomesTrue(() -> Intake.INSTANCE.startIntake.schedule())
                .whenBecomesFalse(() -> Intake.INSTANCE.stopIntake.schedule());
        Button reverseIntakeButton = button(() -> gamepad2.a)
                .toggleOnBecomesTrue()
                .whenBecomesTrue(() -> Intake.INSTANCE.reverseIntake.schedule())
                .whenBecomesFalse(() -> Intake.INSTANCE.stopIntake.schedule());
        Button launcherButton = button(() -> gamepad2.y)
                .toggleOnBecomesTrue()
                .whenBecomesTrue(() -> {
                    LauncherPID.INSTANCE.setLauncherRPM(launcherRPMGoal).schedule();
                    telemetry.addData("Launcher RPM Goal", launcherRPMGoal );
                    telemetry.update();
                })
                .whenBecomesFalse(() -> {
                    LauncherPID.INSTANCE.setLauncherRPM(0.0).schedule();
                    telemetry.addData("Launcher RPM Goal", 0.0 );
                    telemetry.update();
                });

        Button increaseLauncherSpeed = button(() -> gamepad2.dpad_up).and(() -> gamepad2.left_bumper)
                .whenBecomesTrue(() -> {
                    launcherRPMGoal += 10.0;
                    LauncherPID.INSTANCE.setLauncherRPM(launcherRPMGoal).schedule();
                    telemetry.addData("Launcher RPM Goal", launcherRPMGoal );
                    telemetry.update();
                });
        Button decreaseLauncherSpeed = button(() -> gamepad2.dpad_down).and(() -> gamepad2.left_bumper)
                .whenBecomesTrue(() -> {
                    launcherRPMGoal -= 10.0;
                    LauncherPID.INSTANCE.setLauncherRPM(launcherRPMGoal).schedule();
                    telemetry.addData("Launcher RPM Goal", launcherRPMGoal );
                    telemetry.update();
                });

//        Button farLauncherButton = button(() -> gamepad2.b)
//                .toggleOnBecomesTrue()
//                .whenBecomesTrue(() -> Launcher.INSTANCE.farLauncher.schedule())
//                .whenBecomesFalse(() -> Launcher.INSTANCE.startLauncher.schedule());


    }
}