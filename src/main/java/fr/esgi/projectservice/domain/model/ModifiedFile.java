package fr.esgi.projectservice.domain.model;


public class ModifiedFile {

    private Long id;
    private File file;
    private Branch branch;
    private ModifiedFileTypeEnum modificationType;
    private Commit attachedCommit;


    public ModifiedFile() {
    }

    public ModifiedFile(Long id, File file, Branch branch, ModifiedFileTypeEnum modificationType, Commit attachedCommit) {
        this.id = id;
        this.file = file;
        this.branch = branch;
        this.modificationType = modificationType;
        this.attachedCommit = attachedCommit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public ModifiedFileTypeEnum getModificationType() {
        return modificationType;
    }

    public void setModificationType(ModifiedFileTypeEnum modificationType) {
        this.modificationType = modificationType;
    }

    public Commit getAttachedCommit() {
        return attachedCommit;
    }

    public void setAttachedCommit(Commit attachedCommit) {
        this.attachedCommit = attachedCommit;
    }

    @Override
    public String toString() {
        return "ModifiedFile{" +
                "id=" + id +
                ", file=" + file +
                ", branch=" + branch.toString() +
                ", modificationType=" + modificationType +
                ", attachedCommit=" + attachedCommit +
                '}';
    }
}
