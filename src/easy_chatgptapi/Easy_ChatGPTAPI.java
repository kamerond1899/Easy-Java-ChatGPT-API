package easy_chatgptapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

//This class handles the HTTP request to OpenAI's servers to recieve text output from the AI API
public class Easy_ChatGPTAPI {
    private final String apiKey;

    //Public method will recieve API key as a string
    public Easy_ChatGPTAPI(String apiKey) {
        this.apiKey = apiKey;
    }

    //Below method handles the API request
    public String getChatGPTResponse(String systemMessage, String userMessage) {
        String url = "https://api.openai.com/v1/chat/completions"; //OpenAI API endpoint

        String model = "gpt-4o-mini-2024-07-18"; //4o NEW July 2024 model. Other models available on OpenAI website

    //Try API request    
    try {
        URL obj = new URL(url); //URL as a URL object
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();  //java.net HTTP connection to establish internet connection

        //Post request, with Bearer apiKey as the token, and the content type as application/json
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");

        //Request is made in JSon format
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", systemMessage)); //SYSTEM message is what determines AI behavior
        messages.put(new JSONObject().put("role", "user").put("content", userMessage)); //USER message is the user input

        //Create the request body and put the AI model as well as the user and system messages(which are contained in array)
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages);

        String body = requestBody.toString();

        
        connection.setDoOutput(true); //Use the URL connection for output

        //Set up Output Stream to send JSON payload to the openAI API endpoint
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        //Convert characters to bytes and encodes as JSON
        writer.write(body);
        
        //Flush writer's buffer, make sure all data is sent to server immediately
        writer.flush();

        //Close stream once request is made to prevent further writing
        writer.close();

        //Below handles the response from OpenAI. Buffers and reads incoming lines of text
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        //Holder for all the lines recieved from br
        String line;
        //Accumulate the lines and read them from the stream
        StringBuilder response = new StringBuilder();

        //Loop until there's nothing left to read
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        //Close the bufferedReader once done
        br.close();

        //Return OpenAI's response as a string
        return extractMessageFromJSONResponse(response.toString());
        
    //Catch ANY errors incoming
    } catch (IOException e) {
        //Print the error, and then return the String as just the error message
        e.printStackTrace();
        return "Error: " + e.getMessage();
    }
}

    private String extractMessageFromJSONResponse(String response) {
        //This method decodes the JSON response into text
        
        //Finds first occurrence of the text 'content', then add 11 to skip to the actual decoded message
        int start = response.indexOf("content") + 11;
        
        //Finds the first quotation mark after the start index as an indicator of the end
        int end = response.indexOf("\"", start);

        //Return only the substring start to end, to prevent other unnecessary JSON object artifacts from appearing
        return response.substring(start, end);
    }
}
