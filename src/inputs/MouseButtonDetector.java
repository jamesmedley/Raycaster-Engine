/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author james
 */
public class MouseButtonDetector implements MouseListener{

    private boolean scoped = false;
    
    
    @Override
    public void mouseClicked(MouseEvent me) {
//        if(me.getButton() == MouseEvent.BUTTON1){
//           //shoot 
//        }
//        if(me.getButton() == MouseEvent.BUTTON3){
//            scoped = !scoped;
//        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if(me.getButton() == MouseEvent.BUTTON1){
           //shoot 
        }
        if(me.getButton() == MouseEvent.BUTTON3){
            scoped = !scoped;
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }
    
    public boolean isScoped(){
        return this.scoped;
    }
    
}
