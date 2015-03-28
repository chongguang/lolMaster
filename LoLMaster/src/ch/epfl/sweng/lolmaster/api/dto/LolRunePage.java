/**
 * 
 */
package ch.epfl.sweng.lolmaster.api.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class LolRunePage extends LolDTO {
    private List<LolRune> mRunes;
    private Map<String, Double> mStatsMap = new HashMap<String, Double>();
    private String mName;

    public LolRunePage(String name, List<LolRune> runes) {
        mRunes = runes;
        mName = name;
        makeStatsMap();
    }

    private void makeStatsMap() {
        for (LolRune rune : mRunes) {
            String description = rune.getDescription();
            if (mStatsMap.containsKey(description)) {
                Double pastValue = mStatsMap.get(description);
                Double addValue = rune.getStatValue();
                mStatsMap.remove(description);
                mStatsMap.put(description, pastValue + addValue);
            } else {
                mStatsMap.put(description, rune.getStatValue());
            }
        }
    }

    /**
     * Get the Map Stat/Value of the rune Page
     * @return Map Stat/Value of the rune Page
     */
    public Map<String, Double> getStatsMap() {
        return mStatsMap;
    }

    /**
     * Get the name of the rune page
     * @return name of the rune page
     */
    public String getName() {
        return mName;
    }
}
