package com.blue.ui;

import com.blue.app.model.Attempt;
import com.blue.app.model.Student;
import com.blue.app.service.AttemptService;
import com.blue.app.session.AttemptSession;
import com.blue.app.session.StudentSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Route("test-completed")
@CssImport("./styles/result-page.css")
public class TestCompletedView extends VerticalLayout {

    @Autowired
    public TestCompletedView(StudentSession studentSession, AttemptService attemptService, AttemptSession attemptSession) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("result-page");

        Student student = studentSession.getStudent();
        Attempt attempt = attemptSession.getAttempt(); // implement this

        H1 congrats = new H1("🎉 Congratulations, " + student.getName() + "!");
        Span subtitle = new Span("Has completado el examen de ubicación, tu resultado es:");
        subtitle.getStyle().set("font-size", "18px");

        // Create progress bars or badges
        HorizontalLayout A1Bar = createLabeledProgressBar("A1", attempt.getScoreA1());
        HorizontalLayout A2Bar = createLabeledProgressBar("A2", attempt.getScoreA2());
        HorizontalLayout B1Bar = createLabeledProgressBar("B1", attempt.getScoreB1());
        HorizontalLayout B2Bar = createLabeledProgressBar("B2", attempt.getScoreB2());

        //get Result
        Div result= this.getResult(attempt);

        // Feedback summary
        Span summary = new Span(generateFeedback(attempt));
        summary.getStyle().set("margin-top", "20px");

        // CTA Button
        Button enrollButton = new Button("📘 View Course Options");
        enrollButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        enrollButton.addClickListener(e -> UI.getCurrent().navigate("courseOptions"));

        // Layout
        //add(congrats, subtitle, A1Bar, A2Bar, B1Bar, B2Bar, summary, enrollButton);
        add(congrats, subtitle, result, summary);
    }

    private HorizontalLayout createLabeledProgressBar(String label, BigDecimal score) {
        Span levelLabel = new Span("🔷 " + label);
        levelLabel.setWidth("50px");

        ProgressBar progressBar = new ProgressBar();
        progressBar.setMin(0);
        progressBar.setMax(100);
        progressBar.setValue(score.multiply(BigDecimal.valueOf(100)).doubleValue());
        progressBar.setWidth("200px");

        HorizontalLayout layout = new HorizontalLayout(levelLabel, progressBar);
        layout.setAlignItems(Alignment.CENTER);
        return layout;
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
        String subtitleText;
        switch (level) {
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
                .set("height", "180px")
                .set("padding", "16px")
                .set("text-align", "center");


        Span title = new Span("NIVEL");
        title.getStyle().set("font-size", "12px").set("color", "white");

        Span levelSpan = new Span(level);
        levelSpan.getStyle().set("font-size", "48px").set("font-weight", "bold").set("color", "white");

        Span subtitle = new Span(subtitleText);
        subtitle.getStyle().set("margin-top", "8px").set("font-size", "14px").set("font-weight", "bold").set("color", "white");

        card.add(title, levelSpan, subtitle);
        return card;
    }


}
