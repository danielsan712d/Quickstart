package org.firstinspires.ftc.teamcode.auto;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.core.commands.Command;
@Autonomous(name = "Auto Red Next to Goal")
public class PedroRedGoal extends NextFTCOpMode {


    public PedroRedGoal() {


        addComponents(
                new SubsystemComponent(Feeder.INSTANCE, Intake.INSTANCE, Launcher.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE
        );

    }

    private Command autonomousRoutine() {
//        Pose startPose = new Pose(117.7, 127.5, Math.toRadians(45));
        Pose startPose = new Pose(111, 135, Math.toRadians(90));
        Pose shootPose = new Pose (96, 108.5, Math.toRadians(45));
        Pose row1Start = new Pose(96, 84, Math.toRadians(0));
        Pose row1End = new Pose(124, 84, Math.toRadians(0));
        Pose parkPose = new Pose(115, 84, Math.toRadians(270));

        follower().setStartingPose(startPose);

        PathChain startToShoot = follower().pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

        PathChain shootToRow1Start = follower().pathBuilder()
                .addPath(new BezierLine(shootPose, row1Start))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

        PathChain row1StartToEnd = follower().pathBuilder()
                .addPath(new BezierLine(row1Start, row1End))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

        PathChain row1EndToShoot = follower().pathBuilder()
                .addPath(new BezierLine(row1End, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

        PathChain shootToPark = follower().pathBuilder()
                .addPath(new BezierLine(shootPose, parkPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();


        return new SequentialGroup(
                Launcher.INSTANCE.setLauncherPower(0.6),
                new FollowPath(startToShoot, true, 0.7).thenWait(3.0),
                Feeder.INSTANCE.up, // Shoot 1st preload
                Intake.INSTANCE.startIntake.thenWait(1.0),
                Intake.INSTANCE.stopIntake,
                Feeder.INSTANCE.up, // Shoot 2nd preload
                Intake.INSTANCE.startIntake.thenWait(1.0),
                Intake.INSTANCE.stopIntake,
                Feeder.INSTANCE.up, // Shoot 3rd preload
                new FollowPath(shootToRow1Start, true, 0.8),
                Intake.INSTANCE.startIntake.thenWait(1.0),
                new FollowPath(row1StartToEnd, true, 0.4),
                Intake.INSTANCE.stopIntake,
                new FollowPath(row1EndToShoot, true, 0.8).thenWait(1.0),
                Feeder.INSTANCE.up, // Shoot Round 2 1st ball
                Intake.INSTANCE.startIntake.thenWait(1.0),
                Intake.INSTANCE.stopIntake,
                Feeder.INSTANCE.up, // Shoot Round 2 2nd ball
                Intake.INSTANCE.startIntake.thenWait(1.0),
                Intake.INSTANCE.stopIntake,
                Feeder.INSTANCE.up, // Shoot Round 2 3rd ball
                new FollowPath(shootToPark, true, 0.8) // Go to park
        );
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
    }

    @Override
    public void onInit() {
        Feeder.INSTANCE.down.schedule();
    }

}