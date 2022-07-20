package fr.esgi.projectservice.domain.mapper;

import fr.esgi.projectservice.data.entity.GroupEntity;
import fr.esgi.projectservice.domain.model.Group;
import fr.esgi.projectservice.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupDomainMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public GroupDomainMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Group convertCreateRequestToModel(CreateProjectRequest request) {
        return modelMapper.map(request, Group.class);
    }

    public Group convertEntityToModel(GroupEntity groupEntity) {
        if (groupEntity == null) {
            return null;
        }
        Group group = new Group();
        group.setId(groupEntity.getId());
        return group;
    }

    public GroupEntity convertModelToEntity(Group group) {
        return modelMapper.map(group, GroupEntity.class);
    }
}
