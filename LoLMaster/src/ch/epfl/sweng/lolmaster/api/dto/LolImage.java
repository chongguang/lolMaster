package ch.epfl.sweng.lolmaster.api.dto;

import dto.Static.Image;

/**
 * Facilitates the use of an Image object from the API
 * 
 * @author ygrimault
 * 
 */
public class LolImage extends LolDTO {

    private final Image mImage;

    public LolImage(Image image) {
        mImage = image;
    }

    public String getFull() {
        if (mImage != null) {
            return mImage.getFull();
        } else {
            return "ERROR";
        }
    }

    public String getGroup() {
        if (mImage != null) {
            return mImage.getGroup();
        } else {
            return "ERROR";
        }
    }

    public String getSprite() {
        if (mImage != null) {
            return mImage.getSprite();
        } else {
            return "ERROR";
        }
    }
}
