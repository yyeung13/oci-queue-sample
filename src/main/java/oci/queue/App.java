package oci.queue;

import java.util.ArrayList;
import com.oracle.bmc.queue.QueueClient;
import com.oracle.bmc.queue.QueueClient.Builder;
import com.oracle.bmc.queue.requests.GetMessagesRequest;
import com.oracle.bmc.queue.requests.PutMessagesRequest;
import com.oracle.bmc.queue.responses.GetMessagesResponse;
import com.oracle.bmc.queue.responses.PutMessagesResponse;
import com.oracle.bmc.queue.model.PutMessages;
import com.oracle.bmc.queue.model.PutMessagesDetails;
import com.oracle.bmc.queue.model.PutMessagesDetailsEntry;
import com.oracle.bmc.queue.model.GetMessage;
import com.oracle.bmc.queue.model.GetMessages;
import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;

import java.util.List;

public class App {
    public static void main(String[] args) {
        try {
            // Initialize OCI configuration
            String configurationFilePath = "~/.oci/config";
            String profile = "DEFAULT";
	    String endpoint = "https://cell-1.queue.messaging.ap-singapore-1.oci.oraclecloud.com";

            // Authentication
            BasicAuthenticationDetailsProvider provider =
                    new ConfigFileAuthenticationDetailsProvider(configurationFilePath, profile);

            // Initialize Queue Client
            //QueueClient queueClient = new QueueClient(provider);
	    QueueClient queueClient = 
                QueueClient.builder()
                           .endpoint(endpoint) // Set the custom endpoint
                           .build(provider);

            // Queue OCID
            String queueId = "ocid1.queue.oc1.ap-singapore-1.amaaaaaagoffsvaavlvveyyxvv4mnicxwh3htt3a2p4b2yoccrfkzteb3ffq";

            // Send Messages
            PutMessagesDetailsEntry messageEntry = PutMessagesDetailsEntry.builder()
                    .content("Hello, OCI Queue, here I come!")
                    .build();
	    List<PutMessagesDetailsEntry> messageDetails = new ArrayList<>();
	    messageDetails.add(messageEntry);

            //PutMessagesDetails putMessagesDetails = PutMessagesDetails.builder()
                    //.entries(List.of(messageEntry))
                    //.build();

            PutMessagesRequest putMessagesRequest = PutMessagesRequest.builder()
                    .queueId(queueId)
                    .putMessagesDetails(PutMessagesDetails.builder().messages(messageDetails).build())
                    .build();

            PutMessagesResponse putMessagesResponse = queueClient.putMessages(putMessagesRequest);
            //System.out.println("Messages sent successfully: " + putMessagesResponse.getPutMessagesResult().getEntries());
            System.out.println("Messages sent successfully: " + putMessagesResponse.getPutMessages().getMessages());

            // Retrieve Messages
            GetMessagesRequest getMessagesRequest = GetMessagesRequest.builder()
                    .queueId(queueId)
                    .limit(5) // Retrieve up to 5 messages
                    .build();

            GetMessagesResponse getMessagesResponse = queueClient.getMessages(getMessagesRequest);
            GetMessages getMessages = getMessagesResponse.getGetMessages();
	    List<GetMessage> messages = getMessages.getMessages();

            System.out.println("Messages received:");
            for (GetMessage message : messages) {
                System.out.println("Message Content: " + message.getContent());
            }

            // Clean up
            queueClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

