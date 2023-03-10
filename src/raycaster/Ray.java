/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raycaster;

/**
 *
 * @author james
 */
public class Ray {
    private final Vector position;
    private Vector direction;
    
    public Ray(int x, int y, double angle){//cartesian
        this.position = new Vector(x, y);
        this.direction = new Vector(Math.sin(toRadians(angle)), Math.cos(toRadians(angle))).normalise(20);
    }
    
    private double toRadians(double degrees){
        return 0.0174533*degrees;
    }
    
    public void lookAt(int x, int y){
        this.direction = new Vector(x - this.position.getX(), y - this.position.getY());      
        this.direction = this.direction.normalise(20);
    }
    
    
    public Vector getDirection(){
        return this.direction;
    }
    
    public Vector getPosition(){
        return this.position;
    }
    
    public Vector findWallIntersection(Boundary wall){
        
        double wallMag = Math.sqrt(Math.pow(wall.getPoints()[1].getX()-wall.getPoints()[0].getX(),2) + Math.pow(wall.getPoints()[1].getY()-wall.getPoints()[0].getY(),2));
        Vector wallPosition = wall.getPoints()[0];
        Vector wallDirection = wall.getDirection();

        Vector rayPosition = this.position;
        Vector rayDirection = this.direction;
        
        Vector intersection = Vector.lineAndLineIntersectionPoint(rayPosition, rayDirection, wallPosition, wallDirection, wallMag);
        
        return intersection;
    }
    
    
}
