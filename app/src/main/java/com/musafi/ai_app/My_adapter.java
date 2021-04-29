package com.musafi.ai_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class My_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Detect> states;
    private List<Detect> statesAll;
    private OnItemClickListener mItemClickListener;

    public My_adapter(Context context, List<Detect> states) {
        this.context = context;
        this.states = states;
        this.statesAll = new ArrayList<>(states);
    }

    public void updateList(ArrayList<Detect> states) {
        this.states = states;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detect_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final Detect state = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.label.setText(state.getLabel());
            genericViewHolder.score.setText(""+state.getScore());




        }
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    private Detect getItem(int position) {
        return states.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView label;
        private TextView score;



        public ViewHolder(final View itemView) {
            super(itemView);
            this.label = itemView.findViewById(R.id.list_txt_name);
            this.score = itemView.findViewById(R.id.list_txt_res);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }
    }

    public void removeAt(int position) {
        states.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, states.size());
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Detect state);
    }
}