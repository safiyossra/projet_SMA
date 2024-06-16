package Agents.Seller;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;

public class BookSellerAgents extends GuiAgent {
    protected BookSellerContainer gui;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            gui = (BookSellerContainer) args[0];
            gui.setBookSellerAgent(this);
        }
        
        System.out.println("Hello! Seller-agent " + this.getAID().getName() + " is ready.");

        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
        addBehaviour(parallelBehaviour);

        // Enregistrer l'agent vendeur dans les pages jaunes
        parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription dfd = new DFAgentDescription();
                dfd.setName(getAID());
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Vente");
                sd.setName("Vente-livres");
                dfd.addServices(sd);
                try {
                    DFService.register(myAgent, dfd);
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }
        });


        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    switch (msg.getPerformative()) {
                        case ACLMessage.CFP:
                    
                            GuiEvent guiEvent = new GuiEvent(this, 1);
                            guiEvent.addParameter(msg.getContent()); 
                            gui.viewMessage(guiEvent);
                            break;

                        case ACLMessage.ACCEPT_PROPOSAL:
                
                            break;

                        default:
                            // Gérer les autres performatives si nécessaire
                            break;
                    }
                } else {
                    block();
                }
            }
        });
    }


    @Override
    protected void takeDown() {
     
    	 System.out.println("Avant de mourir .....");

    }
   
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
 
    public void onGuiEvent(GuiEvent ev) {
        if(ev.getType() == 1) {
            String prix = (String) ev.getParameter(0);
            String pages = (String) ev.getParameter(1);
            String category = (String) ev.getParameter(2);
            String Description = (String) ev.getParameter(3);
            String content = String.format("Prix: %s, Pages: %s, Catégorie: %s,Description:%s",prix,pages,category,Description);
            ACLMessage offerMsg = new ACLMessage(ACLMessage.INFORM);
            offerMsg.setContent(content);
            offerMsg.addReceiver(new AID("BookBuyerAgent", AID.ISLOCALNAME));
            
            send(offerMsg);
        }
    
 }
    }   

