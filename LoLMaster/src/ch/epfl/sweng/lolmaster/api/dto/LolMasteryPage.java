/**
 * 
 */
package ch.epfl.sweng.lolmaster.api.dto;

import android.util.Log;
import dto.Summoner.Mastery;
import dto.Summoner.MasteryPage;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class LolMasteryPage extends LolDTO {

    private MasteryPage mMasteryPage;
    private LolMasteryTree mMasteryTree;
    private int mOffense = 0;
    private int mDefense = 0;
    private int mUtility = 0;

    public LolMasteryPage(MasteryPage masteryPage, LolMasteryTree masteryTree) {
        this.mMasteryPage = masteryPage;
        this.mMasteryTree = masteryTree;
        makeTree();
    }

    private void makeTree() {
        if (mMasteryPage == null || mMasteryPage.getMasteries() == null) {
            Log.w(this.getClass().getName(),
                "No mastery page to make the tree.");
            return;
        }

        for (Mastery mastery : mMasteryPage.getMasteries()) {
            if (mastery != null) {
                if (mMasteryTree.isOffense(mastery)) {
                    mOffense += mastery.getRank();
                } else if (mMasteryTree.isDefense(mastery)) {
                    mDefense += mastery.getRank();
                } else if (mMasteryTree.isUtility(mastery)) {
                    mUtility += mastery.getRank();
                }
            }
        }
    }

    /**
     * Get Tree's description
     * @return tree's description
     */
    public String getTreeDescription() {
        return mOffense + "/" + mDefense + "/" + mUtility;
    }
}
