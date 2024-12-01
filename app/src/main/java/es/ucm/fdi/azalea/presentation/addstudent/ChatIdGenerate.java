package es.ucm.fdi.azalea.presentation.addstudent;

public class ChatIdGenerate {
    public static String createChatId(String classId, String parentId) {
        return classId + "+" + parentId;
    }
}
