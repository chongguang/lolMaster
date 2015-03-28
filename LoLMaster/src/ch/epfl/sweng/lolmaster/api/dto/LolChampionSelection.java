package ch.epfl.sweng.lolmaster.api.dto;

import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.ChampionSelection;

/**
 * @author fKunstner
 */
final public class LolChampionSelection extends LolDTO {
    private final ChampionSelection mChampionSelection;

    public LolChampionSelection(ChampionSelection eChampionSelection) {
        mChampionSelection = eChampionSelection;
    }

    /**
     * champion's ID
     * @return champion's {@see LolId}
     */
    public LolId getChampionId() {
        return new LolId(mChampionSelection.getChampionId());
    }

    /**
     * current player's internal name
     * @return current player's internal name
     */
    public String getSummonerInternalName() {
        return mChampionSelection.getSummonerInternalName();
    }

    /**
     * current player's first spell ID
     * @return current player's first spell {@see LolId}
     */
    public LolId getSpell1Id() {
        return new LolId(mChampionSelection.getSpell1Id());
    }

    /**
     * current player's second spell ID
     * @return current player's second spell {@see LolId}
     */
    public LolId getSpell2Id() {
        return new LolId(mChampionSelection.getSpell2Id());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof LolChampionSelection) {
            LolChampionSelection that = (LolChampionSelection) object;
            boolean equals = true;
            equals &= getSpell1Id().equals(that.getSpell1Id());
            equals &= getSpell2Id().equals(that.getSpell2Id());
            equals &= getChampionId().equals(that.getChampionId());
            equals &=
                getSummonerInternalName()
                    .equals(that.getSummonerInternalName());
            return equals;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getSpell1Id().hashCode() ^ getSpell2Id().hashCode()
            ^ getChampionId().hashCode() ^ getSummonerInternalName().hashCode();
    }
}
