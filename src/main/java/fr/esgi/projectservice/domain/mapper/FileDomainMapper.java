package fr.esgi.projectservice.domain.mapper;

import fr.esgi.projectservice.data.entity.FileEntity;
import fr.esgi.projectservice.domain.model.File;
import fr.esgi.projectservice.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileDomainMapper {

    private final ModelMapper modelMapper;
    private final CommitDomainMapper commitDomainMapper;
    private final BranchDomainMapper branchDomainMapper;

    @Autowired
    public FileDomainMapper(ModelMapper modelMapper, CommitDomainMapper commitDomainMapper, BranchDomainMapper branchDomainMapper) {
        this.modelMapper = modelMapper;
        this.commitDomainMapper = commitDomainMapper;
        this.branchDomainMapper = branchDomainMapper;
    }

    public File convertCreateRequestToModel(CreateProjectRequest request) {
        return modelMapper.map(request, File.class);
    }

    public File convertEntityToModel(FileEntity fileEntity) {
        File file = new File();
        file.setId(fileEntity.getId());
        file.setFile(null);
        file.setCommitCreation(commitDomainMapper.convertEntityToModel(fileEntity.getCommitEntityCreation(),0));
        file.setDeleted(fileEntity.isDeleted());
        file.setBranch(branchDomainMapper.convertEntityToModel(fileEntity.getBranchEntity()));
        file.setFromInit(fileEntity.isFromInit());
        file.setLastCommitName(fileEntity.getLastCommitName());
        file.setName(fileEntity.getName());
        file.setParentDirectory(null);
        file.setType(false);
        file.setUrl(null);
        return file;
    }

    public FileEntity convertModelToEntity(File file) {
        return modelMapper.map(file, FileEntity.class);
    }

//    public FileUpload convertModelToFileUpload(File file){
//        return new FileUpload(
//            file.getId(),
//                null,
//                "actual",
//                file.getFile().getOriginalFilename(),
//
//        )
//    }
}
