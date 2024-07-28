package easy_chatgptapi;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
//Remember, API key is needed to run this as well as the JSON library.
public class GPT_Test extends JFrame {
    private JTextPane chatBox;
    private JTextArea chatHistory;
    private JButton sendButton;
    private JPanel chatPanel;
    private JScrollPane chatScrollPane;
    private Easy_ChatGPTAPI chatbot;

    public GPT_Test(Easy_ChatGPTAPI chatbot){
        this.chatbot = chatbot;
        setTitle("ChatBot");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatPanel = new JPanel(new BorderLayout());
        sendButton = new JButton("Send");
        chatBox = new JTextPane();
        chatBox.setText("Type your message here...");
        chatHistory = new JTextArea(16, 30);
        chatHistory.setEditable(false);

        //Scrollpane shows chat history
        chatScrollPane = new JScrollPane(chatHistory);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        
        add(chatScrollPane, BorderLayout.CENTER);
        add(chatPanel, BorderLayout.SOUTH);
        chatPanel.add(chatBox, BorderLayout.CENTER);
        chatPanel.add(sendButton, BorderLayout.EAST);

        //Send button action listener
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = chatBox.getText();
                chatHistory.append("You: " + userInput + "\n");
                chatBox.setText("");
                //Change the system message to alter the behavior of the chatGPT 
                String response = chatbot.getChatGPTResponse("You are a chatbot.", userInput);
                chatHistory.append("Bot: " + response + "\n");
            }
        });
    }

    public static void main(String[] args) {
        String apiKey = "YOUR_API_KEY"; //Put API key here. If using publicly, do not expose this API key
        Easy_ChatGPTAPI chatbot = new Easy_ChatGPTAPI(apiKey);

        SwingUtilities.invokeLater(() -> {
            new GPT_Test(chatbot).setVisible(true);
        });
    }
}
