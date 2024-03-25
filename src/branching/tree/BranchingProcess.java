package branching.tree;

import beast.base.core.Description;
import beast.base.core.Function;
import beast.base.core.Input;
import beast.base.evolution.tree.TreeDistribution;
import beast.base.evolution.tree.TreeInterface;

@Description("Branching process tree prior")
public class BranchingProcess extends TreeDistribution {
	final public Input<Function> betaInput = new Input<>("beta", "rate parameter of the branching process", new Constant("1.0"));
	
	private TreeInterface tree;
	private Function beta;
	
	@Override
	public void initAndValidate() {
		tree = treeInput.get();
		if (tree == null) {
			tree = treeIntervalsInput.get().treeInput.get();
		}
		
		beta = betaInput.get();
	}
	
	
	@Override
	public double calculateLogP() {
		logP = 0;
		if (beta.getArrayValue() == 1.0) {
			 // TODO implement
		}
		return logP;
	}
}
