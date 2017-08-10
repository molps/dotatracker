package com.example.nikola.dotatracker;


import com.example.nikola.dotatracker.interfaces.MyListItem;

public class Player implements MyListItem {
    private String userName;
    private long userId;
    private String imageUrl;

    public Player(String userName, long userId, String imageUrl) {
        this.userName = userName;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public long getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int getMyListItemType() {
        return MyListItem.SEARCH_RESULTS_TYPE;
    }
}
