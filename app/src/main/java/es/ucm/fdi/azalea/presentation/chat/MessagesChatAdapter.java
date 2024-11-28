package es.ucm.fdi.azalea.presentation.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.presentation.parent.EventsParentAdapter;


public class MessagesChatAdapter extends RecyclerView.Adapter<MessagesChatAdapter.ViewHolder> {

    private List<MessageModel> mMessagesData;
    private final LayoutInflater mInflater;

    public MessagesChatAdapter(List<MessageModel> chatsInfoArrayList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mMessagesData = chatsInfoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message_chat_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel message = mMessagesData.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
            return mMessagesData != null ? mMessagesData.size() : 0;
        }

    // Clase interna ViewHolder para el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message_input);
            cardView = itemView.findViewById(R.id.message_cardView);
        }

        public void bind(MessageModel messageModel){

            message.setText(messageModel.getMessage());
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();

            if(messageModel.getSenderId().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                cardView.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Misty_Rose));
                layoutParams.setMarginStart(50); //Margen grande a la izquierda para mis mensajes TODO
                layoutParams.setMarginEnd(15); //Margen pequeño a la derecha para mis mensajes
            }
            else{
                cardView.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Cyclomen));
                layoutParams.setMarginStart(16); //Margen pequeño a la izquierda para sus mensajes
                layoutParams.setMarginEnd(15); //Margen grande a la derecha para sus mensajes
            }

        }
    }
}
