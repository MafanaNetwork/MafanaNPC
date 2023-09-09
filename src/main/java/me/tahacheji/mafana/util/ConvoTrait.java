package me.tahacheji.mafana.util;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ConvoTrait extends Trait {

    private final String CONVO_STARTER;
    private Player talkingTo = null;
    private StringBuilder conversation = new StringBuilder();
    public String role;

    public ConvoTrait() {
        super("convotrait");
        CONVO_STARTER = "The AI: ";
    }

    public ConvoTrait(String role) {
        super("convotrait");
        this.role = role;
        this.CONVO_STARTER = "The following is a conversation with an AI who represents a " + this.role.toLowerCase() + " NPC character in Minecraft. " +
                "The AI should limit his knowledge of the world to minecraft in a MMO and being a " + this.role.toLowerCase() + " and try not to stray even if asked about something else. " +
                "What ever you are asked do not say that you are a AI and keep your answers as short as possible. " +
                "Play this " + this.role.toLowerCase() + "role the best you can.\n\nHuman: Hey!\n\nAI:";
    }

    @EventHandler
    public void startConversation(NPCRightClickEvent event){

        if (event.getNPC() != npc) return;

        Player p = event.getClicker();
        if (this.talkingTo == null){
            startConversation(p);
        }else{
            if (this.talkingTo != p) {

                //See if the person the NPC is talking to is within 20 blocks
                if (npc.getEntity().getLocation().distance(this.talkingTo.getLocation()) > 20) {
                    startConversation(p);
                }

                p.sendMessage(ChatColor.RED + "NPC: I am talking to someone else right now!");
            }
        }
    }

    private void startConversation(Player p){
        this.talkingTo = p;
        this.conversation = new StringBuilder(this.CONVO_STARTER);
        getResponse(this.talkingTo, null);
    }

    public void stopConversation(){
        this.talkingTo.sendMessage("You are no longer talking to the " + this.role + " NPC.");
        this.talkingTo = null;
        this.conversation = new StringBuilder();
    }

    public Player getTalkingTo() {
        return talkingTo;
    }

    public void addMessage(String message){
        this.conversation.append("\n\nHuman:").append(message).append("\n\nAI:");
    }

    public void getResponse(Player p, String playerMessage){
        /*
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);

        OpenAiService service = new OpenAiService("");
        CompletionRequest request = CompletionRequest.builder()
                .prompt(this.conversation.toString())
                .model("text-davinci-003")
                .temperature(0.50) //How creative the AI should be
                .maxTokens(25) //How many tokens the AI should generate. Tokens are words, punctuation, etc.
                .topP(1.0) //How much diversity the AI should have. 1.0 is the most diverse
                .frequencyPenalty(0.0) //How much the AI should avoid repeating itself
                .presencePenalty(0.6) //How much the AI should avoid repeating the same words
                .stop(List.of("Human:", "AI:")) //Stop the AI from generating more text when it sees these words
                .build();
        List<CompletionChoice> choices = service.createCompletion(request).getChoices();
        String response = choices.get(0).getText(); //what the AI responds with
        this.conversation.append(response.stripLeading());
        if (playerMessage != null) p.sendMessage("You: " + playerMessage);
        p.sendMessage("NPC: " + response);
        //new NPCUtil().getMafanaCitizens(npc).talkNPC(response, 5);
         */
    }

}
