package com.example.nikola.dotatracker.adapters;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.nikola.dotatracker.HeroStats;
import com.example.nikola.dotatracker.Player;
import com.example.nikola.dotatracker.R;
import com.example.nikola.dotatracker.RecentMatches;
import com.example.nikola.dotatracker.data.DotaContract.DotaFollowing;
import com.example.nikola.dotatracker.interfaces.MyListItem;
import com.example.nikola.dotatracker.interfaces.OnItemViewClickListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = RecAdapter.class.getSimpleName();


    private String[] heroUrlsArray;
    private List<MyListItem> list;
    private RequestManager glide;
    private OnItemViewClickListener listener;
    private Context context;
    private Cursor cursor;


    public RecAdapter(Context context, RequestManager glide, List<MyListItem> list, OnItemViewClickListener listener, Cursor cursor) {
        this.glide = glide;
        this.list = list;
        this.listener = listener;
        this.cursor = cursor;
        this.context = context;
    }

    public RecAdapter(Context context, RequestManager glide, List<MyListItem> list) {
        this.glide = glide;
        this.list = list;
        this.heroUrlsArray = context.getResources().getStringArray(R.array.hero_urls);
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getMyListItemType();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case MyListItem.SEARCH_RESULTS_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
                return new PlayerViewHolder(view);
            case MyListItem.HERO_STATS_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_hero_stats, parent, false);
                return new HeroStatsViewHolder(view);
            case MyListItem.RECENT_MATCHES_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_recent_matches, parent, false);
                return new RecentMatchesViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyListItem currentPlayer = list.get(position);
        switch (holder.getItemViewType()) {
            case MyListItem.SEARCH_RESULTS_TYPE:
                ((PlayerViewHolder) holder).bindType(currentPlayer);
                break;
            case MyListItem.HERO_STATS_TYPE:
                ((HeroStatsViewHolder) holder).bindType(currentPlayer, position);
                break;
            case MyListItem.RECENT_MATCHES_TYPE:
                ((RecentMatchesViewHolder) holder).bindType(currentPlayer);
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public void addList(List<MyListItem> list) {
        if (list != null) {
            this.list = list;
            notifyDataSetChanged();
        }

    }

    public void removeData() {
        if (list != null) {
            if (this.list.size() > 0) {
                this.list.clear();
                notifyDataSetChanged();
            }
        }
    }

    public void setupFollowingList(Cursor cursor) {
        this.cursor = cursor;
    }


    private class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName;
        private TextView tvId;
        private CircleImageView ivImage;
        private Button followButton;

        PlayerViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_userName);
            tvId = (TextView) itemView.findViewById(R.id.tv_userId);
            ivImage = (CircleImageView) itemView.findViewById(R.id.iv_userImage);
            followButton = (Button) itemView.findViewById(R.id.follow_button);
            followButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        private void bindType(MyListItem currentPlayer) {
            cursor.moveToPosition(-1);
            Player item = (Player) currentPlayer;
            long playerId = item.getUserId();
            long followId;
            // this replaced bottom comment ----------------------------------------------
            Uri uri = DotaFollowing.CONTENT_URI.buildUpon().appendPath(String.valueOf(playerId)).build();
            if (context.getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    null).moveToNext()) {
                item.setStatus(true);
                followButton.setText("Unfollow");
            } else {
                followButton.setText("Follow");
                item.setStatus(false);
            }
            // ---------------------------------------------------------------------------------

         /*   while (cursor.moveToNext()) {
                followId = (long) cursor.getInt(cursor.getColumnIndex(DotaFollowing.COLUMN_PLAYER_ID));
                Log.v(LOG_TAG, "FOLLOW TEST: " + (playerId == followId));
                if (playerId == followId) {
                    item.setStatus(true);
                    break;
                } else
                    item.setStatus(false);

            }*/

            if (item.getStatus())
                followButton.setText("Unfollow");
            else
                followButton.setText("Follow");

            tvName.setText(item.getUserName());
            tvId.setText(String.valueOf(item.getUserId()));
            glide.load(item.getImageUrl()).into(ivImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Player player = (Player) list.get(position);
            long playerId = player.getUserId();
            switch (v.getId()) {
                case R.id.itemView:
                    listener.onItemViewClick(playerId);
                    break;
                case R.id.follow_button:
                    if (player.getStatus()) {
                        Uri uri = DotaFollowing.CONTENT_URI.buildUpon().appendPath(String.valueOf(playerId)).build();
                        context.getContentResolver().delete(
                                uri,
                                null,
                                null);
                        followButton.setText("Follow");
                        player.setStatus(false);
                    } else {
                        ContentValues values = new ContentValues();
                        values.put(DotaFollowing.COLUMN_PLAYER_ID, playerId);
                        values.put(DotaFollowing.COLUMN_NAME, player.getUserName());
                        values.put(DotaFollowing.COLUMN_IMAGE_URL, player.getImageUrl());
                        values.put(DotaFollowing.TYPE, DotaFollowing.FOLLOWING_TYPE);
                        context.getContentResolver().insert(
                                DotaFollowing.CONTENT_URI,
                                values);
                        followButton.setText("Unfollow");
                        player.setStatus(true);
                    }
                    break;

            }
        }
    }

    private class HeroStatsViewHolder extends RecyclerView.ViewHolder {
        private View viewHeroStatsDividerTop;
        private ImageView ivHeroStatsImage;
        private TextView tvHeroStatsGamesPlayed;
        private TextView tvHeroStatsWinRate;
        private TextView tvHeroStatsWinLoss;
        private View percentageView;

        public HeroStatsViewHolder(View itemView) {
            super(itemView);

            viewHeroStatsDividerTop = itemView.findViewById(R.id.item_divider_hero_stats_top);
            ivHeroStatsImage = (ImageView) itemView.findViewById(R.id.hero_stats_image);
            tvHeroStatsGamesPlayed = (TextView) itemView.findViewById(R.id.hero_stats_games_played);
            tvHeroStatsWinRate = (TextView) itemView.findViewById(R.id.hero_Stats_winrate);
            tvHeroStatsWinLoss = (TextView) itemView.findViewById(R.id.hero_stats_win_loss);
            percentageView = itemView.findViewById(R.id.percentage_view);


        }

        public void bindType(MyListItem currentHero, int position) {
            HeroStats item = (HeroStats) currentHero;

            if (position > 0) {
                viewHeroStatsDividerTop.setVisibility(View.GONE);
            } else {
                viewHeroStatsDividerTop.setVisibility(View.VISIBLE);
            }
            int heroId = item.getHeroId();
            if (heroId > 24) {
                glide.load(heroUrlsArray[heroId - 2]).into(ivHeroStatsImage);
            } else {
                glide.load(heroUrlsArray[heroId - 1]).into(ivHeroStatsImage);
            }
            tvHeroStatsGamesPlayed.setText(String.valueOf(item.getGamesPlayed()));
            tvHeroStatsWinRate.setText(item.getWinPercentageText());
            tvHeroStatsWinLoss.setText(item.getWinLos());
            adjustPercentageWidthSize(percentageView, item);


        }

        private void adjustPercentageWidthSize(View view, HeroStats currentHero) {
            if (currentHero.getGamesPlayed() == 0) {
                view.getLayoutParams().width = 0;
            } else {
                double percentage = 85 - (85 - (85 * currentHero.getWinRate() / 100));
                view.getLayoutParams().width = Math.round(convertDpToPixel((float) percentage, context));
            }
        }


        /**
         * This method converts dp unit to equivalent pixels, depending on device density.
         *
         * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
         * @param context Context to get resources and device specific display metrics
         * @return A float value to represent px equivalent to dp depending on device density
         */
        private float convertDpToPixel(float dp, Context context) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }


        /**
         * This method converts device specific pixels to density independent pixels.
         *
         * @param px      A value in px (pixels) unit. Which we need to convert into db
         * @param context Context to get resources and device specific display metrics
         * @return A float value to represent dp equivalent to px value
         */
        private float convertPixelsToDp(float px, Context context) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        }
    }

    private class RecentMatchesViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageHero;
        private TextView winText;
        private TextView skillText;
        private TextView modeText;
        private TextView lobbyText;
        private TextView durationText;
        private TextView matchEndedText;

        public RecentMatchesViewHolder(View itemView) {
            super(itemView);
            imageHero = (ImageView) itemView.findViewById(R.id.recent_image);
            winText = (TextView) itemView.findViewById(R.id.recent_win_result);
            skillText = (TextView) itemView.findViewById(R.id.recent_game_skill);
            modeText = (TextView) itemView.findViewById(R.id.recent_game_mode);
            lobbyText = (TextView) itemView.findViewById(R.id.recent_lobby_type);
            durationText = (TextView) itemView.findViewById(R.id.recent_duration);
            matchEndedText = (TextView) itemView.findViewById(R.id.recent_timeTillMatchEnded);
        }

        public void bindType(MyListItem item) {
            RecentMatches currentMatch = (RecentMatches) item;
            winText.setText(currentMatch.getMatchResultText());
            if (currentMatch.getMatchResult() == 1) {
                winText.setTextColor(ContextCompat.getColor(context, R.color.match_won));
            } else {
                winText.setTextColor(ContextCompat.getColor(context, R.color.match_lost));
            }
            skillText.setText(currentMatch.getSkill());
            modeText.setText(currentMatch.getGameMode());
            lobbyText.setText(currentMatch.getLobbyType());
            durationText.setText(currentMatch.getDuration());
            matchEndedText.setText(currentMatch.getTimeTillMatchFinished());
            int heroId = currentMatch.getHeroId();
            if (heroId > 24) {
                glide.load(heroUrlsArray[heroId - 2]).into(imageHero);
            } else {
                glide.load(heroUrlsArray[heroId - 1]).into(imageHero);
            }


        }
    }
}
