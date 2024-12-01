package es.ucm.fdi.azalea.business.model;

import android.graphics.Bitmap;

import java.util.List;

public class StudentModel {
    private String id;
    private String name;
    private String surnames;
    private double weight;
    private double height;
    private String medicalConditions;
    private String allergens;
    private List<String> subjects;
    private String quickContact;
    private String birthday;
    private String address;
    private List<String> parentsNames;
    private List<String> parentsPhones;
    private String classroomId;
    private String parentId;
    private List<String> marksId;
    private String profileImage;

    public StudentModel(){} // constructor vacio necesario para introducir directamente el objeto en Firebase

    public StudentModel(List<String> subjects, String allergens, String medicalConditions,
                        double height, double weight, String surnames, String name,
                        String quickContact, String classroomId, String parentId, String birthday,
                        String address, List<String> parentsNames, List<String> parentsPhones) {
        this.subjects = subjects;
        this.allergens = allergens;
        this.medicalConditions = medicalConditions;
        this.height = height;
        this.weight = weight;
        this.surnames = surnames;
        this.name = name;
        this.quickContact = quickContact;
        this.classroomId = classroomId;
        this.parentId = parentId;
        this.birthday = birthday;
        this.address = address;
        this.parentsNames = parentsNames;
        this.parentsPhones = parentsPhones;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) { this.medicalConditions = medicalConditions;}

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassroomId() { return classroomId; }

    public void setClassroomId(String classroomId) { this.classroomId = classroomId; }

    public String getParentId() { return parentId; }

    public void setParentId(String parentId) { this.parentId = parentId; }

    public String getQuickContact() { return quickContact; }

    public void setQuickContact(String quickContact) { this.quickContact = quickContact; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getBirthday() { return birthday; }

    public void setBirthday(String birthday) { this.birthday = birthday; }

    public List<String> getParentsPhones() { return parentsPhones; }

    public void setParentsPhones(List<String> parentsPhones) { this.parentsPhones = parentsPhones; }

    public List<String> getParentsNames() { return parentsNames; }

    public void setParentsNames(List<String> parentsNames) {
        this.parentsNames = parentsNames;
    }

    public List<String> getMarksId() { return marksId; }

    public void setMarksId(List<String> marksId) { this.marksId = marksId; }

    public String getProfileImage() { return profileImage; }

    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
