package es.ucm.fdi.azalea.business.model;

import java.util.List;

public class ClassRoomModel {
    private String id;
    private String name;
    private List<String> subjects;

    public ClassRoomModel() {}

    public ClassRoomModel(List<String> subjects, String name, String id) {
        this.subjects = subjects;
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }
}