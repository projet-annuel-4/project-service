package fr.esgi.projectservice.bus;

import fr.esgi.projectservice.domain.model.Project;
import fr.esgi.projectservice.dto.ProjectEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("createdProjectProducer")
public class CreatedProjectProducer {
    static final String Project_CREATED_OUTPUT = "createdProjectProducer-out-0";
    private final StreamBridge streamBridge;

    public void projectCreated(Project project) {
        var projectEvent = ProjectEvent.builder()
                .id(project.getId())
                .title(project.getName())
                .build();

        streamBridge.send(Project_CREATED_OUTPUT, projectEvent);

        log.info("New project with id '{}' and title '{}' sent to bus.", projectEvent.getId(), projectEvent.getTitle());
    }
}