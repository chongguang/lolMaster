/**
 * 
 */
package ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist;

import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolImage;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class OutOfGameChampion {
    private String mChampionName;
    private LolId mChampionId;
    private LolImage mChampionImage;

    public OutOfGameChampion(String championName, LolId championId,
        LolImage championImage) {
        this.mChampionId = championId;
        this.mChampionName = championName;
        this.mChampionImage = championImage;
    }

    public String getName() {
        return mChampionName;
    }

    public LolId getId() {
        return mChampionId;
    }

    public LolImage getImage() {
        return mChampionImage;
    }
}
