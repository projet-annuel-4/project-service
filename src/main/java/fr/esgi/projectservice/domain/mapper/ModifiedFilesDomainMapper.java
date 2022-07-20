package fr.esgi.projectservice.domain.mapper;

import fr.esgi.projectservice.data.entity.ModifiedFileEntity;
import fr.esgi.projectservice.domain.model.ModifiedFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifiedFilesDomainMapper {

    private ModelMapper modelMapper;
    private FileDomainMapper fileDomainMapper;
    private BranchDomainMapper branchDomainMapper;
    private CommitDomainMapper commitDomainMapper;

    @Autowired
    public ModifiedFilesDomainMapper(ModelMapper modelMapper, FileDomainMapper fileDomainMapper, BranchDomainMapper branchDomainMapper, CommitDomainMapper commitDomainMapper) {
        this.modelMapper = modelMapper;
        this.fileDomainMapper = fileDomainMapper;
        this.branchDomainMapper = branchDomainMapper;
        this.commitDomainMapper = commitDomainMapper;
    }

    public ModifiedFile convertEntityToModel(ModifiedFileEntity modifiedFileEntity) {
        if (modifiedFileEntity == null) {
            return null;
        }
        ModifiedFile modifiedFile = new ModifiedFile();
        modifiedFile.setId(modifiedFileEntity.getId());
        modifiedFile.setFile(modifiedFileEntity.getFileEntity() == null ? null : fileDomainMapper.convertEntityToModel(modifiedFileEntity.getFileEntity()));
        modifiedFile.setBranch(modifiedFileEntity.getBranchEntity() == null ? null : branchDomainMapper.convertEntityToModel(modifiedFileEntity.getBranchEntity()));
        modifiedFile.setAttachedCommit(modifiedFileEntity.getCommitEntity() == null ? null : commitDomainMapper.convertEntityToModel(modifiedFileEntity.getCommitEntity()));
        modifiedFile.setModificationType(modifiedFileEntity.getModificationType());
        return modifiedFile;
    }

    public ModifiedFileEntity convertModelToEntity(ModifiedFile modifiedFile) {
        if (modifiedFile == null) {
            return null;
        }
        ModifiedFileEntity modifiedFileEntity = new ModifiedFileEntity();
        modifiedFileEntity.setCommitEntity(modifiedFile.getAttachedCommit() == null ?
                null :
                commitDomainMapper.convertModelToEntity(modifiedFile.getAttachedCommit(), 0));
        modifiedFileEntity.setFileEntity(fileDomainMapper.convertModelToEntity(modifiedFile.getFile()));
        modifiedFileEntity.setBranchEntity(branchDomainMapper.convertModelToEntity(modifiedFile.getBranch()));
        modifiedFileEntity.setModificationType(modifiedFile.getModificationType());
        return modifiedFileEntity;
    }
}
