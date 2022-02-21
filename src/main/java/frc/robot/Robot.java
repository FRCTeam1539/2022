// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;


/** This is a demo program showing how to use Mecanum control with the MecanumDrive class. */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 2;
  private static final int kRearLeftChannel = 3;
  private static final int kFrontRightChannel = 1;
  private static final int kRearRightChannel = 0;

  private static final int kJoystickChannel = 0;

  private static final double MAX_Y_SPEED = 1.0;
  private static final double MIN_Y_SPEED = -1.0;
  private static final double MAX_X_SPEED = 1.0;
  private static final double MIN_X_SPEED = -1.0;
  private static final double MAX_Z_ROTATE = 1.0;
  private static final double MIN_Z_ROTATE = -1.0;

  
  private MecanumDrive m_robotDrive;
  private Joystick m_stick;

  @Override
  public void robotInit() {
    WPI_VictorSPX frontLeft = new WPI_VictorSPX(kFrontLeftChannel);
    WPI_VictorSPX rearLeft = new WPI_VictorSPX(kRearLeftChannel);
    WPI_VictorSPX frontRight = new WPI_VictorSPX(kFrontRightChannel);
    WPI_VictorSPX rearRight = new WPI_VictorSPX(kRearRightChannel);

    // Invert the right side motors.
    // You may need to change or remove this to match your robot.
    frontRight.setInverted(true);
    rearRight.setInverted(true);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_stick = new Joystick(kJoystickChannel);
  }

  @Override
  public void teleopPeriodic() {
    double xJoy = m_stick.getX();
    double yJoy = m_stick.getY();
    double zJoy = m_stick.getZ();

    if (xJoy != 0 && yJoy != 0 && zJoy != 0){
      jerryDrivesMech(xJoy, yJoy, zJoy);
    }
    else{
      jerryDrivesMech(0,0,0);
    }
    
  }

  private void jerryDrivesMech(double xSpeed, double ySpeed, double zRotate){
    // Limiting X, Y, and Z speed for the robot drive
    if (xSpeed > 0 && xSpeed > MAX_X_SPEED){
      xSpeed = MAX_X_SPEED;
    }
    if (xSpeed < 0 && xSpeed < MIN_X_SPEED){
      xSpeed = MIN_X_SPEED;
    }

    if (ySpeed > 0 && ySpeed > MAX_Y_SPEED){
      ySpeed = MAX_Y_SPEED;
    }
    if (ySpeed < 0 && ySpeed < MIN_Y_SPEED){
      ySpeed = MIN_Y_SPEED;
    }

    if (zRotate > 0 && zRotate > MAX_Z_ROTATE){
      zRotate = MAX_Z_ROTATE;
    }
    if ( zRotate < 0 && zRotate < MIN_Z_ROTATE){
      zRotate = MIN_Z_ROTATE;
    }

    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    m_robotDrive.driveCartesian(xSpeed, ySpeed, zRotate, 0.0);
  }
}