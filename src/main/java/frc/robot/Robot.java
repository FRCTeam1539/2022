// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Timer;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

//import java.security.interfaces.XECPublicKey;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.motorcontrol.MotorController;
//import edu.wpi.first.wpilibj.motorcontrol.MotorController;
//import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
//import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;



/** This is a demo program showing how to use Mecanum control with the MecanumDrive class. */
public class Robot extends TimedRobot {
  // motor channels
  //right and left side when facing back of the robot
  /*
  built with asciiflow.com

                                  front
       ┌──────────────────┐                  ┌────────────────────┐
       │                  │                  │                    │
       │ ┌──────────────┐ │                  │  ┌───────────────┐ │
       │ │ L front drive│11                  │  │               │ │
       │ │              │ │                  │  │ R drive motor 0 │
       │ └──────────────┘ │                  │  └───────────────┘ │
       │                  │                  │                    │
       │    ┌───────┐     │                  │     ┌───────┐      │
       │    │       │     │                  │     │       │      │
       │    │       │     │                  │     │       │      │
       │    │L raise│10   │                  │     │raise 1│      │
       │    │       │  ┌──┼──┐             ┌─┼─┐   │       │      │
       │    │       │  │  │  │             │ │ │   │       │      │
       │    └───────┘ L│launch 9          R│Launch 2───────┘      │
       │               │  │  │             │ │ │                  │
       │   ┌────────── └──┼──┘             └─┼─┘  ┌───────┐       │
       │   │ intake belt 8│                  │    │hood 3 │       │
       │   └──────────    │                  │    └───────┘       │
       │                  │                  │                    │
       │     ┌───────┐    │                  │      ┌─────┐       │
       │     │       │    └──────────────────┘      │     │       │
       │     │       │                              R Ext 4       │
       │     │       │                              │     │       │
       │     │L ext 7│                              ├─────┤       │
       │     ├───────┤                         ┌────┴─────┴───┐   │
       │  ┌──┴───────┴─────┐                   │              │   │
       │  │                │                   │   R back drive 5 │
       │  │ L back drive 6 │                   └───────────────   │
       │  ├────────────────┤                                      │
       ┴──┴────────────────┴──────────────────────────────────────┘


                                  back



  */
  private static final int kRearRightChannel = 4;
  private static final int kFrontRightChannel = 2;
  private static final int kFrontLeftChannel = 1;
  private static final int kRearLeftChannel = 3;
  private static final int kIntakeBeltChannel = 8;
  private static final int kLaunchMotorRChannel = 2;//
  private static final int kLaunchMotorLChannel = 9;
  private static final int kArmExtRChannel = 4;//
  private static final int kArmExtLChannel = 7;
  private static final int kArmLiftRChannel = 1;//
  private static final int kArmLiftLChannel = 10;
  private static final int kHoodChannel = 3;//

  


  // controller channels
  private static final int kJoystickChannel = 0;
  private static final int kXboxChannel = 1;

  // controls drive limits
  private static final double MAX_Y_SPEED = 1.0;
  private static final double MIN_Y_SPEED = -1.0;
  private static final double MAX_X_SPEED = 1.0;
  private static final double MIN_X_SPEED = -1.0;
  private static final double MAX_Z_ROTATE = 1.0;
  private static final double MIN_Z_ROTATE = -1.0;

  //launch motor speed 
  private static final double LAUNCH_SPEED = 1.0;

  // mechanum motors
  private WPI_TalonSRX frontLeft = new WPI_TalonSRX(kFrontLeftChannel);
  private WPI_TalonSRX rearLeft = new WPI_TalonSRX(kRearLeftChannel);
  private WPI_TalonSRX frontRight = new WPI_TalonSRX(kFrontRightChannel);
  private WPI_TalonSRX rearRight = new WPI_TalonSRX(kRearRightChannel);

  // launch system motors
  private final WPI_VictorSPX m_intakeBeltMotor = new WPI_VictorSPX(kIntakeBeltChannel);
  private final WPI_VictorSPX m_launchMotorR = new WPI_VictorSPX(kLaunchMotorRChannel);
  private final WPI_VictorSPX m_launchMotorL = new WPI_VictorSPX(kLaunchMotorLChannel);
  private final WPI_VictorSPX m_ArmExtR = new  WPI_VictorSPX(kArmExtRChannel);
  private final WPI_VictorSPX m_ArmExtL = new  WPI_VictorSPX(kArmExtLChannel);
  private final WPI_VictorSPX m_ArmLiftR = new  WPI_VictorSPX(kArmLiftRChannel);
  private final WPI_VictorSPX m_ArmLiftL = new  WPI_VictorSPX(kArmLiftLChannel);
  private final WPI_VictorSPX m_Hood = new WPI_VictorSPX(kHoodChannel);

  private boolean armUp = false;
  private boolean armDown = true;
  private double armUpTm = 1.0;
  private Timer armTm = new Timer();
  private double armDownTm = 1.0;
  private boolean armMotionUp = false;
  private boolean armMotionDown = false;

  private MecanumDrive m_robotDrive;
  //private  m_intakeBelt;
  private Joystick m_stick;
  private XboxController m_Xbox;


  @Override
  public void robotInit() {
    
    // Invert the right side motors.
    // You may need to change or remove this to match your robot.
    frontRight.setInverted(true);
    rearRight.setInverted(false); 
    rearLeft.setInverted(true);
    frontLeft.setInverted(true);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_stick = new Joystick(kJoystickChannel);

    m_Xbox = new XboxController(kXboxChannel);

    armTm.stop();
    armTm.reset();
  }

  @Override
  public void teleopPeriodic() {
    // get drive buttons
    double xJoy = m_stick.getX();
    double yJoy = m_stick.getY();
    double zJoy = m_stick.getZ();

    boolean xbcB = m_Xbox.getBButton();
    boolean xbcY = m_Xbox.getYButton();
    boolean xbcA = m_Xbox.getAButton();
    boolean xbcRB = m_Xbox.getRightBumper();
    double xbcLeftStickY = m_Xbox.getLeftY();
    double xbcLeftStickX = m_Xbox.getLeftX();
    double xbcRightStickY = m_Xbox.getRightY();
    double xbcRightStickX = m_Xbox.getRightX();

    // drive controls
    if(xbcB){
     frontLeft.stopMotor();
     frontRight.stopMotor();
     rearLeft.stopMotor();
     rearRight.stopMotor();
    }
    else{
      jerryDrivesMech(xJoy, yJoy, zJoy);
    }

    /* original
    if (!xbcB || (xJoy != 0 && yJoy != 0 && zJoy != 0)){
      jerryDrivesMech(xJoy, yJoy, zJoy);
    }
   else if(xbcB){
     frontLeft.stopMotor();
     frontRight.stopMotor();
     rearLeft.stopMotor();
     rearRight.stopMotor();
    }
    else{
      jerryDrivesMech(0,0,0);
    }
    */

    // intake belt controls
    // option 1
    double intakeSpeed = m_Xbox.getLeftTriggerAxis();

    if (intakeSpeed > 0) {
      m_intakeBeltMotor.set(intakeSpeed);
    }
    else {
      m_intakeBeltMotor.set(0);
    }


    //option 2
    m_intakeBeltMotor.set(m_Xbox.getLeftTriggerAxis());
    m_launchMotorR.set(m_Xbox.getRightTriggerAxis());
    m_launchMotorL.set(m_Xbox.getRightTriggerAxis());



    //automatically move arm up
    if (xbcY && !(armMotionUp || armMotionDown) && !armUp){
      m_ArmExtR.setInverted(false);
      m_ArmExtR.set(1.0);
      m_ArmExtL.setInverted(false);
      m_ArmExtL.set(1.0);
      armTm.start();
      armMotionUp = true;
    }
    else if (armMotionUp && armTm.get() >= armUpTm){
      m_ArmExtR.stopMotor();
      m_ArmExtL.stopMotor();
      armTm.stop();
      armTm.reset();
      armMotionUp = false;
    }

    //automatically move arm down
    if (xbcA && !(armMotionUp || armMotionDown) && !armDown){
      m_ArmExtR.setInverted(true);
      m_ArmExtL.setInverted(true);
      armTm.start();
      armMotionDown = true;
    }
    else if (armMotionDown && armTm.get() >= armDownTm){
      m_ArmExtR.stopMotor();
      m_ArmExtL.stopMotor();
      armTm.stop();
      armTm.reset();
      armMotionDown = false;
    }

    //start the two launch motors
    if (xbcRB){
      //m_launchMotorR.set(LAUNCH_SPEED);
      m_launchMotorL.set(LAUNCH_SPEED);
    }
/*
    //left analog stick manual drive for front
    if (xbcLeftStickY < 0){
      m_ArmExtBack.setInverted(false);
      m_ArmExtBack.set(m_Xbox.getLeftY());
    }

    if (xbcLeftStickY > 0){
      m_ArmExtBack.setInverted(true);
      m_ArmExtBack.set(m_Xbox.getLeftY());
    }

    if (xbcLeftStickX < 0){
      m_ArmLiftBack.setInverted(false);
      m_ArmLiftBack.set(m_Xbox.getLeftX());
    }

    if (xbcLeftStickX > 0){
      m_ArmLiftBack.setInverted(true);
      m_ArmLiftBack.set(m_Xbox.getLeftX());
    }
*/
    //right analog stick manual drive for back
    
    if (xbcRightStickY < 0){
      m_ArmExtR.setInverted(false);
      m_ArmExtR.set(m_Xbox.getRightY());
      m_ArmExtL.setInverted(false);
      m_ArmExtL.set(m_Xbox.getRightY());
    }
    if (xbcRightStickY > 0){
      m_ArmExtR.setInverted(true);
      m_ArmExtR.set(m_Xbox.getRightY());
      m_ArmExtL.setInverted(true);
      m_ArmExtL.set(m_Xbox.getRightY());
    }

    if (xbcRightStickX < 0){
      m_ArmLiftR.setInverted(false);
      m_ArmLiftR.set(m_Xbox.getRightX());
      m_ArmLiftL.setInverted(false);
      m_ArmLiftL.set(m_Xbox.getRightX());
    }

    if (xbcRightStickX > 0){
      m_ArmLiftR.setInverted(true);
      m_ArmLiftR.set(m_Xbox.getRightX());
      m_ArmLiftL.setInverted(true);
      m_ArmLiftL.set(m_Xbox.getRightX());
    }

  if (xbcLeftStickY > 0){
    m_Hood.setInverted(false);
    m_Hood.set(m_Xbox.getLeftY());
  }
  if (xbcLeftStickY < 0){
    m_Hood.setInverted(true);
    m_Hood.set(m_Xbox.getLeftY());
  }


  }

  private void jerryDrivesMech(double xSpeed, double ySpeed, double zRotate){
    // Limiting X, Y, and Z speed for the robot drive
    xSpeed = clipRange(xSpeed, MIN_X_SPEED, MAX_X_SPEED); 
    
    ySpeed = clipRange(ySpeed, MIN_Y_SPEED, MAX_Y_SPEED);
    
    zRotate = clipRange(zRotate, MIN_Z_ROTATE, MAX_Z_ROTATE);

    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    m_robotDrive.driveCartesian(xSpeed, ySpeed, zRotate, 0.0);
  }

  private double clipRange(double value, double min, double max){
    if (value > 0 && value > max){
      value = max;
    }
    if (value < 0 && value < min){
      value = min;
    }
    return value;
  }

}