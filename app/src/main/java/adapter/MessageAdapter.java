package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasechatapp.MessageActivity;
import com.example.firebasechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.User;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Chat> mChat;
    private FirebaseUser mUser;
    private String imageURL;

    public MessageAdapter(Context ctx, List<Chat> chats, String imageURL){
        context = ctx;
        this.mChat = chats;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        return new MessageAdapter.MessageViewHolder(v);
    }
        else {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.MessageViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
      Chat chat = mChat.get(position);

      holder.show_message.setText(chat.getMessage());

      if (imageURL.equals("default")){
          holder.profile_image.setImageResource(R.mipmap.ic_launcher);
      }else {
          Glide.with(context).load(imageURL).into(holder.profile_image);
      }

      if (position == mChat.size()-1){
          if (chat.isIsseen()){
              holder.txt_seen.setText("Seen");
          }else{
              holder.txt_seen.setText("Delivered");
          }
      }else {
          holder.txt_seen.setVisibility(View.GONE) ;
      }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView show_message;
        private CircleImageView profile_image;
        private TextView txt_seen;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.text_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mChat.get(position).getSender().equals(mUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
