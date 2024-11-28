package es.ucm.fdi.azalea.presentation.editstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.databinding.EditStudentFragmentBinding;
import es.ucm.fdi.azalea.databinding.LoginFragmentBinding;
import es.ucm.fdi.azalea.presentation.login.LoginViewModel;

public class EditStudentFragment extends Fragment {
    private EditStudentFragmentBinding binding;
    private editStudentViewModel viewModel;
    private StudentModel sm;
    private EditText direccionEditText,alergiasEditText,diaNacEditText,alturaEditText,
    conMedEditText,nombreEditText,apellidosEditText,pesoEditText,contactoRapidoEditText,
    telefaux1EditText,telefaux2EditText;
    private Button editStudentButton;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = EditStudentFragmentBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(editStudentViewModel.class);
        View view = binding.getRoot();


        bindViews();
        constructSM(savedInstanceState);
        initListeners();
        return view;

    }

    private void initListeners(){

    }
    private void constructSM(Bundle data){
        sm.setAddress(data.getString("direccion"));
        sm.setAllergens(data.getString("alergenos"));
        sm.setBirthday(data.getString("dianac"));
        sm.setName(data.getString("nombre"));
        sm.setHeight(data.getDouble("altura"));
        sm.setMedicalConditions(data.getString("condmed"));
        sm.setSurnames(data.getString("apellido"));
        sm.setId(data.getString("id"));
        sm.setParentId(data.getString("idpadre"));
        sm.setClassroomId(data.getString("idclassroom"));
        sm.setWeight(data.getDouble("peso"));
        sm.setQuickContact(data.getString("contactorapido"));
        List<String> names = new ArrayList<>();
        names.add(data.getString("nombrep1"));
        names.add(data.getString("nombrep2"));
        sm.setParentsNames(names);
        List<String> phones = new ArrayList<>();
        phones.add(data.getString("movil1"));
        phones.add(data.getString("movilp2"));
        sm.setParentsPhones(phones);


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

    }
}
