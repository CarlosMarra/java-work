package project.pkg3;

public class Machine {

    Long[] Memory = new Long[1000];
    long MQ = 0;
    long AC = 0;
    int PC = 0;
    int MemInit = 0;

    public void InitializeMemory(long word) {
        Memory[MemInit] = word;
        MemInit++;
    }

    public void LoadMQ(long MemLoc) {
        MQ = Memory[(int) MemLoc];
    }

    public long MemoryAccess(Long MemLoc) {
        int Address = MemLoc.intValue();
        return Memory[Address];
    }

    public void MQAC() {
        AC = MQ;
    }

    public void SetAC(Long value) {
        AC = value;
    }

    public void SetMQ(Long value) {
        MQ = value;
    }

    public void LoadAC(Long MemLoc) {
        int Address = MemLoc.intValue();
        AC = Memory[Address];
    }

    public void ACMemory(long MemLoc) {
        Memory[(int) MemLoc] = AC;
    }

    public void MQMemory(long MemLoc) {
        Memory[(int) MemLoc] = MQ;
    }

    public long MQ() {
        return MQ;
    }

    public long AC() {
        return AC;
    }

    public Long[] MemDump() {
        return Memory;
    }
}
