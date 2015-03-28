package ch.epfl.sweng.lolmaster.api.dto;

import dto.Static.Image;
import dto.Static.Passive;

/**
 * Facilitates the use of champions' passive
 * 
 * @author ygrimault
 * 
 */
public class LolPassive extends LolDTO {

    private final Passive mPassive;
    private final Image mImage;

    public LolPassive(Passive passive) {
        mPassive = passive;
        mImage = mPassive.getImage();
    }

    /**
     * Get passive's name
     * @return passive's name
     */
    public String getName() {
        return mPassive.getName();
    }

    /**
     * Get passive's description
     * @return passive's description
     */
    public String getDescription() {
        return mPassive.getDescription();
    }

    /**
     * Get passive's full image
     * @return passive's full image
     */
    public String getFull() {
        return mImage.getFull();
    }

    /**
     * Get passive's grouped image
     * @return passive's grouped image
     */
    public String getGroup() {
        return mImage.getGroup();
    }
}
