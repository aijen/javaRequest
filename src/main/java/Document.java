import java.util.List;

public class Document {
    String documentBase64;
    String name;
    String fileExtension;
    String documentId;
}



class signers{
    private String email;
    private String name;
    private String receptionId;
        }
class recipients{
            private List<signers> signers;
        }

class EnvelopeDefinition{
            private String emailSubject;
            private List<Document>documents;
            private recipients recipients;

   }

    private EnvelopeDefinition makeEnvelope(String signerEmail, String signerName) throws IOException {

        String docPdf = config.docPdf; // location of the source document

        byte[] buffer = readFile(docPdf);
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document");
        Document doc1 = new Document();

        String doc1b64 = new String(Base64.encode(buffer));

        doc1.setDocumentBase64(doc1b64);
        doc1.setName("Lorem Ipsum"); // can be different from actual file name
        doc1.setFileExtension("pdf");
        doc1.setDocumentId("3");

        // The order in the docs array determines the order in the envelope
        envelopeDefinition.setDocuments(Arrays.asList(doc1));

        // Create a signer recipient to sign the document, identified by name and email
        // We set the clientUserId to enable embedded signing for the recipient
        // We're setting the parameters via the object creation
        Signer signer1 = new Signer();
        signer1.setEmail(signerEmail);
        signer1.setName(signerName);
        signer1.clientUserId(signerClientId);
        signer1.recipientId("1");

        // Create signHere fields (also known as tabs) on the documents,
        // We're using anchor (autoPlace) positioning
        //
        // The DocuSign platform seaches throughout your envelope's
        // documents for matching anchor strings.
        SignHere signHere1 = new SignHere();
        signHere1.setAnchorString("/sn1/");
        signHere1.setAnchorUnits("pixels");
        signHere1.setAnchorYOffset("20");
        signHere1.setAnchorXOffset("10");

        // Tabs are set per recipient / signer
        Tabs signer1Tabs = new Tabs();
        signer1Tabs.setSignHereTabs(Arrays.asList(signHere1));
        signer1.setTabs(signer1Tabs);

        // Add the recipient to the envelope object
        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer1));
        envelopeDefinition.setRecipients(recipients);

        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelopeDefinition.setStatus("sent");

        return envelopeDefinition;
    }