package ch.epfl.sweng.lolmaster.api.directapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.ApiKeysManager;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolItem;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import constant.staticdata.ChampData;
import constant.staticdata.ItemListData;
import constant.staticdata.MasteryListData;
import constant.staticdata.RuneListData;
import constant.staticdata.SpellData;
import dto.Static.ChampionList;
import dto.Static.ItemList;
import dto.Static.MasteryList;
import dto.Static.RuneList;
import dto.Static.SummonerSpell;
import dto.Static.SummonerSpellList;

/**
 * @author fKunstner
 * 
 */
public final class LolStaticDataApi {
    private static final String NO_RESPONSE_MESSAGE =
        "Riot Api returned a null object or an empty collection";

    private LolStaticDataApi() {

    }

    private static RiotApi getRiotApi() {
        return new RiotApi(ApiKeysManager.getApiKeys().getRiotKey());
    }

    /**
     * @author fKunstner
     * 
     */
    public static final class Rune {
        private static RuneList mRuneCache = null;

        private Rune() {

        }

        public static synchronized RuneList get() throws ApiException {
            if (mRuneCache == null) {
                try {
                    mRuneCache =
                        getRiotApi().getDataRuneList(null, null,
                            RuneListData.ALL);
                } catch (RiotApiException e) {
                    throw new ApiException(e);
                }
            }
            return mRuneCache;
        }
    }

    /**
     * @author fKunstner
     * 
     */
    public static final class Item {
        private static Map<LolId, LolItem> mItemCache = null;

        private Item() {

        }

        public static synchronized Map<LolId, LolItem> get()
            throws ApiException {
            if (mItemCache == null) {
                ItemList itemList;
                try {
                    itemList =
                        getRiotApi().getDataItemList(null, null,
                            ItemListData.ALL);
                    if (itemList == null || itemList.getData().isEmpty()) {
                        throw new ApiException(NO_RESPONSE_MESSAGE);
                    }
                } catch (RiotApiException e) {
                    throw new ApiException(e);
                }

                Map<LolId, LolItem> itemByIds = new HashMap<LolId, LolItem>();
                for (Entry<String, dto.Static.Item> entry : itemList.getData()
                    .entrySet()) {
                    itemByIds.put(new LolId(entry.getValue().getId()),
                        new LolItem(entry.getValue()));
                }
                mItemCache = itemByIds;
            }
            return mItemCache;
        }

    }

    /**
     * @author fKunstner
     * 
     */
    public static final class Mastery {
        private static MasteryList mMasteryCache = null;

        private Mastery() {

        }

        public static synchronized MasteryList get() throws ApiException {
            if (mMasteryCache == null) {
                try {
                    mMasteryCache =
                        getRiotApi().getDataMasteryList(null, null,
                            MasteryListData.ALL);
                } catch (RiotApiException e) {
                    throw new ApiException(e);
                }
            }
            return mMasteryCache;
        }
    }

    /**
     * @author fKunstner
     * 
     */
    public static final class Champion {
        private static Map<LolId, LolChampionData> mChampionCache = null;

        private Champion() {

        }

        public static synchronized Map<LolId, LolChampionData> get()
            throws ApiException {

            if (mChampionCache == null) {

                ChampionList championList;
                try {
                    championList =
                        getRiotApi().getDataChampionList((String) null,
                            (String) null, true, ChampData.IMAGE);
                    if (championList == null
                        || championList.getData().isEmpty()) {
                        throw new ApiException(NO_RESPONSE_MESSAGE);
                    }
                } catch (RiotApiException e) {
                    throw new ApiException(e);
                }

                Map<String, dto.Static.Champion> championMap =
                    championList.getData();

                Map<LolId, LolChampionData> championDataMap =
                    new HashMap<LolId, LolChampionData>();
                for (Entry<String, dto.Static.Champion> entry : championMap
                    .entrySet()) {

                    LolId id = new LolId(entry.getValue().getId());
                    LolChampionData champion =
                        new LolChampionData(entry.getValue());

                    championDataMap.put(id, champion);
                }
                mChampionCache = championDataMap;
            }
            return mChampionCache;
        }

        /**
         * @author fKunstner
         * 
         */
        public static final class FullData {
            private static Map<LolId, LolChampionData> mFullChampionCache =
                new HashMap<LolId, LolChampionData>();

            private FullData() {

            }

            public static synchronized LolChampionData get(int id)
                throws RiotApiException {
                LolId lolId = new LolId(id);
                if (!mFullChampionCache.containsKey(lolId)) {
                    LolChampionData champion =
                        new LolChampionData(getRiotApi().getDataChampion(id,
                            (String) null, (String) null, true, ChampData.ALL));
                    mFullChampionCache.put(lolId, champion);
                }
                return mFullChampionCache.get(lolId);
            }
        }
    }

    /**
     * @author fKunstner
     * 
     */
    public static final class Spell {
        private static Map<LolId, LolSummonerSpell> mSpellCache = null;

        private Spell() {

        }

        public static synchronized Map<LolId, LolSummonerSpell> get()
            throws ApiException {
            if (mSpellCache == null) {
                SummonerSpellList spellList;
                try {
                    spellList =
                        getRiotApi().getDataSummonerSpellList(null, null,
                            false, SpellData.ALL);
                    if (spellList == null || spellList.getData().isEmpty()) {
                        throw new ApiException(NO_RESPONSE_MESSAGE);
                    }
                } catch (RiotApiException e) {
                    throw new ApiException(e);
                }

                Map<LolId, LolSummonerSpell> spellByIds =
                    new HashMap<LolId, LolSummonerSpell>();
                for (Entry<String, SummonerSpell> entry : spellList.getData()
                    .entrySet()) {
                    spellByIds.put(new LolId(entry.getValue().getId()),
                        new LolSummonerSpell(entry.getValue()));
                }

                mSpellCache = spellByIds;
            }
            return mSpellCache;
        }
    }
}
