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
    private int[] childsID;

    public Memory(int memoryID, String memoryText, int parentID, int[] childsID) {
        this.memoryID = memoryID;
        this.memoryText = memoryText;
        this.parentID = parentID;
        this.childsID = childsID;
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

    public int[] getChildsID() {
        return childsID;
    }
}
