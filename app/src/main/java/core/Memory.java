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
    // IV that holds the number of connections that this memory has
    private int numConnections;

    /**
     * Private constructor of the Memory class.
     * This constructor can only be called from the Builder inner class.
     * This will ensure that all Memory objects are created using the Builder Pattern.
     */
    private Memory(MemoryBuilder builder) {
//        setting the IV's of the Memory object using the IV's of the Builder object sent as a parameter
        this.memoryID = builder.memoryID;
        this.memoryText = builder.memoryText;
        this.filePath = builder.filePath;
        this.numConnections = builder.numConnections;
    }

    // This is the inner Builder class that will be responsible for creating new Memory objects using the Builder Pattern.
    public static class MemoryBuilder {
        //        These IV's are correlated with the IV's of the outer Memory class
        private int memoryID;
        private String memoryText;
        private String filePath;
        private int numConnections;

        /**
         * This is the constructor of the Builder inner class.
         * It will take as parameters only the mandatory field to create a new memory.
         * All other IV's of the Memory object are optional.
         *
         * @param memoryID       the ID of the memory we want to create
         * @param memoryText     the actual text that we want to save as memory
         * @param numConnections the number of connections this memory has
         */
        public MemoryBuilder(int memoryID, String memoryText, int numConnections) {
//            initiating the mandatory IV's of the Memory object into the Builder object for later usage in the Memory constructor
            this.memoryID = memoryID;
            this.memoryText = memoryText;
            this.numConnections = numConnections;
        }

        /**
         * method of the Builder class that will set the filePath IV if optionally defined.
         *
         * @param filePath the path to the imagefile that this memory holds
         */
        public MemoryBuilder filePath(String filePath) {
//            setting the optional String IV with the given parameter
            this.filePath = filePath;
//            returning this same builder object for later usage in the construction process of Memory objects
            return this;
        }

        /**
         * This is the actually build method of the Builder Pattern.
         * This method will use the private constructor of the Memory class to build and return a new memory object.
         *
         * @return the built Memory object
         */
        public Memory build() {
//            checking to see if the Builder object does not have the optional IV set and if not, set them with the blank options
            if (this.filePath == null) this.filePath = null;
            return new Memory(this);
        }
    }

    public int getMemoryID() {
        return memoryID;
    }

    public void setMemoryID(int memoryID) {
        this.memoryID = memoryID;
    }

    public String getMemoryText() {
        return memoryText;
    }

    public void setMemoryText(String memoryText) {
        this.memoryText = memoryText;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getNumConnections() {
        return numConnections;
    }
}
