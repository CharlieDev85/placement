package com.blue.ui;

import com.blue.app.model.Student;
import com.blue.app.service.StudentService;
import com.blue.app.session.StudentSession;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "/student-form", layout = BasicLayout.class)
@CssImport("./styles/quiz-view.css")

public class StudentFormView extends VerticalLayout {

    @Autowired
    private StudentSession studentSession;

    @Autowired
    private StudentService studentService;

    public StudentFormView() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassName("quiz-background");

        Div card = new Div();
        card.addClassName("quiz-card");


        H2 title2 = new H2("Información de Contacto");

        Html instructions = new Html(
                "<p>Por favor llena los siguientes datos que nos servirán para informarte sobre el resultado de tu test.</p>"
        );

        TextField nameField = new TextField("Nombre Completo");
        nameField.setWidthFull();

        EmailField emailField = new EmailField("Correo Electrónico");
        emailField.setWidthFull();

        NumberField whatsappField = new NumberField("Número de Whatsapp");
        whatsappField.setWidthFull();

        CheckboxGroup<String> scheduleGroup = new CheckboxGroup<>();
        scheduleGroup.setLabel("Horario de preferencia");
        scheduleGroup.setItems("De 7:00 pm a 8:00 pm", "De 8:00 pm a 9:00 pm");
        scheduleGroup.addThemeName("vertical");
        scheduleGroup.setWidthFull();

        RadioButtonGroup<String> ageRangeField = new RadioButtonGroup<>();
        ageRangeField.setLabel("Rango de edad");
        ageRangeField.setItems("De 14 a 20 años", "De 21 a 30 años", "De 30 años en adelante");
        ageRangeField.addThemeName("vertical");
        ageRangeField.setWidthFull();

        Button startTestBtn = new Button("Iniciar test");
        startTestBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        startTestBtn.addClickListener(e -> {
            if (nameField.isEmpty() || emailField.isEmpty() || whatsappField.isEmpty()) {
                Notification.show("Por favor, completa todos los campos requeridos.");
                return;
            }

            Student student = new Student();
            student.setName(nameField.getValue());
            student.setEmail(emailField.getValue());
            student.setWhatsappNum(whatsappField.getValue().toString());
            student.setSchedulesPref(scheduleGroup.getSelectedItems().stream().toList());
            student.setAgeRange(ageRangeField.getValue());

            //Save to DB using StudentService
            studentSession.setStudent(student);
            studentService.save(student);
            startTestBtn.getUI().ifPresent(ui -> {
                ui.navigate("quiz");
            });

            Notification.show("Student Registered: " + student.getName());
        });

        card.add(title2, instructions, nameField, emailField, whatsappField,scheduleGroup, ageRangeField, startTestBtn);
        add(card);
    }

    private void startTestBtnClick(){

    }

}
