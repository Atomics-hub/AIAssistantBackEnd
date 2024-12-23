package com.example.aiassistant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;


public class AssistantConversation {



    String modelName;
    public String assistantId;
    public Thread thread;


    @Value("${openai.api.key}")
    public String API_KEY = System.getenv("OPENAI_API_KEY");
    private String ASSISTANT_ID = System.getenv("ASSISTANT_ID");
    private static final String BASE_URL = "https://api.openai.com/v1";

    public AssistantConversation(){
        this.modelName = "gpt-3.5-turbo";
    }

    public AssistantConversation(String assistantId) {
        this.modelName = "gpt-3.5-turbo";
        this.assistantId = assistantId;
        Object threadResponse = this.createThread();
        if (threadResponse != null) {
            this.thread = new Thread(threadResponse);
        }
    }

    public void createAssistant(){
        try {
            // URL for the OpenAI Chat Completion endpoint
            URL url = new URL("https://api.openai.com/v1/assistants");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Setting headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");  // Adding the beta HTTP header
            connection.setDoOutput(true);

            // JSON payload
            String jsonInputString = """
                {
                    "model": "gpt-3.5-turbo",
                    "temperature": 0.5,
                    "name":"API_ACCESS_TEST_JAVA"
                }
            """;


            // String jsonInputString = "{ \"model\": \"" + this.modelName +
            //                                "\", \"temperature\": \"" + 0.5 +
            //                                "\", \"name\": \"" + assistantName +
            //                                "\" }";


            // Sending the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Handling the response
            int status = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            if (status == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response from OpenAI: " + response.toString());


                    //create ObjectMapper instance
                    ObjectMapper objectMapper = new ObjectMapper();

                    //read JSON like DOM Parser
                    JsonNode rootNode = objectMapper.readTree(response.toString());
                    this.assistantId = rootNode.path("id").toString();

                    // Getting rid of pair of double quotes, there's probably a more elegant way to do this
                    this.assistantId = this.assistantId.substring(1,this.assistantId.length()-1);

                }
            } else {
                System.out.println("Error: " + status);
                System.out.println("Msg: " + msg);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAssistant(){
        try {
            // URL for the OpenAI Chat Completion endpoint
            URL url = new URL("https://api.openai.com/v1/assistants/" + this.assistantId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Setting headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");  // Adding the beta HTTP header

            // Handling the response
            int status = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            if (status == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
//                    System.out.println("Response from OpenAI: " + response.toString());


                }
            } else {
                System.out.println("Error: " + status);
                System.out.println("Msg: " + msg);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }

    /**
     * Creates conversation thread
     * @return Thread object
     */
    private Object createThread(){
        try {
            // URL for the OpenAI Chat Completion endpoint
            URL url = new URL("https://api.openai.com/v1/threads");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Setting headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");  // Adding the beta HTTP header

            // Handling the response
            int status = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            if (status == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
//                    System.out.println("Response from OpenAI: " + response.toString());
//                    this.thread = new Thread(response);
                    return response;

                }
            } else {
                System.out.println("Error: " + status);
                System.out.println("Msg: " + msg);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void deleteThread(){
        try {
            // URL for the OpenAI Chat Completion endpoint
            URL url = new URL("https://api.openai.com/v1/threads/" + this.thread.getThreadId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Setting headers
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");  // Adding the beta HTTP header

            // Handling the response
            int status = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            if (status == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response from OpenAI: " + response.toString());
                }
            } else {
                System.out.println("Error: " + status);
                System.out.println("Msg: " + msg);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.thread = null;
    }

    private Object getThread(String threadId){
        try {
            // URL for the OpenAI Chat Completion endpoint
            URL url = new URL("https://api.openai.com/v1/threads/" + threadId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Setting headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");  // Adding the beta HTTP header

            // Handling the response
            int status = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            if (status == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response;
                }
            } else {
                System.out.println("Error: " + status);
                System.out.println("Msg: " + msg);
            }

            connection.disconnect();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setThread() {
        if (this.thread == null) {
            try {
                System.out.println("Creating new thread...");
                URL url = new URL(BASE_URL + "/threads");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Setting headers
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("OpenAI-Beta", "assistants=v2");
                connection.setDoOutput(true);

                // Empty POST body as per API docs
                try (OutputStream os = connection.getOutputStream()) {
                    os.write("{}".getBytes(StandardCharsets.UTF_8));
                }

                int status = connection.getResponseCode();
                String msg = connection.getResponseMessage();
                System.out.println("Thread creation status: " + status);

                if (status == 200 || status == 201) {
                    String response = readResponse(connection);
                    System.out.println("Thread creation response: " + response);
                    this.thread = new Thread(response);
                    System.out.println("Thread ID: " + this.thread.getThreadId());
                } else {
                    System.err.println("Error creating thread: " + status + " " + msg);
                    throw new RuntimeException("Failed to create thread");
                }

                connection.disconnect();
            } catch (Exception e) {
                System.err.println("Error in setThread: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to set thread", e);
            }
        }
    }

    public Object createUserMessage(String threadId, String message){
        try {
            // URL for the OpenAI Chat Completion endpoint
            URL url = new URL("https://api.openai.com/v1/threads/" + threadId + "/messages");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Setting headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");  // Adding the beta HTTP header
            connection.setDoOutput(true);

            // JSON payload
            String jsonInputString = "{ \"role\": \"" + "user" +
                    "\", \"content\": \"" + message +
                    "\" }";

            // Sending the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }


            // Handling the response
            int status = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            if (status == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
//                    System.out.println("Response from OpenAI: " + response.toString());
                    return response;

                }
            } else {
                System.out.println("Error: " + status);
                System.out.println("Msg: " + msg);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Object assistantReply() {
        try {
            // Check if we have valid IDs
            if (thread == null || thread.getThreadId() == null) {
                throw new RuntimeException("Invalid thread ID");
            }
            if (assistantId == null) {
                throw new RuntimeException("Invalid assistant ID");
            }

            URL url = new URL(BASE_URL + "/threads/" + thread.getThreadId() + "/runs");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");
            connection.setDoOutput(true);

            // Simplified JSON payload
            String jsonInputString = "{\"assistant_id\":\"" + assistantId + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
            }

            int status = connection.getResponseCode();
            String response = readResponse(connection);

            if (status != 200 && status != 201) {
                System.err.println("Error in assistantReply: " + status + "\n" + response);
                throw new RuntimeException("Failed to get assistant reply");
            }

            // Wait for run to complete
            ObjectMapper mapper = new ObjectMapper();
            JsonNode runNode = mapper.readTree(response);
            String runId = runNode.get("id").asText();

            // Wait for completion (you might want to implement a proper polling mechanism)
            java.lang.Thread.sleep(2000);

            return response;
        } catch (Exception e) {
            System.err.println("Error in assistantReply: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get assistant reply", e);
        }
    }

    /**
     * Returns a list of Message objects sorted by created_at in descending order by default
     * @param threadId ID corresponding to thread containing the messages
     * @return list of Message objects
     */
    public Object getMessages(String threadId){
        try {
            // URL for the OpenAI Chat Completion endpoint
            URL url = new URL("https://api.openai.com/v1/threads/" + threadId + "/messages");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Setting headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");  // Adding the beta HTTP header


            // Handling the response
            int status = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            if (status == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response;
                }
            } else {
                System.out.println("Error: " + status);
                System.out.println("Msg: " + msg);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getMostRecentMessage(JsonNode messages) {
        try {
            System.out.println("Raw messages response: " + messages.toPrettyString());

            // Check for the data array in the response
            JsonNode dataArray = messages.get("data");
            if (dataArray == null || !dataArray.isArray()) {
                System.out.println("No data array found in messages");
                return "I apologize, but I couldn't process that message properly.";
            }

            // Find the most recent assistant message
            for (JsonNode message : dataArray) {
                // Check if it's an assistant message
                if (message.has("role") && "assistant".equals(message.get("role").asText())) {
                    JsonNode contentArray = message.get("content");
                    if (contentArray != null && contentArray.isArray() && contentArray.size() > 0) {
                        JsonNode firstContent = contentArray.get(0);
                        if (firstContent.has("text")) {
                            JsonNode textObject = firstContent.get("text");
                            if (textObject.has("value")) {
                                return textObject.get("value").asText();
                            }
                        }
                    }
                }
            }

            System.out.println("No valid assistant message found");
            return "I apologize, but I couldn't find a valid response.";
        } catch (Exception e) {
            System.err.println("Error parsing message content: " + e.getMessage());
            e.printStackTrace();
            return "I encountered an error while processing the message.";
        }
    }

    // I don't think this is necessary, tagging it for now.
    @Deprecated
    public HashMap<String, String> listMessages(String threadId) {
        Object messagesResponse = this.getMessages(threadId);
        HashMap<String, String> resultMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();


        try {
            // Parse the JSON string
            JsonNode root = mapper.readTree(messagesResponse.toString());
            JsonNode dataArray = root.path("data");

            // Iterate over each element in the data array
            for (JsonNode dataItem : dataArray) {
                String id = dataItem.path("created_at").asText();
                JsonNode contentArray = dataItem.path("content");

                // Extract "value" from the first content item if available
                if (contentArray.isArray() && contentArray.size() > 0) {
                    JsonNode textNode = contentArray.get(0).path("text");
                    String contentValue = textNode.path("value").asText();
                    resultMap.put(id, contentValue);
                }
            }


            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Currently uses already-existent assistant, sets message thread and allows user to send queries to assistant to
     * receive replies back to advance conversation
     */
    public void basicConversation(){

        AssistantConversation basicConversation = new AssistantConversation(this.ASSISTANT_ID);

        String assistant = basicConversation.getAssistant();

        basicConversation.setThread();


        Scanner scan = new Scanner(System.in);
        String input;

        do {
            System.out.print("Query: ");
            input = scan.nextLine();

            Object userMessage = basicConversation.createUserMessage(basicConversation.thread.getThreadId(), input);

            Object assistantReply = basicConversation.assistantReply();
            Object conversation = basicConversation.getMessages(basicConversation.thread.getThreadId());
            System.out.println(basicConversation.getMostRecentMessage((JsonNode) conversation));


        } while (!input.equals("quit"));
    }

    public void defaultRAGConversation(){
        AssistantConversation RAGConversation = new AssistantConversation(this.ASSISTANT_ID);

        String assistant = RAGConversation.getAssistant();

        RAGConversation.setThread();


        Scanner scan = new Scanner(System.in);
        String input;

        do {
            System.out.print("Query: ");
            input = scan.nextLine();

            Object userMessage = RAGConversation.createUserMessage(RAGConversation.thread.getThreadId(), input);

            Object assistantReply = RAGConversation.assistantReply();
            Object conversation = RAGConversation.getMessages(RAGConversation.thread.getThreadId());
            //note: Only the assistant message is printed out here since the CLI retains the user's query
            System.out.println(RAGConversation.getMostRecentMessage((JsonNode) conversation));


        } while (!input.equals("quit"));
    }

    /**
     * Given the complete file path with filename.ext, file is uploaded to OpenAI. Not sure where yet
     * Individual files can be up to 512 MB, and the size of all files uploaded by one organization can be up to 100 GB.
     * There seems to be a workaround for this file size limitation through uploading by parts limited to 64 MB with
     * a total file size limit of 8 GB
     *
     * NOTE: this method does not lead to the actual uploading. It must be completed.
     * @param filePath complete filepath with extension
     * @return pendingUpload (Upload object with status "pending")
     */
    private Object addUpload(String filePath){

        try {
            File file = new File(filePath);
            String fileName = file.getName();
            long fileBytes = file.length();
            String filePurpose = "assistants";
            String fileMimeType = Files.probeContentType(file.toPath());


//            System.out.println("File Name: " + fileName);
//            System.out.println("File Size (in bytes): " + fileBytes);
//            System.out.println("File Purpose: " + filePurpose);
//            System.out.println("File MIME Type: " + fileMimeType);



            // URL for the OpenAI Chat Completion endpoint
            URL url = new URL("https://api.openai.com/v1/uploads");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Setting headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // JSON payload
            String jsonInputString = "{ \"filename\": \"" + fileName +
                    "\", \"purpose\": \"" + filePurpose +
                    "\", \"bytes\": " + fileBytes +
                    ", \"mime_type\": \"" + fileMimeType +
                    "\" }";

            // Sending the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Handling the response
            int status = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            if (status == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
//                    System.out.println("Response from OpenAI: " + response.toString());
                    return response;

                }
            } else {
                System.out.println("Error: " + status);
                System.out.println("Msg: " + msg);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public void createAssistantForFileUpload() {
        try {
            URL url = new URL(BASE_URL + "/assistants");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v1");
            connection.setDoOutput(true);

            Map<String, Object> payload = new HashMap<>();
            payload.put("model", "gpt-4-turbo-preview");
            payload.put("name", "API_ACCESS_TEST_JAVA");
            payload.put("instructions", "You are a helpful assistant.");

            // Add tools configuration
            List<Map<String, String>> tools = new ArrayList<>();
            Map<String, String> retrieval = new HashMap<>();
            retrieval.put("type", "retrieval");
            tools.add(retrieval);

            payload.put("tools", tools);

            ObjectMapper mapper = new ObjectMapper();
            try (OutputStream os = connection.getOutputStream()) {
                mapper.writeValue(os, payload);
            }

            int status = connection.getResponseCode();
            String response = readResponse(connection);

            System.out.println("Assistant creation status: " + status);
            System.out.println("Assistant creation response: " + response);

            if (status == 200 || status == 201) {
                JsonNode rootNode = mapper.readTree(response);
                this.assistantId = rootNode.get("id").asText();
                System.out.println("Assistant created successfully with ID: " + this.assistantId);
            } else {
                System.err.println("Error creating assistant. Status: " + status);
                System.err.println("Response: " + response);
            }

            connection.disconnect();
        } catch (Exception e) {
            System.err.println("Failed to create assistant: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String uploadFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist: " + filePath);
            }

            URL url = new URL(BASE_URL + "/files");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8), true)) {

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"purpose\"\r\n\r\n");
                writer.append("assistants").append("\r\n");

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(file.getName()).append("\"\r\n");
                writer.append("Content-Type: application/octet-stream\r\n\r\n");
                writer.flush();

                Files.copy(file.toPath(), os);
                os.flush();

                writer.append("\r\n");
                writer.append("--").append(boundary).append("--").append("\r\n");
            }

            int status = connection.getResponseCode();
            String response = readResponse(connection);

            if (status == 200 || status == 201) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response);
                String fileId = rootNode.get("id").asText();
                System.out.println("File uploaded successfully. File ID: " + fileId);
                return fileId;
            } else {
                System.err.println("Error uploading file. Status: " + status);
                System.err.println("Response: " + response);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Upload failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        connection.getResponseCode() >= 400
                                ? connection.getErrorStream()
                                : connection.getInputStream(),
                        StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    /**
     * Retrieves file information after upload
     * @param fileId The ID of the uploaded file
     * @return FileResponse object containing file details, or null if failed
     */
    public FileResponse getFileInfo(String fileId) {
        try {
            URL url = new URL(BASE_URL + "/files/" + fileId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

            int status = connection.getResponseCode();
            String response = readResponse(connection);

            if (status == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response, FileResponse.class);
            } else {
                System.err.println("Error getting file info. Status: " + status);
                System.err.println("Response: " + response);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Failed to get file info: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Attaches an uploaded file to the assistant
     * @param fileId The ID of the uploaded file
     * @return boolean indicating success
     */
    public boolean attachFileToAssistant(String fileId) {
        if (assistantId == null) {
            System.err.println("No assistant ID available. Create an assistant first.");
            return false;
        }

        try {
            URL url = new URL(BASE_URL + "/assistants/" + assistantId + "/files");  // Changed endpoint
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v1");
            connection.setDoOutput(true);

            Map<String, String> payload = new HashMap<>();
            payload.put("file_id", fileId);  // Changed to single file_id

            ObjectMapper mapper = new ObjectMapper();
            try (OutputStream os = connection.getOutputStream()) {
                mapper.writeValue(os, payload);
            }

            int status = connection.getResponseCode();
            String response = readResponse(connection);

            if (status == 200 || status == 201) {
                System.out.println("File attached to assistant successfully.");
                return true;
            } else {
                System.err.println("Error attaching file. Status: " + status);
                System.err.println("Response: " + response);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to attach file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Response class for file information
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FileResponse {
        public String id;
        public String object;
        public long bytes;
        public long created_at;
        public String filename;
        public String purpose;
        public String status;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getObject() { return object; }
        public void setObject(String object) { this.object = object; }
        public long getBytes() { return bytes; }
        public void setBytes(long bytes) { this.bytes = bytes; }
        public long getCreatedAt() { return created_at; }
        public void setCreatedAt(long created_at) { this.created_at = created_at; }
        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }
        public String getPurpose() { return purpose; }
        public void setPurpose(String purpose) { this.purpose = purpose; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

//    public static void main(String[] args){
//
//        // todo:
//        //  1) modify addUpload method to use the /files API endpoint. This seems specific to files within the file
//        //  limitation. The 600 page OSHA reg that was used is only 9 MB. For the initial project, a baked in file-size
//        //  limitation would be beneficial in terms of cost.
//        //  2) retrieveUpload, some way of tracking what files we're dealing with for easy lookup
//        //  3) Vector store creation, this is a parameter used when creating/modifying assistant with file_search as a tool
//
//
////        AssistantConversation test = new AssistantConversation();
////
////        Object testUploadResponse = test.addUpload("cs514_exception_handling_worksheet.pdf");
//
//        AssistantConversation conversation = new AssistantConversation();
//
//        // TOMS ADDED IN CODE TO CREATE A NEW ASSISTANT AND ATTACH THE UPLOADED FILE. AT THE MOMENT THE FILE IS HARDCODED
//        // COMMENT THIS IN TO TEST IT. COMMENTED OUT NOW TO NOT SPAM UPLOADS
//        conversation.createAssistantForFileUpload();
//        String fileId = conversation.uploadFile("cs514_exception_handling_worksheet.pdf");
//        System.out.println("Uploaded file ID: " + fileId);
//        if (fileId != null) {
//            // Get file info
//            FileResponse fileInfo = conversation.getFileInfo(fileId);
//            if (fileInfo != null) {
//                System.out.println("File status: " + fileInfo.getStatus());
//                System.out.println("File size: " + fileInfo.getBytes() + " bytes");
//            }
//
//            // Attach file to assistant
//            boolean attached = conversation.attachFileToAssistant(fileId);
//            System.out.println("File attached to assistant: " + attached);
//        }
//
//        conversation.defaultRAGConversation();
//    }
}