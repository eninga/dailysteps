package edb.eningabiye.dailysteps.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import edb.eningabiye.dailysteps.R;
import edb.eningabiye.dailysteps.model.Step;

public class StatAdapter extends RecyclerView.Adapter<StatAdapter.ViewHolder> {
    private ArrayList<Step> data;
    Context context;
    public StatAdapter(ArrayList<Step> myDataset, Context context) {
        this.context = context;
        this.data = myDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step step = data.get(position);
        SharedPreferences sharedPref = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        holder.username.setText(sharedPref.getString("username", null));
        holder.date.setText(String.valueOf(step.getMonth()+" "+step.getYear()));
        holder.steps.setText(String.valueOf(step.getSteps()));
        if(step.getPercent() >= 0){
            holder.percent.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            holder.percent.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.percent.setText(context.getString(R.string.percent, step.getPercent(), "%"));
    }

    @Override
    public int getItemCount() {
        return null != data ? data.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, date, steps, percent;
        ImageButton imageButton;
        ViewHolder(View v) {
            super(v);
            username = v.findViewById(R.id.username);
            date = v.findViewById(R.id.date);
            steps = v.findViewById(R.id.steps);
            percent = v.findViewById(R.id.percent);
            imageButton = v.findViewById(R.id.share);
        }

    }
}
