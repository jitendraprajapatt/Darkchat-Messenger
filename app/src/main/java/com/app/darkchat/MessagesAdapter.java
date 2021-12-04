package com.app.darkchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.darkchat.ModelClass.Msg;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.darkchat.Dashboard.ChatActiviy.rImage;
import static com.app.darkchat.Dashboard.ChatActiviy.sImage;

public class MessagesAdapter extends RecyclerView.Adapter{
    Context context ;
    ArrayList<Msg> messagesArraylist;
    int item_send = 1;
    int  item_receive = 2;

    public MessagesAdapter(Context context, ArrayList<Msg> messagesArraylist) {
        this.context = context;
        this.messagesArraylist = messagesArraylist;
    }

    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        if(viewType ==item_send){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new senderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false);
            return new receiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {

        Msg messageObj = messagesArraylist.get(position);
        if (holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder =(senderViewHolder) holder;
            viewHolder.txtMessage.setText(messageObj.getMessage());

            Picasso.get().load(sImage).into(viewHolder.cImageView);
        }else{
            receiverViewHolder viewHolder =(receiverViewHolder) holder;
            viewHolder.txtMessage.setText(messageObj.getMessage());
            Picasso.get().load(rImage).into(viewHolder.cImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messagesArraylist.size();
    }

    @Override
    public int getItemViewType(int position) {
        Msg message = messagesArraylist.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderUid())){

            return item_send;
        }else{
            return item_receive;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView  cImageView;
        TextView txtMessage;

        public senderViewHolder(@NonNull  View itemView) {
            super(itemView);
            cImageView = (CircleImageView) itemView.findViewById(R.id.img);
            txtMessage = itemView.findViewById(R.id.user_namee);

        }
    }
    class receiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView  cImageView;
        TextView txtMessage;
        public receiverViewHolder(@NonNull  View itemView) {
            super(itemView);
            cImageView = (CircleImageView) itemView.findViewById(R.id.img);
            txtMessage = itemView.findViewById(R.id.user_name);
        }
    }
}
