package branching.tree;


/**
 * times should be independent and distributed 1/(1+x)^2
 * therefore y ~ uniform(0,1)
 * then x = y/(1-y)
 */

import beast.base.core.Description;
import beast.base.core.Function;
import beast.base.core.Input;
import beast.base.evolution.tree.TreeDistribution;
import beast.base.evolution.tree.TreeInterface;

@Description("Branching process tree prior")
public class BranchingProcess extends TreeDistribution {
	final public Input<Function> betaInput = new Input<>("beta", "rate parameter of the branching process", new Constant("1.0"));
	final public Input<Function> originInput = new Input<>("origin", "origin time of the branching process", new Constant("2.0"));
	
	private TreeInterface tree;
	private Function beta;
	private Function origin;
	
	@Override
	public void initAndValidate() {
		tree = treeInput.get();
		if (tree == null) {
			tree = treeIntervalsInput.get().treeInput.get();
		}
		
		beta = betaInput.get();
		origin = originInput.get();
	}
	
	
	@Override
	public double calculateLogP() {
		logP = 0;
		
		// collect relative split times
		int taxonCount = tree.getLeafNodeCount();
		double [] t  = new double[taxonCount];
		double h = origin.getArrayValue();//tree.getRoot().getHeight();
		if (h < tree.getRoot().getHeight()) {
			return Double.NEGATIVE_INFINITY;
		}
		double b = beta.getArrayValue();
		int k = t.length;
		
		if (b == 1.0) {
			for (int i = 0; i < t.length-1; i++) {
				t[i] = 1.0 - (h - tree.getNode(taxonCount + i).getHeight()) / h; 
				//t[i] = (h - tree.getNode(taxonCount + i).getHeight()) / h; 
			}
			t[t.length-1] = 1.0;
			logP = Math.log(k) + Math.log(calcIntegral(t));
		} else {
			for (int i = 0; i < t.length-1; i++) {
				double s = tree.getNode(taxonCount + i).getHeight() / h;
				t[i] = Math.signum(b) * (Math.exp(b * (1-s)) - 1); 
			}
			t[t.length-1] = 1.0;
			logP = Math.log(k) + (k-1) * Math.log(Math.signum(b) * b) + Math.log(Math.signum(b)*(1-Math.exp(-b)));
			logP += Math.log(calcIntegral(t));
			
		}
		if (Double.isInfinite(logP)) {
			logP = Double.NEGATIVE_INFINITY;
		}
		return logP;
	}
	
	private double calcIntegral(double [] t) {
		// maximum number of steps
		int N = 10000;
		// upper bound on integral
		double max = 1000.0;
		double delta = max / N;
		double theta = 0;
		double y = 0; 
		int k = t.length;
		double prev = 0;
		for (int i = 0; i < N; i++) {			
			double x = Math.pow(theta, k-1);
			for (int j = 0; j < k; j++) {
				double r = (1.0+theta*t[j]);
				x *= 1.0/(r*r);
			}
			theta += delta;

			y += delta * (prev + (x - prev) * 0.5);
			prev = x;
//			if (i == N-1) {
//				System.out.println(y + " " + dx);
//			}
		}
		return y;
	}
	
	private double calcIntegral0(double [] t) {
		int k = t.length;
		double result = 0;
		
		for (int j = 0; j < k; j++) {
			if (t[j] > 0.0) {
				double logG = 1.0;
				for (int l = 0; l < k; l++) {
					if (l != j) {
						double x = t[j] - t[l];
						logG += Math.log(t[j]+t[l]) - Math.signum(x) * Math.log(Math.abs(x));
					}
				}
				
				double logF = 0;
				for (int i = 0; i < k; i++) {
					if (i != j) {
						logF += Math.log(t[j]) - Math.log((t[j] - t[i])*(t[j] - t[i]));
					}
				}
				
				result += (t[j] - Math.exp(logG) * Math.log(t[j])/t[j]) * Math.exp(logF);
			}
		}
		
		return Math.abs(result);
//		if (k % 2 == 1) {
//			return result;
//		} else {
//			return -result;
//		}
	}
	
	@Override
	protected boolean requiresRecalculation() {
		return true;
	}
}
