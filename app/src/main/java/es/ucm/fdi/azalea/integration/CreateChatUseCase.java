package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.implementations.ChatRepositoryImp;
import es.ucm.fdi.azalea.business.model.ChatModel;

public class CreateChatUseCase {

    private ChatRepositoryImp chatRepositoryImp;

    public CreateChatUseCase(){
        this.chatRepositoryImp = new ChatRepositoryImp();
    }

    public void execute(ChatModel chatModel) {//El model solo tiene el id del chat.
        BusinessFactory.getInstance().getChatRepository().create(chatModel);
    }
}
