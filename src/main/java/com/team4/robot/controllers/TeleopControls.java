package com.team4.robot.controllers;

import java.util.Arrays;
import java.util.List;

import com.team4.lib.commands.CommandBase;
import com.team4.lib.commands.TeleopCommandRunner;
import com.team4.robot.Robot;
import com.team4.robot.commands.MoveArmCommand;
import com.team4.robot.commands.teleop.ClimbCommand;
import com.team4.robot.commands.teleop.ConveyorCommand;
import com.team4.robot.commands.teleop.DriveCommand;
import com.team4.robot.commands.teleop.IntakeCommand;
import com.team4.robot.commands.teleop.ShooterCommand;
import com.team4.robot.commands.teleop.ToggleWinchCommand;

public class TeleopControls extends TeleopCommandRunner{
    public static DriverController mDriverController = new DriverController();
    public static OperatorController mOperatorController = new OperatorController();
    
    List<CommandBase> mCommands; 

    public TeleopControls()
    {
        mCommands = Arrays.asList(
            new DriveCommand(),
            new IntakeCommand(),
            new ConveyorCommand(),
            new ShooterCommand(),
            new ClimbCommand()
        );
    }

    @Override
    public void runTeleop()
    {

        mCommands.forEach(c -> c.execute());
        
        if (mOperatorController.getDeployIntake())
        {
            runCommand(new MoveArmCommand());
        }
        

		if (mOperatorController.getCompressorToggle()) {

			Robot.mCompressor.enableDigital();

		} else {
			Robot.mCompressor.disable();
        } 		
	

        if(mDriverController.changeWinch()){
            runCommand(new ToggleWinchCommand());
        }    
    }
}
