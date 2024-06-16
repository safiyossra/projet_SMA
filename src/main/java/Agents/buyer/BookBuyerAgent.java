package Agents.buyer;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class BookBuyerAgent  extends GuiAgent {
	protected BookBuyerContainer BookBuyerContainer;
	protected AID[] ListVendeur;
	
    @Override
    protected void setup() {
    	if (this.getArguments().length == 1) {
    	    BookBuyerContainer = (BookBuyerContainer) getArguments()[0];
    	    BookBuyerContainer.bookBuyerAgent = this;
    	}

ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
addBehaviour(parallelBehaviour);

parallelBehaviour.addSubBehaviour(new TickerBehaviour(this, 60000) { // Recherche tous les 10 secondes
    @Override
    protected void onTick() {
        try {
            DFAgentDescription template = new DFAgentDescription(); 
            ServiceDescription sd = new ServiceDescription(); 
            sd.setType("Vente"); 
            template.addServices(sd); 

            DFAgentDescription[] result = DFService.search(myAgent, template); // Effectuer la recherche
            System.out.println("Les agents vendeurs suivants ont été trouvés :");
            ListVendeur = new AID[result.length]; // Initialisation du tableau pour stocker les AID des vendeurs
            for (int i = 0; i < result.length; ++i) {
                ListVendeur[i] = result[i].getName(); // Stockage des AID des agents vendeurs trouvés
                System.out.println(ListVendeur[i].getName()); // Affichage des noms des agents vendeurs
            }
        } catch (FIPAException fe) {
            fe.printStackTrace(); 
    }
    }
});



parallelBehaviour.addSubBehaviour(new CyclicBehaviour()
{
	  	  @Override
	  public void action() {
	      // Réception d'un message
	      ACLMessage msg = receive();
	      if (msg != null) {
	          // Log le message dans l'interface du BookBuyerContainer
	          BookBuyerContainer.logMessage(msg);

	          System.out.println("Message reçu de " + msg.getSender().getName() + ": " + msg.getContent());

	          // Préparer et envoyer une réponse
	          ACLMessage reply = msg.createReply();
	          reply.setPerformative(ACLMessage.INFORM);
	          reply.setContent("Essayer d'acheter =>" + msg.getContent());
	          send(reply);

	          // Préparer le message CFP
	          String NomLivre = msg.getContent();
	          ACLMessage aclMsg = new ACLMessage(ACLMessage.CFP);
	          aclMsg.setContent(NomLivre);

	          // Vérifier si la liste des vendeurs n'est pas vide
	          if (ListVendeur != null && ListVendeur.length > 0) {
	              for (AID aid : ListVendeur) {
	                  aclMsg.addReceiver(aid);
	              }
	              send(aclMsg); // Envoyer le message CFP aux vendeurs
	          } else {
	              System.out.println("Aucun vendeur trouvé pour envoyer le CFP.");
	          }
	      } else {
	          block();
	      }
	  }

  });
}

    	
	protected void onGuiEvent(GuiEvent evt) {
		
		
	}
}
