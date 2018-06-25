package edb.eningabiye.dailysteps.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edb.eningabiye.dailysteps.R;
import edb.eningabiye.dailysteps.model.Message;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private ArrayList<Message> messages = new ArrayList<>();
    private Context context;

    public MessageAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message, parent, false);
        return new MessageAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.nom.setText(message.getNom());
        holder.date.setText(message.getDate());
        holder.message.setText(message.getMessage());
        holder.steps.setText(message.getSteps());
    }

    @Override
    public int getItemCount() {
        if(messages != null){
            return messages.size();
        }else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nom, date, message, steps;
        ViewHolder(View v) {
            super(v);
            nom = v.findViewById(R.id.nom);
            date = v.findViewById(R.id.date_sent);
            message = v.findViewById(R.id.message);
            steps= v.findViewById(R.id.steps_sent);
        }

    }
}
