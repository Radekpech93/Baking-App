package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    private static final String TAG = StepAdapter.class.getSimpleName();

    private List<Step> mSteps;
    private final Context mContext;
    private OnStepClickListener mListener;

    public interface OnStepClickListener {
        void onListItemClick(List<Step> steps, int position);
    }

    public StepAdapter(Context context, List<Step> steps, OnStepClickListener listener) {
        mContext = context;
        mSteps = steps;
        mListener = listener;
    }


    @NonNull
    @Override
    public StepAdapter.StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.StepHolder holder, int position) {
        Step step = mSteps.get(position);
        holder.stepShortDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mSteps != null) {
            return mSteps.size();
        } else {
            return 0;
        }
    }


    class StepHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView stepShortDescription;

        StepHolder(View view) {
            super(view);

            stepShortDescription = view.findViewById(R.id.tv_step_short_description);

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListener.onListItemClick(mSteps, position);
        }
    }
}
