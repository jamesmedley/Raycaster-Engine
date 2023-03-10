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
public class Gun {
    
    private boolean scoped = false;
    private int ammo;
    
    
    public Gun(int ammo){
        this.ammo = ammo;
    }
    
    public void setScoped(boolean scoped){
        this.scoped = scoped;
    }
    
    public boolean getScoped(){
        return this.scoped;
    }
    
    
}
