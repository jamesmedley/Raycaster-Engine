/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wall_textures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author james
 */
public class WallTextures {
    
    public static BufferedImage BRICKS;
    public static BufferedImage FIRE;
    public static BufferedImage GALVANIZED_METAL;
    public static BufferedImage METAL;
    public static BufferedImage PAINTED_METAL;
    public static BufferedImage REINFORCED_METAL;
    public static BufferedImage TNT_BARREL;
    public static BufferedImage WOOD;
    public static BufferedImage MINECRAFT_OAK_PLANKS;

    public WallTextures(){
        URL resource;
        try {
            
            resource = WallTextures.class.getResource("/wall_textures/bricks.jpg");
            BRICKS = ImageIO.read(resource);
            
            resource = WallTextures.class.getResource("/wall_textures/fire2.jpg");
            FIRE = ImageIO.read(resource);
            
            resource = WallTextures.class.getResource("/wall_textures/galvanized metal.jpg");
            GALVANIZED_METAL = ImageIO.read(resource);
            
            resource = WallTextures.class.getResource("/wall_textures/metal.jpg");
            METAL = ImageIO.read(resource);
            
            resource = WallTextures.class.getResource("/wall_textures/painted metal.jpg");
            PAINTED_METAL = ImageIO.read(resource);
            
            resource = WallTextures.class.getResource("/wall_textures/reinforced metal.jpg");
            REINFORCED_METAL = ImageIO.read(resource);
            
            resource = WallTextures.class.getResource("/wall_textures/tnt_barrel.png");
            TNT_BARREL = ImageIO.read(resource);
            
            resource = WallTextures.class.getResource("/wall_textures/wood.png");
            WOOD = ImageIO.read(resource);
            
            resource = WallTextures.class.getResource("/wall_textures/minecraft oak planks.png");
            MINECRAFT_OAK_PLANKS = ImageIO.read(resource);
        
        } catch (IOException ex) {
            Logger.getLogger(WallTextures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
    
    
}
