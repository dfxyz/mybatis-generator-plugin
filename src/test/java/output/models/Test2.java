package output.models;

public class Test2 extends Test2Key {
    public String description;

    public Test2(Integer provinceId, Integer cityId, String description) {
        super(provinceId, cityId);
        this.description = description;
    }

    public Test2() {
        super();
    }
}