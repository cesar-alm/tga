package fr.badcookie20.tga.player;

import fr.badcookie20.tga.utils.ConfigUtils;

public class Statistics {

    private int won;
    private int lost;
    private int surrendered;
    private int wonBySurrender;

    private Statistics(TGAPlayer p) {
        won = ConfigUtils.getStatistic(p, ConfigUtils.WON_EXTENSION);
        lost = ConfigUtils.getStatistic(p, ConfigUtils.LOST_EXTENSION);
        surrendered = ConfigUtils.getStatistic(p, ConfigUtils.SURRENDERED_EXTENSION);
        wonBySurrender = ConfigUtils.getStatistic(p, ConfigUtils.WON_BY_SURRENDER_EXTENSION);
    }

    public int getTotalGames() {
        return won+lost+surrendered+wonBySurrender;
    }

    public int getWonGames() {
        return won;
    }

    public int getLostGames() {
        return lost;
    }

    public int getSurrenderedGames() {
        return surrendered;
    }

    public int getWonBySurrenderGames() {
        return wonBySurrender;
    }

    public static Statistics getStatistics(TGAPlayer p) {
        return new Statistics(p);
    }

}
