package org.firstinspires.ftc.teamcode.mechanisms;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class Feeder implements Subsystem {
    public static final Feeder INSTANCE = new Feeder();
    private Feeder() { }

    private ServoEx servo = new ServoEx("feeder");

    public Command down = new SetPosition(servo, 0.9).requires(this);
    public Command up = new SequentialGroup(
            new SetPosition(servo, 0.4).requires(this).thenWait(0.2),
            new SetPosition(servo, 0.9).requires(this).thenWait(0.2)
            );
}