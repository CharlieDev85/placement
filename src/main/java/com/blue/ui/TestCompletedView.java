package com.blue.ui;

import com.blue.app.model.Attempt;
import com.blue.app.model.Result;
import com.blue.app.model.Student;
import com.blue.app.service.AttemptService;
import com.blue.app.session.AttemptSession;
import com.blue.app.session.StudentSession;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Route("test-completed")
@CssImport("./styles/result-page.css")
public class TestCompletedView extends VerticalLayout {

    private static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public TestCompletedView(StudentSession studentSession, AttemptService attemptService, AttemptSession attemptSession) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("result-page");

        Student student = studentSession.getStudent();
        Attempt attempt = attemptSession.getAttempt(); // implement this

        H1 congrats = new H1("🎉 Congratulations, " + toProperCase(student.getName()) + "!");
        Span subtitle = new Span("Has completado el examen de ubicación, tu resultado es:");
        subtitle.addClassName("result-subtitle");
        //subtitle.getStyle().set("font-size", "18px");


        //get Result
        Div resultCard= this.getResult(attempt);

        // --- Chat / Advisor section ---
        Div chat = buildAdvisorChat(student, attempt);

        // CTA buttons (WhatsApp + Enroll)
        HorizontalLayout ctas = buildCtas(student);

        // Feedback summary
        Span summary = new Span(generateFeedback(attempt));
        summary.getStyle().set("margin-top", "20px");

        // Layout
        add(congrats, subtitle, resultCard, summary, chat, ctas);
    }

    private Div buildAdvisorChat(Student student, Attempt attempt) {
        Div chatContainer = new Div();
        chatContainer.addClassName("chat-container");

        // Header (avatar + name)
        Div header = new Div();
        header.addClassName("chat-header");

        Image avatar = new Image("images/advisor.png", "Advisor");
        avatar.addClassName("chat-avatar");

        Div headerText = new Div();
        headerText.addClassName("chat-header-text");
        headerText.add(new Span("Blue Academy"));
        Span role = new Span("Asesor/a • Recomendación personalizada");
        role.addClassName("chat-role");
        headerText.add(role);

        header.add(avatar, headerText);

        // Bubble (typing first, then message)
        Div bubble = new Div();
        bubble.addClassName("chat-bubble");

        Div typing = new Div();
        typing.addClassName("typing");
        typing.setText("Escribiendo…");

        bubble.add(typing);

        chatContainer.add(header, bubble);

        // After a short delay, replace typing with the real message
        String messageHtml = buildAdvisorMessageHtml(student, attempt);

        UI ui = UI.getCurrent();
        ui.setPollInterval(500);
        SCHEDULER.schedule(() -> {
            try {
                ui.access(() -> {
                    bubble.removeAll();
                    bubble.add(new Html("<div class='chat-message'>" + messageHtml + "</div>"));
                    ui.setPollInterval(-1);
                    //ui.push(); // if using @Push
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 1200, TimeUnit.MILLISECONDS);


        return chatContainer;
    }

    private String buildAdvisorMessageHtml(Student student, Attempt attempt) {
        // Your current “level” logic:
        Result level = attempt.getResult(); // If you already set it in getResult()
        if (level == null) {
            // fallback: call getResult(attempt) earlier already sets attempt.setResult(...)
            level = Result.A1;
        }

        // TODO later: replace these 3 with your Recommendation logic
        String course = "Basic 4";
        String schedule = "7:00 PM a 8:00 PM";
        String startDate = "12 de enero, 2026";

        String firstName = toProperCase(student.getName()).split(" ")[0];

        // Message that feels like a human advisor wrote it
        return ""
                + "Hola <b>" + firstName + "</b> 😊<br><br>"
                + "Gracias por hacer tu test. Según tus respuestas, tu nivel actual es <b>" + level.getDisplayName() + "</b> 🎯<br><br>"
                + "✅ <b>Mi recomendación para que avances rápido</b> es que empieces con:<br>"
                + "📘 <b>" + course + "</b><br>"
                + "🕖 <b>" + schedule + "</b><br>"
                + "📅 Inicia el <b>" + startDate + "</b><br><br>"
                + "Con Blue Academy vas a tener:<br>"
                + "✨ Clases en vivo (practicas y preguntas en el momento)<br>"
                + "🗣️ Enfoque conversacional (hablas desde el inicio)<br>"
                + "👥 Grupos pequeños (más participación real)<br>"
                + "📚 Material y plataforma incluidos<br><br>"
                + "¿Te ayudo a reservar tu espacio? 💙";
    }

    private HorizontalLayout buildCtas(Student student) {
        // WhatsApp button (Anchor wrapping a Button)
        String phone = "50242281260"; // change later
        String text = "Hola, soy " + toProperCase(student.getName()) + ". Ya hice el test y quiero inscribirme 🙂";
        String waUrl = "https://wa.me/" + phone + "?text=" + java.net.URLEncoder.encode(text, java.nio.charset.StandardCharsets.UTF_8);

        Button whatsappBtn = new Button("💬 Hablar por WhatsApp");
        whatsappBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Anchor waLink = new Anchor(waUrl, "");
        waLink.setTarget("_blank");
        waLink.add(whatsappBtn);

        Button enrollBtn = new Button("✅ Inscribirme ahora");
        enrollBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        enrollBtn.addClickListener(e -> UI.getCurrent().navigate("enroll"));

        HorizontalLayout actions = new HorizontalLayout(enrollBtn, waLink);
        actions.addClassName("cta-row");
        actions.setJustifyContentMode(JustifyContentMode.CENTER);
        actions.setWidthFull();
        return actions;
    }


    private String generateFeedback(Attempt attempt) {
        BigDecimal scoreA1 = attempt.getScoreA1();
        BigDecimal scoreA2 = attempt.getScoreA2();
        BigDecimal scoreB1 = attempt.getScoreB1();
        BigDecimal scoreB2 = attempt.getScoreB2();

        // Find the max score
        BigDecimal max = scoreA1;
        String level = "A1";

        if (scoreA2.compareTo(max) > 0) {
            max = scoreA2;
            level = "A2";
        }
        if (scoreB1.compareTo(max) > 0) {
            max = scoreB1;
            level = "B1";
        }
        if (scoreB2.compareTo(max) > 0) {
            max = scoreB2;
            level = "B2";
        }

        // Provide feedback based on the strongest level
        return switch (level) {
            case "A1" -> "¡Buen comienzo! Estás construyendo una base sólida en inglés. Sigamos fortaleciendo esas habilidades esenciales. ¡Estás en el camino correcto!";
            case "A2" -> "¡Genial! Te mueves con seguridad por lo básico. Es el momento perfecto para dar el siguiente paso hacia un inglés más completo. ¡Sigue así!";
            case "B1" -> "¡Muy bien! Tienes un buen dominio del inglés intermedio. Trabajemos juntos para llevar tu nivel al siguiente escalón. ¡Tú puedes!";
            case "B2" -> "¡Excelente trabajo! Estás muy cerca de la fluidez. Ya puedes manejar conversaciones avanzadas con confianza. ¡Aprovecha este impulso y da el salto final!";
            default -> "¡Gracias por completar el test! Cada paso cuenta en tu camino hacia el dominio del inglés. ¡Sigue practicando y creciendo!";
        };
    }

    public Div getResult(Attempt attempt){
        BigDecimal scoreA1 = attempt.getScoreA1();
        BigDecimal scoreA2 = attempt.getScoreA2();
        BigDecimal scoreB1 = attempt.getScoreB1();
        BigDecimal scoreB2 = attempt.getScoreB2();
        BigDecimal max = scoreA1;
        Result result = Result.A1;

        if (scoreA2.compareTo(max) > 0) {
            max = scoreA2;
            result = Result.A2;
        }
        if (scoreB1.compareTo(max) > 0) {
            max = scoreB1;
            result = Result.B1;
        }
        if (scoreB2.compareTo(max) > 0) {
            max = scoreB2;
            result = Result.B2;
        }
        attempt.setResult(result);
        String subtitleText;
        switch (result.getDisplayName()) {
            case "A1" -> subtitleText = "BASIC";
            case "A2" -> subtitleText = "BASIC";
            case "B1" -> subtitleText = "INTERMEDIATE";
            case "B2" -> subtitleText = "INTERMEDIATE";
            default -> subtitleText = "BASIC";
        }


        Div card = new Div();
        card.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("border-radius", "16px")
                .set("box-shadow", "0 4px 10px rgba(0,0,0,0.1)")
                .set("background-color", "#4F81F7")
                .set("color", "white")
                .set("width", "150px")
                .set("height", "100px")//height
                .set("padding", "16px")
                .set("text-align", "center");


        Span title = new Span("NIVEL");
        title.getStyle().set("font-size", "12px").set("color", "white");

        Span levelSpan = new Span(result.getDisplayName());
        levelSpan.getStyle().set("font-size", "48px").set("font-weight", "bold").set("color", "white");

        Span subtitle = new Span(subtitleText);
        subtitle.getStyle().set("margin-top", "8px").set("font-size", "14px").set("font-weight", "bold").set("color", "white");

        card.add(title, levelSpan, subtitle);
        return card;
    }

    public static String toProperCase(String input) {
        if (input == null || input.isEmpty()) return input;

        return Arrays.stream(input.toLowerCase().split(" "))
                .map(word -> word.isEmpty() ? word :
                        word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }


}
