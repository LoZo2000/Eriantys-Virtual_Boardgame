package it.polimi.ingsw.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * This class contains some methods to help in the gui construction, these methods principally scale the objects in the gui
 */
public class ScalingUtils {
    private final static HashMap<String, ImageIcon> images = new HashMap<>();
    private final static int widthScreen = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final static int heightScreen = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public static int scaleX(int x){
        return Math.round(x/(float)1366*widthScreen);
    }

    public static int scaleY(int y){
        return Math.round(y/(float)768*heightScreen);
    }

    public static int scaleX(int x, int oldDim){
        return Math.round(x/(float)oldDim*scaleX(oldDim));
    }

    public static int scaleY(int y, int oldDim){
        return Math.round(y/(float)oldDim*scaleY(oldDim));
    }

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
     * This method is called to scale a font
     * @param fontNotScaled is the font to scale
     * @return an int representing the size of the font scaled
     */
    public static int scaleFont(int fontNotScaled){
        return fontNotScaled*widthScreen/1366;
    }

    /**
     * This method is called to scale an icon
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
