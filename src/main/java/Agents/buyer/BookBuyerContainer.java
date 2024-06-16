package Agents.buyer;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.lang.acl.ACLMessage;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class BookBuyerContainer extends Application {
  
  protected BookBuyerAgent bookBuyerAgent;
  protected ListView<String> listViewMessages;
  protected ObservableList<String>ObservableListData;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startContainer();
        Label label = new Label("Liste des livres");
        BorderPane borderPane = new BorderPane();
        VBox vbox1 = new VBox();
        vbox1.setPadding(new Insets(90));
        ObservableListData = FXCollections.observableArrayList();
        listViewMessages = new ListView<>(ObservableListData);
        vbox1.getChildren().add(listViewMessages);
        borderPane.setCenter(vbox1);

        // Création des boutons
        Button button1 = new Button("✔️ACCEPT"); // Icône "accept"
        Button button2 = new Button("REFUSE❌");
        button1.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 10px; fx-margin:-10px;-fx-font-size: 14px;text-align: center;  ");
        button2.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10px; -fx-font-size: 14px;text-align: center;");
        button1.setOnAction(new EventHandler<ActionEvent>() {
        	   boolean button1Clicked = false;
               boolean button2Clicked = false;
            @Override
            public void handle(ActionEvent event) {
            	boolean button1Clicked = false;
            	
            	 if (!button1Clicked) {
                     System.out.println("Button 1 (accepter) was clicked.");
                     // Envoyer un message "accepter" au vendeur
                     sendMessageToSellerOnce("accepter");
                     // Désactiver le bouton pour éviter les clics répétés
                     button1.setDisable(true);
                     button1Clicked = true;
                 }
            }
        });

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	boolean button2Clicked = false;
            	 if (!button2Clicked) {
                     System.out.println("Button 1 (accepter) was clicked.");
                     // Envoyer un message "accepter" au vendeur
                     sendMessageToSellerOnce("accepter");
                     // Désactiver le bouton pour éviter les clics répétés
                     button2.setDisable(true);
                     button2Clicked = true;
                 }
            }
        });

        // Méthode pour envoyer un message au vendeur
        


        VBox buttonsContainer = new VBox(10); // Créez un conteneur pour les boutons
        buttonsContainer.getChildren().addAll(button1, button2); // Ajoutez les boutons au conteneur
        borderPane.setBottom(buttonsContainer); // Ajoutez le conteneur de boutons au bas de BorderPane
        buttonsContainer.setSpacing(10);
        
        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/file2.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Acheteur container");
        primaryStage.show();
    }
    private boolean messageSent = false; // Déclarer cette variable en tant que membre de classe

    public void sendMessageToSellerOnce(String message) {
        if (!messageSent) {
            if (bookBuyerAgent != null) {
                ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
                aclMessage.setContent(message);
                aclMessage.addReceiver(bookBuyerAgent.getAID());
                // Envoyer le message à l'agent vendeur
                bookBuyerAgent.send(aclMessage);
                // Marquer que le message a été envoyé
                messageSent = true;
                System.out.println("Vendeur a été ajouté");
            } else {
                System.out.println("L'agent vendeur n'est pas initialisé.");
            }
        } else {
            System.out.println("Le message a déjà été envoyé.");
        }
    }


    public void startContainer() {
        try {
            Runtime runtime = Runtime.instance();
            ProfileImpl profileImpl = new ProfileImpl(false);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            AgentContainer container = runtime.createAgentContainer(profileImpl);

            AgentController agentController = container.createNewAgent("BookBuyerAgent", "Agents.buyer.BookBuyerAgent", new Object[]{this});
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void logMessage(ACLMessage aclMessage) {
    	Platform.runLater(()->{
    	ObservableListData.add(aclMessage.getSender().getName()+"=>"+aclMessage.getContent());	
    });
    }

}