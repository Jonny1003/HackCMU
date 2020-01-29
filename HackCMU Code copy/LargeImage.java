import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Jonathan Ke (jak2)
 * @9/13/2019
 */

public class LargeImage{

    private BufferedImage mainImg;
    private int width;
    private int height;

    private SubImage[][] imageArray;

    //create attributes of mainImg, m columns, n rows
    public LargeImage(BufferedImage img, int m, int n){
        mainImg = img;
        width = mainImg.getWidth();
        height = mainImg.getHeight();

        //cut image into m*n smaller images, store in imageArray
        imageArray = new SubImage[m][n];
        int subWidth = width/m;
        int subHeight = height/n;
        for (int x = 0; x < m; x++){
            for (int y = 0; y < n; y++){
                BufferedImage sub = mainImg.getSubimage(x*subWidth,y*subHeight,subWidth,subHeight);
                imageArray[x][y] = new SubImage(sub,-1);
            }
        }
    }

    public BufferedImage getSubimage(int x, int y){
        return imageArray[x][y].getImage();
    }

    public SubImage[][] getSubImageArray(){
        return imageArray;
    }

    //loads an image file as BufferedImage
    public static BufferedImage loadImage(String file){
        BufferedImage b = null;
        try{
            File f = new File(file);
            b = ImageIO.read(f);
        } catch (Exception e){
            System.out.println(file);
            e.printStackTrace();
        }
        return b;
    }

    //gets main image
    public BufferedImage getMainImage(){
        return mainImg;
    }

    public static void main(String[] args){
        JFrame f = new JFrame("MAIN IMAGE");
        
        BufferedImage flower = loadImage("golden_retriever_73.jpg");
        LargeImage test = new LargeImage(flower,10,10);
        JLabel l = new JLabel(new ImageIcon(test.getMainImage()));

        BufferedImage img = test.getMainImage();

        f.add(l);
        f.pack();
        f.setFocusable(true);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}