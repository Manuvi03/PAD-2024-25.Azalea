package es.ucm.fdi.azalea.presentation.chat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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

    private final String TAG = "MessagesChatAdapter";
    private List<MessageModel> mMessagesData;
    private final LayoutInflater mInflater;
    private String myUserId;

    public MessagesChatAdapter(List<MessageModel> chatsInfoArrayList, String myUserId, Context context) {
        Log.d(TAG, "creacion del adpater de los msgs");
        this.mInflater = LayoutInflater.from(context);
        this.mMessagesData = chatsInfoArrayList;
        this.myUserId = myUserId;
    }

    public void setmMessagesData(List<MessageModel> messages){
        this.mMessagesData = messages;
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

            if(Objects.equals(messageModel.getSenderId(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                cardView.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Misty_Rose));
                layoutParams.setMarginStart(50); //Margen grande a la izquierda para mis mensajes
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
/*// constantes
    private final String TAG = "ClassroomListAdapter";
    private final String STUDENT_ID_KEY = "studentId";
    private final String STUDENT_IMAGE_KEY = "studentImage";

    // atributos
    private List<StudentModel> mStudentsData;           // todos los alumnos de la clase
    private List<StudentModel> mStudentsFilteredData;   // los alumnos de la busqueda
    private final LayoutInflater mInflater;
    ClassroomStudentSharedViewModel viewModel;

    public ClassroomListAdapter(List<StudentModel> studentInfoList, Context context, ClassroomStudentSharedViewModel viewModel) {
        Log.d(TAG, "Se crea el ClassroomListAdapter");
        this.mInflater = LayoutInflater.from(context);
        this.mStudentsData = studentInfoList;
        this.mStudentsFilteredData = studentInfoList;
        this.viewModel = viewModel;
    }

    public void setStudentsData(List<StudentModel> students) {
        this.mStudentsData = students;
    }

    public void setStudentsFilteredData(List<StudentModel> students) {
        this.mStudentsFilteredData = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infla el layout para cada elemento de la vista
        View view = mInflater.inflate(R.layout.teacher_classroom_list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Configuración de la UI para cada elemento
        StudentModel studentModel = mStudentsFilteredData.get(position);

        // el string del nombre se concatena formateadamente
        holder.name.setText(mInflater.getContext().getString(R.string.student_name,
                studentModel.getName(), studentModel.getSurnames()));

        // el string de contacto rapido
        holder.contact.setText(mInflater.getContext().getString(R.string.student_contact,
                studentModel.getQuickContact()));

        // se genera la URL con el nombre de usuario y se genera la foto de perfil
        // TODO no me gustan los colores random que ponen en el background, deberiamos definir nosotros unos y luego buscarlos
        String path = "https://ui-avatars.com/api/?name=" + studentModel.getName() + "&background=random&length=1&rounded=true";
        Picasso.get()
                .load(path)
                .placeholder(R.drawable.teacher_classroom_student_image)
                .error(R.drawable.teacher_classroom_student_image_error)
                .into(holder.profileImage);

        // listener para acceder a la informacion de cada alumno, abriendo el nuevo Fragment
        holder.itemView.setOnClickListener(view -> {
            Log.d(TAG, "Se cambia de fragment a StudentFragment");

            viewModel.setStudentId(studentModel.getId());
            viewModel.setStudentProfileImage(path);

            // se crea el fragment
            ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.teacher_fragment_container_view, StudentFragment.class, null)
                    .addToBackStack("ClassroomListAdapter")
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        // devuelve el tamaño de la lista de estudiantes
        return mStudentsFilteredData != null ? mStudentsFilteredData.size() : 0;
    }

    // clase interna ViewHolder para el RecyclerView, se enlaza con la vista de cada elemento
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView name, contact;

        public ViewHolder(View view) {
            super(view);
            profileImage = view.findViewById(R.id.teacher_classroom_student_image);
            name = view.findViewById(R.id.teacher_classroom_student_name);
            contact = view.findViewById(R.id.teacher_classroom_student_contact);
        }
    }

    // implementa la busqueda en la lista
    public List<StudentModel> searchStudent(String s){
        Log.d(TAG, "Se buscan estudiantes filtrados por el texto del buscador");
        // se inicializa la lista
        List<StudentModel> list = new ArrayList<>();

        // si el texto de la busqueda es vacio, se muestra toda la clase
        if(s.isBlank()) list.addAll(mStudentsData);
        // en otro caso se realiza la busqueda
        else{
            // se busca en todos los estudiantes
            for(StudentModel sm : mStudentsData) {
                String fullName = sm.getName() + " " + sm.getSurnames();
                if(checkTexts(sm, s, fullName)) {
                    list.add(sm);
                }
            }
        }
        return list;
    }

    // comprueba si el texto coincide con algun estudiante
    private boolean checkTexts(StudentModel sm, String s, String fullName){
        return sm.getName().toLowerCase().contains(s)
                || sm.getSurnames().toLowerCase().contains(s)
                || fullName.toLowerCase().trim().contains(s)
                || normalize(sm.getName().toLowerCase()).contains(s)
                || normalize(sm.getSurnames().toLowerCase()).contains(s)
                || normalize(fullName.trim().toLowerCase()).contains(s);
    }

    // quita acentos de los caracteres especiales
    private String normalize(String s){
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[^\\p{ASCII}]", "");
        return s;
    }
*/