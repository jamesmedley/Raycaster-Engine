/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raycaster;

import inputs.KeyDetector;
import inputs.MouseButtonDetector;
import inputs.MouseMovementDetector;
import inputs.MouseWheelDetector;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import wall_textures.WallTextures;

/**
 *
 * @author james
 */
public class Display {
    private Canvas mapCanvas, renderCanvas;
    private Frame frame;
    
    private final KeyDetector keyDetect = new KeyDetector();
    private final MouseMovementDetector mouseDetect = new MouseMovementDetector();
    private final MouseWheelDetector mouseWheelDetect = new MouseWheelDetector();
    private final MouseButtonDetector mouseButtonDetect = new MouseButtonDetector();
    
    private final int FRAMEWIDTH = 1200;
    private final int FRAMEHEIGHT = 1000;
  
    private int oldMouseX;
    private boolean turnR = true, turnL = false;
    
    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
    
    public static void main(String[] args) {
        Display d = new Display();
        
        d.mapCanvas = new Canvas();
        d.mapCanvas.setSize(d.FRAMEWIDTH, d.FRAMEHEIGHT);
        d.mapCanvas.setBackground(Color.orange);
        d.mapCanvas.setFocusable(false);
        
        d.renderCanvas = new Canvas();
        d.renderCanvas.setSize(d.FRAMEWIDTH, d.FRAMEHEIGHT);
        d.renderCanvas.setBackground(Color.GREEN);
        d.renderCanvas.addMouseMotionListener(d.mouseDetect);
        d.renderCanvas.setFocusable(false);
        d.renderCanvas.setCursor(d.blankCursor);
        d.renderCanvas.addMouseWheelListener(d.mouseWheelDetect);
        d.renderCanvas.addMouseListener(d.mouseButtonDetect);
        
        d.frame = new JFrame();
        d.frame.addKeyListener(d.keyDetect);
        d.frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing (WindowEvent e){
                System.exit(0);
            }
        });
        d.frame.setSize(2*d.FRAMEWIDTH, d.FRAMEHEIGHT);
        d.frame.setVisible(true);
        d.frame.setResizable(true);    
        d.frame.setLayout(new BorderLayout());
        d.frame.add(d.mapCanvas, BorderLayout.CENTER);
        d.frame.add(d.renderCanvas, BorderLayout.EAST);
        
        d.render();
    }
    
    private int[] toCartesian(int x, int y){ //screen coordinate to cartesian
        int cartX = x - FRAMEWIDTH/2;
        int cartY = FRAMEHEIGHT/2 - y;
        return new int[]{cartX, cartY};
    }
    
    private int toCartesianX(int x){ //screen coordinate to cartesian
        int cartX = x - FRAMEWIDTH/2;
        return cartX;
    }
    
    private int toCartesianY(int y){ //screen coordinate to cartesian
        int cartY = FRAMEHEIGHT/2 - y;
        return cartY;
    }
    
    private int[] toPixelCoords(int x, int y){
        int pixX = x + FRAMEWIDTH/2;
        int pixY = FRAMEHEIGHT/2 - y;
        return new int[]{pixX, pixY};
    }
    
    private int toPixelCoordsX(int x){
        int pixX = x + FRAMEWIDTH/2;
        return pixX;
    }
    
    private int toPixelCoordsY(int y){
        int pixY = FRAMEHEIGHT/2 - y;
        return pixY;
    }
        
    private double inverseSquare(double d){
        return (d*d)/(FRAMEHEIGHT*FRAMEHEIGHT);
    }
    
    private void render(){
        Thread thread;
        thread = new Thread(){
            @Override
            public void run(){
                try {
                    GraphicsConfiguration gcMap = mapCanvas.getGraphicsConfiguration();
                    VolatileImage vImageMap = gcMap.createCompatibleVolatileImage(FRAMEWIDTH, FRAMEHEIGHT);
                    GraphicsConfiguration gcRender = renderCanvas.getGraphicsConfiguration();
                    VolatileImage vImageRender = gcRender.createCompatibleVolatileImage(FRAMEWIDTH, FRAMEHEIGHT);

                    Ray[] rays;
                    ArrayList<double[]> scene;
                    ArrayList<Boundary> wallTextures;
                    Player player = new Player(0, 0, 0, 3);
                    Robot robot = new Robot();
                    
                    double direction = 0;
                    
                    new WallTextures();
                    Boundary[] walls = new Boundary[5]; //generate wall objects here
                    //edge boundaries
                    walls[0] = new Boundary(-(FRAMEWIDTH/2), (FRAMEHEIGHT/2), (FRAMEWIDTH/2), (FRAMEHEIGHT/2), WallTextures.WOOD);//top
                    walls[1] = new Boundary(-(FRAMEWIDTH/2), -(FRAMEHEIGHT/2), (FRAMEWIDTH/2), -(FRAMEHEIGHT/2), WallTextures.TNT_BARREL);//bottom
                    walls[2] = new Boundary(-(FRAMEWIDTH/2), (FRAMEHEIGHT/2), -(FRAMEWIDTH/2 - 1), -(FRAMEHEIGHT/2), WallTextures.FIRE);//left
                    walls[3] = new Boundary((FRAMEWIDTH/2), (FRAMEHEIGHT/2), (FRAMEWIDTH/2 - 1), -(FRAMEHEIGHT/2), WallTextures.REINFORCED_METAL);//right
                    //inner walls
                    walls[4] = new Boundary(-300, 400, 300, -200, WallTextures.GALVANIZED_METAL);//top
                    keyDetect.updateWalls(walls);
                    NPC npcTest = new NPC(0, 1);
                    
                    while(true){
                        double startTime = System.nanoTime();
                        scene = new ArrayList();
                        wallTextures = new ArrayList();
                        if(vImageMap.validate(gcMap) == VolatileImage.IMAGE_INCOMPATIBLE){
                            vImageMap = gcMap.createCompatibleVolatileImage(FRAMEWIDTH, FRAMEHEIGHT);
                        }
                        Graphics gMap = vImageMap.getGraphics();
                        
                        //draw background
                        gMap.setColor(Color.BLACK);
                        gMap.fillRect(0, 0, FRAMEWIDTH, FRAMEHEIGHT);
                        
                        //draw walls
                        gMap.setColor(Color.RED);
                        for(Boundary wall:walls){
                            int[] pixelXYStart = toPixelCoords((int)wall.getPoints()[0].getX(), (int)wall.getPoints()[0].getY());
                            int[] pixelXYEnd = toPixelCoords((int)wall.getPoints()[1].getX(), (int)wall.getPoints()[1].getY());
                            gMap.drawLine(pixelXYStart[0], pixelXYStart[1], pixelXYEnd[0], pixelXYEnd[1]);
                        }
                        
                        
                        int mouseX = mouseDetect.getMouseX();
                        int mouseY = mouseDetect.getMouseY();   
                        if(!frame.isFocused() | keyDetect.isPaused()){
                            keyDetect.setPaused(true);
                            oldMouseX = (FRAMEWIDTH/2);
                            renderCanvas.setCursor(null);
                            continue;
                        }else{
                            renderCanvas.setCursor(blankCursor);
                        }
                        if(frame.isFocused() & !keyDetect.isPaused()){
                            Point p = renderCanvas.getLocationOnScreen();
                            robot.mouseMove(p.x+(FRAMEWIDTH/2), p.y+mouseY);
                            if(mouseX > oldMouseX){
                            turnR = true;
                            turnL = false;
                            }else if(mouseX < oldMouseX){
                                turnR = false;
                                turnL = true;
                            }else{
                                turnR = false;
                                turnL = false;
                            }
                            oldMouseX = FRAMEWIDTH/2;
 
                            double rotationSpeed = (mouseX - oldMouseX)/30.0;
                            if(turnR){
                            direction += rotationSpeed;
                            }else if(turnL){
                                direction += rotationSpeed;
                            }else{
                                direction += 0;
                            }
                            
                            if(keyDetect.isCrouching()){
                                player.updatePlayer((int)keyDetect.getX(), (int)keyDetect.getY(), direction, 1.5);
                            }else if(keyDetect.isJumping()){
                                player.updatePlayer((int)keyDetect.getX(), (int)keyDetect.getY(), direction, 2);
                            }else{
                                player.updatePlayer((int)keyDetect.getX(), (int)keyDetect.getY(), direction, 4);
                            }
                            keyDetect.updateParticle(player);
                        }
                       
                        mouseY -=(FRAMEWIDTH/2);
                        mouseY *=-1;
                        //draw rays
                        rays = player.getRays();
                        for (Ray ray : rays) {
                            Boundary currentWall = null;
                            double closest = Double.POSITIVE_INFINITY;
                            Vector intersectionPoint = null;
                            for(Boundary wall:walls){
                                Vector wallIntersectionPoint = ray.findWallIntersection(wall);
                                if(wallIntersectionPoint == null){ //if no intersection to wall skip to next wall
                                    continue;
                                }
                                
                                double mag =  wallIntersectionPoint.subtractVector(player.getPosition()).magnitude(); //distance to a wall
                                if(mag < closest){//finds closest wall interseciton
                                    closest = mag;
                                    intersectionPoint = wallIntersectionPoint;
                                    currentWall = wall;
                                }
                            }
                            if(intersectionPoint != null){
                                gMap.setColor(Color.yellow);
                                gMap.drawLine(toPixelCoordsX((int) ray.getPosition().getX()), toPixelCoordsY((int) ray.getPosition().getY()), toPixelCoordsX((int)intersectionPoint.getX())-3/2, toPixelCoordsY((int)intersectionPoint.getY())-3/2);
                                Vector[] wallPoints = currentWall.getPoints();
                                double distanceAlongWall = intersectionPoint.subtractVector(wallPoints[0]).magnitude();
                                double wallLength = wallPoints[1].subtractVector(wallPoints[0]).magnitude();
                                double angle = ray.getDirection().angle(player.getDirection());
                                double correction = Math.cos(angle);
                                double rawDistance = intersectionPoint.subtractVector(player.getPosition()).magnitude();
                                scene.add(new double[]{correction * rawDistance, distanceAlongWall, wallLength});
                                wallTextures.add(currentWall);
                            }else{
                                gMap.setColor(Color.white);
                                gMap.drawLine(toPixelCoordsX((int) ray.getPosition().getX()), toPixelCoordsY((int) ray.getPosition().getY()), toPixelCoordsX((int) (ray.getPosition().getX() + ray.getDirection().getX())), toPixelCoordsY((int) (ray.getPosition().getY() + ray.getDirection().getY())));
                                scene.add(new double[]{(double)10000, -1, -1});
                                wallTextures.add(currentWall);
                            }
                        }
                        
                        gMap.dispose();
                        gMap = mapCanvas.getGraphics();
                        gMap.drawImage(vImageMap, 0, 0, FRAMEWIDTH, FRAMEHEIGHT, null);
                        gMap.dispose();
                        
                       
                        if(vImageRender.validate(gcRender) == VolatileImage.IMAGE_INCOMPATIBLE){
                            vImageRender = gcRender.createCompatibleVolatileImage(FRAMEWIDTH, FRAMEHEIGHT);
                        }
                        Graphics gRender = vImageRender.getGraphics();
                        double yOffset = keyDetect.getYOffset();
                        int yAdj;
                       // System.out.println(yOffset);
                        gRender.setColor(Color.BLUE);
                        gRender.fillRect(0, 0, FRAMEWIDTH, FRAMEHEIGHT/2 + mouseY);
                        gRender.setColor(Color.darkGray);
                        gRender.fillRect(0, FRAMEHEIGHT/2 + mouseY, FRAMEWIDTH, FRAMEHEIGHT/2 - mouseY);
                        
                        double res = (double)FRAMEWIDTH/scene.size();
                        for(int i=0;i<scene.size();i++){
                            double height =  (FRAMEHEIGHT * 3000) / (player.getFOV() * scene.get(i)[0]);
                            if(scene.get(i)[1] == -1){
                                continue;
                            }
                            double proportion = Math.floor(scene.get(i)[2]/100) * (scene.get(i)[1]/scene.get(i)[2]);
                            int imgNum = (int)Math.floor(proportion);
                            proportion = proportion - imgNum;
                            BufferedImage texture = wallTextures.get(i).getTexture();
                            int imgWidth = texture.getWidth();
                            int imgHeight = texture.getHeight();
                            yAdj = mouseY + (int)(50*yOffset/scene.get(i)[0]);
                            gRender.drawImage(texture, (int)(i*res), FRAMEHEIGHT/2- (int)height + yAdj, (int)(i*res) + (int)res+1, FRAMEHEIGHT/2+(int)height + yAdj, (int)(proportion*imgWidth), 0, (int)(proportion*imgWidth) + 1, imgHeight, Color.getHSBColor(30.242f, 0, 1-(float)inverseSquare(scene.get(i)[0])), frame);
                            int brightness = (int)(256 - (256 * (1-inverseSquare(scene.get(i)[0]))));
                            if(brightness>255){
                                brightness = 255;
                            }
                            gRender.setColor(new Color(0,0,0,brightness));
                            gRender.fillRect((int)(i*res), FRAMEHEIGHT/2-(int)height+ yAdj, (int)res+1, (int)height*2);
                        }
                        
                        
                        //draw NPC
                        double distanceToNPC = npcTest.relativePosition(player).getY();
                        double dv = (FRAMEWIDTH/2) / Math.tan(toRadians(player.getFOV()) / 2);
                        double height = 10 * (dv / distanceToNPC);
                        int brightness = (int)(256 - (256 * (1-inverseSquare(distanceToNPC))));
                        if(brightness>255){
                            brightness = 255;
                        }
                        
                        if(height>0){ //if actually in FOV then draw
                            gRender.drawImage(WallTextures.BRICKS, toPixelCoordsX((int)(npcTest.relativePosition(player).getX() * (dv / distanceToNPC))), FRAMEHEIGHT/2 + mouseY + (int)(50 * yOffset/ distanceToNPC), (int)height, (int)height, frame);
                            gRender.setColor(new Color(0,0,0,brightness));
                            gRender.fillRect(toPixelCoordsX((int)(npcTest.relativePosition(player).getX() * (dv / distanceToNPC))), FRAMEHEIGHT/2 + mouseY + (int)(50 * yOffset/ distanceToNPC), (int)height, (int)height);
                        }
                        
                        //draw FPS and pause 
                        double endTime = System.nanoTime();
                        double timeDiff = endTime - startTime;
                        double timeDiffSecs = timeDiff/1000000000;
                        double fps = (1/timeDiffSecs);
                        gRender.setColor(Color.WHITE);
                        gRender.drawString("FPS: "+(int)fps, 3, 12);
                        if(keyDetect.isPaused()){
                            gRender.drawString("PAUSED", FRAMEWIDTH - 50, 12);
                        }
     
                        //draw crosshair
                        gRender.drawLine(FRAMEWIDTH/2, FRAMEHEIGHT/2+10, FRAMEWIDTH/2, FRAMEHEIGHT/2-10); //vertical
                        gRender.drawLine(FRAMEWIDTH/2-10, FRAMEHEIGHT/2, FRAMEWIDTH/2+10, FRAMEHEIGHT/2); //horizontal
                        
                        gRender.dispose();
                        gRender = renderCanvas.getGraphics();
                        gRender.drawImage(vImageRender, 0, 0, FRAMEWIDTH, FRAMEHEIGHT, null);
                        gRender.dispose();
                    }
                } catch (AWTException ex) {
                    Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
                }
  
            }  
        };
        thread.start();
        
    }
    
    private double toRadians(double degrees){
        return 0.0174533*degrees;
    }
     
    
}
