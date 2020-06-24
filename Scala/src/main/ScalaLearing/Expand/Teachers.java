package Expand;

public class Teachers implements Comparable<Teachers> {
    private String name;
    private int faceValue;

    public Teachers(String name, int faceValue) {
        this.name = name;
        this.faceValue = faceValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(int faceValue) {
        this.faceValue = faceValue;
    }

    @Override
    public int compareTo(Teachers o) {
        return this.faceValue - o.faceValue;
    }
}
