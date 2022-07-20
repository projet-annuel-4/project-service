package fr.esgi.projectservice.domain.mapper;

import fr.esgi.projectservice.data.entity.DeltaEntity;
import fr.esgi.projectservice.domain.model.Delta;
import fr.esgi.projectservice.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeltaDomainMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public DeltaDomainMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Delta convertCreateRequestToModel(CreateProjectRequest request) {
        return modelMapper.map(request, Delta.class);
    }

    public Delta convertEntityToModel(DeltaEntity deltaEntity) {
        return modelMapper.map(deltaEntity, Delta.class);
    }

    public DeltaEntity convertModelToEntity(Delta delta) {
        return modelMapper.map(delta, DeltaEntity.class);
    }

}
