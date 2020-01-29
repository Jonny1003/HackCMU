import javax.swing.*;
/**
 * @Jonathan Ke
 * @9/14/2019
 * 
 * Runs Application
**/

public class RunGUI implements Runnable{
    @Override
    public void run(){
        new GUI("hackathon.jpg","golden_retriever");
    }

    public static void main(String[] args){
        RunGUI g = new RunGUI();
        SwingUtilities.invokeLater(g);
    }   
}
