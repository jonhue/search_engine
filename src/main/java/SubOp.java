public final class SubOp<T> extends BinOp<Integer> {
  public SubOp(Expression<Integer> left, Expression<Integer> right) {
    super(left, right);
  }

  protected Integer func(Integer left, Integer right) {
    return left - right;
  }

  protected String symbol() {
    return "-";
  }
}
