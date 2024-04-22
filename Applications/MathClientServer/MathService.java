package Applications.MathClientServer;

class MathService implements MathServiceInterface {

    @Override
    public double doAdd(double a, double b) {
        return a + b;
    }

    @Override
    public double doSqrt(double a) {
        return Math.sqrt(a);
    }
}
