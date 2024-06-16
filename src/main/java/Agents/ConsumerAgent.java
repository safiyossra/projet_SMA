package Agents;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.ControllerException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.gui.GuiEvent;
import jade.gui.GuiAgent;
public class ConsumerAgent  extends GuiAgent {
    protected ConsumerContainer ConsumerContainer;
    @Override
    protected void setup() {
    	 String bookName = null;
    	if(this.getArguments().length==1) {
    		
    		ConsumerContainer=(ConsumerContainer)getArguments()[0];
    		ConsumerContainer.ConsumerAgent=this;
    	}
        System.out.println("Salut je suis Consumer!");
        System.out.println("Salut l'acheteur :"+
        		this.getAID().getName()+" est prêt \n je tente d'acheter lelivre :"+bookName);
        
        
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
        addBehaviour(parallelBehaviour);
        
        parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println("Tâche 1 exécutée.");
            }
        
     
        });
        
        parallelBehaviour.addSubBehaviour(new WakerBehaviour(this, 5000) { // 5000 ms = 5 secondes
            @Override
            protected void onWake() {
                System.out.println("5 secondes se sont écoulées. Deuxième WakerBehaviour déclenché.");
            }
        });
        
        
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
        	@Override
        	 public void action() {
                ACLMessage msg = receive();
                
                if (msg != null) {
                    System.out.println("Message reçu de : " + msg.getSender().getName());
                    System.out.println("Contenu du message : " + msg.getContent());
                    System.out.println("Acte de communication (performative) : " + ACLMessage.getPerformative(msg.getPerformative()));
                    String language = msg.getLanguage() != null ? msg.getLanguage() : "Non spécifié";
                    System.out.println("Langage de communication : " + language);
                    String protocole = msg.getProtocol() == null ? "Non spécifié" : msg.getProtocol();
                    System.out.println("Protocole : " + protocole);
                    
                    ConsumerContainer.logMessage(msg);;
                    
                    System.out.println("Réponse envoyée à " + msg.getSender().getName());
                } else {
                    block();
                }
            }
        });
    }
     
    @Override
    protected void beforeMove() {
        try {
            System.out.println("Avant de migrer vers une nouvelle location ....." + this.getContainerController().getContainerName());
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
  
    
       
    @Override
    protected void afterMove() {
        try {
            System.out.println("Je viens d'arriver à une nouvelle location ....." + this.getContainerController().getContainerName());
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void takeDown() {
        System.out.println("Avant de mourir .....");
    }
    
    protected void onGuiEvent(GuiEvent evt) {
        if(evt.getType() == 1) {
            String bookName = (String)evt.getParameter(0);
            System.out.println("Agent=>" + getAID().getName() + "=>" + bookName);
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(bookName);
        
            msg.addReceiver(new AID("BookBuyerAgent", AID.ISLOCALNAME));
            send(msg);
        }
    }

        
}

