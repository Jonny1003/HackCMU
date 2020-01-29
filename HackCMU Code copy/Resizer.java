import java.awt.image.BufferedImage;
import java.awt.Image;


/**
 * @Jonathan Ke
 * @9/13/2019
 * 
 * Resizes images efficiently
 */

public class Resizer{
    
    //resizes the input image to the given dimensions
    public static BufferedImage resize(BufferedImage b, int width, int height){
        Image i = b.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage out = new BufferedImage(i.getWidth(null), 
        i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        out.getGraphics().drawImage(i, 0, 0, null);
        return out;
    }

    //crops the image to the new width and height
    public static BufferedImage crop(BufferedImage b, int width, int height){
        int x = (b.getWidth()-width)/2;
        int y = (b.getHeight()-height)/2;
        return b.getSubimage(x, y, width, height);
    }

    public static BufferedImage resizeAndCrop(BufferedImage b, int w, int h){
        BufferedImage out = resize(b,w,h*b.getWidth()/w);
        return crop(out,w,h);
    }

    
}