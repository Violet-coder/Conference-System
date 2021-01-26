package gateway;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serializable Gateway is for reading or write a loca .ser file with specified path.
 * */
public class SerializableGateway {
    private static final Logger logger = Logger.getLogger(SerializableGateway.class.getName());

    /**
     * Read the serialized object from the given file path. If file is not found,
     * an IOException is caught and return a new object. When calling this method,
     * it should be in a try catch block.
     * @param filePath      the file path to read from
     * */
    public Object readFromFile(String filePath) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(filePath);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the input object
            Object tm = input.readObject();
            input.close();
            return tm;
        } catch (IOException e) {
            /*logger.log(Level.SEVERE, "Cannot read form input file, returning" +
                    "a new Object.", e);*/

            return new Object();

        }

    }

    /**
     * Save the serializable object to the specified path. It throws an
     * IOException when the writing action fails. When calling this method,
     * it should be in a try-catch block.
     * @param filePath the file path to write
     * @param tm  the serializable object to store
     * */
    public void saveToFile(String filePath, Object tm) throws IOException {
            OutputStream file = new FileOutputStream(filePath);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);

            //serialize the map
            output.writeObject(tm);
            output.close();


    }
}
