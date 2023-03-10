/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputs;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import raycaster.Boundary;
import raycaster.Player;
import raycaster.Vector;

/**
 *
 * @author james
 */
public class KeyDetector implements KeyListener{

    private HashSet<Integer> pressedKeys = new HashSet<>();
    private Boundary[] walls;
    private double x, y;
    private Player p;
    private boolean paused = false;
    private boolean jumping = false;
    private boolean crouching = false;
    private boolean LShiftHeld = false;
    private double yOffset = 0;

    public KeyDetector(){
        this.x = 0;
        this.y = 0;
        
        new Timer(5, (ActionEvent arg0) -> {
            String keysString = "";
            LShiftHeld = false;
            if(!pressedKeys.isEmpty()){
                Iterator<Integer> i = pressedKeys.iterator();
                while(i.hasNext()){
                    try {
                        int keycode = i.next();
                        //System.out.println(keycode);
                        keysString += keycode + ",";
                        Vector direction = this.p.getDirection();
                        double shortest = 10000;
                        for(Boundary wall:walls){
                            if(p.shortestDistanceToWall(wall)<shortest){
                                shortest = p.shortestDistanceToWall(wall); //use to test if distance to closest wall is closer and within limit before moving closer to wall
                            }
                        }
                        switch(keycode){
                            case 87://W
                                if(!paused){
                                    this.x+=direction.getX();
                                    this.y+=direction.getY();
                                }
                                break;
                            case 65://A
                                if(!paused){
                                    this.x-=direction.getY();
                                    this.y+=direction.getX();
                                }
                                break;
                            case 83://S
                                if(!paused){
                                    this.x-=direction.getX();
                                    this.y-=direction.getY();
                                }
                                break;
                            case 68://D
                                if(!paused){
                                    this.x+=direction.getY();
                                    this.y-=direction.getX();
                                }   
                                break;
                            case 80://P
                                paused = !paused;
                                Thread.sleep(500);
                                break;
                            case 32://space
                                if(!paused){
                                    if(jumping){
                                        break;
                                    }else{
                                        jumping = true;
                                        Thread jump;
                                        jump = new Thread(){
                                            @Override
                                            public void run(){
                                                for(int i = 0; i<81; i++){
                                                    yOffset = (40*i) + (0.5 * -1 * i * i);
                                                    try {
                                                        Thread.sleep(5);
                                                    } catch (InterruptedException ex) {
                                                        Logger.getLogger(KeyDetector.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                                jumping = false;
                                            }
                                        };
                                        jump.start();
                                    }
                                }
                                break;
                            case 16: //L shift
                                LShiftHeld = true;
                                if(!paused){
                                    if(crouching){
                                        break;
                                    }else{
                                        crouching = true;
                                        yOffset -= 200;
                                    }
                                }    
                                break;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(KeyDetector.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if(!LShiftHeld & crouching){
                yOffset += 200;
                crouching = false;
            }
        }).start();
    }

    public void updateWalls(Boundary[] walls){
        this.walls = walls;
    }
    
    public void updateParticle(Player p){
        this.p = p;
    }
    
    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }
    
    public double getYOffset(){
        return this.yOffset;
    }
    
    public boolean isCrouching(){
        return this.crouching;
    }
    
    public boolean isJumping(){
        return this.jumping;
    }
    
    public boolean isPaused(){
        return this.paused;
    }
    
    public boolean setPaused(boolean paused){
        return this.paused = paused;
    }
    
    @Override
    public void keyPressed(KeyEvent ovent){
        int keyCode = ovent.getKeyCode();
        pressedKeys.add(keyCode);
    }
    @Override
    public void keyReleased(KeyEvent ovent){
        int keyCode = ovent.getKeyCode();
        pressedKeys.remove(keyCode);
    }
    @Override
    public void keyTyped(KeyEvent ovent){

    }

}

