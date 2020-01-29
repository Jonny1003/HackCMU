import java.awt.image.BufferedImage;
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;

public class FileInput implements ActionListener{

    JTextField in1;
    JTextField in2;
    GUI g;

    public FileInput(JTextField pic, JTextField file, GUI g){
        in1 = pic;
        in2= file;
        this.g = g;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String picText = in1.getText();
        String file = in2.getText();
        g.setImageUtilized(file);
        g.setOriginalImage(picText);
        g.setMosaic();
    }
}