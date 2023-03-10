/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raycaster;

import java.awt.image.BufferedImage;

/**
 *
 * @author james
 */
public class Boundary {
    private final Vector a;//start point of boundary line
    private final Vector b;//end point of boundary line
    private final BufferedImage texture;
    Boundary(int x1, int y1, int x2, int y2, BufferedImage texture){//cartesian
        this.a = new Vector(x1, y1);//position vector
        this.b = new Vector(x2, y2);//position vector
        this.texture = texture;
    }
    
    public Vector[] getPoints(){
        return new Vector[]{a, b};
    }
    
    public Vector getDirection(){
        return b.subtractVector(a).normalise(1);
    }
    
    public BufferedImage getTexture(){
        return this.texture;
    }
}
