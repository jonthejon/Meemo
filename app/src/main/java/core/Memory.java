package core;

/**
 * POJO that will hold all the information concerning a particular memory retrieved from the database.
 */

public class Memory {

//    IV that holds the memory ID
    private int memoryID;
//    IV that holds the memory text
    private String memoryText;
//    IV that holds the memory's parent ID
    private int parentID;
//    IV that holds the memory's children ID. If null or length 0, no children.
    private int[] childrenIDs;
//    IV that holds the file path of this memory if it exists
    private String filePath;

    /**
     * Private constructor of the Memory class.
     * This constructor can only be called from the Builder inner class.
     * This will ensure that all Memory objects are created using the Builder Pattern.*/
    private Memory(MemoryBuilder builder) {
//        setting the IV's of the Memory object using the IV's of the Builder object sent as a parameter
        this.memoryID = builder.memoryID;
        this.memoryText = builder.memoryText;
        this.parentID = builder.parentID;
        this.childrenIDs = builder.childrenIDs;
        this.filePath = builder.filePath;
    }

    /**
     * This is the inner Builder class that will be responsible for creating new Memory objects using the Builder Pattern.*/
    public static class MemoryBuilder {
//        These IV's are correlated with the IV's of the outer Memory class
        private int memoryID;
        private String memoryText;
        private int parentID;
        private int[] childrenIDs;
        private String filePath;

        /**
         * This is the constructor of the Builder inner class.
         * It will take as parameters only the mandatory field to create a new memory.
         * All other IV's of the Memory object are optional.*/
        public MemoryBuilder(int memoryID, String memoryText, int parentID) {
//            initiating the mandatory IV's of the Memory object into the Builder object for later usage in the Memory constructor
            this.memoryID = memoryID;
            this.memoryText = memoryText;
            this.parentID = parentID;
        }

        /**
         * Optional setter of the Children IDs array.
         * This method is part of the Builder Pattern.*/
        public MemoryBuilder childrenIDs(int[] childrenIDs) {
//            setting the optional int[] IV with the given parameter
            this.childrenIDs = childrenIDs;
//            returning this same builder object for later usage in the construction process of Memory objects
            return this;
        }

        public MemoryBuilder filePath(String filePath) {
//            setting the optional String IV with the given parameter
            this.filePath = filePath;
//            returning this same builder object for later usage in the construction process of Memory objects
            return this;
        }

        /**
         * This is the actually build method of the Builder Pattern.
         * This method will use the private constructor of the Memory class to build and return a new memory object.*/
        public Memory build() {
//            creating a new blank int[] variable to be set to the IV if the client did not set this IV
            int[] blankChildrenIDs = new int[0];
//            creating a new black String variable to be set to the IV if the client did not set this IV
            String blackFilePath = null;
//            checking to see if the Builder object does not have the optional IV's set and if not, set them with the blank options
            if (this.childrenIDs == null) this.childrenIDs = blankChildrenIDs;
            if (this.filePath == null) this.filePath = blackFilePath;
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

    public int getParentID() {
        return parentID;
    }

    public int[] getChildrenIDs() {
        return childrenIDs;
    }

    public String getFilePath() {
        return filePath;
    }
}
