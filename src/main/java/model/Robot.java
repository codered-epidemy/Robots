package model;

public class Robot {
    private volatile double robotPositionX; // = 100;
    private volatile double robotPositionY; // = 100;
    private volatile double robotDirection; // = 0;

    public Robot(double positionX, double positionY, double direction){
        this.robotPositionX = positionX;
        this.robotPositionY = positionY;
        this.robotDirection = direction;
    }

    public  void setRobotPositionX(double positionX){
        this.robotPositionX = positionX;
    }

    public  void setRobotPositionY(double positionY){
        this.robotPositionY = positionY;
    }

    public  void setRobotDirection(double direction){
        this.robotDirection = direction;
    }

    public  double getRobotPositionX(){
        return this.robotPositionX;
    }

    public  double getRobotPositionY(){
        return this.robotPositionY;
    }

    public  double getRobotDirection(){
        return this.robotDirection;
    }
}