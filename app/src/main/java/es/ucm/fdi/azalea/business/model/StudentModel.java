package es.ucm.fdi.azalea.business.model;

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

    public StudentModel(){}

    public StudentModel(List<String> subjects, String allergens, String medicalConditions,
                        double height, double weight, String surnames, String name, String id) {
        this.subjects = subjects;
        this.allergens = allergens;
        this.medicalConditions = medicalConditions;
        this.height = height;
        this.weight = weight;
        this.surnames = surnames;
        this.name = name;
        this.id = id;
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

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

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
}