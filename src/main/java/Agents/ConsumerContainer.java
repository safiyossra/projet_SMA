package Agents;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import javafx.scene.layout.BorderPane;
import jade.wrapper.AgentContainer;
import javafx.collections.FXCollections;
import jade.wrapper.AgentController;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import javafx.collections.ObservableList;
public class ConsumerContainer extends Application {
	protected ConsumerAgent ConsumerAgent;
	ObservableList<String> ObservableListData;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Démarre le conteneur d'agents JADE
         startContainer();
        
    	 BorderPane borderPane = new BorderPane();
    	HBox hbox = new HBox();
       hbox.setPadding(new Insets(10));
    	hbox.setSpacing(10);
    	Label label = new Label("Nom du livre:");
    	label.setStyle("-fx-font-size: 15pt; -fx-text-fill: #F0FFFF;-fx-font-family: \"Times New Roman\";-fx-padding-left: 40px;");
    	
    	// Création d'un TextField pour l'entrée de l'utilisateur
        TextField textField = new TextField();
        textField.setPromptText("Entrez le nom du livre");
        textField.setStyle("-fx-font-size: 11pt; -fx-text-fill: #000000;-fx-font-family: \"Chalkduster\";-fx-padding-left: 40px;");
        Button button = new Button("Rechercher \uD83D\uDD0E");
        button.setStyle("-fx-font-size: 11pt; -fx-text-fill: #6495ED;-fx-font-family: \"Bookman\";-fx-padding-left: 40px;");
        hbox.getChildren().addAll(label, textField, button);
   	    borderPane.setTop(hbox);
   	
        button.setOnAction(event -> {
            System.out.println("Recherche du livre: " + textField.getText());
        });
        
      
        ObservableListData = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<String>(ObservableListData);
        VBox vbox1 = new VBox();
        vbox1.getChildren().add(listView);
        vbox1.setPadding(new Insets(90));
        vbox1.setSpacing(10);
       
      

        borderPane.setCenter(vbox1);
        
        // Action du bouton pour ajouter des livres à la liste
        button.setOnAction(event -> {
            String bookName = textField.getText();
            GuiEvent GuiEvent=new GuiEvent(this,1);
            if (!bookName.isEmpty()) {
            	GuiEvent.addParameter(bookName); // Ajoute le livre à la liste
                textField.clear(); // Nettoie le TextField
            }
            
            ConsumerAgent.onGuiEvent(GuiEvent);
        });
        
        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/file.css").toExternalForm());
        primaryStage.setTitle("Consumer container");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startContainer() {
        try {
            Runtime runtime = Runtime.instance();
            ProfileImpl profileImpl = new ProfileImpl(false);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            AgentContainer container = runtime.createAgentContainer(profileImpl);
            
            AgentController ConsumerController =
                    container.createNewAgent("Consumer1", "Agents.ConsumerAgent", new Object[]{this});
            ConsumerController.start();
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