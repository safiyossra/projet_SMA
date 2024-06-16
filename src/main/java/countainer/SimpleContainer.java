package countainer;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties; 
import jade.wrapper.AgentContainer;
public class SimpleContainer {
	public static void main(String[] args) {
		try {
		Runtime runtime=Runtime.instance();
		ProfileImpl profileImpl=new ProfileImpl(false); 
		profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
		AgentContainer agentContainer=runtime.createAgentContainer(profileImpl);
		agentContainer.start();
		} catch (Exception e) { e.printStackTrace(); }
		}

}
