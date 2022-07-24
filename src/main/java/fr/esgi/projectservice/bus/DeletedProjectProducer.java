package fr.esgi.projectservice.bus;

import fr.esgi.projectservice.domain.model.Project;
import fr.esgi.projectservice.dto.ProjectEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("deletedProjectProducer")
public class DeletedProjectProducer {
    static final String Project_DELETED_OUTPUT = "deletedProjectProducer-out-0";
    private final StreamBridge streamBridge;

    public void projectDeleted(Project project) {
        var projectEvent = ProjectEvent.builder()
                .id(project.getId())
                .title(project.getName())
                .build();

        streamBridge.send(Project_DELETED_OUTPUT, projectEvent);

        log.info("Delete project with id '{}' and title '{}' sent to bus.", projectEvent.getId(), projectEvent.getTitle());
    }
}