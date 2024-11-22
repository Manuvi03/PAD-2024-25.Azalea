package es.ucm.fdi.azalea.presentation.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClassroomStudentSharedViewModel extends ViewModel {

    // datos compartidos entre ClassRoomFragment y StudentFragment
    private final MutableLiveData<String> studentId = new MutableLiveData<>();
    private final MutableLiveData<String> studentImage = new MutableLiveData<>();

    public LiveData<String> getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId.setValue(studentId);
    }

    public LiveData<String> getStudentProfileImage() {
        return studentImage;
    }

    public void setStudenProfileImage(String profileImage) {
        this.studentImage.setValue(profileImage);
    }
}
