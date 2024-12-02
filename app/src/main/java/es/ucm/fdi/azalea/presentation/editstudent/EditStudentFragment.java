package es.ucm.fdi.azalea.presentation.editstudent;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.databinding.EditStudentFragmentBinding;
import es.ucm.fdi.azalea.databinding.LoginFragmentBinding;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.login.LoginViewModel;

public class EditStudentFragment extends Fragment {
    private static final String TAG = "editStudentFragment";
    private EditStudentFragmentBinding binding;
    private editStudentViewModel viewModel;
    private editStudentSharedViewModel sharedViewModel;
    private StudentModel sm;
    private EditText direccionEditText,alergiasEditText,diaNacEditText,alturaEditText,
    conMedEditText,nombreEditText,apellidosEditText,pesoEditText,contactoRapidoEditText,
    telefaux1EditText,telefaux2EditText;
    private Button editStudentButton;
    FrameLayout loadinLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = EditStudentFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(editStudentViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(editStudentSharedViewModel.class);


        getStudentData();
        bindViews();
        initListeners();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        // el Fragment puede verse solo en vertical
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        // restablece la orientacion a la de la activity
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void getStudentData(){
        String id = sharedViewModel.getIdStudent().getValue();
        viewModel.getStudentDataEvent().observe(getViewLifecycleOwner(),event->{
            if(event instanceof Event.Success){
                loadinLayout.setVisibility(View.GONE);
                sm = ((Event.Success<StudentModel>) event).getData();
                direccionEditText.setText(sm.getAddress());
                alergiasEditText.setText(sm.getAllergens());
                diaNacEditText.setText(sm.getBirthday());
                alturaEditText.setText(sm.getHeight()+ "");
                pesoEditText.setText(sm.getWeight() + "");
                contactoRapidoEditText.setText(sm.getQuickContact());
                nombreEditText.setText(sm.getName());
                apellidosEditText.setText(sm.getSurnames());
                conMedEditText.setText(sm.getMedicalConditions());
                if(sm.getParentsPhones() != null){
                    telefaux1EditText.setText(sm.getParentsPhones().get(0));
                    if(sm.getParentsPhones().size() == 2)
                        telefaux2EditText.setText(sm.getParentsPhones().get(1));
                }




            }else if(event instanceof Event.Error){
                loadinLayout.setVisibility(View.GONE);
                Toast.makeText(requireActivity(),getString(R.string.editstudent_errorStudentData),Toast.LENGTH_LONG).show();

            }else{
                loadinLayout.setVisibility(View.VISIBLE);
            }
        } );

        viewModel.getStudentData(id);
    }


    private void initListeners(){
        editStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean correctData = true;
                if(viewModel.verifyDate(getString(R.string.editstudent_errorDateFormat),diaNacEditText) &&
                        viewModel.verifyDoubles(getString(R.string.editstudent_errorDoubleFormat),alturaEditText,pesoEditText) &&
                        viewModel.verifyPhones(getString(R.string.editstudent_errorPhoneFormat),telefaux1EditText,telefaux2EditText,contactoRapidoEditText)){
                    sm.setAddress(direccionEditText.getText().toString());
                    sm.setAllergens(alergiasEditText.getText().toString());
                    sm.setBirthday(diaNacEditText.getText().toString());

                    //Utilizo 0.0 ya que Double.NaN no puede ponerse en la BD y en el caso de numeros
                    //tienen que tener un valor asignado en la bd
                    if(alturaEditText.getText().toString().isEmpty())
                        sm.setHeight(0.0);
                    else
                        sm.setHeight(Double.valueOf(alturaEditText.getText().toString()));
                    if(pesoEditText.getText().toString().isEmpty())
                        sm.setWeight(0.0);
                    else
                        sm.setWeight(Double.valueOf(pesoEditText.getText().toString()));

                    sm.setMedicalConditions(conMedEditText.getText().toString());
                    sm.setName(nombreEditText.getText().toString());
                    sm.setSurnames(apellidosEditText.getText().toString());
                    sm.setQuickContact(contactoRapidoEditText.getText().toString());
                    List<String> listatelef = new ArrayList<>();
                    listatelef.add(telefaux1EditText.getText().toString());
                    listatelef.add(telefaux2EditText.getText().toString());
                    sm.setParentsPhones(listatelef);

                    viewModel.updateStudent(sm);
                }


            }
        });
        viewModel.getUpdateDataEvent().observe(getViewLifecycleOwner(), event ->{
            if(event instanceof Event.Success){
                loadinLayout.setVisibility(View.GONE);
                Toast.makeText(requireActivity(),getString(R.string.editstudent_successUpdate),Toast.LENGTH_LONG).show();
            }else if(event instanceof Event.Error){
                loadinLayout.setVisibility(View.GONE);
                Toast.makeText(requireActivity(),getString(R.string.editstudent_errorUpdate),Toast.LENGTH_LONG).show();

            }else{
                loadinLayout.setVisibility(View.VISIBLE);

            }
        } );
    }



    private void bindViews(){
        direccionEditText = binding.teacherEditStudentDireccionEditText;
        alergiasEditText = binding.teacherEditStudentAlergiasEditText;
        diaNacEditText = binding.teacherEditStudentDiaNacEditText;
        alturaEditText = binding.teacherEditStudentAlturaEditText;
        conMedEditText = binding.teacherEditStudentCondMedEditText;
        nombreEditText = binding.teacherEditStudentNombreEditText;
        apellidosEditText = binding.teacherEditStudentApellidosEditText;
        pesoEditText = binding.teacherEditStudentPesoEditText;
        contactoRapidoEditText = binding.teacherEditStudentContactoRapidoEditText;
        telefaux1EditText = binding.teacherEditStudentTelefaux1EditText;
        telefaux2EditText = binding.teacherEditStudentTelefaux2EditText;
        editStudentButton = binding.teacherEditStudentButton;
        loadinLayout = binding.teacherEditStudentLoadingOverlay;

    }
}
