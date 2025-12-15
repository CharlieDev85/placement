package com.blue.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class LandingView extends VerticalLayout {

    public LandingView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        Image logo = new Image("images/english-app-logo.png", "Logo");
        logo.setHeight("100px");

        H1 title = new H1("Test Your English Skills!");
        Button startButton = new Button("Continuar", VaadinIcon.ARROW_RIGHT.create());

        startButton.addClickListener(e -> startButton.getUI().ifPresent(ui ->
                ui.navigate("welcome")
        ));

        add(logo, title, startButton);
    }
}

