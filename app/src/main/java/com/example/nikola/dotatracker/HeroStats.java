package com.example.nikola.dotatracker;

import com.example.nikola.dotatracker.interfaces.MyListItem;

import java.text.DecimalFormat;

public class HeroStats implements MyListItem {
    private int gamesPlayed;
    private int gamesWon;
    private int heroId;

    public HeroStats(int gamesPlayed, int gamesWon, int heroId) {

        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.heroId = heroId;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getHeroId() {
        return heroId;
    }

    public String getWinLos() {
        int loss = gamesPlayed - gamesWon;
        return gamesWon + "/" + loss;
    }

    public String getWinPercentageText() {
        if (gamesPlayed != 0) {
            DecimalFormat newFormat = new DecimalFormat("#.##");
            double winRate;
            winRate = ((double) gamesWon / (double) gamesPlayed) * 100;
            winRate = Double.parseDouble(newFormat.format(winRate));
            return winRate + " %";

        }
        return "0.0 %";
    }

    public double getWinRate() {
        if (gamesPlayed != 0) {
            DecimalFormat newFormat = new DecimalFormat("#.##");
            double winRate;
            winRate = ((double) gamesWon / (double) gamesPlayed) * 100;
            winRate = Double.parseDouble(newFormat.format(winRate));
            return winRate;
        } else {
            return 0;
        }
    }

    @Override
    public int getMyListItemType() {
        return MyListItem.HERO_STATS_TYPE;
    }
}