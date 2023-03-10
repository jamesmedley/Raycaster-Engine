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
public class NPC {
    private Vector position;
    
    
    public NPC(double x, double y){
        this.position = new Vector(x, y);;
    }
    
    
    public void setPosition(double x, double y){ //cartesian
        this.position = new Vector(x, y);
    }
    
    
    public Vector getPosition(){
        return this.position;
    }
    
    public double distanceFromPlayer(Player player){
        Vector npcToPlayer = this.position.subtractVector(player.getPosition());
        double distance = npcToPlayer.magnitude();
        return distance;
    }
    
    public Vector relativePosition(Player player){
        Vector npcToPlayer = this.position.subtractVector(player.getPosition());
        double playerDir = Math.atan2(player.getDirection().getX(), player.getDirection().getY());
        if(playerDir<0){
            playerDir += 2*Math.PI;
        }
        double vx = (npcToPlayer.getX() * Math.cos(playerDir)) - (npcToPlayer.getY() * Math.sin(playerDir));
        double vy = (npcToPlayer.getX() * Math.sin(playerDir)) + (npcToPlayer.getY() * Math.cos(playerDir));
        Vector newPos = new Vector(vx, vy);
        return newPos;
    }
    
}
