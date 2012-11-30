package pattern.graph;

import pattern.Value;

abstract class Expr {
  abstract ContextExpr toContextExpr();
  abstract Value toValue();
}