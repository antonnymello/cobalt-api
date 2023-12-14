package module;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.button.misc.ButtonRow;
import it.auties.whatsapp.model.button.misc.ButtonSection;
import it.auties.whatsapp.model.contact.ContactJid;
import it.auties.whatsapp.model.contact.ContactJidProvider;
import it.auties.whatsapp.model.message.button.ListMessage;
import it.auties.whatsapp.model.message.button.ListMessage.Type;
import it.auties.whatsapp.model.message.standard.AudioMessage;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing WhatsApp API...");

        Whatsapp app = Whatsapp.webBuilder()
                .lastConnection()
                .name("WhatsappAPI")
                .autodetectListeners(true)
                .acknowledgeMessages(false)
                .checkPatchMacks(true)
                .unregistered(QrHandler.toFile(Paths.get("qr.png"), QrHandler.ToFileConsumer.toTerminal()))
                .connect().join();

        System.out.println("WhatsApp API initialized!");
        System.out.println("Waiting to send message to establish connection...");

        try {
            Thread.sleep(2000);
            List<String> contacts = getContacts();
            System.err.println("Sending message to " + contacts.size() + " contacts");

            for (String contact : contacts) {
                System.out.println("Sending message to " + contact);
                ContactJidProvider contactJidProvider = ContactJid.of(contact).toJid();

                System.err.println("Sending normal message");
                app.sendMessage(contactJidProvider, "Hello World");
                Thread.sleep(500);

                System.err.println("Sending audio message");
                byte[] audio = new URL("https://dl.espressif.com/dl/audio/ff-16b-2c-44100hz.mp4")
                        .openStream()
                        .readAllBytes();
                AudioMessage audioMessage = AudioMessage.simpleBuilder().media(audio).build();
                app.sendMessage(contactJidProvider, audioMessage);
                Thread.sleep(500);

                System.err.println("Sending list message");
                ListMessage listMessage = createOptionsList();
                app.sendMessage(contactJidProvider, listMessage);

                System.err.println("Waiting 2 seconds to send message to next contact");
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static ListMessage createOptionsList() {
        List<ButtonRow> buttons = createOptions();
        ButtonSection firstSection = createSection("Options", buttons);
        ButtonSection secondSection = createSection("Options 2", buttons);
        return ListMessage.builder()
                .sections(List.of(firstSection, secondSection))
                .title("Options list")
                .description("Select a single option")
                .listType(Type.SINGLE_SELECT)
                .build();
    }

    private static List<String> getContacts() {
        List<String> contacts = new ArrayList<>();

        return contacts;
    }

    private static List<ButtonRow> createOptions() {
        List<ButtonRow> buttons = new ArrayList<>();
        buttons.add(ButtonRow.of("First option", "A nice description"));
        buttons.add(ButtonRow.of("Second option", "A nice description"));
        buttons.add(ButtonRow.of("Third option", "A nice description"));
        buttons.add(ButtonRow.of("Fourth option", "A nice description"));
        buttons.add(ButtonRow.of("Fifth option", "A nice description"));
        return buttons;
    }

    private static ButtonSection createSection(String title, List<ButtonRow> buttons) {
        ButtonSection section = ButtonSection.of(title, buttons);
        return section;
    }

}