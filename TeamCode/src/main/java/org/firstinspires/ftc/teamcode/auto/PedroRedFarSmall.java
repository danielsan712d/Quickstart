package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherPID;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "Auto Red Far Small Triangle")
public class PedroRedFarSmall extends NextFTCOpMode {


    public PedroRedFarSmall() {


        addComponents(
                new SubsystemComponent(Feeder.INSTANCE, Intake.INSTANCE, LauncherPID.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE
        );

    }

    private Command autonomousRoutine() {
        Pose startPose = new Pose(84, 11, Math.toRadians(68));
        Pose shootPose = new Pose (84, 11, Math.toRadians(40));
        Pose row3Start = new Pose(96, 35, Math.toRadians(0));
        Pose row3End = new Pose(120, 35, Math.toRadians(0));
        Pose movePoints = new Pose(84, 35, Math.toRadians(0));
        // Pose parkPose = new Pose(115, 84, Math.toRadians(270));

        follower().setStartingPose(startPose);

        PathChain startToShoot = follower().pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

        PathChain shootToRow3Start = follower().pathBuilder()
                .addPath(new BezierLine(shootPose, row3Start))
                .setLinearHeadingInterpolation(shootPose.getHeading(), row3Start.getHeading())
                .build();

        PathChain row3StartToEnd = follower().pathBuilder()
                .addPath(new BezierLine(row3Start, row3End))
                .setLinearHeadingInterpolation(row3Start.getHeading(), row3End.getHeading())
                .build();

        PathChain row3EndToShoot = follower().pathBuilder()
                .addPath(new BezierLine(row3End, shootPose))
                .setLinearHeadingInterpolation(row3End.getHeading(), shootPose.getHeading())
                .build();
        PathChain startPoseToMovePoints = follower().pathBuilder()
                .addPath(new BezierLine(startPose, movePoints))
                .setLinearHeadingInterpolation(row3End.getHeading(), shootPose.getHeading())
                .build();

//        PathChain shootToPark = follower().pathBuilder()
//                .addPath(new BezierLine(shootPose, parkPose))
//                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
//                .build();


        return new SequentialGroup(
//                LauncherPID.INSTANCE.setLauncherPower(0.7).thenWait(7.0),
             //   new FollowPath(startToShoot, true, 0.7),
                LauncherPID.INSTANCE.setLauncherRPM(750.0).thenWait(10.0),
                Feeder.INSTANCE.up, // Shoot 1st preload
                Intake.INSTANCE.startIntake.thenWait(1.0),
                Intake.INSTANCE.stopIntake.thenWait(4.0),
                Feeder.INSTANCE.up, // Shoot 2nd preload
                Intake.INSTANCE.startIntake.thenWait(1.0),
                Intake.INSTANCE.stopIntake.thenWait(4.0),
                Feeder.INSTANCE.up, // Shoot 3rd preload
                new FollowPath(shootToRow3Start, true, 0.5).afterTime(5.0)
             //   Intake.INSTANCE.startIntake.thenWait(1.0),
              //  new FollowPath(row3StartToEnd, true, 0.4),
               // Intake.INSTANCE.stopIntake
        );
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
    }

    @Override
    public void onInit() {
        Feeder.INSTANCE.down.schedule();
        ActiveOpMode.telemetry().update();


    }
}
