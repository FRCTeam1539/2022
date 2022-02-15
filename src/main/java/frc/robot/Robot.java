// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
    
package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.motorcontrol.MotorController;
//import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

//import java.io.Console;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * This is a demo program showing the use of the DifferentialDrive class, specifically it contains
 * the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private Joystick m_leftStick;
  private Joystick m_rightStick;
  private Timer timer;

  private final WPI_VictorSPX m_leftMotor = new WPI_VictorSPX(11);
  private final WPI_VictorSPX m_rightMotor = new WPI_VictorSPX(12);

  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightMotor.setInverted(false);
    m_leftMotor.setInverted(true);

    m_leftStick = new Joystick(1);
    m_rightStick = new Joystick(0);
    m_myRobot = new DifferentialDrive(m_leftMotor, m_rightMotor);
    timer = new Timer();
    timer.reset();
    timer.start();
  }

  public void simpleTankDrive(double left, double right) {
    // If the right stick input is negative, invert the right motor
    if (right < 0) m_rightMotor.setInverted(true);
    else m_rightMotor.setInverted(false);
    
    // If the left stick input is negative, invert the left motor
    if (left < 0) m_leftMotor.setInverted(false);
    else m_leftMotor.setInverted(true);

    // Tank drive with absolute value of joystick inputs
    m_myRobot.tankDrive(Math.abs(left), Math.abs(right));
  }

  /* * * * * * * *\
  | Tele-Operated |
  \* * * * * * * */

  @Override
  public void teleopPeriodic() {
    // Drive the robot
    simpleTankDrive(m_leftStick.getY(), m_rightStick.getY());
    // Print the current match time
    System.out.printf("Match Timer: %f\n",timer.get());
  }
  
  /* * * * * * * *\
  | Autonomous    |
  \* * * * * * * */

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();
  }

  @Override
  public void autonomousPeriodic() {
    // Get the match time
    System.out.printf("Match Timer: %f\n",timer.get());
    // If autonomous period is over, stop early
    if (timer.get() > 15.0) {
      simpleTankDrive(0.0, 0.0);
      return;
    }
    // Time-based driving
    if     (timer.get() < 2.0) simpleTankDrive(0.5, 0.5);
    else if (timer.get() < 3.0) simpleTankDrive(0.0, 0.0);
    else if (timer.get() < 5.0) simpleTankDrive(-0.5, -0.5);
    else if (timer.get() < 6.0) simpleTankDrive(0.0, 0.0);
    else if (timer.get() < 8.0) simpleTankDrive(-0.49, 0.49);
    else if (timer.get() < 9.0) simpleTankDrive(0.0, 0.0);
    else if (timer.get() < 11.0) simpleTankDrive(0.49, -0.49);
  }
}