package model;

public class Target {
    private volatile int targetPositionX; // = 150;
    private volatile int targetPositionY; // = 100;

    public Target(int positionX, int positionY){
        this.targetPositionX = positionX;
        this.targetPositionY = positionY;
    }

    public void setTargetPositionX(int positionX){
        this.targetPositionX = positionX;
    }

    public void setTargetPositionY(int positionY){
        this.targetPositionY = positionY;
    }

    public int getTargetPositionX(){
        return this.targetPositionX;
    }

    public int getTargetPositionY(){
        return this.targetPositionY;
    }
}
