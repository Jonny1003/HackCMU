import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Jonathan Ke 
 * @9/13/2019
 * 
 * Creates a subImage object with average rgb for the image
 */

 public class SubImage {

    private BufferedImage img;
    //colors
    private int r;
    private int g;
    private int b;
    private int index;

    

    public SubImage(BufferedImage img, int i){
        this.img = img;
        index = i;
        
        //find average of each pixel
        int r = 0, g = 0, b = 0;

        for (int x = 0; x < img.getWidth(); x++){
            for (int y = 0; y < img.getHeight(); y++){
                r += findR(x,y);
                g += findG(x,y);
                b += findB(x,y);
            }
        }
        this.r = (r/(img.getWidth()*img.getHeight()));
        this.g = (g/(img.getWidth()*img.getHeight()));
        this.b = (b/(img.getWidth()*img.getHeight()));
    }

    private int findR(int x,int y){
        int pixel = img.getRGB(x,y);
        return (pixel >> 16) & 0xff;
    }

    private int findB(int x, int y){
        int pixel = img.getRGB(x,y);
        return (pixel) & 0xff;
    }

    private int findG(int x, int y){
        int pixel = img.getRGB(x,y);
        return (pixel >> 8) & 0xff;
    }

    public BufferedImage getImage(){
        return img;
    }

    public int getRed(){
        return r;
    }

    public int getGreen(){
        return g;
    }

    public int getBlue(){
        return b;
    }

    public void setRGB(int newRed, int newGreen, int newBlue){
        r = newRed;
        g = newGreen;
        b = newBlue;
    }

    //finds the distance between r,g,b for this image and another
    public int getDistance(int red,int green,int blue){
        int rs = red-r;
        int gs = green-g;
        int bs = blue-b;
        return rs*rs+gs*gs+bs*bs;
    }

    //implementation of v2 distance function for comparision
    public void reweight(int red, int green, int blue, int k, int out){
        r = yangFunc(r,red,k,out);
        g = yangFunc(g,green,k,out);
        b = yangFunc(b,blue,k,out);
    }

    //Yangs special function for magical shit
    public static int yangFunc(int x, int x1, int k, int d){
        d = (int) Math.sqrt(d);
        return (int)(x + (k-d)/d*(x-x1));
    }

    public int getIndex(){
        return index;
    }

    public static void main(String[] args){
        SubImage test = new SubImage(LargeImage.loadImage("black.jpg"));
        JFrame f = new JFrame("SUBIMAGE");
            
        JLabel l = new JLabel(new ImageIcon(test.getImage()));
    
        f.add(l);
        f.pack();
        f.setFocusable(true);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
 }