package com.example.nikola.dotatracker;


import com.example.nikola.dotatracker.interfaces.MyListItem;

public class RecentMatches implements MyListItem {

    private int heroId;

    // Da li je mec dobijen?
    private boolean radiantWin;
    private int playerSlot;

    // Unknown, Normal, Hight, Very High
    private String gameSkill;

    // All Pick, Random Draft, Single Draft etc...
    private int gameMode;

    // Normal, Ranked, Battle Draft, Tournament etc..
    private int lobbyType;

    // Duration of the match in seconds
    private int duration;

    // Start time of the match in milliseconds
    private long startTime;

    public RecentMatches(int heroId, boolean radiantWin, int playerSlot, String gameSkill, int gameMode, int lobbyType, int duration, long startTime) {
        this.heroId = heroId;
        this.radiantWin = radiantWin;
        this.playerSlot = playerSlot;
        this.gameSkill = gameSkill;

        this.gameMode = gameMode;
        this.lobbyType = lobbyType;
        this.duration = duration;
        this.startTime = startTime;
    }

    public int getHeroId() {
        return heroId;
    }

    public String getTimeTillMatchFinished() {
        long timeTillMatchFinished = System.currentTimeMillis() - ((startTime * 1000) + (duration * 1000));
        long timeInSeconds = timeTillMatchFinished / 1000;
        long timeInMinutes = timeInSeconds / 60;
        long timeInHours = timeInMinutes / 60;
        long timeInDays = timeInHours / 24;
        long timeInMonths = timeInDays / 30;
        long timeInYears = timeInMonths / 12;

        if (timeInMinutes < 60) {
            return timeInMinutes + " minutes ago";
        } else {
            if (timeInHours < 24) {
                if (timeInHours < 2) {
                    return "a hour ago";
                } else {
                    return timeInHours + " hours ago";
                }
            } else {
                if (timeInDays < 30) {
                    if (timeInDays < 2) {
                        return "a day ago";
                    } else {
                        return timeInDays + " days ago";
                    }
                } else {
                    if (timeInMonths < 12) {
                        if (timeInMonths < 2) {
                            return "a month ago";
                        } else {
                            return timeInMonths + " months ago";
                        }
                    } else {
                        if (timeInYears < 2) {
                            return "a year ago";
                        } else {
                            return timeInYears + " years ago";
                        }
                    }
                }
            }
        }
    }

    public String getDuration() {
        int hours, minutes, seconds;
        String min, sec;
        hours = (duration / 60) / 60;
        if (hours < 1) {
            minutes = duration / 60;
            seconds = duration % 60;
            if (seconds < 10) {
                sec = "0" + seconds;
            } else {
                sec = String.valueOf(seconds);
            }
            if (minutes < 10) {
                min = "0" + minutes;
            } else {
                min = String.valueOf(minutes);
            }
            return min + ":" + sec;
        } else {
            minutes = (duration / 60) % 60;
            seconds = duration - (hours * 60 * 60) - (minutes * 60);
            if (seconds < 10) {
                sec = "0" + seconds;
            } else {
                sec = String.valueOf(seconds);
            }
            if (minutes < 10) {
                min = "0" + minutes;
            } else {
                min = String.valueOf(minutes);
            }
            return hours + ":" + min + ":" + sec;
        }

    }

    public int getMatchResult() {
        if ((radiantWin && playerSlot <= 4) || (!radiantWin && playerSlot > 4)) {
            return 1;
        } else {
            return 0;
        }
    }


    public String getMatchResultText() {
        if ((radiantWin && playerSlot <= 4) || (!radiantWin && playerSlot > 4)) {
            return "Won";
        } else {
            return "Lost";
        }
    }


    public String getSkill() {
        if (gameSkill.contentEquals("null")) {
            return "";
        } else {
            switch (Integer.parseInt(gameSkill)) {
                case 1:
                    return "Normal";
                case 2:
                    return "High";
                case 3:
                    return "Very High";
                default:
                    return "Unknown Skill";
            }
        }
    }

    public String getGameMode() {
        switch (gameMode) {
            case 2:
                return "Captains Mode";
            case 3:
                return "Random Draft";
            case 4:
                return "Single Draft";
            case 5:
                return "All Random";
            case 8:
                return "Reverse CM";
            case 12:
                return "Least Played";
            case 13:
                return "Limited Heroes";
            case 16:
                return "Captains Draft";
            case 18:
                return "Ability Draft";
            case 19:
                return "Event";
            case 20:
                return "AR Death Match";
            case 21:
                return "1v1 Mid";
            case 22:
                return "All Pick";
            default:
                return "Unknown Mode";
        }
    }

    public String getLobbyType() {
        switch (lobbyType) {
            case 0:
                return "Normal";
            case 1:
                return "Practice";
            case 2:
                return "Tournament";
            case 3:
                return "Tutorial";
            case 4:
                return "Co-Op Bots";
            case 7:
                return "Ranked";
            case 8:
                return "1v1 Mid";
            case 9:
                return "Battle Cup";
            default:
                return "Unknown Lobby";
        }
    }

    @Override
    public int getMyListItemType() {
        return MyListItem.RECENT_MATCHES_TYPE;
    }
}
