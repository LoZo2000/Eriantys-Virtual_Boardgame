package it.polimi.ingsw.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * This class contains some methods to help in the gui construction, these methods principally scale the objects in the GUI
 */
public class ScalingUtils {
    private final static HashMap<String, ImageIcon> images = new HashMap<>();
    private final static int widthScreen = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final static int heightScreen = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    /**
     * This method return the scaled X value compared to the screen resolution using as base resolution 1366
     * @param x is the value that need to be rescaled
     * @return the scaled value
     */
    public static int scaleX(int x){
        return Math.round(x/(float)1366*widthScreen);
    }

    /**
     * This method return the scaled Y value compared to the screen resolution using as base resolution 768
     * @param y is the value that need to be rescaled
     * @return the scaled value
     */
    public static int scaleY(int y){
        return Math.round(y/(float)768*heightScreen);
    }

    /**
     * This method return the scaled X value compared to the original dimension of the container: this dimension will be rescaled based on the screen resolution
     * @param x is the value that need to be rescaled
     * @param containerOriginalDim is the value not scaled of the container
     * @return the rescaled value
     */
    public static int scaleX(int x, int containerOriginalDim){
        return Math.round(x/(float)containerOriginalDim*scaleX(containerOriginalDim));
    }

    /**
     * This method return the scaled Y value compared to the original dimension of the container: this dimension will be rescaled based on the screen resolution
     * @param y is the value that need to be rescaled
     * @param containerOriginalDim is the value not scaled of the container
     * @return the rescaled value
     */
    public static int scaleY(int y, int containerOriginalDim){
        return Math.round(y/(float)containerOriginalDim*scaleY(containerOriginalDim));
    }

    /**
     * This method loads an ImageIcon from the Resources
     * @param path is the path of the file inside the "resources" folder that needs to be loaded
     * @return the ImageIcon object loaded or null if the specified file can't be found
     */
    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ScalingUtils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * This method return the Image contained in the path
     * @param path is the path where the image is placed
     * @return an ImageIcon object representing the image requested
     */
    public static ImageIcon getImage(String path){
        if(images.containsKey(path)){
            return images.get(path);
        } else{
            ImageIcon image = createImageIcon(path);
            images.put(path, image);
            return image;
        }
    }

    /**
     * This method is called to scale a font compared to the width of the screen
     * @param fontNotScaled is the font to scale
     * @return an int representing the size of the font scaled
     */
    public static int scaleFont(int fontNotScaled){
        return fontNotScaled*widthScreen/1366;
    }

    /**
     * This method is called to scale a Default icon used inside Message Dialogs
     * @param icon is the icon to scale
     * @return an Icon object that is the icon scaled
     */
    public static Icon scaleDefaultIcon(Icon icon){
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return new ImageIcon(bufferedImage.getScaledInstance(scaleX(40), scaleX(40), Image.SCALE_SMOOTH));
    }
}
