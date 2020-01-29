import java.awt.image.BufferedImage;
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

/**
 * Maps image values to find best pairs 
 * 
 * @Jonathan Ke 
 * @9/13/2019
 */

 public class MapPoints{

    //returns best fit image from newImages
    public static SubImage findBestImage(int randVal, SubImage baseImage, SubImage[] newImages, int index0, int[][] bannedList){
    //public static int findBestImage(SubImage baseImage, SubImage[] newImages){
        int bestImageIndex = 0;
        int bestDistance = 999999999;
        //finds the average distance
        for (int i = 0;  i < newImages.length; i++){
            SubImage curr = newImages[i];
           
            int dist = baseImage.getDistance(curr.getRed(),curr.getGreen(),curr.getBlue());
            if (dist < bestDistance && !inArray(index0, i, bannedList)){
                bestDistance = dist;
                bestImageIndex = i;
            }
        }
        //update bannedList
        if (randVal != 0){
            for (int i = 0; i < Math.random()*randVal; i++){
                 bannedList = shiftElementsLeft(bannedList);
             }
             bannedList[bannedList.length-1][0] = index0;
             bannedList[bannedList.length-1][1] = bestImageIndex;
        }
        return newImages[bestImageIndex];
    }
    
    private static Boolean inArray(int i1, int i2, int[][] a){
        for (int j = 0; j < a.length; j++){
            if (i1 == a[j][0] && i2 == a[j][1]) {
                return true;
            }
        }
        return false;
    }

    public static SubImage[][] findImageMosaic(int randVal, SubImage[][] imgArray, SubImage[] imgList){
        int w = imgArray.length;
        int h = imgArray[0].length;
        SubImage[][] outArray = new SubImage[w][h];

        //filter elements
        Filter filteredArray = new Filter(imgList, imgList.length);

        //create a ban system
        int[][] bannedList = new int[1][2];
        if (randVal > 0) {
            bannedList = new int[randVal][2];
            //populate banned list with set size of elements
            for (int i = 0; i < bannedList.length; i++){
                bannedList[i][0] = -1;
                bannedList[i][1] = -1;
            }
        }

        //finding best image for each element of imgArray
        for (int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){
                //get image we are processing
                SubImage img = imgArray[x][y];
                //find index1 and index2 of output
                int index = filteredArray.getIndex(img.getRed(),img.getGreen(),img.getBlue());
                SubImage[] newImage = filteredArray.getBox(img.getRed(),img.getGreen(),img.getBlue()); 

                //get the best image for this location
                SubImage sub = findBestImage(randVal, imgArray[x][y],newImage, index, bannedList);
                //int bestIndex = findBestImage(imgArray[x][y],newImage);

                //set best found image
                outArray[x][y] = sub;
            }
        }
        return outArray;
    }

    private static void printBannedList(int[] a){
        for (int i : a){
            System.out.print(i+" ");
        }
        System.out.println();
    }

    private static int[][] shiftElementsLeft(int[][] a){
        int[] lastVal = a[0];
        for (int i = 1; i < a.length; i++){
            a[i-1] = a[i];
        }
        a[a.length-1] = lastVal;
        return a;
    }

    
    public static BufferedImage pasteImages(SubImage[][] in, int w, int h){
        BufferedImage out = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);

        int xU = in[0][0].getImage().getWidth();
        int yU = in[0][0].getImage().getHeight();

        Graphics g = out.getGraphics();

        for (int x = 0; x < in.length; x++){
            for (int y = 0; y < in[0].length; y++){
                g.drawImage(in[x][y].getImage(), xU*x, yU*y, null);
            }
        }
        g.dispose();

        return out;
    }

    public static BufferedImage loadImage(String name, int num){
        BufferedImage b = null;
        try{
            File f = new File(name+num+".jpg");
            b = ImageIO.read(f);
        } catch (Exception e){
            try{
                File f = new File(name+num+".jpeg");
             b = ImageIO.read(f);
            } catch (Exception e1){
                try{
                    File f = new File(name+num+".png");
                    b = ImageIO.read(f);
                } catch (Exception e2){  
                    System.out.println(num);
                    e2.printStackTrace();
                }
            }
        }
        return b;
    }

    //private static final int SIZE = 40;

    public static SubImage[] getImages(String name, int size, int newWidth, int newHeight){
        int w = newWidth;
        int h = newHeight;

        int numPics = size;


        //SubImage[] imgs = new SubImage[numPics];
        ArrayList<SubImage> imgs = new ArrayList<SubImage>();
        for (int i = 1; i <= numPics; i++){
            BufferedImage img = null;
            img = loadImage(name,i);
            if(img != null)
            {
                img = Resizer.resize(img, w, h);
                imgs.add(new SubImage(img,i));
            }
        }

        return imgs.toArray(new SubImage[imgs.size()]);
    }

 
    public static void main(String[] args){
        //timer for program runtime
        long startTime = System.nanoTime();

        int SIZE = 30; //The grid of the original image
        int randVal = 5;
        int imageWidth = 800; //The height of the image scaled

        String originalImage = "golden_retriever/golden_retriever_1.jpg"; //The Big Image
        String imagesUtilized = "golden_retriever"; //Directory of Small Images
        //int numOfPics = 25;
        
        BufferedImage img1 = LargeImage.loadImage(originalImage);
        int imageHeight = img1.getHeight()*(imageWidth/img1.getWidth()); //The width of the image scaled
        img1 = Resizer.resize(img1, imageHeight, imageWidth);
        LargeImage lgImage = new LargeImage(img1, SIZE, SIZE);
        SubImage[] goldenRetrievers = getImages(imagesUtilized+"/"+imagesUtilized+"_",new File(imagesUtilized+"/").listFiles().length-1,img1.getWidth()/SIZE,img1.getHeight()/SIZE);
        //SubImage[] goldenRetrievers = getImages(imagesUtilized+"/"+imagesUtilized+"_",numOfPics,img1.getWidth()/SIZE,img1.getHeight()/SIZE);
        SubImage[][] output = findImageMosaic(randVal, lgImage.getSubImageArray(), goldenRetrievers);

        BufferedImage finalImage = pasteImages(output,img1.getWidth(),img1.getHeight());


        JFrame f = new JFrame("TESTING");
            
        JLabel l = new JLabel(new ImageIcon(finalImage));
    
        f.add(l);
        f.pack();
        f.setFocusable(true);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }

 }
