import java.awt.image.BufferedImage;
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

/**
 * @Jonathan Ke @9/14/2019
 * 
 *User interface for ImageMosaic display
 **/

public class GUI implements ActionListener, MouseListener{

    private int size; // number of images per row of image
    private int randVal; // randomness of picture control
    private int finalWidth; // pic width
    private int finalHeight; // pic height

    //scale factor for smaller image
    private int scaleFactor = 4;

    private String imagesUtilized;


    // images stored in window
    private BufferedImage origImage;
    private BufferedImage outputImage;

    // panels and components
    private JLabel errorMessage = new JLabel(" ");;
    private JTextField sizeTextBox;
    private JTextField randomTextBox;
    private JTextField mainImageBox;
    private JTextField folderBox;
    private JLabel pic;
    private JLabel scaledLabel;

    private SubImage[] inputImages;

    public GUI(String original, String folder) {
        String originalImageStr = original; // The Big Image
        imagesUtilized = folder; // Directory of Small Images
        origImage = LargeImage.loadImage(originalImageStr);

        // fill in field variables EDIT FOR DIFFERENTIAL APPEARANCES
        size = 80;
        randVal = 5;
        finalWidth = 700;
        finalHeight = origImage.getHeight() * finalWidth / origImage.getWidth(); // height as scale factor of width
        // rescale original image to these specifications
        
        origImage = Resizer.resize(origImage, finalWidth, finalHeight);

        // get output from mosaic algorithm in MapPoints
        outputImage = getMosaic();

        // draw the GUI
        GridLayout gridLayout = new GridLayout(0, 1);
        JPanel pRight = new JPanel(gridLayout);

        gridLayout.setHgap(0);
        gridLayout.setVgap(0);

        JLabel sizeField = new JLabel("Enter Resolution Quality (1-200):");
        sizeTextBox = new JTextField();

        JLabel randomField = new JLabel("Enter Randomness Factor (0-20):");
        randomTextBox = new JTextField();

        JButton enterParameters = new JButton("Enter");
        enterParameters.addActionListener(this);

        JLabel originalImageLabel = new JLabel("Enter main image:");
        mainImageBox = new JTextField();

        JLabel fileLabel = new JLabel("Enter image file:");
        folderBox = new JTextField();

        FileInput fileInput = new FileInput(mainImageBox,folderBox,this);

        JButton enterFiles = new JButton("Image Reload");
        enterFiles.addActionListener(fileInput);

        scaledLabel = new JLabel(new ImageIcon(getScaledOriginal()));

        pRight.add(scaledLabel);
        pRight.add(sizeField);
        pRight.add(sizeTextBox);
        pRight.add(randomField);
        pRight.add(randomTextBox);
        pRight.add(enterParameters);
        pRight.add(errorMessage);
        pRight.setMaximumSize(gridLayout.minimumLayoutSize(pRight));

        JPanel pLeft = new JPanel(new GridLayout(1,0));
        pLeft.add(originalImageLabel);
        pLeft.add(mainImageBox);
        pLeft.add(fileLabel);
        pLeft.add(folderBox);
        pLeft.add(enterFiles);

        pic = new JLabel(new ImageIcon(getFinalImage())); //actual mosaic image used
        pic.addMouseListener(this);

        //final panel appended to f
        JPanel pFinal = new JPanel(new BorderLayout(1, 1));
        JPanel pSub = new JPanel(new BorderLayout(1,1));
        pSub.add(pic,BorderLayout.LINE_START);
        pSub.add(pRight,BorderLayout.CENTER);
        pFinal.add(pSub,BorderLayout.CENTER);
        pFinal.add(pLeft, BorderLayout.SOUTH);

        JFrame f = new JFrame("Mosaic Lab");   
        f.add(pFinal);
        f.pack();
        f.setFocusable(true);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setImageUtilized(String a){
        imagesUtilized = a;
    }

    public void setOriginalImage(String a){
        origImage = LargeImage.loadImage(a);
        finalWidth = 700;
        finalHeight = origImage.getHeight() * finalWidth / origImage.getWidth();
        origImage = Resizer.resize(origImage, finalWidth, finalHeight);
    }

    //button action listener
    @Override
    public void actionPerformed(ActionEvent e) {
                String sizeText = sizeTextBox.getText();
                String randText = randomTextBox.getText();
                int size = this.size;
                int rand = this.randVal;
                try{
                    size = Integer.parseInt(sizeText);
                } catch (Exception ex){
                    setErrorMessage("INVALID RESOLUTION VALUE");
                    sizeTextBox.setText("");
                    return;
                }
                try{
                    rand = Integer.parseInt(randText);
                } catch (Exception ex){
                    setErrorMessage("INVALID RANDOMNESS FACTOR");
                    randomTextBox.setText("");
                    return;
                }
                if (size < 1 || size > 200) {
                    setErrorMessage("INVALID RESOLUTION VALUE");
                    return;
                } else if (rand > 20 || rand < 0){
                    setErrorMessage("INVALID RANDOMNESS FACTOR");
                    return;
                } else {
                    this.size = size;
                    this.randVal = rand;
                    sizeTextBox.setText("");
                    randomTextBox.setText("");
                    setErrorMessage(" ");
                    System.out.println(this.size+ " "+this.randVal);
                    setMosaic();
                }
            }

    //mouse listener methods
    @Override
    public void mouseClicked(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        System.out.println(x+" "+y);


        BufferedImage clickedImage = getSubImage(x,y);

        //find image and update scaledLabel
        scaledOriginal = Resizer.resize(clickedImage, finalWidth/scaleFactor, finalHeight/scaleFactor);
        scaledLabel.setIcon(new ImageIcon(scaledOriginal));
    }

    private SubImage[][] outputArray;

    //x and y are the pixel coordinates of the mosaic image
    private BufferedImage getSubImage(int x, int y){
        int pixelSizeX = finalWidth/size;
        int pixelSizeY = finalHeight/size;
        int iX = x/pixelSizeX;
        int iY = Math.max(y/pixelSizeY-1,0);
        SubImage s = outputArray[iX][iY];
        int imageIndex = s.getIndex();
        System.out.println(imageIndex);
        BufferedImage b = MapPoints.loadImage(imagesUtilized+"/"+imagesUtilized+"_",(imageIndex));
        return b;
    }


    @Override
    public void mousePressed(MouseEvent e){
        
    }

    @Override
    public void mouseReleased(MouseEvent e){
        
    }

    @Override
    public void mouseEntered(MouseEvent e){
        
    }

    @Override
    public void mouseExited(MouseEvent e){
        
    }

    public void setErrorMessage(String msg){
        errorMessage.setText(msg);
    }

    public BufferedImage getMosaic(){
        // Construct object maps and points
        LargeImage lg = new LargeImage(origImage, size, size);
        // the images used to mosaic
        inputImages = MapPoints.getImages(imagesUtilized + "/" + imagesUtilized + "_",
                new File(imagesUtilized + "/").listFiles().length - 1, origImage.getWidth() / size,
                origImage.getHeight() / size);
    
        outputArray = MapPoints.findImageMosaic(randVal, lg.getSubImageArray(), inputImages);

        //scaled original image
        scaledOriginal = Resizer.resize(lg.getMainImage(), finalWidth/scaleFactor, finalHeight/scaleFactor);

        return MapPoints.pasteImages(outputArray,lg.getMainImage().getWidth(),lg.getMainImage().getHeight());   
    }

    public void setMosaic(){
        outputImage = getMosaic();
        pic.setIcon(new ImageIcon(outputImage));
        scaledLabel.setIcon(new ImageIcon(scaledOriginal));
        System.out.println("DONE!");
    }

    private BufferedImage scaledOriginal;

    private Image getScaledOriginal(){
        return scaledOriginal;
    }

    public BufferedImage getFinalImage(){
        return outputImage;
    }

    public BufferedImage getOriginalImage(){
        return origImage;
    }

    public static void main(String[] args){
        GUI g = new GUI();
    }
}