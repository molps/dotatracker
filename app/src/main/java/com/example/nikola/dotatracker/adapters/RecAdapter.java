package com.example.nikola.dotatracker.adapters;


import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.nikola.dotatracker.HeroStats;
import com.example.nikola.dotatracker.Player;
import com.example.nikola.dotatracker.R;
import com.example.nikola.dotatracker.RecentMatches;
import com.example.nikola.dotatracker.interfaces.MyListItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private String[] heroUrlsArray;
    private List<MyListItem> list;
    private RequestManager glide;
    private OnItemViewClickListener listener;
    private Context context;


    public RecAdapter(RequestManager glide, List<MyListItem> list, OnItemViewClickListener listener) {
        this.glide = glide;
        this.list = list;
        this.listener = listener;
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

    public interface OnItemViewClickListener {
        void onItemViewClick(long playerId);
    }

    private class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName;
        private TextView tvId;
        private CircleImageView ivImage;

        PlayerViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_userName);
            tvId = (TextView) itemView.findViewById(R.id.tv_userId);
            ivImage = (CircleImageView) itemView.findViewById(R.id.iv_userImage);
            itemView.setOnClickListener(this);
        }

        private void bindType(MyListItem currentPlayer) {
            Player item = (Player) currentPlayer;

            tvName.setText(item.getUserName());
            tvId.setText(String.valueOf(item.getUserId()));
            glide.load(item.getImageUrl()).into(ivImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            long playerId = ((Player) list.get(position)).getUserId();
            listener.onItemViewClick(playerId);

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
