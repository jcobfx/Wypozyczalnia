package pl.com.foks.data;

import pl.com.foks.repository.IRepositoryEntry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IRepositoryDataManager<E extends IRepositoryEntry<E>> {
    /**
     * Saves entries from the repository to the file
     * @param entries list of entries to save
     * @throws IOException if the file cannot be written
     */
    void save(List<E> entries) throws IOException;

    /**
     * Loads entries from the file
     * @return list of entries
     * @throws FileNotFoundException if the file cannot be read
     */
    List<E> load() throws FileNotFoundException;
}
