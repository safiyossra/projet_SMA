package Agents.Seller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class BookSellerContainer extends Application {
    protected BookSellerAgents bookSellerAgent;
    protected ObservableList<String> observableList;
    protected AgentContainer agentContainer;
   protected BookSellerContainer bookSellerContainer;

    public static void main(String[] args) {
        launch(BookSellerContainer.class);
    }

    @Override
    public void start(Stage primaryStage) {
        startContainer();
        bookSellerContainer=this;
        primaryStage.setTitle("Vendeur");
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        Label label = new Label("Nom de Vendeur:");
        TextField textField = new TextField();
        Button button = new Button("Déployer l'agent vendeur");
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        button.setOnAction(event -> {
            // Code à exécuter lorsque le bouton est cliqué
            // Affichez le message "Vendeur a été ajouté"
            System.out.println("Vendeur a été ajouté");
        });
        hBox.getChildren().add(label);
        hBox.getChildren().add(textField);
        hBox.getChildren().add(button);
        borderPane.setTop(hBox);
        VBox vBox=new VBox();
        
     // Configuration pour le prix du livre et nombre de pages
        VBox detailsVBox = new VBox(10);
        detailsVBox.setPadding(new Insets(10));
        
        Label priceLabel = new Label("Prix du Livre:");
        priceLabel.setStyle("-fx-font-size: 11pt; -fx-text-fill: #6495ED;-fx-font-family: \"serif\";-fx-margin-left: 40px;");
        TextField priceTextField = new TextField();
        priceTextField.setMaxWidth(350);
        priceTextField.setStyle("-fx-padding-right: 10px;");
        Label pagesLabel = new Label("Nombre de Pages:");
        pagesLabel.setStyle("-fx-font-size: 11pt; -fx-text-fill: #6495ED;-fx-font-family: \"serif\";-fx-padding-left: 40px;");
        TextField pagesTextField = new TextField();
        pagesTextField.setStyle("-fx-padding-right: 10px;");
        pagesTextField.setMaxWidth(350);
        Label categorieLabel = new Label("Catégorie:");
        categorieLabel.setStyle("-fx-font-size: 11pt; -fx-text-fill: #6495ED;-fx-font-family: \"serif\";-fx-padding-left: 40px;");
        TextField CatégorieTextField = new TextField();
        CatégorieTextField.setStyle("-fx-padding-right: 10px;");
        CatégorieTextField.setMaxWidth(350);
        Label DescLabel = new Label("Courte description:");
        DescLabel.setStyle("-fx-font-size: 11pt; -fx-text-fill: #6495ED;-fx-font-family: \"serif\";-fx-padding-left: 40px;");
        TextField DescTextField = new TextField();
        DescTextField.setStyle("-fx-padding-right: 10px;");
        DescTextField.setMaxWidth(350);
        DescTextField.setPrefWidth(200);
        Button sendButton = new Button("Envoyer");


sendButton.setStyle("-fx-background-color: #DEB887; -fx-text-fill: white; -fx-font-size: 14px;-fx-padding-left: 40px;");
        
        detailsVBox.getChildren().addAll(priceLabel, priceTextField, pagesLabel, pagesTextField,categorieLabel ,CatégorieTextField,DescLabel,DescTextField,sendButton);
        
        // Ajout des VBox et HBox au layout principal
        VBox topVBox = new VBox(10, hBox, detailsVBox);
        borderPane.setTop(topVBox);
        
        GridPane gridePane=new GridPane();
        observableList=FXCollections.observableArrayList();
        ListView<String> listViewMessages = new ListView<String>(observableList); 
        gridePane.add(listViewMessages, 0, 0);
        listViewMessages.setPrefWidth(460);
//        ColumnConstraints column = new ColumnConstraints();
//        column.setHgrow(Priority.ALWAYS); // La colonne s'étendra autant que possible
//        gridePane.getColumnConstraints().add(column);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);


        
        // Appliquer les styles CSS
        vBox.applyCss();
        vBox.getChildren().add(gridePane);
        borderPane.setCenter(vBox);
        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/file3.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        button.setOnAction(event -> { // Correction de l'utilisation d'EventHandler
            try {
                String nomVendeur = textField.getText(); // Correction du nom de la variable
                AgentController agentController = agentContainer.createNewAgent(nomVendeur, "Agents.Seller.BookSellerAgents", new Object[]{this});
                agentController.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        });
        sendButton.setOnAction(event -> {
            String price = priceTextField.getText();
            String pages = pagesTextField.getText();
            String Catégorie=CatégorieTextField.getText();
            String Desc=DescTextField.getText();
            // Créer et envoyer un GuiEvent à l'agent vendeur avec le prix et les pages
            if (bookSellerAgent != null) {
                GuiEvent guiEvent = new GuiEvent(this,1 ); // Utilisez un identifiant d'événement approprié
                guiEvent.addParameter(price);
                guiEvent.addParameter(pages);
                guiEvent.addParameter(Catégorie);
                guiEvent.addParameter(Desc);
                bookSellerAgent.postGuiEvent(guiEvent); // Assurez-vous que la méthode postGuiEvent existe et traite correctement les GuiEvent
            }
        });
    }


    public void setBookSellerAgent(BookSellerAgents bookSellerAgent) {
        this.bookSellerAgent = bookSellerAgent;
    }
    
    public BookSellerAgents getBookSellerAgent() {
        return bookSellerAgent;
    }


 // Méthode pour gérer l'événement GUI reçu
    public void viewMessage(GuiEvent guiEvent) {
        if (guiEvent.getType() == 1) { // Vérifiez l'identifiant de l'événement
            String nomLivre = (String) guiEvent.getParameter(0);
            Platform.runLater(() -> {
                observableList.add(nomLivre); // Supposons que observableList est liée à une ListView
            });
        }
    }

    public void startContainer() {
        try {
            Runtime runtime = Runtime.instance();
            ProfileImpl profileImpl = new ProfileImpl(false);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            agentContainer = runtime.createAgentContainer(profileImpl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}