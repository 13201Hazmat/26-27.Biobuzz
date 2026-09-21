package org.firstinspires.ftc.teamcode.data;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.lynx.NextLynxModule;

public class Config {

    //Control Hub
    public static NextLynxModule aModule = RobotController.controlHub();
    public static int aPort = 0;
    public static NextLynxModule bModule = RobotController.controlHub();
    public static int bPort = 1;
    public static NextLynxModule cModule = RobotController.controlHub();
    public static int cPort = 2;
    public static NextLynxModule dModule = RobotController.controlHub();
    public static int dPort = 3;

    //Servos
    public static NextLynxModule eModule = RobotController.controlHub();
    public static int ePort = 0;
    public static NextLynxModule fModule = RobotController.controlHub();
    public static int fPort = 1;
    public static NextLynxModule gModule = RobotController.controlHub();
    public static int gPort = 2;
    public static NextLynxModule hModule = RobotController.controlHub();
    public static int hPort = 3;
    public static NextLynxModule iModule = RobotController.controlHub();
    public static int iPort = 4;


    // Expansion Hub
    public static NextLynxModule jModule = RobotController.expansionHub();
    public static int jPort = 0;
    public static NextLynxModule kModule = RobotController.expansionHub();
    public static int kPort = 1;
    public static NextLynxModule lModule = RobotController.expansionHub();
    public static int lPort = 2;
    public static NextLynxModule mModule = RobotController.expansionHub();
    public static int mPort = 3;

}