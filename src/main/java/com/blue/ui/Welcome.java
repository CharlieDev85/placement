package com.blue.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("welcome")
public class Welcome extends VerticalLayout {
    public Welcome() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        addClassName("welcome-background");

        Div card = new Div();
        card.addClassName("quiz-card");
        H1 title = new H1("Welcome to Blue Academy");
        Html welcomeMessage = new Html(
                "<p><b>¡Bienvenido al examen de ubicación de Blue Academy!</b><br><br>" +
                        "Gracias por elegirnos para acompañarte en tu camino de aprendizaje del inglés. <br><br>" +
                        "Este examen nos permitirá conocer tu nivel actual para ofrecerte una experiencia personalizada, y adaptada a tus objetivos. \uD83D\uDE80<br><br>" +
                        "<b>Tienes 45 minutos para completarlo.</b><br><br>" +
                        "Cuando estés listo, puedes continuar.</p>"
        );
        Button nextBtn = new Button("Continuar", VaadinIcon.ARROW_RIGHT.create());
        nextBtn.addClickListener(e -> nextBtn.getUI().ifPresent(ui -> ui.navigate("student-form")));
        card.add(title,welcomeMessage,nextBtn);
        add(card);
    }
}
