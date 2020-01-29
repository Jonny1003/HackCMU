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
        new GUI("golden_retriever/golden_retriever_6.jpg","golden_retriever");
    }

    public static void main(String[] args){
        RunGUI g = new RunGUI();
        SwingUtilities.invokeLater(g);
    }   
}
