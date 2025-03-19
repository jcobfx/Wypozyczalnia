package pl.com.foks.repository;

public interface IRepositoryEntry<T extends IRepositoryEntry<T>> {
    int getIdentifier();
    T deepClone();
    String toCSV();
}
