/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputs;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author james
 */
public class MouseWheelDetector implements MouseWheelListener{

    private int FOV;
    
    public MouseWheelDetector(){
        this.FOV = 60;
    }
    
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        if (mwe.getWheelRotation() < 0) {
            this.FOV--;    
        } else {
            this.FOV++; 
        }
    }
    
    public int getFOV(){
        return this.FOV;
    }
    
}
