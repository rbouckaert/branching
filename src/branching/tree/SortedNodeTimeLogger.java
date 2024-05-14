package branching.tree;


import java.io.PrintStream;
import java.util.Arrays;

import beast.base.core.BEASTObject;
import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.core.Loggable;
import beast.base.evolution.tree.TreeInterface;

@Description("logger that reporst internal node heights sorted by time")
public class SortedNodeTimeLogger extends BEASTObject implements Loggable {
    final public Input<TreeInterface> treeInput = new Input<>("tree", "tree over which to calculate a prior or likelihood");

    private TreeInterface tree;
    
	@Override
	public void initAndValidate() {
		tree = treeInput.get();
	}

	@Override
	public void init(PrintStream out) {
		for (int i = 0; i < tree.getInternalNodeCount(); i++) {
			out.print("internalNodeTime." + i + "\t");
		}
	}

	@Override
	public void log(long sample, PrintStream out) {
		double [] internalNodeTime = new double[tree.getInternalNodeCount()];
		int n = tree.getLeafNodeCount();
		for (int i = 0; i < tree.getInternalNodeCount(); i++) {
			internalNodeTime[i] = tree.getNode(n + i).getHeight();
		}	
		
		Arrays.sort(internalNodeTime);
		
		for (int i = 0; i < tree.getInternalNodeCount(); i++) {
			out.print(internalNodeTime[i] + "\t");
		}
	}

	@Override
	public void close(PrintStream out) {
	}

}
