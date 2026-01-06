package com.blue.ui;

import com.blue.app.model.Course;
import com.blue.app.model.EnrolledStudent;
import com.blue.app.model.Recommendation;
import com.blue.app.model.RecommendationMade;
import com.blue.app.model.Schedule;
import com.blue.app.model.Student;
import com.blue.app.service.EnrolledStudentService;
import com.blue.app.service.RecommendationMadeService;
import com.blue.app.session.StudentSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("enroll")
@CssImport("./styles/enroll-view.css")
public class EnrollView extends VerticalLayout {
    private final EnrolledStudentService enrolledStudentService;
    private final RecommendationMadeService recommendationMadeService;
    private Recommendation recommendation;

    public EnrollView(StudentSession studentSession, RecommendationMadeService recommendationMadeService,
                      EnrolledStudentService enrolledStudentService) {
        this.recommendationMadeService = recommendationMadeService;
        this.enrolledStudentService = enrolledStudentService;

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassName("enroll-background");

        Student student = studentSession.getStudent();
        if (student == null) {
            UI.getCurrent().navigate("student-form");
            return;
        }

        Optional<RecommendationMade> recommendationMade = recommendationMadeService.findLatestForStudent(student);
        recommendation = recommendationMade.map(RecommendationMade::getRecommendation).orElse(null);

        String studentName = toProperCase(student.getName());
        String courseLabel = "Por definir";
        String scheduleLabel = "Por definir";
        String startDateLabel = "Por definir";
        Course courseValue = null;
        Schedule scheduleValue = null;
        LocalDate startDateValue = null;

        if (recommendation != null) {
            courseValue = recommendation.getRecommendedCourse();
            scheduleValue = recommendation.getRecommendedSchedule();
            startDateValue = recommendation.getStartingDate();
        }

        if (courseValue != null) {
            courseLabel = courseValue.getDisplayName();
        }
        if (scheduleValue != null) {
            scheduleLabel = scheduleValue.getDisplayName();
        }
        if (startDateValue != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", new Locale("es", "ES"));
            startDateLabel = startDateValue.format(formatter);
        }

        Div card = new Div();
        card.addClassName("enroll-card");

        H1 title = new H1("¡Ya casi terminamos, " + studentName.split(" ")[0] + "!");
        Paragraph subtitle = new Paragraph("Revisa tu inscripción sugerida y confirma tu lugar en Blue Academy.");
        subtitle.addClassName("enroll-subtitle");

        TextField nameField = new TextField("Nombre");
        nameField.setValue(studentName);
        nameField.setReadOnly(true);
        nameField.setWidthFull();

        TextField courseField = new TextField("Curso recomendado");
        courseField.setValue(courseLabel);
        courseField.setReadOnly(true);
        courseField.setWidthFull();

        TextField scheduleField = new TextField("Horario recomendado");
        scheduleField.setValue(scheduleLabel);
        scheduleField.setReadOnly(true);
        scheduleField.setWidthFull();

        TextField startDateField = new TextField("Fecha de inicio");
        startDateField.setValue(startDateLabel);
        startDateField.setReadOnly(true);
        startDateField.setWidthFull();

        Div infoBox = new Div(new Span("💡 Si necesitas ajustar algún detalle, un asesor podrá ayudarte después de confirmar."));
        infoBox.addClassName("enroll-info");

        Span successMessage = new Span("¡Inscripción completada! Te contactaremos muy pronto para darte la bienvenida.");
        successMessage.addClassName("enroll-success");
        successMessage.setVisible(false);

        Button enrollButton = new Button("Completar inscripción");
        enrollButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);

        Course finalCourseValue = courseValue;
        Schedule finalScheduleValue = scheduleValue;
        LocalDate finalStartDateValue = startDateValue;

        enrollButton.addClickListener(event -> {
            if (recommendation == null) {
                Notification notification = Notification.show("Aún no tenemos una recomendación disponible.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            EnrolledStudent enrolledStudent = new EnrolledStudent(student, finalCourseValue, finalScheduleValue, finalStartDateValue);
            enrolledStudentService.save(enrolledStudent);

            Notification notification = Notification.show("Inscripción completada con éxito.");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            successMessage.setVisible(true);
            enrollButton.setEnabled(false);
        });

        if (recommendation == null) {
            Div warning = new Div(new Span("No encontramos tu recomendación. Completa el test para continuar."));
            warning.addClassName("enroll-warning");
            card.add(title, subtitle, warning);
        } else {
            card.add(title, subtitle, nameField, courseField, scheduleField, startDateField, infoBox, enrollButton, successMessage);
        }

        add(card);
    }

    private String toProperCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        return Arrays.stream(input.toLowerCase().split(" "))
                .map(word -> word.isEmpty() ? word :
                        word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
