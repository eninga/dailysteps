package edb.eningabiye.dailysteps;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import edb.eningabiye.dailysteps.model.Step;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<Step> data;
    Context context;
    ListAdapter(ArrayList<Step> myDataset, Context context) {
        this.context = context;
        this.data = myDataset;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        Step step = data.get(position);
        holder.username.setText(step.getUser().toString());
        holder.date.setText(String.valueOf(step.getDate()));
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
        // each data item is just a string in this case
        TextView username, date, steps, percent;
        ImageButton imageButton;
        ViewHolder(View v) {
            super(v);
            username = v.findViewById(R.id.username);
            date = v.findViewById(R.id.date);
            steps = v.findViewById(R.id.steps);
            percent = v.findViewById(R.id.percent);
            imageButton = v.findViewById(R.id.share);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ShareStepsActivity.class);
                    i.putExtra("name",data.get(getLayoutPosition()).getUser().getName());
                    i.putExtra("date",data.get(getLayoutPosition()).getDate());
                    i.putExtra("steps",data.get(getLayoutPosition()).getSteps());
                    context.startActivity(i);
                }
            });
        }

    }

}
