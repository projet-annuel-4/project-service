package com.example.projectservicev.domain.mapper;

import com.example.projectservicev.data.entity.GroupEntity;
import com.example.projectservicev.domain.model.Group;
import com.example.projectservicev.domain.model.Project;
import com.example.projectservicev.request.CreateProjectRequest;
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

    public Group convertCreateRequestToModel(CreateProjectRequest request){
        return modelMapper.map(request, Group.class);
    }

    public Group convertEntityToModel(GroupEntity groupEntity){
        return modelMapper.map(groupEntity, Group.class);
    }

    public GroupEntity convertModelToEntity(Group group){
        return modelMapper.map(group, GroupEntity.class);
    }
}
