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
public class Vector {
    private final double x;
    private final double y;
    
    public Vector(double x, double y){//cartesian
        this.x = x;
        this.y = y;
    }
    
    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }

//other vector operations
    
    public Vector addVector(Vector v){
        return new Vector(this.x + v.getX(), this.y + v.getY());
    }
    
    public Vector subtractVector(Vector v){
        return new Vector(this.x - v.getX(), this.y - v.getY());
    }
    
    public Vector normalise(double length){
        double mag = Math.sqrt(x*x + y*y);
        return new Vector(length*(this.x/mag), length*(this.y/mag));
    }
    
    public double angle(Vector v){ //returns angle between 2 vector directions in radians
        double dotProduct = this.x*v.getX() + this.y*v.getY();
        //System.out.println(dotProduct);
        double productOfMags = this.magnitude() * v.magnitude();
       // System.out.println(productOfMags);
        double angle = Math.acos(dotProduct / productOfMags);
        if(Double.isNaN(angle)){
            return 0;
        }else{
            return angle;  
        }
       
    }
    
    public double dotProduct(Vector v){
        return this.x*v.x + this.y*v.y;
    }
    
    
    public double pointAndLineShortestDistance(Vector linePoint, Vector lineDirection){
        double x = lineDirection.getX();//wall direction x
        double y = lineDirection.getY();//wall direction y
        double i = linePoint.getX();//wall point x
        double j = linePoint.getY();//wall point y
        
        double n = this.x;//point x position
        double m = this.y;//point y position
        
        double mu = (x*(n - i) + y*(m - j)) / (x*x + y*y);
        
        Vector pointToLine = new Vector((i + mu*x - n) , (j + mu*y - m));
        
        return pointToLine.magnitude();
    } 
    
    public static Vector lineAndLineIntersectionPoint(Vector l1Position, Vector l1Direction, Vector l2Position, Vector l2Direction, double lineLengthLim){
        double wallMag = lineLengthLim;
        //ray vectos
        double x = l1Position.getX();
        double y = l1Position.getY();
        double u = l1Direction.getX();
        double v = l1Direction.getY();
        //wall vectors
        double i = l2Position.getX();
        double j = l2Position.getY();
        double m = l2Direction.getX();
        double n = l2Direction.getY();
        double lambda;
        double mu;
        lambda = (m*(j-y) + n*(x-i)) / (m*v - n*u);
        mu = (x + u*lambda -i) / m; 

        if(((x + u*lambda)-(i + m*mu)<0.5) && ((y + v*lambda)-(j + n*mu))<0.5 && mu<=wallMag && mu>=0 && lambda>0){//check if lines intersect within limit
            return new Vector((x + u*lambda), (y + v*lambda));
        }else{
            return null;
        }
    }
    
    public double magnitude(){
        return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
    }
}
