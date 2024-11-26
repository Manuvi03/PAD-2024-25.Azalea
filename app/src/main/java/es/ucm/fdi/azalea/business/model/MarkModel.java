package es.ucm.fdi.azalea.business.model;

public class MarkModel {
    private String id;
    private String subject;
    private double mark;
    private String studentId;

    public MarkModel() {
    }

    public MarkModel(String id, String subject, double mark, String studentId) {
        this.id = id;
        this.subject = subject;
        this.mark = mark;
        this.studentId = studentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
