/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raycaster;

import java.util.ArrayList;


/**
 *
 * @author james
 */
public final class Player {
    private Vector position;
    private double FOV = 45;
    private final double raysPerDeg = 16;
    private Ray[] rays = new Ray[(int)(FOV*raysPerDeg)];
    
    private Vector direction;
    
    public Player(int x, int y, int angle, int speed){//cartesian
        updatePlayer(x, y, angle, speed);
    }
    
    public void updatePlayer(int x, int y, double angle, double speed){
        this.rays = new Ray[(int)(FOV*raysPerDeg)];
        this.position = new Vector(x, y);
        this.direction = new Vector(Math.sin(toRadians(angle)), Math.cos(toRadians(angle))).normalise(speed);
        int count=0;
        for(double i=-(this.FOV/2); i<(this.FOV/2); i+=(1/raysPerDeg)){
            this.rays[count] = new Ray((int)this.position.getX(), (int)this.position.getY(), (i)+angle);
            count++;
        }
    }
    
    public double shortestDistanceToWall(Boundary wall){
        return this.position.pointAndLineShortestDistance(wall.getPoints()[0], wall.getDirection());
    }
    
    
    private double toRadians(double degrees){
        return 0.0174533*degrees;
    }
    
    public Vector getPosition(){
        return this.position;
    }
    
    public Vector getDirection(){
        return this.direction;
    }
    
    public double getFOV(){
        return this.FOV;
    }
    
    public void setFOV(double FOV){
        this.FOV = FOV;
    }
    
    public Ray[] getRays(){
        return this.rays;
    }
}
