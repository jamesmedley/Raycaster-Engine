/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author james
 */
public class MouseMovementDetector implements MouseMotionListener{

    private int mouseX, mouseY;
    
    
    public int getMouseX(){
        return this.mouseX;
    }
    
    public int getMouseY(){
        return this.mouseY;
    }
    
    
    @Override
    public void mouseDragged(MouseEvent me) {
        this.mouseX = me.getX();
        this.mouseY = me.getY();    }

    @Override
    public void mouseMoved(MouseEvent me) {
        this.mouseX = me.getX();
        this.mouseY = me.getY();
    }
    
}
