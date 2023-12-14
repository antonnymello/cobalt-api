package module;

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

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing WhatsApp API...");

        Whatsapp app = Whatsapp.webBuilder()
                .newConnection()
                .name("WhatsappAPI")
                .autodetectListeners(true)
                .acknowledgeMessages(false)
                .checkPatchMacks(true)
                .unregistered(QrHandler.toFile(Paths.get("qr.png"), QrHandler.ToFileConsumer.toTerminal()))
                .connect().join();

        System.out.println("WhatsApp API initialized!");
        System.out.println("Waiting to send message to establish connection...");

        try {
            Thread.sleep(30000);
            List<String> contacts = getContacts();
            System.err.println("Sending message to " + contacts.size() + " contacts");

            for (String contact : contacts) {
                System.out.println("Sending message to " + contact);
                ContactJidProvider contactJidProvider = ContactJid.of(contact).toJid();
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
        ButtonSection section = createSection("Options", buttons);
        ListMessage listMessage = ListMessage.builder()
                .sections(List.of(section))
                .title("Opções")
                .description("Escolha uma das opções abaixo")
                .listType(Type.SINGLE_SELECT)
                .build();
        return listMessage;
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