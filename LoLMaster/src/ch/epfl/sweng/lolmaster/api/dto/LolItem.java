package ch.epfl.sweng.lolmaster.api.dto;

import dto.Static.Image;
import dto.Static.Item;

/**
 * @author lcg31439
 * 
 */
public class LolItem extends LolDTO {
    private final Item mItem;

    public LolItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item passed is null");
        }
        if (item.getId() < 0) {
            throw new IllegalArgumentException(
                "The provided item has invalid Id : " + item.getId());
        }
        mItem = item;
    }

    /**
     * Get item's name
     * @return item's name
     */
    public String getName() {
        return mItem.getName();
    }

    /**
     * Get item's image
     * @return item's image
     */
    public Image getImageData() {
        return mItem.getImage();
    }

}
