package org.firstinspires.ftc.teamcode.mechanisms;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() { }

    private MotorEx motor =  new MotorEx("intake").reversed().brakeMode();

    public Command startIntake = new SetPower(motor, 1.0);
    public Command stopIntake = new SetPower(motor, 0.0);

    public Command reverseIntake = new SetPower(motor, -0.5);
}
