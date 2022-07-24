package fr.esgi.projectservice.service;

import fr.esgi.projectservice.data.entity.GroupEntity;
import fr.esgi.projectservice.data.repository.GroupRepository;
import fr.esgi.projectservice.domain.mapper.GroupDomainMapper;
import fr.esgi.projectservice.domain.model.Group;
import fr.esgi.projectservice.dto.GroupEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupDomainMapper groupDomainMapper;

    public GroupEntity findGroupById(Long id){
        Optional<GroupEntity> groupEntity = groupRepository.findById(id);
        if( groupEntity.isEmpty() ){
            throw new RuntimeException("no group found");
        }
        return groupEntity.get();
    }

    public Group saveGroup(GroupEntity groupModel) {
        var group= groupRepository.save(groupModel);
        return groupDomainMapper.convertEntityToModel(group);
    }

    public void createGroup(GroupEvent groupModel) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(groupModel.getId());
        groupEntity.setName(groupModel.getName());
        saveGroup(groupEntity);

    }
}
