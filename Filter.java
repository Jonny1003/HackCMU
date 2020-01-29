import java.util.ArrayList;

/**
 * @Jonathan Ke, Poojan Palwai
 * @9/14/2019
 * 
 * Sorts array of images into a box
 */

 public class Filter{

    private int numImages;
    private int boxDimensions;
    //private int elementsPerBox = (int) (numImages/Math.pow(boxDimensions,3))*10;
    private int containerSpace;
   
    private ArrayList<ArrayList<SubImage>> filteredBox = new ArrayList<ArrayList<SubImage>>();

    public Filter(SubImage[] a, int numImages){
        this.numImages = numImages;
        boxDimensions = (int) Math.pow(this.numImages,0.25);
        containerSpace = 256/boxDimensions;

        //loop through and initialize arrayLists inside cube
        for (int i = 0; i < (int)Math.pow(boxDimensions,3); i++){
            filteredBox.add(new ArrayList<SubImage>());
        }
        
        for (SubImage i : a){
            int boxIndex = getIndex(i.getRed(),i.getGreen(),i.getBlue());
            ArrayList<SubImage> container = filteredBox.get(boxIndex);
            //if (container.size() < elementsPerBox){
                container.add(i);
            //}
        }
        printFilter();
    }

    //returns approriate index in fliteredBox
    public int getIndex(int r, int g, int b){
        return r/containerSpace + g/containerSpace*boxDimensions + 
        b/containerSpace*boxDimensions*boxDimensions;
    }

    public SubImage[] getBox(int r, int g, int b){
        int index = getIndex(r,g,b);
        ArrayList<SubImage> box = filteredBox.get(index);
        if (box.size() == 0){
            if (r < 255-containerSpace){
                return getBox(r+containerSpace,g,b);
            } 
            else if(g < 255-containerSpace){
                return getBox(r,g+containerSpace,b);
            }
            else if(b < 255-containerSpace){
                return getBox(r,g,b+containerSpace);
            }
            else {
                for(int i = 0; i < filteredBox.size(); i++)
                {
                    if (filteredBox.get(i).size() > 0){
                        ArrayList<SubImage> out = filteredBox.get(i);
                        return out.toArray(new SubImage[out.size()]);
                    }
                }
            }
        } 
        return box.toArray(new SubImage[box.size()]);
    }

    public void printFilter()
    {
        int count = 0;
        for(int i = 0; i< filteredBox.size(); i++)
        {
            count += filteredBox.get(i).size();
            System.out.println(i + "\t" +filteredBox.get(i).size() + " Red: " + (i % 4) + " Green: " + (i % 16)/4 + " Blue: " + (i / 16));
        }
        System.out.println("Total Images: " + count);
    }
 }