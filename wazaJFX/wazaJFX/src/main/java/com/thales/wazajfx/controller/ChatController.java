package com.thales.wazajfx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.thales.wazajfx.WazaApplication;
import com.thales.wazajfx.model.CesarJNI;
import com.thales.wazajfx.model.Chat;
import com.thales.wazajfx.model.Message;
import com.thales.wazajfx.model.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import com.thales.wazajfx.utils.HttpRequests;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ChatController implements Initializable {
    @FXML
    public Label labelChatName;

    @FXML
    public TextArea txtMessageToSend;
    @FXML
    public Button btnDeco;
    @FXML
    public Button btnChangeChat;

    @FXML
    public ListView listViewMessageInChat;
    @FXML
    public ListView listUserChat;
    @FXML
    public Button btnSendMessage;
    @FXML
    public Pane paneListview;
    @FXML
    public VBox mainVbox;

    private Chat chat = WazaApplication.getMyChat();

    private ObservableList<User> usersInChatObservable;

    private ObservableList<Message> messageInChatObservable;

    //utilisateur connecte
    private ObjectProperty<User> selectedUser = new SimpleObjectProperty<>();

    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        initializeButtons();
        initializeText();
        //Permet de rafraichir la liste des message mais consomme trop de ressource et n est pas une solution satisfaisante
        //timeline();
    }

    private void initializeButtons() {

        this.btnDeco.setOnMouseClicked(mouseEvent -> {
            WazaApplication.setScreen("userConnect");
        });
        this.btnChangeChat.setOnMouseClicked(mouseEvent -> {
            WazaApplication.setScreen("accueil");
        });


        this.btnSendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            //Message messageSend = new Message(txtMessageToSend.getText());

            //Encryptage du message avec cle=5
            //Ensuite on devra associer à un user une clé pub et privée
            int keyPubServeur=1;
            //Ici, il faudra modifier le césar "caractère + Key " a-> f , decryptage "caractère - key"
            //Par un système de KeyPub et KeyPrivate (les clés sont identiques = 5)
            String messageEncoded=new CesarJNI().encrypt(keyPubServeur, txtMessageToSend.getText());

            //Vérification du fonctionnement de libCesar.so (ok)
            //(attention aux noms de méthodes et penser à ajouter la librairie
            //dans les lib java du projet (project File,structure, module,+,library, java , libfic.so et gfic.dll
            //Pour compatibilité avec windows

            Message messageSend = new Message(messageEncoded);

            messageSend.setAuthor(WazaApplication.getConnectedUser());
            messageSend.setChat(chat);

            try {
                    String restUrl = "http://localhost:8080/waza/api/messages/";
                    URL urlRequest = new URL(restUrl);
                    HttpURLConnection httpRequest = (HttpURLConnection) urlRequest.openConnection();
                    httpRequest.setRequestMethod("POST");
                    httpRequest.setDoOutput(true);
                    ObjectMapper objectMapperAdd = new ObjectMapper();
                    objectMapperAdd.registerModule(new JavaTimeModule());
                    byte[] out = objectMapperAdd.writeValueAsString(messageSend).getBytes(StandardCharsets.UTF_8);
                    int length = out.length;

                    httpRequest.setFixedLengthStreamingMode(length);
                    httpRequest.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    httpRequest.connect();
                    try (OutputStream os = httpRequest.getOutputStream()) {
                        os.write(out);
                    }
                    httpRequest.disconnect();
                } catch (Exception e) {
                System.out.println("Error: " + e);
                }
            txtMessageToSend.clear();

            //je laisse 0.2s d'arrêt entre l'envoi et la réception du
            // message pour que la requête se fasse avant la réception
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            chargeMessage();
        });

    }


    private void initializeTableView() {

        chargeList();
        chargeMessage();
        listUserChat.setCellFactory(param->new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty){
                super.updateItem(item,empty);
                if (empty||item==null){
                    setText(null);
                }
                else {
                    setText(item.getPseudo());
                }
            }
        });

        listUserChat.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            selectedUser.setValue((User) t1);
        });


        //initialisation des messages du chat
        listViewMessageInChat.setCellFactory(param->new ListCell<Message>() {

            @Override
            protected void updateItem(Message item, boolean empty){
                //A revoir plus tard
                /*String pseudoHour=item.getAuthor().getPseudo()+" "+item.getDateMessage().getYear()+"/"+
                        item.getDateMessage().getMonth().getValue()+"/"+
                        item.getDateMessage().getDayOfMonth()+" - "+
                        item.getDateMessage().getHour()+"H"+
                        item.getDateMessage().getMinute()+
                        " :\n"+item.getContents();*/

                super.updateItem(item,empty);
                if (empty||item==null){
                    setText(null);
                }
                else {
                    //setText(pseudoHour);

                    setText(item.getAuthor().getPseudo()+" "+item.getDateMessage().getYear()+"/"+
                            item.getDateMessage().getMonth().getValue()+"/"+
                            item.getDateMessage().getDayOfMonth()+" - "+
                            item.getDateMessage().getHour()+"H"+
                            item.getDateMessage().getMinute()+
                            " :\n"+item.getContents());

                }
            }
    });
    }

    //On charge la liste de user dans le chat
    private void chargeList() {
        GluonObservableList<User> gotList = HttpRequests.getAllUserByChat(chat.getId());
        gotList.setOnSucceeded(connectStateEvent -> {
            this.usersInChatObservable= FXCollections.observableArrayList(gotList);
            listUserChat.getItems().addAll(usersInChatObservable);

        });
    }

    private void chargeMessage() {
        GluonObservableObject<Chat> myChat = HttpRequests.getAllMessageByChat(chat.getId());
        myChat.setOnSucceeded(connectStateEvent -> {
            this.messageInChatObservable= FXCollections.observableArrayList(myChat.get().getMessages());
            listViewMessageInChat.getItems().clear();

            listViewMessageInChat.getItems().addAll(messageInChatObservable);

            //Test pour faire descendre la scrollBar jusqu'en bas
            mainVbox.getOnScroll();

        });
    }

    private void initializeText() {
        labelChatName.isVisible();
        this.labelChatName.setText("****");
        WazaApplication.connectedChatProperty().addListener((observableValue, chat, t1) -> {
            this.labelChatName.setText(WazaApplication.getMyChat().getName());
        });
        this.labelChatName.setText(WazaApplication.getMyChat().getName());
    }
    public void timeline(){
    KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
    chargeMessage();
}   );
    Timeline timeline = new Timeline(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    }

}
