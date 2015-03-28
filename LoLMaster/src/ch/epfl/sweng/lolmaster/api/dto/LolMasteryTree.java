/**
 * 
 */
package ch.epfl.sweng.lolmaster.api.dto;

import java.util.List;

import dto.Static.MasteryTree;
import dto.Static.MasteryTreeItem;
import dto.Static.MasteryTreeList;
import dto.Summoner.Mastery;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class LolMasteryTree extends LolDTO {

    private MasteryTree mMasteryTree;

    public LolMasteryTree(MasteryTree masteryTree) {
        this.mMasteryTree = masteryTree;
    }

    /**
     * Check if the mastery is offense
     * @param mastery The mastery to check
     * @return true if the mastery is offense and false if not
     */
    public boolean isOffense(Mastery mastery) {
        return treeScan(mMasteryTree.getOffense(), mastery);
    }

    /**
     * Check if the mastery is defense
     * @param mastery The mastery to check
     * @return true if the mastery is defense and false if not
     */
    public boolean isDefense(Mastery mastery) {
        return treeScan(mMasteryTree.getDefense(), mastery);
    }

    /**
     * Check if the mastery is utility
     * @param mastery The mastery to check
     * @return true if the mastery is utility and false if not
     */
    public boolean isUtility(Mastery mastery) {
        return treeScan(mMasteryTree.getUtility(), mastery);
    }

    private boolean treeScan(List<MasteryTreeList> tree, Mastery mastery) {
        for (MasteryTreeList list : tree) {
            for (MasteryTreeItem item : list.getMasteryTreeItems()) {
                if (item != null && mastery.getId() == item.getMasteryId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
