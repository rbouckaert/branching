package branching.tree;

import beast.base.core.Description;
import beast.base.core.Function;
import beast.base.core.Input;
import beast.base.evolution.tree.TreeDistribution;
import beast.base.evolution.tree.TreeInterface;

@Description("Branching process tree prior")
public class BranchingProcess extends TreeDistribution {
	final public Input<Function> betaInput = new Input<>("beta", "rate parameter of the branching process", new Constant("1.0"));
	final public Input<Function> thetanput = new Input<>("bthetaeta", "theta parameter of the branching process", new Constant("0.5"));
	
	private TreeInterface tree;
	private Function beta;
	private Function theta;
	
	@Override
	public void initAndValidate() {
		tree = treeInput.get();
		if (tree == null) {
			tree = treeIntervalsInput.get().treeInput.get();
		}
		
		beta = betaInput.get();
		theta = thetanput.get();
	}
	
	
	@Override
	public double calculateLogP() {
		logP = 0;
		
		// collect relative split times
		int taxonCount = tree.getLeafNodeCount();
		double [] t  = new double[taxonCount - 1];
		double h = tree.getRoot().getHeight();
		double b = beta.getArrayValue();
		int k = t.length;
		
		if (b == 1.0) {
			for (int i = 0; i < t.length; i++) {
				t[i] = 1.0 - (h - tree.getNode(taxonCount + i).getHeight()) / h; 
			}
			logP = Math.log(k) + Math.log(calcIntegral(t, theta.getArrayValue()));
		} else {
			for (int i = 0; i < t.length; i++) {
				double s = tree.getNode(taxonCount + i).getHeight() / h;
				t[i] = Math.signum(b) * (Math.exp(b * (1-s)) - 1); 
			}
			logP = Math.log(k) + (k-1) * Math.log(Math.signum(b) * b) + Math.log(Math.signum(b)*(1-Math.exp(-b)));
			logP += Math.log(calcIntegral(t, theta.getArrayValue()));
			
		}
		return logP;
	}
	
	
	private double calcIntegral(double [] t, double theta) {
		int k = t.length;
		double result = 0;
		
		for (int j = 0; j < k; j++) {
			double logG = 1.0;
			for (int l = 0; l < k; l++) {
				if (l != j) {
					logG += Math.log(t[j]+t[l]) - Math.log(t[j] - t[l]);
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
		
		if (k % 2 == 0) {
			return result;
		} else {
			return -result;
		}
	}
}
