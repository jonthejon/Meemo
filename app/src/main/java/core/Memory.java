package core;

/**
 * POJO that will hold all the information concerning a particular memory retrieved from the database.
 */

public class Memory {

//    IV that holds the memory ID
    private int memoryID;
//    IV that holds the memory text
    private String memoryText;
//    IV that holds the file path of this memory if it exists
    private String filePath;
//    IV that holds the type of connection this memory has with the memory that called it
    private int connection;

    /**
     * Private constructor of the Memory class.
     * This constructor can only be called from the Builder inner class.
     * This will ensure that all Memory objects are created using the Builder Pattern.*/
    private Memory(MemoryBuilder builder) {
//        setting the IV's of the Memory object using the IV's of the Builder object sent as a parameter
        this.memoryID = builder.memoryID;
        this.memoryText = builder.memoryText;
        this.filePath = builder.filePath;
        this.connection = builder.connection;
    }

    /**
     * This is the inner Builder class that will be responsible for creating new Memory objects using the Builder Pattern.*/
    public static class MemoryBuilder {
//        These IV's are correlated with the IV's of the outer Memory class
        private int memoryID;
        private String memoryText;
        private String filePath;
        private int connection;

        /**
         * This is the constructor of the Builder inner class.
         * It will take as parameters only the mandatory field to create a new memory.
         * All other IV's of the Memory object are optional.*/
        public MemoryBuilder(int memoryID, String memoryText) {
//            initiating the mandatory IV's of the Memory object into the Builder object for later usage in the Memory constructor
            this.memoryID = memoryID;
            this.memoryText = memoryText;
        }

        /**
         * method of the Builder class that will set the filePath IV if optionally defined.*/
        public MemoryBuilder filePath(String filePath) {
//            setting the optional String IV with the given parameter
            this.filePath = filePath;
//            returning this same builder object for later usage in the construction process of Memory objects
            return this;
        }

        public MemoryBuilder connection(int connection) {
//            setting the optional String IV with the given parameter
            this.connection = connection;
//            returning this same builder object for later usage in the construction process of Memory objects
            return this;
        }

        /**
         * This is the actually build method of the Builder Pattern.
         * This method will use the private constructor of the Memory class to build and return a new memory object.*/
        public Memory build() {
//            creating the default value of the connection IV if it is not set before the memory creation
            int defaultConnection = 1;
//            checking to see if the Builder object does not have the optional IV set and if not, set them with the blank options
            if (this.filePath == null) this.filePath = null;
            if (this.connection == 0) this.connection = defaultConnection;
//            calling the private constructor of the Memory object sending this Builder object as parameter and returning the result to the client
            return new Memory(this);
        }
    }

    public int getMemoryID() {
        return memoryID;
    }

    public String getMemoryText() {
        return memoryText;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getConnection() {
        return connection;
    }
}
